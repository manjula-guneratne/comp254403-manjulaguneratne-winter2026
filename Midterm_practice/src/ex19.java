
public class ex19 {

    static class Node {
        int data;
        Node next;
        Node(int d){ data=d; }
    }

    public static boolean contains(Node head, int key) {

        // ----- Base Case 1: reached end of list -----
        if (head == null)
            return false;

        // ----- Base Case 2: found the key -----
        if (head.data == key)
            return true;

        // ----- Recursive Case: search the rest -----
        return contains(head.next, key);
    }

    public static void main(String[] args) {
        Node head = new Node(5);
        head.next = new Node(3);
        head.next.next = new Node(9);

        System.out.println(contains(head,3));  // true
        System.out.println(contains(head,7));  // false
    }
}