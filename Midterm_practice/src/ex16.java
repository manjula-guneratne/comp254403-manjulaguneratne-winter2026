import java.util.*;

public class ex16 {

    static class Node {
        int data;
        Node next;
        Node(int d){ data = d; }
    }

    Node head;

    // Removes duplicates in-place
    public void removeDuplicates() {

        if (head == null) return;

        HashSet<Integer> seen = new HashSet<>();

        Node current = head;
        Node prev = null;

        while (current != null) {

            if (seen.contains(current.data)) {
                // Duplicate → remove node
                prev.next = current.next;
            } else {
                // First time seeing this value
                seen.add(current.data);
                prev = current;
            }

            current = current.next;
        }
    }

    // Build list from array
    public static ex16 fromArray(int[] a) {
        ex16 list = new ex16();
        if (a.length == 0) return list;

        list.head = new Node(a[0]);
        Node tail = list.head;

        for (int i = 1; i < a.length; i++) {
            tail.next = new Node(a[i]);
            tail = tail.next;
        }

        return list;
    }

    // Convert list to comma-separated string
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node curr = head;

        while (curr != null) {
            sb.append(curr.data);
            if (curr.next != null) sb.append(",");
            curr = curr.next;
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        ex16 l = fromArray(new int[]{1,2,3,2,4,1});
        l.removeDuplicates();
        System.out.println(l);  // EXPECTED: 1,2,3,4
    }
}