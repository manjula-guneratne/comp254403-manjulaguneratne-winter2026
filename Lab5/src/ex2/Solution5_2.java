package ex2;

//Node class
class Node{
    int key;
    Node left, right;

    Node(int item){
        key = item;
        left = right = null;
    }
}

public class Solution5_2 {

    Node root;

    public Solution5_2(){
        root = null;
    }

    //Insert a new node with a given key
    public void insert(int key) {
        root = insertRec(root, key);
    }

    private Node insertRec(Node root, int key) {
        if (root == null) {
            root = new Node(key);
            return root;
        }
        if (key < root.key)
            root.left = insertRec(root.left, key);
        else if (key > root.key)
            root.right = insertRec(root.right, key);
        return root;
    }

    public void postorder() {
        postorderRec(root);
    }

    private void postorderRec(Node root) {
        if (root != null) {
            postorderRec(root.left);
            postorderRec(root.right);
            System.out.print(root.key + " "); // ← node visited LAST, after both children
        }
    }

    public void printHeights() {
        System.out.printf("%-10s %-15s%n", "Element", "Subtree Height");
        System.out.println("─────────────────────────────");
        printHeightsRec(root);
    }

    private int printHeightsRec(Node node) {
        if (node == null)
            return -1;                                       // empty subtree

        int leftHeight  = printHeightsRec(node.left);       // 1. recurse left
        int rightHeight = printHeightsRec(node.right);      // 2. recurse right

        int height = 1 + Math.max(leftHeight, rightHeight); // 3. compute height

        System.out.printf("%-10d %-15d%n", node.key, height); // 4. print (postorder visit)

        return height;                                       // 5. return to parent
    }

        public static void main(String[] args) {

        Solution5_2 tree = new Solution5_2();

        // Same insertions as the original code
        tree.insert(50);
        tree.insert(30);
        tree.insert(20);
        tree.insert(40);
        tree.insert(70);
        tree.insert(60);
        tree.insert(80);

        System.out.print("Inorder traversal: ");
        tree.postorder();
        System.out.println("\n");

        System.out.println("=== Exercise 2 — Element + Subtree Height ===");
        System.out.println("(printed in postorder — children before parent)\n");
        tree.printHeights();

        // ── verify expected results ───────────────────────────────────────────
        System.out.println("\nExpected:");
        System.out.println("  Leaves (20, 40, 60, 80)  → height 0");
        System.out.println("  Internal (30, 70)         → height 1");
        System.out.println("  Root (50)                 → height 2");
    }
}
