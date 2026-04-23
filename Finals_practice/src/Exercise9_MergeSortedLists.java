public class Exercise9_MergeSortedLists {

    // ─── Minimal doubly-linked list ───────────────────────────────────────────
    static class DList {
        private static class Node {
            int v; Node prev, next;
            Node(int v) { this.v = v; }
        }
        private Node head, tail;
        private int size = 0;

        public void addLast(int v) {
            Node x = new Node(v);
            if (tail == null) { head = tail = x; }
            else              { tail.next = x; x.prev = tail; tail = x; }
            size++;
        }

        public boolean isEmpty() { return size == 0; }
        public int size()        { return size; }

        /** Peek at the front without removing. */
        public Integer first() { return head == null ? null : head.v; }

        /** Remove and return the front element (like Iterator.remove on the head). */
        private int pollFirst() {
            int v = head.v;
            head = head.next;
            if (head != null) head.prev = null;
            else              tail = null;
            size--;
            return v;
        }

        // ── mergeTwoSorted ────────────────────────────────────────────────────
        /**
         * Classic two-pointer merge of two sorted lists into a new sorted list.
         *
         * Algorithm:
         *   While both lists have elements:
         *     - Take the smaller front element and append it to the result.
         *   Drain whichever list still has elements.
         *
         * Time:  O(n + m)   n = |a|, m = |b|
         * Space: O(1) extra (we reuse the nodes conceptually; here we addLast)
         */
        public static DList mergeTwoSorted(DList a, DList b) {
            DList result = new DList();

            // While BOTH lists have elements, always pick the smaller front
            while (!a.isEmpty() && !b.isEmpty()) {
                if (a.first() <= b.first())
                    result.addLast(a.pollFirst());   // take from a
                else
                    result.addLast(b.pollFirst());   // take from b
            }

            // Drain the remaining list (at most one of these loops runs)
            while (!a.isEmpty()) result.addLast(a.pollFirst());
            while (!b.isEmpty()) result.addLast(b.pollFirst());

            return result;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (Node x = head; x != null; x = x.next) {
                if (sb.length() > 1) sb.append(", ");
                sb.append(x.v);
            }
            return sb.append("]").toString();
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  main
    // ═════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {

        System.out.println("════ Exercise 9 — Merge Two Sorted Positional Lists ════\n");

        // ── Test 1: exercise-sheet example ────────────────────────────────────
        DList a = new DList();
        a.addLast(1); a.addLast(4); a.addLast(7);

        DList b = new DList();
        b.addLast(2); b.addLast(5); b.addLast(6);

        System.out.println("List a: " + a);
        System.out.println("List b: " + b);

        DList merged = DList.mergeTwoSorted(a, b);
        System.out.println("Merged: " + merged);  // [1, 2, 4, 5, 6, 7]
        System.out.println("PASS  : " + merged.toString().equals("[1, 2, 4, 5, 6, 7]"));

        // ── Test 2: one empty list ────────────────────────────────────────────
        System.out.println("\n--- Test 2: one list is empty ---");
        DList c = new DList();
        c.addLast(3); c.addLast(8);
        DList empty = new DList();

        System.out.println("merge([3,8], []) = " + DList.mergeTwoSorted(c, empty)); // [3, 8]
        DList c2 = new DList(); c2.addLast(3); c2.addLast(8);
        System.out.println("merge([], [3,8]) = " + DList.mergeTwoSorted(empty, c2)); // [3, 8]

        // ── Test 3: equal-length, all elements interleaved ────────────────────
        System.out.println("\n--- Test 3: perfectly interleaved ---");
        DList d1 = new DList(); d1.addLast(1); d1.addLast(3); d1.addLast(5);
        DList d2 = new DList(); d2.addLast(2); d2.addLast(4); d2.addLast(6);
        System.out.println("merge([1,3,5],[2,4,6]) = " + DList.mergeTwoSorted(d1, d2));
        // [1, 2, 3, 4, 5, 6]

        // ── Test 4: duplicate values ──────────────────────────────────────────
        System.out.println("\n--- Test 4: duplicate values ---");
        DList e1 = new DList(); e1.addLast(1); e1.addLast(3); e1.addLast(3);
        DList e2 = new DList(); e2.addLast(2); e2.addLast(3); e2.addLast(5);
        System.out.println("merge([1,3,3],[2,3,5]) = " + DList.mergeTwoSorted(e1, e2));
        // [1, 2, 3, 3, 3, 5]

        // ── Test 5: single elements ───────────────────────────────────────────
        System.out.println("\n--- Test 5: single elements ---");
        DList f1 = new DList(); f1.addLast(10);
        DList f2 = new DList(); f2.addLast(5);
        System.out.println("merge([10],[5]) = " + DList.mergeTwoSorted(f1, f2));  // [5, 10]

        // ── Test 6: already in order (no swaps needed) ────────────────────────
        System.out.println("\n--- Test 6: a entirely before b ---");
        DList g1 = new DList(); g1.addLast(1); g1.addLast(2); g1.addLast(3);
        DList g2 = new DList(); g2.addLast(7); g2.addLast(8); g2.addLast(9);
        System.out.println("merge([1,2,3],[7,8,9]) = " + DList.mergeTwoSorted(g1, g2));
        // [1, 2, 3, 7, 8, 9]

        // ── Trace ─────────────────────────────────────────────────────────────
        System.out.println("\n--- Trace of Test 1 merge ---");
        System.out.println("a=[1,4,7]  b=[2,5,6]");
        System.out.println("  1 vs 2  -> take 1 from a    result=[1]");
        System.out.println("  4 vs 2  -> take 2 from b    result=[1,2]");
        System.out.println("  4 vs 5  -> take 4 from a    result=[1,2,4]");
        System.out.println("  7 vs 5  -> take 5 from b    result=[1,2,4,5]");
        System.out.println("  7 vs 6  -> take 6 from b    result=[1,2,4,5,6]");
        System.out.println("  b empty -> drain a: take 7  result=[1,2,4,5,6,7]");
    }
}
