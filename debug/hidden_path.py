# Libraries
import sys
import pdb

# Data Structure Libraries
from collections import deque   # BFS/DFS
import heapq                    # UCS/A*/Greedy

from dataclasses import dataclass  # Node Structure


@dataclass
class Node:
    x: int
    y: int
    parent: 'Node' = None
    cost: int = 0   # total path cost from the start to this node
    heuristic: int = 0


# Parse the map file
def parse_map(filename):
    with open(filename, 'r') as f:
        lines = [line.strip() for line in f if line.strip()]

    # Parse Map Dimensions
    dim = lines[0]
    width, height = map(int, dim.split('x'))

    # Starting Tile
    start_line = lines[1]
    start_x, start_y = map(int, start_line.split('-'))
    start = (start_x, start_y)

    # Grid Representation
    grid = []
    for line in lines[2:]:
        grid.append(list(line))

    # Safety checks
    if len(grid) != height:
        raise ValueError(f"Expected {height} rows, but got {len(grid)}")

    for row in grid:
        if len(row) != width:
            raise ValueError(f"Expected row length {width}, but got {len(row)}")

    # Goal Tile
    goal = None
    for y in range(height):
        for x in range(width):
            if grid[y][x] == 'X':
                goal = (x, y)

    return width, height, start, goal, grid


# Check whether a coordinate is inside the map boundary
def in_bounds(x, y, width, height):
    return 0 <= x < width and 0 <= y < height


# Return Cost
def tile_cost(tile):
    if tile == '.':
        return 1
    elif tile == 'M':
        return 2
    elif tile == 'B':
        return 3
    elif tile == 'X':
        return 1
    elif tile.isdigit():
        return 1
    else:
        return 1


# Successor Check
def is_portal(tile):
    return tile.isdigit()


def is_entrance(tile):
    return tile.isdigit() and int(tile) % 2 == 1


def is_exit(tile):
    return tile.isdigit() and int(tile) % 2 == 0


def find_portals(grid, width, height):
    portal_positions = {}

    # Record all digit tiles
    for y in range(height):
        for x in range(width):
            tile = grid[y][x]
            if is_portal(tile):
                portal_positions[int(tile)] = (x, y)

    # Odd -> Even
    portals = {}
    for number, pos in portal_positions.items():
        if number % 2 == 1:  # odd = entrance
            exit_number = number + 1
            if exit_number in portal_positions:
                portals[pos] = portal_positions[exit_number]

    return portals


# Sucessor
def get_successors(node, grid, width, height, directions=None):
    successors = []

    if directions is None:
        directions = [
            (-1, 0),  # Left
            (1, 0),   # Right
            (0, -1),  # Up
            (0, 1),   # Down
        ]

    for dx, dy in directions:
        new_x = node.x + dx
        new_y = node.y + dy

        if not in_bounds(new_x, new_y, width, height):
            continue

        tile = grid[new_y][new_x]

        if tile == 'W':
            continue

        new_cost = node.cost + tile_cost(tile)

        child = Node(
            x=new_x,
            y=new_y,
            parent=node,
            cost=new_cost
        )

        successors.append(child)

    return successors


def get_teleport_successor(node, portals):
    current_pos = (node.x, node.y)

    if current_pos not in portals:
        return None

    exit_x, exit_y = portals[current_pos]

    teleport_node = Node(
        x=exit_x,
        y=exit_y,
        parent=node,
        cost=node.cost
    )

    return teleport_node



# Reconstruct the final path by following parent pointers
def reconstruct_path(node):
    path = []
    current = node

    while current is not None:
        path.append((current.x, current.y))
        current = current.parent

    path.reverse()
    return path



# Manhattan distance heuristic
def heuristic(x, y, goal):
    goal_x, goal_y = goal
    return abs(x - goal_x) + abs(y - goal_y)


# BFS
def bfs_search(start, goal, grid, width, height, portals):

    start_node = Node(start[0], start[1])

    queue = deque([start_node])
    visited = set()
    expanded = []

    while queue:
        current = queue.popleft()

        if (current.x, current.y) in visited:
            continue

        visited.add((current.x, current.y))
        expanded.append((current.x, current.y))

        # Goal
        if (current.x, current.y) == goal:
            return current, expanded

        # Teleport
        teleport_node = get_teleport_successor(current, portals)
        if teleport_node is not None:
            if (teleport_node.x, teleport_node.y) not in visited:
                queue.append(teleport_node)
            continue

        # Normal expansion
        successors = get_successors(current, grid, width, height)

        for child in successors:
            if (child.x, child.y) not in visited:
                queue.append(child)

    return None, expanded

# DFS
def dfs_search(start, goal, grid, width, height, portals):
    start_node = Node(x=start[0], y=start[1], parent=None, cost=0)

    stack = [start_node]
    visited = set()
    expanded = []

    while stack:
        current = stack.pop()

        if (current.x, current.y) in visited:
            continue

        visited.add((current.x, current.y))
        expanded.append((current.x, current.y))

        if (current.x, current.y) == goal:
            return current, expanded

        # If the current node is a portal entrance,  visit the entrance then exit
        teleport_node = get_teleport_successor(current, portals)
        if teleport_node is not None:
            if (teleport_node.x, teleport_node.y) not in visited:
                stack.append(teleport_node)
            continue

        # Otherwise
        successors = get_successors(current, grid, width, height)

        for child in reversed(successors):
            if (child.x, child.y) not in visited:
                stack.append(child)

    # No path found
    return None, expanded



