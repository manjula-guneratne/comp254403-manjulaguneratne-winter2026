public class Exercise7_BST {

    static class Node {
        int k;
        Node l, r;
        Node(int k) { this.k = k; }
    }

    Node root;

    // ─── insert ───────────────────────────────────────────────────────────────
    /**
     * Inserts key k into the BST.  Duplicates are ignored.
     *
     * Rule: go LEFT if k < node.k, go RIGHT if k > node.k.
     * When we reach null, that is where the new node belongs.
     *
     * Time: O(h)  h = height of tree (O(log n) average, O(n) worst).
     */
    public void insert(int k) {
        root = insertRec(root, k);
    }

    private Node insertRec(Node node, int k) {
        if (node == null) return new Node(k);   // found the correct empty slot
        if      (k < node.k) node.l = insertRec(node.l, k);
        else if (k > node.k) node.r = insertRec(node.r, k);
        // k == node.k → duplicate, do nothing
        return node;
    }

    // ─── contains ────────────────────────────────────────────────────────────
    /**
     * Returns true if key k exists in the BST.
     *
     * Same navigation as insert: go left/right based on comparison.
     * Returns false when we fall off the tree (null).
     *
     * Time: O(h)
     */
    public boolean contains(int k) {
        return containsRec(root, k);
    }

    private boolean containsRec(Node node, int k) {
        if (node == null)  return false;          // fell off tree — not found
        if (k == node.k)   return true;           // found
        if (k < node.k)    return containsRec(node.l, k);
        return               containsRec(node.r, k);
    }

    // ─── inorder (Left → Root → Right) ───────────────────────────────────────
    /**
     * Prints all keys in ascending (sorted) order.
     * Inorder traversal of a BST always gives sorted output — this is
     * the key property used by TreeMap.entrySet().
     *
     * Time: O(n)
     */
    public void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(Node n) {
        if (n == null) return;
        inorder(n.l);            // recurse left subtree first
        System.out.print(n.k + " ");
        inorder(n.r);            // recurse right subtree last
    }

    // ─── extra: delete (for completeness) ────────────────────────────────────
    /**
     * Removes key k from the BST.
     * Three cases:
     *   1. No children  → just remove the node.
     *   2. One child    → promote the child.
     *   3. Two children → replace with inorder successor (leftmost in right subtree),
     *                     then delete the successor from the right subtree.
     */
    public void delete(int k) { root = deleteRec(root, k); }

    private Node deleteRec(Node node, int k) {
        if (node == null) return null;
        if      (k < node.k) { node.l = deleteRec(node.l, k); }
        else if (k > node.k) { node.r = deleteRec(node.r, k); }
        else {
            // Found the node to delete
            if (node.l == null) return node.r;   // case 1 or 2
            if (node.r == null) return node.l;   // case 2
            // Case 3: find inorder successor (min of right subtree)
            Node successor = minNode(node.r);
            node.k = successor.k;
            node.r = deleteRec(node.r, successor.k);
        }
        return node;
    }

    private Node minNode(Node node) {
        while (node.l != null) node = node.l;
        return node;
    }

    // ─── height ──────────────────────────────────────────────────────────────
    public int height() { return heightRec(root); }
    private int heightRec(Node n) {
        if (n == null) return -1;
        return 1 + Math.max(heightRec(n.l), heightRec(n.r));
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  main
    // ═════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {

        System.out.println("════ Exercise 7 — Binary Search Tree ════\n");

        // ── Test 1: exercise-sheet sequence ───────────────────────────────────
        //
        //   Insert: 5, 2, 8, 1, 3
        //
        //         5            <- root
        //        / \
        //       2   8
        //      / \
        //     1   3
        //
        Exercise7_BST t = new Exercise7_BST();
        int[] keys = {5, 2, 8, 1, 3};
        for (int k : keys) t.insert(k);

        System.out.println("contains(3) = " + t.contains(3)); // true
        System.out.println("contains(9) = " + t.contains(9)); // false
        System.out.print("inorder:       "); t.inorder();      // 1 2 3 5 8

        // PASS checks
        System.out.println("\n--- PASS checks ---");
        System.out.println("contains(3) = true  : " + (t.contains(3) == true));
        System.out.println("contains(9) = false : " + (t.contains(9) == false));

        // ── Test 2: duplicates are ignored ────────────────────────────────────
        System.out.println("\n--- Test 2: duplicate insertion ---");
        t.insert(5);  // duplicate root
        t.insert(2);  // duplicate interior node
        System.out.print("Inorder (duplicates ignored): "); t.inorder(); // still 1 2 3 5 8

        // ── Test 3: delete ────────────────────────────────────────────────────
        System.out.println("\n--- Test 3: delete ---");
        System.out.print("Before delete(2): "); t.inorder();  // 1 2 3 5 8
        t.delete(2);                                           // node with 2 children
        System.out.print("After  delete(2): "); t.inorder();  // 1 3 5 8
        t.delete(8);                                           // leaf
        System.out.print("After  delete(8): "); t.inorder();  // 1 3 5

        // ── Test 4: sorted input → degenerate tree ────────────────────────────
        System.out.println("\n--- Test 4: sorted insertion (worst case) ---");
        Exercise7_BST worst = new Exercise7_BST();
        for (int k : new int[]{1, 2, 3, 4, 5}) worst.insert(k);
        System.out.println("Height of sorted-input BST: " + worst.height()); // 4 (like a linked list)
        System.out.print("Inorder: "); worst.inorder();  // 1 2 3 5

        // ── Test 5: bigger tree ───────────────────────────────────────────────
        System.out.println("\n--- Test 5: insert 6,2,9,1,4,8 ---");
        Exercise7_BST t2 = new Exercise7_BST();
        for (int k : new int[]{6, 2, 9, 1, 4, 8}) t2.insert(k);
        System.out.print("Inorder: "); t2.inorder(); // 1 2 4 6 8 9
        System.out.println("Height : " + t2.height()); // 2
        System.out.println("contains(4) = " + t2.contains(4)); // true
        System.out.println("contains(7) = " + t2.contains(7)); // false

        // ── Key property reminder ─────────────────────────────────────────────
        System.out.println("\n--- Key property ---");
        System.out.println("Inorder traversal of a BST always produces keys in SORTED order.");
        System.out.println("This is why TreeMap.entrySet() uses tree.inorder().");
    }
}
