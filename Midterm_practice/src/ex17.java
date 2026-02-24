
import java.util.*;

public class ex17 {

    // ----- Array Linear Search -----
    public static int linearSearchArray(int[] a, int key) {
        for (int i = 0; i < a.length; i++)
            if (a[i] == key) return i;
        return -1;
    }

    // ----- Linked List Linear Search -----
    public static int linearSearchList(SinglyLinkedList.Node head, int key) {
        SinglyLinkedList.Node current = head;

        int index = 0;
        while (current != null) {
            if (current.data == key)
                return index;

            current = current.next;
            index++;
        }
        return -1;
    }

    public static void main(String[] args) {

        int N = 100_000;

        // Build array 0..N-1
        int[] array = new int[N];
        for (int i = 0; i < N; i++)
            array[i] = i;

        // Build linked list 0..N-1
        SinglyLinkedList.Node head = buildList(N);

        int key = -1; // NOT PRESENT (worst-case search)

        // -------- Measure Array Search --------
        long t1 = System.nanoTime();
        for (int i = 0; i < 1_000_000; i++)
            linearSearchArray(array, key);
        long t2 = System.nanoTime();

        // -------- Measure List Search --------
        long t3 = System.nanoTime();
        for (int i = 0; i < 1_000_000; i++)
            linearSearchList(head, key);
        long t4 = System.nanoTime();

        System.out.println("Array search time: " + (t2 - t1) / 1e6 + " ms");
        System.out.println("List search time:  " + (t4 - t3) / 1e6 + " ms");
    }

    // ----- Build singly linked list 0→1→…→N-1 -----
    static SinglyLinkedList.Node buildList(int N) {

        if (N == 0) return null;

        SinglyLinkedList.Node head = new SinglyLinkedList.Node(0);
        SinglyLinkedList.Node current = head;

        for (int i = 1; i < N; i++) {
            current.next = new SinglyLinkedList.Node(i);
            current = current.next;
        }

        return head;
    }
}


// Minimal Node definition (if not already provided)
class SinglyLinkedList {
    static class Node {
        int data;
        Node next;
        Node(int d){ data = d; }
    }
}