# UCS
def ucs_search(start, goal, grid, width, height, portals):
    start_node = Node(start[0], start[1], parent=None, cost=0)

    pq = []
    counter = 0
    heapq.heappush(pq, (0, start_node.x, start_node.y, counter, start_node))

    explored = set()
    frontier_cost = {(start_node.x, start_node.y): 0}
    expanded = []

    while pq:
        current_cost, _, _, _, current = heapq.heappop(pq)
        current_pos = (current.x, current.y)

        # stale entry
        if current_pos not in frontier_cost or current_cost != frontier_cost[current_pos]:
            continue

        del frontier_cost[current_pos]

        if current_pos in explored:
            continue

        explored.add(current_pos)
        expanded.append(current_pos)

        if current_pos == goal:
            return current, expanded

        # teleport
        teleport_node = get_teleport_successor(current, portals)
        if teleport_node is not None:
            tp_pos = (teleport_node.x, teleport_node.y)

            if tp_pos not in explored:
                if tp_pos not in frontier_cost or teleport_node.cost < frontier_cost[tp_pos]:
                    counter += 1
                    frontier_cost[tp_pos] = teleport_node.cost
                    heapq.heappush(
                        pq,
                        (
                            teleport_node.cost,
                            teleport_node.x,
                            teleport_node.y,
                            counter,
                            teleport_node
                        )
                    )
            continue

        successors = get_successors(current, grid, width, height)

        for child in successors:
            child_pos = (child.x, child.y)

            if child_pos in explored:
                continue

            if child_pos not in frontier_cost or child.cost < frontier_cost[child_pos]:
                counter += 1
                frontier_cost[child_pos] = child.cost
                heapq.heappush(
                    pq,
                    (
                        child.cost,
                        child.x,
                        child.y,
                        counter,
                        child
                    )
                )

    return None, expanded



# Greedy
def get_effective_heuristic(node, goal, portals):
    current_pos = (node.x, node.y)

    # If this node is a portal entrance, use the exit position
    if current_pos in portals:
        exit_x, exit_y = portals[current_pos]
        return heuristic(exit_x, exit_y, goal)

    return heuristic(node.x, node.y, goal)


def greedy_search(start, goal, grid, width, height, portals):
    start_node = Node(x=start[0], y=start[1], parent=None, cost=0)
    start_node.heuristic = get_effective_heuristic(start_node, goal, portals)

    pq = []
    counter = 0
    heapq.heappush(
        pq,
        (start_node.heuristic, start_node.x, start_node.y, counter, start_node)
    )

    visited = set()
    expanded = []

    while pq:
        current_h, _, _, _, current = heapq.heappop(pq)
        current_pos = (current.x, current.y)

        if current_pos in visited:
            continue

        visited.add(current_pos)
        expanded.append(current_pos)

        if current_pos == goal:
            return current, expanded

        # teleport
        teleport_node = get_teleport_successor(current, portals)
        if teleport_node is not None:
            tp_pos = (teleport_node.x, teleport_node.y)

            if tp_pos not in visited:
                teleport_node.heuristic = get_effective_heuristic(
                    teleport_node, goal, portals
                )
                counter += 1
                heapq.heappush(
                    pq,
                    (
                        teleport_node.heuristic,
                        teleport_node.x,
                        teleport_node.y,
                        counter,
                        teleport_node
                    )
                )
            continue

        # normal successors
        successors = get_successors(current, grid, width, height)

        for child in successors:
            child_pos = (child.x, child.y)

            if child_pos not in visited:
                child.heuristic = get_effective_heuristic(child, goal, portals)
                counter += 1
                heapq.heappush(
                    pq,
                    (
                        child.heuristic,
                        child.x,
                        child.y,
                        counter,
                        child
                    )
                )

    return None, expanded


