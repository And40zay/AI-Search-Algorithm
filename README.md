# A1-AI: Search Algorithm Implementation

A university assignment implementing various search algorithms to find a path from a start position to a treasure ('X') on a grid map.

## Features

- **6 Search Algorithms:**
  - `B` - Breadth-First Search (BFS)
  - `D` - Depth-First Search (DFS)
  - `U` - Uniform Cost Search (UCS)
  - `G` - Greedy Best-First Search
  - `A` - A* Search
  - `H` - Hill Climbing

- **Grid Elements:**
  - `.` - Normal tile (cost: 1)
  - `M` - Mud tile (cost: 2)
  - `B` - Bush tile (cost: 3)
  - `W` - Wall (impassable)
  - `X` - Treasure (goal)
  - `1-2, 3-4, ...` - Teleport portals (odd = entrance, even = exit)

## Project Structure

```
A1-AI/
├── src/
│   ├── Main.java          # Entry point
│   ├── Tree.java          # Search algorithms
│   ├── Node.java          # Node data structure
│   ├── Position.java      # Position class
│   └── hidden_path.py     # Python implementation
├── data/
│   ├── example1.txt       # Test maps
│   ├── example2.txt
│   ├── example3.txt
│   └── ...
└── README.md
```

## Map File Format

```
<width>x<height>
<start_x>-<start_y>
<grid rows...>
```

Example (`data/example3.txt`):
```
5x3
0-0
.W...
.W.W.
...WX
```

## Usage

### Java Implementation

```bash
cd /home/jake/Desktop/A1-AI
javac src/*.java
java -cp src Main <strategy> <map_file>
```

Example:
```bash
java -cp src Main A data/example3.txt
```

### Python Implementation

```bash
cd /home/jake/Desktop/A1-AI
python3 src/hidden_path.py <strategy> <map_file>
```

Example:
```bash
python3 src/hidden_path.py A data/example3.txt
```

## Output

```
A* Search Initiated
Expanded: (0, 0)(1, 0)(2, 0)(2, 1)(2, 2)(2, 3)(2, 4)
Path Found: [(0, 0), (1, 0), (2, 0), (2, 1), (2, 2), (2, 3), (2, 4)]
Taking this path will cost: 7 Willpower
```

## Author

Jake

## License

MIT License
