import java.util.ArrayList;

public class Exercise6_MinHeap {

    private final ArrayList<Integer> a = new ArrayList<>();

    // ─── Parent / child index formulas ────────────────────────────────────────
    private int parent(int i) { return (i - 1) / 2; }
    private int left(int i)   { return 2 * i + 1; }
    private int right(int i)  { return 2 * i + 2; }

    private void swap(int i, int j) {
        int tmp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, tmp);
    }

    // ─── swim (upheap) ────────────────────────────────────────────────────────
    /**
     * After inserting at the last position, bubble it UP until
     * the heap-order property is restored.
     *
     * Heap-order: every parent ≤ both children (min-heap).
     * While a[i] < a[parent(i)]  →  swap and move up.
     *
     * Time: O(log n)
     */
    private void swim(int i) {
        while (i > 0 && a.get(i) < a.get(parent(i))) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    // ─── sink (downheap) ──────────────────────────────────────────────────────
    /**
     * After moving the last element to the root (during removeMin),
     * sink it DOWN until heap-order is restored.
     *
     * At each step: find the SMALLER child; if that child < current, swap.
     *
     * Time: O(log n)
     */
    private void sink(int i) {
        int n = a.size();
        while (left(i) < n) {                           // while left child exists
            int smallChild = left(i);
            if (right(i) < n && a.get(right(i)) < a.get(left(i)))
                smallChild = right(i);                   // right is smaller

            if (a.get(i) <= a.get(smallChild)) break;   // heap property OK

            swap(i, smallChild);
            i = smallChild;
        }
    }

    // ─── add ──────────────────────────────────────────────────────────────────
    /**
     * Insert x into the heap.
     * 1. Append x to the end of the array.
     * 2. swim to restore heap-order from the bottom up.
     *
     * Time: O(log n)
     */
    public void add(int x) {
        a.add(x);
        swim(a.size() - 1);
    }

    // ─── removeMin ────────────────────────────────────────────────────────────
    /**
     * Remove and return the minimum element (always at index 0).
     * 1. Save a[0] (the min).
     * 2. Move the last element to a[0].
     * 3. Remove the last slot.
     * 4. sink(0) to restore heap-order from the top down.
     *
     * Time: O(log n)
     */
    public int removeMin() {
        if (a.isEmpty()) throw new RuntimeException("Heap is empty");
        int min = a.get(0);
        int last = a.remove(a.size() - 1);  // remove and get last element
        if (!a.isEmpty()) {
            a.set(0, last);                 // place it at root
            sink(0);                        // restore heap property
        }
        return min;
    }

    // ─── peek & helpers ───────────────────────────────────────────────────────
    public int peek()        { return a.get(0); }
    public int size()        { return a.size(); }
    public boolean isEmpty() { return a.isEmpty(); }

    /** Print the internal array and a rough tree layout. */
    public void dump() {
        System.out.println("  Internal array: " + a);
        System.out.print("  Tree layout:    ");
        int level = 1, count = 0;
        for (int i = 0; i < a.size(); i++) {
            if (count == level) { System.out.print("| "); level *= 2; count = 0; }
            System.out.print(a.get(i) + " ");
            count++;
        }
        System.out.println();
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  main
    // ═════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {

        System.out.println("════ Exercise 6 — MinHeap ════\n");

        // ── Test 1: exercise-sheet sequence ───────────────────────────────────
        Exercise6_MinHeap h = new Exercise6_MinHeap();
        System.out.println("Inserting: 7, 3, 5, 1");
        h.add(7);
        System.out.print("  After add(7): "); h.dump();
        h.add(3);
        System.out.print("  After add(3): "); h.dump();
        h.add(5);
        System.out.print("  After add(5): "); h.dump();
        h.add(1);
        System.out.print("  After add(1): "); h.dump();

        System.out.println("\nremoving in order (should be 1, 3, 5, 7):");
        System.out.println("  removeMin() = " + h.removeMin()); // 1
        System.out.println("  removeMin() = " + h.removeMin()); // 3
        System.out.println("  removeMin() = " + h.removeMin()); // 5
        System.out.println("  removeMin() = " + h.removeMin()); // 7

        // ── PASS checks ──────────────────────────────────────────────────────
        System.out.println("\n--- PASS checks ---");
        Exercise6_MinHeap h2 = new Exercise6_MinHeap();
        h2.add(7); h2.add(3); h2.add(5); h2.add(1);
        System.out.println("removeMin()=1 PASS: " + (h2.removeMin() == 1));
        System.out.println("removeMin()=3 PASS: " + (h2.removeMin() == 3));
        System.out.println("removeMin()=5 PASS: " + (h2.removeMin() == 5));
        System.out.println("removeMin()=7 PASS: " + (h2.removeMin() == 7));

        // ── Test 2: heap sort via repeated removeMin ───────────────────────────
        System.out.println("\n--- Test 2: Heap sort via repeated removeMin ---");
        int[] input = {42, 15, 8, 23, 4, 16, 1, 100};
        Exercise6_MinHeap h3 = new Exercise6_MinHeap();
        for (int x : input) h3.add(x);
        System.out.print("Sorted output: ");
        while (!h3.isEmpty()) System.out.print(h3.removeMin() + " ");
        System.out.println();
        // 1 4 8 15 16 23 42 100

        // ── Test 3: index formulas ─────────────────────────────────────────────
        System.out.println("\n--- Index formulas (for 0-indexed array) ---");
        System.out.println("parent(j) = (j-1)/2");
        System.out.println("left(j)   = 2j + 1");
        System.out.println("right(j)  = 2j + 2");
        System.out.println("Example: node at j=3 -> parent=" + ((3-1)/2) +
                           ", left=" + (2*3+1) + ", right=" + (2*3+2));

        // ── Test 4: add after removes ─────────────────────────────────────────
        System.out.println("\n--- Test 4: interleaved add/removeMin ---");
        Exercise6_MinHeap h4 = new Exercise6_MinHeap();
        h4.add(10); h4.add(20);
        System.out.println("peek after 10,20: " + h4.peek());       // 10
        h4.removeMin();
        h4.add(5);
        System.out.println("peek after removeMin, add(5): " + h4.peek()); // 5
        h4.add(1);
        System.out.println("peek after add(1): " + h4.peek());      // 1
    }
}
