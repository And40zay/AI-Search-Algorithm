import java.util.ArrayList;
import java.util.List;

public class Node {
    private final Position position;
    private final List<Node> children;
    private Node parent;
    Node left;
    Node right;
    Node up;
    Node down;
    private Position teleportExit;

    public Node(Position position) {
        this(position, null);
    }

    public Node(Position position, Node parent) {
        this.position = position;
        this.parent = parent;
        this.children = new ArrayList<>(); 
        this.left = null;
        this.right = null;
        this.up = null;
        this.down = null;

    }

    public Position getPosition() {
        return position;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public List<Node> getChildren() {
        return children;
    }

    public Position getTeleportExit() {
        return teleportExit;
    }

    public void setTeleportExit(Position teleportExit) {
        this.teleportExit = teleportExit;
    }

    @Override
    public String toString() {
        // Swapped: show (y, x) instead of (x, y) per university format
        return "(" + position.getY() + ", " + position.getX() + ")";
    }
}