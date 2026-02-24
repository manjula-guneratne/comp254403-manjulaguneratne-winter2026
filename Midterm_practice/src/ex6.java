public class ex6 {

    private static int total = 0;

    static class Node {
        int data;
        Node next;
        Node(int d){ data = d; }
    }

    public static int sum(Node head) {

        // Base case
        if(head == null)
            return 0;

        return head.data + sum(head.next);
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        System.out.println(sum(head)); // 6
    }

}
