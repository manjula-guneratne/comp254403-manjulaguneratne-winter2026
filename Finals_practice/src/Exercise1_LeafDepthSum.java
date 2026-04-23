public class Exercise1_LeafDepthSum {

    static class Node {
        int val;
        Node left, right;
        Node(int v) { val = v; }
    }

    /**
     * Returns the sum of depths of all LEAF nodes.
     * A leaf has no left and no right child.
     *
     * Strategy: DFS, carrying current depth.
     * - If at a leaf  → add depth to total.
     * - Otherwise     → recurse into existing children.
     *
     * Time:  O(n)  — every node visited once.
     * Space: O(h)  — recursion stack = tree height.
     */
    public static int sumLeafDepths(Node root) {
        return dfs(root, 0);
    }

    private static int dfs(Node node, int depth) {
        if (node == null) return 0;                          // empty subtree
        if (node.left == null && node.right == null)
            return depth;                                    // leaf → contribute depth
        return dfs(node.left,  depth + 1)
             + dfs(node.right, depth + 1);                   // recurse both sides
    }

    public static void main(String[] args) {

        // ── Test 1: tree from the exercise sheet ──────────────────────────────
        //
        //        10          <- depth 0  (internal)
        //       /  \
        //      5   20        <- depth 1  (internal)
        //     /      \
        //    3        30     <- depth 2  (both LEAVES)
        //
        // Leaves: 3 (depth 2) and 30 (depth 2)  =>  sum = 2 + 2 = 4

        Node r1 = new Node(10);
        r1.left  = new Node(5);
        r1.right = new Node(20);
        r1.left.left   = new Node(3);
        r1.right.right = new Node(30);

        int result1 = sumLeafDepths(r1);
        System.out.println("Test 1 result : " + result1);  // expected: 4
        System.out.println("Test 1 PASS   : " + (result1 == 4));

        // ── Test 2: single node ───────────────────────────────────────────────
        //   Root is the only node → it is also a leaf at depth 0 → sum = 0
        Node r2 = new Node(42);
        int result2 = sumLeafDepths(r2);
        System.out.println("\nTest 2 result : " + result2);  // expected: 0
        System.out.println("Test 2 PASS   : " + (result2 == 0));

        // ── Test 3: 3-level balanced tree ────────────────────────────────────
        //
        //          1         <- depth 0
        //        /   \
        //       2     3      <- depth 1
        //      / \   / \
        //     4   5 6   7   <- depth 2  (all leaves)
        //
        // 4 leaves × depth 2 = 8

        Node r3 = new Node(1);
        r3.left  = new Node(2);  r3.right = new Node(3);
        r3.left.left  = new Node(4); r3.left.right  = new Node(5);
        r3.right.left = new Node(6); r3.right.right = new Node(7);

        int result3 = sumLeafDepths(r3);
        System.out.println("\nTest 3 result : " + result3);  // expected: 8
        System.out.println("Test 3 PASS   : " + (result3 == 8));

        // ── Test 4: right-skewed path (worst-case height = n-1) ──────────────
        //
        //   1 → 2 → 3 → 4 → 5   (only node 5 is a leaf at depth 4)
        //
        Node r4 = new Node(1);
        r4.right = new Node(2);
        r4.right.right = new Node(3);
        r4.right.right.right = new Node(4);
        r4.right.right.right.right = new Node(5);

        int result4 = sumLeafDepths(r4);
        System.out.println("\nTest 4 result : " + result4);  // expected: 4
        System.out.println("Test 4 PASS   : " + (result4 == 4));

        // ── Trace explanation ─────────────────────────────────────────────────
        System.out.println("\n--- Trace for Test 1 ---");
        System.out.println("dfs(10, 0) -> internal, recurse");
        System.out.println("  dfs(5,  1) -> internal, recurse");
        System.out.println("    dfs(3,  2) -> LEAF! return 2");
        System.out.println("    dfs(null,2) -> return 0");
        System.out.println("  dfs(20, 1) -> internal, recurse");
        System.out.println("    dfs(null,2) -> return 0");
        System.out.println("    dfs(30, 2) -> LEAF! return 2");
        System.out.println("Total = 2 + 2 = 4");
    }
}
