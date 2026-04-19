import java.util.*;

public class Tree {
    private Node root;
    public int willPower;
    private final char[][] grid;
    private final int rows;
    private final int cols;
    private final Position start;
    private final boolean[][] visited;
    private final ArrayList<Character> result;
    private final ArrayList<Node> fringe;
    private final ArrayList<Node> expanded;
    private final Queue<Node> qui;
    private Node treasureNode;
    

    public Tree(char[][] grid, int rows, int cols, Position start) {
        this.root = null;
        this.willPower = 0;
        this.grid = grid;
        this.rows = rows;
        this.cols = cols;
        this.start = start;
        this.visited = new boolean[rows][cols];
        this.result = new ArrayList<>();
        fringe = new ArrayList<>();
        expanded = new ArrayList<>();
        this.qui = new LinkedList<>();
    }

    public int getWillPower() {
        return willPower;
    }

    public void addChildFringe(Node child) {
        fringe.add(child);
    }

    public void addChildExpanded(Node child) {
        expanded.add(child);
    }

    public void setWillPower(int willPower) {
        this.willPower = willPower;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public List<Position> backtrackPath() {
        ArrayList<Position> path = new ArrayList<>();
        Node node = treasureNode;
        while (node != null) {
            path.add(0, node.getPosition());
            node = node.getParent();
        }
        return path;
    }

    public Position teleport(int x, int y)
    {
        char cell = grid[x][y];

        // Only teleport if standing on an odd digit
        if(Character.isDigit(cell) && (cell - '0') % 2 != 0)
        {
            char destination = (char)(cell + 1);
            for(int i = 0; i < rows; i++)
            {
                for(int j = 0; j < cols; j++)
                {
                    if(grid[i][j] == destination)
                    {
                        return new Position(i, j);
                    }
                }
            }
        }
        return null;
    }
    public void mark_walls_visited() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 'W') {
                    visited[r][c] = true;
                }
            }
        }
    }
    public boolean moveLeft(int startX, int startY, Node current, Queue<Node> queue, int left)
    {
        if(startY - 1 >= left && !visited[startX][startY - 1] && grid[startX][startY - 1] != 'W')
        {
            Node child = new Node(new Position(startX, startY - 1), current);
            current.left = child;
            fringe.add(child);
            visited[startX][startY - 1] = true;
            queue.offer(child);

            if(grid[startX][startY - 1] == 'X') { treasureNode = child; return true; }
            else if(grid[startX][startY - 1] == 'M') { willPower += 2; }
            else if(grid[startX][startY - 1] == '.') { willPower += 1; }
            else if(grid[startX][startY - 1] == 'B') { willPower += 3; }

            Position teleportPos = teleport(startX, startY - 1);
            if(teleportPos != null)
            {
                Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), child);
                fringe.add(node);
                visited[teleportPos.getX()][teleportPos.getY()] = true;
                queue.offer(node);
                willPower += 1;
            }
        }
        return false;
    }

    public boolean moveRight(int startX, int startY, Node current, Queue<Node> queue, int right)
    {
        if(startY + 1 <= right && !visited[startX][startY + 1] && grid[startX][startY + 1] != 'W')
        {
            Node child = new Node(new Position(startX, startY + 1), current);
            current.right = child;
            fringe.add(child);
            visited[startX][startY + 1] = true;
            queue.offer(child);

            if(grid[startX][startY + 1] == 'X') { treasureNode = child; return true; }
            else if(grid[startX][startY + 1] == 'M') { willPower += 2; }
            else if(grid[startX][startY + 1] == '.') { willPower += 1; }
            else if(grid[startX][startY + 1] == 'B') { willPower += 3; }

            Position teleportPos = teleport(startX, startY + 1);
            if(teleportPos != null)
            {
                Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), child);
                fringe.add(node);
                visited[teleportPos.getX()][teleportPos.getY()] = true;
                queue.offer(node);
                willPower += 1;
            }
        }
        return false;
    }

    public boolean moveUp(int startX, int startY, Node current, Queue<Node> queue, int top)
    {
        if(startX - 1 >= top && !visited[startX - 1][startY] && grid[startX - 1][startY] != 'W')
        {
            Node child = new Node(new Position(startX - 1, startY), current);
            current.up = child;
            fringe.add(child);
            visited[startX - 1][startY] = true;
            queue.offer(child);

            if(grid[startX - 1][startY] == 'X') { treasureNode = child; return true; }
            else if(grid[startX - 1][startY] == 'M') { willPower += 2; }
            else if(grid[startX - 1][startY] == '.') { willPower += 1; }
            else if(grid[startX - 1][startY] == 'B') { willPower += 3; }

            Position teleportPos = teleport(startX - 1, startY);
            if(teleportPos != null)
            {
                Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), child);
                fringe.add(node);
                visited[teleportPos.getX()][teleportPos.getY()] = true;
                queue.offer(node);
                willPower += 1;
            }
        }
        return false;
    }

    public boolean moveDown(int startX, int startY, Node current, Queue<Node> queue, int bottom)
    {
        if(startX + 1 <= bottom && !visited[startX + 1][startY] && grid[startX + 1][startY] != 'W')
        {
            Node child = new Node(new Position(startX + 1, startY), current);
            current.down = child;
            fringe.add(child);
            visited[startX + 1][startY] = true;
            queue.offer(child);

            if(grid[startX + 1][startY] == 'X') { treasureNode = child; return true; }
            else if(grid[startX + 1][startY] == 'M') { willPower += 2; }
            else if(grid[startX + 1][startY] == '.') { willPower += 1; }
            else if(grid[startX + 1][startY] == 'B') { willPower += 3; }

            Position teleportPos = teleport(startX + 1, startY);
            if(teleportPos != null)
            {
                Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), child);
                fringe.add(node);
                visited[teleportPos.getX()][teleportPos.getY()] = true;
                queue.offer(node);
                willPower += 1;
            }
        }
        return false;
    }

    public ArrayList<Node> BFS()  
    {
        if(start == null || grid == null)
        {
            return new ArrayList<>();
        }
        resetVisited();
        mark_walls_visited(); 
        
        root = new Node(start);
        Queue<Node> queue = new LinkedList<>();
        Node startNode = new Node(new Position(start.getX(), start.getY()));
        fringe.add(startNode);
        visited[start.getX()][start.getY()] = true;
        queue.offer(startNode);
        
        int initialWillPower = 0;
        boolean foundTreasure = false;

        int top = 0;
        int left = 0;
        int bottom = rows - 1;
        int right = cols - 1;

        while(!queue.isEmpty() && !foundTreasure)
        {
            Node current = queue.poll();
            int startX = current.getPosition().getX();
            int startY = current.getPosition().getY();

            // Check if current is treasure
            if(grid[startX][startY] == 'X')
            {
                treasureNode = current;
                foundTreasure = true;
                break;
            }

            // Left
            if(startY - 1 >= left && !visited[startX][startY - 1] && grid[startX][startY - 1] != 'W')
            {
                Node child = new Node(new Position(startX, startY - 1), current);
                fringe.add(child);
                visited[startX][startY - 1] = true;
                queue.offer(child);

                if(grid[startX][startY - 1] == 'X') { treasureNode = child; foundTreasure = true; }
                else if(grid[startX][startY - 1] == 'M') { initialWillPower += 2; }
                else if(grid[startX][startY - 1] == '.') { initialWillPower += 1; }
                else if(grid[startX][startY - 1] == 'B') { initialWillPower += 3; }

                Position teleportPos = teleport(startX, startY - 1);
                if(teleportPos != null)
                {
                    Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), child);
                    fringe.add(node);
                    visited[teleportPos.getX()][teleportPos.getY()] = true;
                    queue.offer(node);
                    initialWillPower += 1;
                }
            }

            if(foundTreasure) break;

            // Right
            if(startY + 1 <= right && !visited[startX][startY + 1] && grid[startX][startY + 1] != 'W')
            {
                Node child = new Node(new Position(startX, startY + 1), current);
                fringe.add(child);
                visited[startX][startY + 1] = true;
                queue.offer(child);

                if(grid[startX][startY + 1] == 'X') { treasureNode = child; foundTreasure = true; }
                else if(grid[startX][startY + 1] == 'M') { initialWillPower += 2; }
                else if(grid[startX][startY + 1] == '.') { initialWillPower += 1; }
                else if(grid[startX][startY + 1] == 'B') { initialWillPower += 3; }

                Position teleportPos = teleport(startX, startY + 1);
                if(teleportPos != null)
                {
                    Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), child);
                    fringe.add(node);
                    visited[teleportPos.getX()][teleportPos.getY()] = true;
                    queue.offer(node);
                    initialWillPower += 1;
                }
            }

            if(foundTreasure) break;

            // Up  
            if(startX - 1 >= top && !visited[startX - 1][startY] && grid[startX - 1][startY] != 'W')
            {
                Node child = new Node(new Position(startX - 1, startY), current);
                fringe.add(child);
                visited[startX - 1][startY] = true;
                queue.offer(child);

                if(grid[startX - 1][startY] == 'X') { treasureNode = child; foundTreasure = true; }
                else if(grid[startX - 1][startY] == 'M') { initialWillPower += 2; }
                else if(grid[startX - 1][startY] == '.') { willPower += 1; }
                else if(grid[startX - 1][startY] == 'B') { willPower += 3; }

                Position teleportPos = teleport(startX - 1, startY);
                if(teleportPos != null)
                {
                    Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), child);
                    fringe.add(node);
                    visited[teleportPos.getX()][teleportPos.getY()] = true;
                    queue.offer(node);
                    initialWillPower += 1;
                }
            }

            if(foundTreasure) break;

            // Down
            if(startX + 1 <= bottom && !visited[startX + 1][startY] && grid[startX + 1][startY] != 'W')
            {
                Node child = new Node(new Position(startX + 1, startY), current);
                fringe.add(child);
                visited[startX + 1][startY] = true;
                queue.offer(child);

                if(grid[startX + 1][startY] == 'X') { treasureNode = child; foundTreasure = true; }
                else if(grid[startX + 1][startY] == 'M') { initialWillPower += 2; }
                else if(grid[startX + 1][startY] == '.') { initialWillPower += 1; }
                else if(grid[startX + 1][startY] == 'B') { initialWillPower += 3; }

                Position teleportPos = teleport(startX + 1, startY);
                if(teleportPos != null)
                {
                    Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), child);
                    fringe.add(node);
                    visited[teleportPos.getX()][teleportPos.getY()] = true;
                    queue.offer(node);
                    initialWillPower += 1;
                }
            }
        }

        if(!foundTreasure)
        {
            System.out.println("NO PATH FOUND!");
        }

        this.willPower = initialWillPower;
        return fringe;
    }

    public ArrayList<Node> buildTree(Tree tree) {
        if (start == null || grid == null) {
            return new ArrayList<>();
        }
        resetVisited();

        // Pre-mark all walls as visited so they are never considered
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 'W') {
                    visited[r][c] = true;
                }
            }
        }

        root = new Node(start);
        Node startNode = new Node(new Position(start.getX(), start.getY()));
        fringe.add(startNode);
        visited[start.getX()][start.getY()] = true;
        Node current = startNode;
        int startX = start.getX();
        int startY = start.getY();
        int initialWillPower = 0;
        boolean foundTreasure = false;

        int top = 0;
        int left = 0;
        int bottom = rows - 1;
        int right = cols - 1;

        while (top <= bottom && left <= right) {
            boolean moved = false;

            // Left
            for(int j = startY - 1; j >= left; j--)
            {
                if(startX >= 0 && startX < rows && j >= 0 && j < cols
                   && !visited[startX][j] && grid[startX][j] != 'W')
                {
                    Node child = new Node(new Position(startX, j), current);
                    fringe.add(child);
                    visited[startX][j] = true;
                    startY = j;
                    current = child;
                    moved = true;

                    Position teleportPos = teleport(startX, startY);
                    if(teleportPos != null)
                    { 
                        Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), current);
                        fringe.add(node);
                        visited[teleportPos.getX()][teleportPos.getY()] = true;
                        startX = teleportPos.getX(); 
                        startY = teleportPos.getY(); 
                        current = node;
                        initialWillPower += 1; 
                    }

                    if(grid[startX][startY] == 'X')       
                        { 
                            initialWillPower +=1; 
                            treasureNode = current; foundTreasure = true; break; 
                        }  
                    else if(grid[startX][startY] == 'M')  { initialWillPower += 2; }
                    else if(grid[startX][startY] == '.')  { initialWillPower += 1; }
                    else if(grid[startX][startY] == 'B')  { initialWillPower += 3; }
                }
                else { break; }
            }
            if(foundTreasure) break;

            // Right
            for(int j = startY + 1; j <= right; j++)
            {
                if(startX >= 0 && startX < rows && j >= 0 && j < cols
                   && !visited[startX][j] && grid[startX][j] != 'W')
                {
                    Node child = new Node(new Position(startX, j), current);
                    fringe.add(child);
                    visited[startX][j] = true;
                    startY = j;
                    current = child;
                    moved = true;

                    Position teleportPos = teleport(startX, startY);
                    if(teleportPos != null)
                    { 
                        Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), current);
                        fringe.add(node);
                        visited[teleportPos.getX()][teleportPos.getY()] = true;
                        startX = teleportPos.getX(); 
                        startY = teleportPos.getY(); 
                        current = node;
                        initialWillPower += 1; 
                    }

                    if(grid[startX][startY] == 'X')       
                        { 
                            initialWillPower +=1; 
                            treasureNode = current; foundTreasure = true; break; 
                        }
                    else if(grid[startX][startY] == 'M')  { initialWillPower += 2; }
                    else if(grid[startX][startY] == '.')  { initialWillPower += 1; }
                    else if(grid[startX][startY] == 'B')  { initialWillPower += 3; }
                }
                else { break; }
            }
            if(foundTreasure) break;

            // Up
            for(int i = startX - 1; i >= top; i--)
            {
                if(i >= 0 && i < rows && startY >= 0 && startY < cols
                   && !visited[i][startY] && grid[i][startY] != 'W')
                {
                    Node child = new Node(new Position(i, startY), current);
                    fringe.add(child);
                    visited[i][startY] = true;
                    startX = i;
                    current = child;
                    moved = true;

                    Position teleportPos = teleport(startX, startY);
                    if(teleportPos != null)
                    { 
                        Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), current);
                        fringe.add(node);
                        visited[teleportPos.getX()][teleportPos.getY()] = true;
                        startX = teleportPos.getX(); 
                        startY = teleportPos.getY(); 
                        current = node;
                        initialWillPower += 1; 
                    }

                    if(grid[startX][startY] == 'X')       
                        {
                            initialWillPower +=1; 
                            treasureNode = current; foundTreasure = true; break; 
                        }
                    else if(grid[startX][startY] == 'M')  { initialWillPower += 2; }
                    else if(grid[startX][startY] == '.')  { initialWillPower += 1; }
                    else if(grid[startX][startY] == 'B')  { initialWillPower += 3; }
                }
                else { break; }
            }
            if(foundTreasure) break;

            // Down
            for(int i = startX + 1; i <= bottom; i++)
            {
                if(i >= 0 && i < rows && startY >= 0 && startY < cols
                   && !visited[i][startY] && grid[i][startY] != 'W')
                {
                    Node child = new Node(new Position(i, startY), current);
                    fringe.add(child);
                    visited[i][startY] = true;
                    startX = i;
                    current = child;
                    moved = true;

                    Position teleportPos = teleport(startX, startY);
                    if(teleportPos != null)
                    { 
                        Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), current);
                        fringe.add(node);
                        visited[teleportPos.getX()][teleportPos.getY()] = true;
                        startX = teleportPos.getX(); 
                        startY = teleportPos.getY(); 
                        current = node;
                        initialWillPower += 1; 
                    }

                    if(grid[startX][startY] == 'X')       
                        { 
                            initialWillPower +=1;
                            treasureNode = current; foundTreasure = true; break; 

                        }
                    else if(grid[startX][startY] == 'M')  { initialWillPower += 2; }
                    else if(grid[startX][startY] == '.')  { initialWillPower += 1; }
                    else if(grid[startX][startY] == 'B')  { initialWillPower += 3; }
                }
                else { break; }
            }
            if(foundTreasure) break;

            // Only backtrack if ALL four directions had no valid move
            if (!moved) {
                if (current.getParent() != null) {
                    current = current.getParent();
                    startX = current.getPosition().getX();
                    startY = current.getPosition().getY();
                } else {
                    System.out.println("NO PATH FOUND!");
                    return fringe;
                }
            }
        }

        this.willPower = initialWillPower;
        return fringe;
    }
    public void hn()
    {
        
    }

    public void traverseGrid() {
        if (root == null) {
            return;
        }
        resetVisited();
        result.clear();
        traverseDfs(root);
    }

    private void resetVisited() {
        for (boolean[] row : visited) {
            Arrays.fill(row, false);
        }
    }

    public void buildDfs(Node node) {
        if (node == null || node.getPosition() == null) {
            return;
        }
        int x = node.getPosition().getX();
        int y = node.getPosition().getY();

        if (x < 0 || x >= rows || y < 0 || y >= cols || visited[x][y]) {
            return;
        }

        visited[x][y] = true;

        for (int[] dir : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && !visited[newX][newY]) {
                Node child = new Node(new Position(newX, newY));
                node.addChild(child);
                buildDfs(child);
            }
        }
    }

    public void traverseDfs(Node node) {
        if (node == null || node.getPosition() == null) {
            return;
        }
        int x = node.getPosition().getX();
        int y = node.getPosition().getY();

        if (x < 0 || x >= rows || y < 0 || y >= cols || visited[x][y]) {
            return;
        }

        visited[x][y] = true;
        result.add(grid[x][y]);

        for (Node child : node.getChildren()) {
            traverseDfs(child);
        }
    }

    public ArrayList<Character> getResult() {
        return new ArrayList<>(result);
    }
    public void findNeighbors(Node current, int top, int left, int bottom, int right)
    {
        int x = current.getPosition().getX();
        int y = current.getPosition().getY();

        // Left neighbor
        if(y - 1 >= left && !visited[x][y - 1] && grid[x][y - 1] != 'W')
        {
            current.left = new Node(new Position(x, y - 1), current);
            // Check if left neighbor is a teleport entrance
            Position teleportExit = teleport(x, y - 1);
            if(teleportExit != null)
            {
                current.left.setTeleportExit(teleportExit);
            }
        }
        // Up neighbor
        if(x - 1 >= top && !visited[x - 1][y] && grid[x - 1][y] != 'W')
        {
            current.up = new Node(new Position(x - 1, y), current);
            // Check if up neighbor is a teleport entrance
            Position teleportExit = teleport(x - 1, y);
            if(teleportExit != null)
            {
                current.up.setTeleportExit(teleportExit);
            }
        }
        // Down neighbor
        if(x + 1 <= bottom && !visited[x + 1][y] && grid[x + 1][y] != 'W')
        {
            current.down = new Node(new Position(x + 1, y), current);
            // Check if down neighbor is a teleport entrance
            Position teleportExit = teleport(x + 1, y);
            if(teleportExit != null)
            {
                current.down.setTeleportExit(teleportExit);
            }
        }
        // Right neighbor
        if(y + 1 <= right && !visited[x][y + 1] && grid[x][y + 1] != 'W')
        {
            current.right = new Node(new Position(x, y + 1), current);
            // Check if right neighbor is a teleport entrance
            Position teleportExit = teleport(x, y + 1);
            if(teleportExit != null)
            {
                current.right.setTeleportExit(teleportExit);
            }
        }
    }
    public void findNode(Node current, int top, int left, int bottom, int right)
    {
        int x = current.getPosition().getX();
        int y = current.getPosition().getY();

        // Left neighbor
        if(y - 1 >= left && !visited[x][y - 1] && grid[x][y - 1] != 'W')
        {
            current.left = new Node(new Position(x, y - 1), current);
        }
        // Up neighbor
        if(x - 1 >= top && !visited[x - 1][y] && grid[x - 1][y] != 'W')
        {
            current.up = new Node(new Position(x - 1, y), current);
        }
        // Down neighbor
        if(x + 1 <= bottom && !visited[x + 1][y] && grid[x + 1][y] != 'W')
        {
            current.down = new Node(new Position(x + 1, y), current);
        }
        // Right neighbor
        if(y + 1 <= right && !visited[x][y + 1] && grid[x][y + 1] != 'W')
        {
            current.right = new Node(new Position(x, y + 1), current);
        }
    }

    public int manhattanDistanceWithTeleport(Node node, Node goal)
    {
        int x = node.getPosition().getX();
        int y = node.getPosition().getY();
        
        // Normal manhattan distance from current position
        int normalDist = Math.abs(x - goal.getPosition().getX()) + Math.abs(y - goal.getPosition().getY());
        
        // If this node has a teleport exit, use the exit position for heuristic
        if(node.getTeleportExit() != null)
        {
            int exitX = node.getTeleportExit().getX();
            int exitY = node.getTeleportExit().getY();
            // Distance from teleport exit to goal + 1 for the teleport step
            int teleportDist = Math.abs(exitX - goal.getPosition().getX()) + Math.abs(exitY - goal.getPosition().getY()) + 1;
            return Math.min(normalDist, teleportDist);
        }
        
        return normalDist;
    }

    public Node findBestNeighbor(Node current, Node goal, int top, int left, int bottom, int right)
    {
        findNeighbors(current, top, left, bottom, right);
        
        Node best = null;
        int bestDistance = Integer.MAX_VALUE;

        if(current.left != null)
        {
            int dist = manhattanDistanceWithTeleport(current.left, goal);
            if(dist < bestDistance) { bestDistance = dist; best = current.left; }
        }
        if(current.up != null)
        {
            int dist = manhattanDistanceWithTeleport(current.up, goal);
            if(dist < bestDistance) { bestDistance = dist; best = current.up; }
        }
        if(current.down != null)
        {
            int dist = manhattanDistanceWithTeleport(current.down, goal);
            if(dist < bestDistance) { bestDistance = dist; best = current.down; }
        }
        if(current.right != null)
        {
            int dist = manhattanDistanceWithTeleport(current.right, goal);
            if(dist < bestDistance) { bestDistance = dist; best = current.right; }
        }

        return best;
    }
    public int Cases(Node node)
    {
        char cell = grid[node.getPosition().getX()][node.getPosition().getY()];
        if(cell == '.')
        {
            return 1;
        }
        else if(cell == 'M')
        {
            return 2;
        }
        else if(cell == 'B')
        {
            return 3;
        }
        else if(cell == 'X')
        {
            return 0; // Treasure should be prioritized (lowest cost)
        }
        else if(cell == 'S')
        {
            return 5; //for practice
        }
        else if(Character.isDigit(cell))
        {
            return -1;
        }
        return 1; // Default case
    }

    public Node findBestNode(Node current, Node goal, int top, int left, int bottom, int right)
    {
        findNode(current, top, left, bottom, right);
        
        Node best = null;
        int currentCost = Cases(current);
        int bestValue = Integer.MAX_VALUE;

        // Find the neighbor with the lowest cost (hill climbing minimizes cost)
        if(current.left != null)
        {
            int cost = Cases(current.left);
            if(cost < bestValue)
            {
                bestValue = cost;
                best = current.left;
            }
        }
        if(current.up != null)
        {
            int cost = Cases(current.up);
            if(cost < bestValue)
            {
                bestValue = cost;
                best = current.up;
            }
        }
        if(current.down != null)
        {
            int cost = Cases(current.down);
            if(cost < bestValue)
            {
                bestValue = cost;
                best = current.down;
            }
        }
        if(current.right != null)
        {
            int cost = Cases(current.right);
            if(cost < bestValue)
            {
                bestValue = cost;
                best = current.right;
            }
        }
        
        // Hill climbing: only move if neighbor is better than or equal to current
        if(best != null && bestValue > currentCost)
        {
            return null; // Local maximum reached
        }

        return best;
    }

    public Node Cost_Search(Node current, Node goal, int top, int left, int bottom, int right)
    {
        findNeighbors(current, top, left, bottom, right);
        
        Node best = null;
        int bestDistance = Integer.MAX_VALUE;

        if(current.left != null)
        {
            int dist = g_n(current.left);
            if(dist < bestDistance) 
            { 
                bestDistance = dist; 
                best = current.left; 
            }
        }
        if(current.up != null)
        {
            int dist = g_n(current.up);
            if(dist < bestDistance) 
            { 
                bestDistance = dist; 
                best = current.up; 
            }
        }
        if(current.down != null)
        {
            int dist = g_n(current.down);
            if(dist < bestDistance) 
            { 
                bestDistance = dist; 
                best = current.down; 
            }
        }
        if(current.right != null)
        {
            int dist = g_n(current.right);
            if(dist < bestDistance) 
            { 
                bestDistance = dist; 
                best = current.right; 
            }
        }

        return best;
    }


    public Node h_n(Node current, Node goal, int top, int left, int bottom, int right)
    {
        findNeighbors(current, top, left, bottom, right);
        
        Node best = null;
        int bestDistance = Integer.MAX_VALUE;

        if(current.left != null)
        {
            int H_n = manhattanDistanceWithTeleport(current.left, goal);
            int G_n = g_n(current.left);
            int f_n = H_n + G_n;
            if(f_n < bestDistance) 
            { 
                bestDistance = f_n; 
                best = current.left; 
            }
        }
        if(current.up != null)
        {
            int H_n = manhattanDistanceWithTeleport(current.up, goal);
            int G_n = g_n(current.up);
            int f_n = H_n + G_n;
            if(f_n < bestDistance) 
            { 
                bestDistance = f_n; 
                best = current.up; 
            }
        }
        if(current.down != null)
        {
            int H_n = manhattanDistanceWithTeleport(current.down, goal);
            int G_n = g_n(current.down);
            int f_n = H_n + G_n;
            if(f_n < bestDistance) 
            { 
                bestDistance = f_n; 
                best = current.down; 
            }
        }
        if(current.right != null)
        {
            int H_n = manhattanDistanceWithTeleport(current.right, goal);
            int G_n = g_n(current.right);
            int f_n = H_n + G_n;
            if(f_n < bestDistance) 
            { 
                bestDistance = f_n; 
                best = current.right; 
            }
        }

        return best;
    }

    public int g_n(Node node)
    {
        int count = 0;
        for(Node ancestor = node; ancestor != null; ancestor = ancestor.getParent())
        {
            char cell = grid[ancestor.getPosition().getX()][ancestor.getPosition().getY()];
            if(cell == 'X') { count += 1; }
            else if(cell == 'M') { count += 2; }
            else if(cell == '.') { count += 1; }
            else if(cell == 'B') { count += 3; }
        }
        return count;
    }
    public ArrayList<Node> UCS()
    {
        if(grid == null || start == null)
        {
            return new ArrayList<>();
        }
        resetVisited();
        mark_walls_visited();
        Node goal = findGoal();
        if(goal == null)
        {
            System.out.println("NO GOAL FOUND!");
            return fringe;
        }
        root = new Node(start);
        Node startNode = new Node(new Position(start.getX(), start.getY()));
        fringe.add(startNode);
        visited[start.getX()][start.getY()] = true;
        Node current = startNode;

        int top = 0;
        int left = 0;
        int bottom = rows - 1;
        int right = cols - 1;
        int initialWillPower = 0;
        boolean foundTreasure = false;

        while(!foundTreasure)
        {
            if(grid[current.getPosition().getX()][current.getPosition().getY()] == 'X')
            {
                treasureNode = current;
                foundTreasure = true;
                break;
            }
            
            Node best = Cost_Search(current, goal, top, left, bottom, right);
            if(best == null)            
            {
                System.out.println("NO PATH FOUND!");
                break;     
            }
            fringe.add(best);
            visited[best.getPosition().getX()][best.getPosition().getY()] = true;
            char cell = grid[best.getPosition().getX()][best.getPosition().getY()];
            if(cell == 'X') { treasureNode = best; foundTreasure = true; initialWillPower += 1; }
            else if(cell == 'M') { initialWillPower += 2; }
            else if(cell == 'B') { initialWillPower += 3; }

            Position teleportPos = teleport(best.getPosition().getX(), best.getPosition().getY());
            if(teleportPos != null)
            {
                Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), best);
                fringe.add(node);
                visited[teleportPos.getX()][teleportPos.getY()] = true;
                current = node;
                initialWillPower += 1;
            }
            else
            {
                current = best;
            }
        }
        this.willPower = initialWillPower;
        return fringe;
    }

    public Node findGoal()
    {
        for(int r = 0; r < rows; r++)
        {
            for(int c = 0; c < cols; c++)
            {
                if(grid[r][c] == 'X')
                {
                    return new Node(new Position(r, c));
                }
            }
        }
        return null;
    }
    public ArrayList<Node> A_star()
    {
        if(grid == null || start == null)
        {
            return new ArrayList<>();
        }
        Queue<Node> qui = new LinkedList<>();
        resetVisited();
        mark_walls_visited();

        Node goal = findGoal();
        if(goal == null)
        {
            System.out.println("NO GOAL FOUND!");
            return fringe;
        }

        root = new Node(start);
        Node startNode = new Node(new Position(start.getX(), start.getY()));
        fringe.add(startNode);
        visited[start.getX()][start.getY()] = true;
        Node current = startNode;

        int top = 0;
        int left = 0;
        int bottom = rows - 1;
        int right = cols - 1;
        int initialWillPower = 0;
        boolean foundTreasure = false;

        while(!foundTreasure)
        {
            if(grid[current.getPosition().getX()][current.getPosition().getY()] == 'X')
            {
                treasureNode = current;
                foundTreasure = true;
                break;
            }
            
            Node best = h_n(current, goal, top, left, bottom, right);
            if(best == null)            
            {
                System.out.println("NO PATH FOUND!");
                break;     
            }
            fringe.add(best);
            visited[best.getPosition().getX()][best.getPosition().getY()] = true;
            char cell = grid[best.getPosition().getX()][best.getPosition().getY()];
            if(cell == 'X') { treasureNode = best; foundTreasure = true; initialWillPower += 1; }
            else if(cell == 'M') { initialWillPower += 2; }
            else if(cell == 'B') { initialWillPower += 3; }

            Position teleportPos = teleport(best.getPosition().getX(), best.getPosition().getY());
            if(teleportPos != null)
            {
                Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), best);
                fringe.add(node);
                visited[teleportPos.getX()][teleportPos.getY()] = true;
                current = node;
                initialWillPower += 1;
            }
            else
            {
                current = best;
            }
        }

        this.willPower = initialWillPower;
        return fringe;


    }

    public ArrayList<Node> greedy_Search()
    {
        if(grid == null || start == null)
        {
            return new ArrayList<>();
        }
        resetVisited();
        mark_walls_visited();

        Node goal = findGoal();
        if(goal == null)
        {
            System.out.println("NO GOAL FOUND!");
            return fringe;
        }

        root = new Node(start);
        Node startNode = new Node(new Position(start.getX(), start.getY()));
        fringe.add(startNode);
        visited[start.getX()][start.getY()] = true;
        Node current = startNode;

        int top = 0;
        int left = 0;
        int bottom = rows - 1;
        int right = cols - 1;
        int initialWillPower = 0;
        boolean foundTreasure = false;

        while(!foundTreasure)
        {
            if(grid[current.getPosition().getX()][current.getPosition().getY()] == 'X')
            {
                treasureNode = current;
                foundTreasure = true;
                break;
            }

            Node best = findBestNeighbor(current, goal, top, left, bottom, right);
            if(best == null)
            {
                System.out.println("NO PATH FOUND!");
                break;
            }

            fringe.add(best);
            visited[best.getPosition().getX()][best.getPosition().getY()] = true;

            char cell = grid[best.getPosition().getX()][best.getPosition().getY()];
            if(cell == 'X') { treasureNode = best; foundTreasure = true; initialWillPower += 1; }
            else if(cell == 'M') { initialWillPower += 2; }
            else if(cell == 'B') { initialWillPower += 3; }

            Position teleportPos = teleport(best.getPosition().getX(), best.getPosition().getY());
            if(teleportPos != null)
            {
                Node node = new Node(new Position(teleportPos.getX(), teleportPos.getY()), best);
                fringe.add(node);
                visited[teleportPos.getX()][teleportPos.getY()] = true;
                current = node;
                initialWillPower += 1;
            }
            else
            {
                current = best;
            }
        }

        this.willPower = initialWillPower;
        return fringe;
    }
    public ArrayList<Node> Hill_Climbing()
    {
        if(grid == null || start == null)
        {
            return new ArrayList<>();
        }
        resetVisited();
        mark_walls_visited();

        Node goal = findGoal();
        if(goal == null)
        {
            System.out.println("NO GOAL FOUND!");
            return fringe;
        }

        root = new Node(start);
        Node startNode = new Node(new Position(start.getX(), start.getY()));
        fringe.add(startNode);
        visited[start.getX()][start.getY()] = true;
        Node current = startNode;

        int top = 0;
        int left = 0;
        int bottom = rows - 1;
        int right = cols - 1;
        int initialWillPower = 0;
        boolean foundTreasure = false;

        while(!foundTreasure)
        {
            if(grid[current.getPosition().getX()][current.getPosition().getY()] == 'X')
            {
                treasureNode = current;
                foundTreasure = true;
                break;
            }

            Node best = findBestNode(current, goal, top, left, bottom, right);
            if(best == null)
            {
                System.out.println("NO PATH FOUND!");
                break;
            }

            fringe.add(best);
            visited[best.getPosition().getX()][best.getPosition().getY()] = true;

            char cell = grid[best.getPosition().getX()][best.getPosition().getY()];
            if(cell == 'X') { treasureNode = best; foundTreasure = true; initialWillPower += 1; }
            else if(cell == 'M') { initialWillPower += 2; }
            else if(cell == '.') { initialWillPower += 1; }
            else if(cell == 'B') { initialWillPower += 3; }
        }
        this.willPower = initialWillPower;
        return fringe;
    }
}

//S, .  .
//.  .  .
//.  .  X

/*
S   B   B   B
B   B   B   B
B   B   B   X



*/


