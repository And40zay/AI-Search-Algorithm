import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String strategy;
        String filename;
        
        if (args.length < 2) {
            strategy = "A";
            filename = "data/example6.txt";  // Relative path from project root
        } else {
            strategy = args[0];
            filename = args[1];
        }
        
        runStrategy(strategy, filename);
    }
    
    public static void runStrategy(String strategy, String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            char[][] grid = null;
            int rows = 0;
            int cols = 0;
            int currentRow = 0;
            Position startPosition = null;

            while(scanner.hasNextLine()) 
            {
                String line = scanner.nextLine().trim();
                
                if(line.contains("x")) 
                {
                    String[] gridPath = line.split("x");
                    rows = Integer.parseInt(gridPath[1]);
                    cols = Integer.parseInt(gridPath[0]);  
                    grid = new char[rows][cols];
                
                }
                else if(line.contains("-")) 
                {
                    String[] coord = line.split("-");
                    int x = Integer.parseInt(coord[1]);//check if coord is the same as matrix
                    int y = Integer.parseInt(coord[0]);
                    
                    startPosition = new Position(x, y);
                }
                else if (!line.isEmpty() && grid != null && currentRow < rows) 
                {
                    for (int col = 0; col < cols && col < line.length(); col++) 
                    {
                        grid[currentRow][col] = line.charAt(col);
                    }
                    currentRow++;
                }
            }
            
            if (grid != null && startPosition != null) //rREMOVE ME
            {
                grid[startPosition.getX()][startPosition.getY()] = 'S';

                for (int r = 0; r < rows; r++) 
                {
                    for (int c = 0; c < cols; c++) 
                    {
                        if(grid[r][c] == 'X')
                        {
                            Node goal = new Node(new Position(r, c), null);
                        }
                        System.out.print(grid[r][c]);
                    }
                    System.out.println();
                }

                Tree tree = new Tree(grid, rows, cols, startPosition);
                Node node = new Node(startPosition);
                tree.setRoot(node);

                switch(strategy)
                {
                    case "B":
                        System.out.println("BFS Search Initiated");
                        ArrayList<Node> bfsFringe = tree.BFS();
                        StringBuilder bfsExpandedStr = new StringBuilder();
                        for(Node n : bfsFringe)
                        {
                            bfsExpandedStr.append(n.getPosition().toString());
                        }
                        System.out.println("Expanded: " + bfsExpandedStr.toString());
                        List<Position> bfsPath = tree.backtrackPath();
                        System.out.println("Path Found: " + bfsPath.toString());
                        System.out.println("Taking this path will cost: " + tree.getWillPower() + " Willpower");
                        break;
                    case "D":
                        System.out.println("DFS Search Initiated");
                        ArrayList<Node> fringe = tree.buildTree(tree);
                        StringBuilder expandedStr = new StringBuilder();
                        for(Node n : fringe)
                        {
                            expandedStr.append(n.getPosition().toString());
                        }
                        System.out.println("Expanded: " + expandedStr.toString());
                        List<Position> path = tree.backtrackPath();
                        System.out.println("Path Found: " + path.toString());
                        System.out.println("Taking this path will cost: " + tree.getWillPower() + " Willpower");
                        break;
                    case "U":
                        System.out.println("UCS Search Initiated");
                        ArrayList<Node> ucsFringe = tree.UCS();
                        StringBuilder ucsExpandedStr = new StringBuilder();
                        for(Node n : ucsFringe)
                        {
                            ucsExpandedStr.append(n.getPosition().toString());
                        }
                        System.out.println("Expanded: " + ucsExpandedStr.toString());
                        List<Position> ucsPath = tree.backtrackPath();
                        System.out.println("Path Found: " + ucsPath.toString());
                        System.out.println("Taking this path will cost: " + tree.getWillPower() + " Willpower");
                        break;
                    case "G":
                        System.out.println("Greedy Search Initiated");
                        ArrayList<Node> greedyFringe = tree.greedy_Search();
                        StringBuilder greedyExpandedStr = new StringBuilder();
                        for(Node n : greedyFringe)
                        {
                            greedyExpandedStr.append(n.getPosition().toString());
                        }
                        System.out.println("Expanded: " + greedyExpandedStr.toString());
                        List<Position> greedyPath = tree.backtrackPath();
                        System.out.println("Path Found: " + greedyPath.toString());
                        System.out.println("Taking this path will cost: " + tree.getWillPower() + " Willpower");
                        break;
                    case "A":
                        System.out.println("A* Search Initiated");
                        ArrayList<Node> aStarFringe = tree.A_star();
                        StringBuilder aStarExpandedStr = new StringBuilder();
                        for(Node n : aStarFringe)                        {
                            aStarExpandedStr.append(n.getPosition().toString());
                        }
                        System.out.println("Expanded: " + aStarExpandedStr.toString());
                        List<Position> aStarPath = tree.backtrackPath();
                        System.out.println("Path Found: " + aStarPath.toString());
                        System.out.println("Taking this path will cost: " + tree.getWillPower() + " Willpower");
                        break;
                    case "H":
                        System.out.println("Hill-Climbing Search Initiated");
                        ArrayList<Node> hill_climb = tree.Hill_Climbing();
                         StringBuilder hillClimbExpandedStr = new StringBuilder();
                        for(Node n : hill_climb)                        {
                            hillClimbExpandedStr.append(n.getPosition().toString());
                        }
                        System.out.println("Expanded: " + hillClimbExpandedStr.toString());
                         List<Position> hillClimbPath = tree.backtrackPath();
                        System.out.println("Path Found: " + hillClimbPath.toString());  
                        System.out.println("Taking this path will cost: " + tree.getWillPower() + " Willpower");
                        break;
                    default:
                        System.out.println("Invalid strategy. Please choose B, D, U, G, A, or H.");
                        return;
                }
            }
        } 
        catch(IOException e) 
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
}