# A*
def astar_search(start, goal, grid, width, height, portals):
    start_node = Node(x=start[0], y=start[1], parent=None, cost=0)
    start_node.heuristic = get_effective_heuristic(start_node, goal, portals)

    pq = []
    counter = 0

    start_f = start_node.cost + start_node.heuristic
    heapq.heappush(
        pq,
        (start_f, start_node.x, start_node.y, counter, start_node)
    )

    explored = set()
    frontier_cost = {(start_node.x, start_node.y): 0}
    expanded = []

    while pq:
        current_f, _, _, _, current = heapq.heappop(pq)
        current_pos = (current.x, current.y)

        # stale entry check
        if current_pos not in frontier_cost or current.cost != frontier_cost[current_pos]:
            continue

        del frontier_cost[current_pos]

        if current_pos in explored:
            continue

        explored.add(current_pos)
        expanded.append(current_pos)

        if current_pos == goal:
            return current, expanded

        # teleport
        teleport_node = get_teleport_successor(current, portals)
        if teleport_node is not None:
            tp_pos = (teleport_node.x, teleport_node.y)

            if tp_pos not in explored:
                teleport_node.heuristic = get_effective_heuristic(
                    teleport_node, goal, portals
                )

                if tp_pos not in frontier_cost or teleport_node.cost < frontier_cost[tp_pos]:
                    counter += 1
                    frontier_cost[tp_pos] = teleport_node.cost
                    teleport_f = teleport_node.cost + teleport_node.heuristic

                    heapq.heappush(
                        pq,
                        (
                            teleport_f,
                            teleport_node.x,
                            teleport_node.y,
                            counter,
                            teleport_node
                        )
                    )
            continue

        # normal successors
        successors = get_successors(current, grid, width, height)

        for child in successors:
            child_pos = (child.x, child.y)

            if child_pos in explored:
                continue

            child.heuristic = get_effective_heuristic(child, goal, portals)

            if child_pos not in frontier_cost or child.cost < frontier_cost[child_pos]:
                counter += 1
                frontier_cost[child_pos] = child.cost
                child_f = child.cost + child.heuristic

                heapq.heappush(
                    pq,
                    (
                        child_f,
                        child.x,
                        child.y,
                        counter,
                        child
                    )
                )

    return None, expanded


# Hill-climbing
def hill_climbing_search(start, goal, grid, width, height, portals):
    start_node = Node(x=start[0], y=start[1], parent=None, cost=0)
    start_node.heuristic = get_effective_heuristic(start_node, goal, portals)

    current = start_node
    visited = set()
    expanded = []

    while True:
        current_pos = (current.x, current.y)

        if current_pos in visited:
            return None, expanded

        visited.add(current_pos)
        expanded.append(current_pos)

        if current_pos == goal:
            return current, expanded

        # Teleport
        teleport_node = get_teleport_successor(current, portals)
        if teleport_node is not None:
            tp_pos = (teleport_node.x, teleport_node.y)

            if tp_pos in visited:
                return None, expanded

            teleport_node.heuristic = get_effective_heuristic(
                teleport_node, goal, portals
            )
            current = teleport_node
            continue

        # Normal successors
        successors = get_successors(current, grid, width, height)

        candidates = []
        for child in successors:
            child_pos = (child.x, child.y)

            if child_pos not in visited:
                child.heuristic = get_effective_heuristic(child, goal, portals)
                candidates.append(child)

        # No available move
        if not candidates:
            return None, expanded

        # Keep your original tie-break
        best_child = min(candidates, key=lambda node: (node.heuristic, node.x, node.y))

        best_tile = grid[best_child.y][best_child.x]
        best_is_portal_entrance = is_entrance(best_tile)

        # Stop only if there is no improvement, allow equality when stepping onto a portal entrance
        if best_child.heuristic > current.heuristic:
            return None, expanded

        if best_child.heuristic == current.heuristic and not best_is_portal_entrance:
            return None, expanded

        current = best_child



# Expanded nodes output
def format_expanded(expanded):
    return ''.join(f"({x}, {y})" for x, y in expanded)


# Test
def main(strategy, filename, debug=False):
    if debug:
        print("Debug mode enabled")
        pdb.set_trace()

    width, height, start, goal, grid = parse_map(filename)
    portals = find_portals(grid, width, height)

    strategy_names = {
        'B': 'BFS',
        'D': 'DFS',
        'U': 'UCS',
        'G': 'Greedy',
        'A': 'A*',
        'H': 'Hill-climbing'
    }

    search_name = strategy_names.get(strategy, strategy)
    print(f"{search_name} Search Initiated")

    if strategy == 'D':
        goal_node, expanded = dfs_search(start, goal, grid, width, height, portals)
    elif strategy == 'B':
        goal_node, expanded = bfs_search(start, goal, grid, width, height, portals)
    elif strategy == 'U':
        goal_node, expanded = ucs_search(start, goal, grid, width, height, portals)
    elif strategy == 'G':
        goal_node, expanded = greedy_search(start, goal, grid, width, height, portals)
    elif strategy == 'A':
        goal_node, expanded = astar_search(start, goal, grid, width, height, portals)
    elif strategy == 'H':
        goal_node, expanded = hill_climbing_search(start, goal, grid, width, height, portals)
    else:
        print("not implemented yet.")
        return

    print(f"Expanded: {format_expanded(expanded)}")

    if goal_node is not None:
        path = reconstruct_path(goal_node)
        print(f"Path Found: {path}")
        print(f"Taking this path will cost: {goal_node.cost} Willpower")
    else:
        print("NO PATH FOUND!")


if __name__ == '__main__':
    debug = False
    args = sys.argv[1:]
    if '--debug' in args:
        debug = True
        args.remove('--debug')

    if len(args) < 2:
        strategy = 'H'
        filename = 'data/example2.txt'
    else:
        strategy = args[0]
        filename = args[1]

    main(strategy, filename, debug=debug)