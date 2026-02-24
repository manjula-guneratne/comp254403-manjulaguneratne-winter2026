public class ex13 {

    static class Node {
        int data;
        Node next;

        Node(int d) { data = d; }
    }

    public static int josephus(int n, int k) {

        Node head = null;
        Node prev = null;

        // ---------- BUILD THE CIRCLE ----------
        for (int i = 1; i <= n; i++) {

            Node current = new Node(i);   // FIXED: use i, not k

            if (head == null) {           // first node
                head = current;           // FIXED: set head
            } else {
                prev.next = current;      // link previous → current
            }

            prev = current;               // move prev forward
        }

        prev.next = head;                 // close the circle

        // ---------- JOSEPHUS ELIMINATION ----------
        Node curr = prev;   // start just before head (helps deletion)

        while (curr.next != curr) {  // more than one person left

            // move k-1 steps
            for (int i = 1; i < k; i++) {
                curr = curr.next;
            }

            // delete k-th person
            curr.next = curr.next.next;
        }

        return curr.data;  // survivor
    }

    public static void main(String[] args) {
        System.out.println(josephus(7,3)); // 4
    }
}