import java.util.*;

public class Exercise2_NodesAtDepth {

    static class Node{
        int v;
        Node l,r;
        Node(int v) {this.v = v;}
    }

    public static int countAtDepth_BFS(Node root, int d){
        if(root == null) return 0;

        Deque<Node> nodeQ = new ArrayDeque<>();
        Deque<Integer> depQ = new ArrayDeque<>();
        nodeQ.add(root);
        depQ.add(0);

        int count =0;
        while(!nodeQ.isEmpty()){
            Node cur = nodeQ.poll();
            int dep = depQ.poll();

            if (dep ==d) count++;
            if (dep >= d) continue;

            if(cur.l != null) {
                nodeQ.add(cur.l);
                depQ.add(dep +1);
            }
            if(cur.r != null) {
                nodeQ.add(cur.r);
                depQ.add(dep +1);
            }
        }

        return count;
    }

    public static  int countAtDepth(Node root, int d){
        if(root == null) return 0;
        if(d == 0) return 1;
        return countAtDepth(root.l, d-1)
                + countAtDepth(root.r, d-1);
    }

    public static void main(String[] args){
        Node r=new Node(1);
        r.l=new Node(2); r.r=new Node(3);
        r.l.l=new Node(4); r.l.r=new Node(5); r.r.r=new Node(6);
        System.out.println(countAtDepth(r,0)); // 1
        System.out.println(countAtDepth(r,1)); // 2
        System.out.println(countAtDepth(r,2)); // 3
    }
}
