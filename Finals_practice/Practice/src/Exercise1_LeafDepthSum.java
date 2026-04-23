public class Exercise1_LeafDepthSum {

    static class Node {
        int val;
        Node left, right;
        Node(int v){ val=v; }
    }
    public static int sumLeafDepths(Node root) {
        return dfs(root,0);
    }

    private static int dfs(Node node, int depth){
        if (node == null) return 0;  //empty
        if (node.left == null && node.right == null)
            return depth;
        return dfs(node.left, depth+1) +
                dfs(node.right, depth + 1);
    }

    public static void main(String[] args) {
        Node r = new Node(10);
        r.left = new Node(5);
        r.right = new Node(20);
        r.left.left = new Node(3);
        r.right.right = new Node(30);
        System.out.println(sumLeafDepths(r)); // expected: 1 (node 3 at depth2) + 1 (node 30 at depth2) = 4
    }
}
