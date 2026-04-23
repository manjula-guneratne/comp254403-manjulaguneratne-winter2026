import java.util.ArrayDeque;
import java.util.Deque;

public class Exercise2_NodesAtDepth {

    static class Node {
        int v;
        Node l, r;
        Node(int v) { this.v = v; }
    }

    // ── Solution A: BFS (level-order) ─────────────────────────────────────────
    /**
     * Count nodes exactly at depth d using BFS.
     * We track each node's depth alongside it in the queue.
     *
     * Time:  O(n)  — visits every node at most once.
     * Space: O(w)  — w = max width of the tree (queue size).
     */
    public static int countAtDepth_BFS(Node root, int d) {
        if (root == null) return 0;

        // Queue stores pairs: [node, currentDepth]
        // We simulate pairs using two parallel ArrayDeques
        Deque<Node> nodeQ  = new ArrayDeque<>();
        Deque<Integer> depQ = new ArrayDeque<>();
        nodeQ.add(root);
        depQ.add(0);

        int count = 0;
        while (!nodeQ.isEmpty()) {
            Node cur  = nodeQ.poll();
            int  dep  = depQ.poll();

            if (dep == d)  count++;            // found a node at target depth
            if (dep >= d)  continue;           // no need to go deeper

            if (cur.l != null) { nodeQ.add(cur.l); depQ.add(dep + 1); }
            if (cur.r != null) { nodeQ.add(cur.r); depQ.add(dep + 1); }
        }
        return count;
    }

    // ── Solution B: DFS (recursive) ───────────────────────────────────────────
    /**
     * Count nodes exactly at depth d using recursive DFS.
     * When depth==0 and node is non-null, that node is at the target level.
     *
     * Time:  O(n)  — worst case visits every node.
     * Space: O(h)  — recursion stack.
     */
    public static int countAtDepth(Node root, int d) {
        if (root == null) return 0;
        if (d == 0)       return 1;            // this node is at the target depth
        return countAtDepth(root.l, d - 1)
             + countAtDepth(root.r, d - 1);
    }

    public static void main(String[] args) {

        // ── Tree from the exercise sheet ──────────────────────────────────────
        //
        //          1              <- depth 0  (1 node)
        //         / \
        //        2   3            <- depth 1  (2 nodes)
        //       / \    \
        //      4   5    6         <- depth 2  (3 nodes)
        //
        Node r = new Node(1);
        r.l = new Node(2);  r.r = new Node(3);
        r.l.l = new Node(4); r.l.r = new Node(5); r.r.r = new Node(6);

        System.out.println("=== DFS solution ===");
        System.out.println("depth 0: " + countAtDepth(r, 0));  // 1
        System.out.println("depth 1: " + countAtDepth(r, 1));  // 2
        System.out.println("depth 2: " + countAtDepth(r, 2));  // 3
        System.out.println("depth 3: " + countAtDepth(r, 3));  // 0  (no nodes)

        System.out.println("\n=== BFS solution ===");
        System.out.println("depth 0: " + countAtDepth_BFS(r, 0));  // 1
        System.out.println("depth 1: " + countAtDepth_BFS(r, 1));  // 2
        System.out.println("depth 2: " + countAtDepth_BFS(r, 2));  // 3
        System.out.println("depth 3: " + countAtDepth_BFS(r, 3));  // 0

        System.out.println("\n=== PASS checks ===");
        System.out.println("d=0 PASS: " + (countAtDepth(r, 0) == 1));
        System.out.println("d=1 PASS: " + (countAtDepth(r, 1) == 2));
        System.out.println("d=2 PASS: " + (countAtDepth(r, 2) == 3));
        System.out.println("d=3 PASS: " + (countAtDepth(r, 3) == 0));

        // ── Additional test: balanced full tree, depth 3 ─────────────────────
        //
        //              A                    depth 0: 1
        //           /     \
        //          B       C                depth 1: 2
        //         / \     / \
        //        D   E   F   G              depth 2: 4
        //       /
        //      H                            depth 3: 1

        Node a = new Node(1);
        a.l = new Node(2);   a.r = new Node(3);
        a.l.l = new Node(4); a.l.r = new Node(5);
        a.r.l = new Node(6); a.r.r = new Node(7);
        a.l.l.l = new Node(8);

        System.out.println("\n=== Deeper tree test ===");
        System.out.println("depth 0: " + countAtDepth(a, 0));  // 1
        System.out.println("depth 1: " + countAtDepth(a, 1));  // 2
        System.out.println("depth 2: " + countAtDepth(a, 2));  // 4
        System.out.println("depth 3: " + countAtDepth(a, 3));  // 1

        // ── Trace ─────────────────────────────────────────────────────────────
        System.out.println("\n--- Recursive trace for countAtDepth(r, 2) ---");
        System.out.println("countAtDepth(1, 2)");
        System.out.println("  = countAtDepth(2, 1) + countAtDepth(3, 1)");
        System.out.println("  = [countAtDepth(4,0) + countAtDepth(5,0)] + [countAtDepth(null,0) + countAtDepth(6,0)]");
        System.out.println("  = [1 + 1] + [0 + 1]  =  3");
    }
}
