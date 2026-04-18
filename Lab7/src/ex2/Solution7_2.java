package ex2;

import java.util.Comparator;

// ── Queue interface ───────────────────────────────────────────────────────────

interface Queue<E> {
    int size();
    boolean isEmpty();
    void enqueue(E e);
    E first();
    E dequeue();
}

// ── SinglyLinkedList ──────────────────────────────────────────────────────────

class SinglyLinkedList<E> {
    private static class Node<E> {
        E element;
        Node<E> next;
        Node(E e, Node<E> n) { element = e; next = n; }
    }
    private Node<E> head = null, tail = null;
    private int size = 0;

    public int size()        { return size; }
    public boolean isEmpty() { return size == 0; }
    public E first()         { return isEmpty() ? null : head.element; }

    public void addLast(E e) {
        Node<E> n = new Node<>(e, null);
        if (isEmpty()) head = n; else tail.next = n;
        tail = n;
        size++;
    }
    public E removeFirst() {
        if (isEmpty()) return null;
        E val = head.element;
        head = head.next;
        size--;
        if (size == 0) tail = null;
        return val;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        Node<E> w = head;
        while (w != null) {
            sb.append(w.element);
            if (w != tail) sb.append(", ");
            w = w.next;
        }
        return sb.append(")").toString();
    }
}

// ── LinkedQueue ───────────────────────────────────────────────────────────────

class LinkedQueue<E> implements Queue<E> {
    private SinglyLinkedList<E> list = new SinglyLinkedList<>();
    public int size()        { return list.size(); }
    public boolean isEmpty() { return list.isEmpty(); }
    public void enqueue(E e) { list.addLast(e); }
    public E first()         { return list.first(); }
    public E dequeue()       { return list.removeFirst(); }
    public String toString() { return list.toString(); }
}

// ════════════════════════════════════════════════════════════════════════════════
// Solution7_2 — Bottom-Up Merge Sort using a Queue of Queues
// ════════════════════════════════════════════════════════════════════════════════

public class Solution7_2 {

    // ════════════════════════════════════════════════════════════════════════
    // EXERCISE 2 — Bottom-Up Merge Sort (Queue of Queues)
    //
    // Algorithm:
    //
    //   Phase 1 – Distribute:
    //       Take each element from S and place it into its own
    //       single-element queue (trivially sorted).
    //       Collect all those queues into an outer Queue<Queue<K>>.
    //
    //       Input [85, 24, 63, 45]:
    //       outer → [85] [24] [63] [45]
    //
    //   Phase 2 – Merge passes:
    //       While the outer queue holds more than one inner queue:
    //           Dequeue two inner queues, merge them into one sorted
    //           queue, enqueue the result back into the outer queue.
    //       Each pass halves the number of inner queues and doubles
    //       their size.  After log(n) passes one queue remains.
    //
    //       Pass 1: merge [85][24]→[24,85]  merge [63][45]→[45,63]
    //       outer → [24,85] [45,63]
    //
    //       Pass 2: merge [24,85][45,63]→[24,45,63,85]
    //       outer → [24,45,63,85]   ← done
    //
    //   Phase 3 – Drain:
    //       Move every element from the single remaining inner queue
    //       back into the original queue S.
    //
    // Complexity: O(n log n) time, O(n) extra space.
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Merges two sorted queues (q1, q2) into one sorted queue and returns it.
     * Both q1 and q2 are empty after the call.
     */
    private static <K> Queue<K> mergeTwoQueues(Queue<K> q1, Queue<K> q2,
                                               Comparator<K> comp) {
        Queue<K> merged = new LinkedQueue<>();

        // while both queues have elements, take the smaller front element
        while (!q1.isEmpty() && !q2.isEmpty()) {
            if (comp.compare(q1.first(), q2.first()) <= 0)
                merged.enqueue(q1.dequeue());
            else
                merged.enqueue(q2.dequeue());
        }

        // drain whichever queue still has remaining elements
        while (!q1.isEmpty()) merged.enqueue(q1.dequeue());
        while (!q2.isEmpty()) merged.enqueue(q2.dequeue());

        return merged;
    }

    /**
     * Sorts queue S in place using bottom-up merge sort.
     */
    public static <K> void bottomUpMergeSort(Queue<K> S, Comparator<K> comp) {
        if (S.size() < 2) return;   // already sorted

        // Phase 1 — place each element into its own queue, collect in outer
        Queue<Queue<K>> outer = new LinkedQueue<>();
        while (!S.isEmpty()) {
            Queue<K> single = new LinkedQueue<>();
            single.enqueue(S.dequeue());
            outer.enqueue(single);
        }

        // Phase 2 — repeatedly merge pairs of inner queues
        while (outer.size() > 1) {
            Queue<K> q1 = outer.dequeue();
            Queue<K> q2 = outer.dequeue();
            outer.enqueue(mergeTwoQueues(q1, q2, comp));
        }

        // Phase 3 — drain the single sorted inner queue back into S
        Queue<K> sorted = outer.dequeue();
        while (!sorted.isEmpty()) S.enqueue(sorted.dequeue());
    }

    // ════════════════════════════════════════════════════════════════════════
    // main — tests bottom-up merge sort on integers and strings
    // ════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        System.out.println("=== Exercise 2: Bottom-Up Merge Sort (Queue of Queues) ===\n");

        // --- Integer example ---
        Comparator<Integer> intComp = (a, b) -> a.compareTo(b);

        Queue<Integer> intQ = new LinkedQueue<>();
        for (int v : new int[]{85, 24, 63, 45, 17, 31, 96, 50})
            intQ.enqueue(v);

        System.out.println("Before (integers): " + intQ);
        bottomUpMergeSort(intQ, intComp);
        System.out.println("After  (integers): " + intQ);

        // --- String example ---
        Comparator<String> strComp = (a, b) -> a.compareTo(b);

        Queue<String> strQ = new LinkedQueue<>();
        for (String w : new String[]{"banana", "apple", "mango", "cherry",
                "date", "elderberry", "fig", "avocado"})
            strQ.enqueue(w);

        System.out.println("\nBefore (strings): " + strQ);
        bottomUpMergeSort(strQ, strComp);
        System.out.println("After  (strings): " + strQ);

        // --- Edge cases ---
        Queue<Integer> empty = new LinkedQueue<>();
        bottomUpMergeSort(empty, intComp);
        System.out.println("\nEmpty queue: " + empty);

        Queue<Integer> single = new LinkedQueue<>();
        single.enqueue(42);
        bottomUpMergeSort(single, intComp);
        System.out.println("Single element: " + single);

        Queue<Integer> dups = new LinkedQueue<>();
        for (int v : new int[]{3, 1, 4, 1, 5, 9, 2, 6, 5, 3})
            dups.enqueue(v);
        System.out.println("\nBefore (duplicates): " + dups);
        bottomUpMergeSort(dups, intComp);
        System.out.println("After  (duplicates): " + dups);
    }
}