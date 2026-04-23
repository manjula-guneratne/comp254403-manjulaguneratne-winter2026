// ─────────────────────────────────────────────────────────────────────────────
//  Exercise3_QuickSort.java
//  Requires SharedClasses.java in the same src/ folder.
//  (Queue, SinglyLinkedList, LinkedQueue are defined there.)
// ─────────────────────────────────────────────────────────────────────────────

import java.util.Comparator;

// ─── Order class ──────────────────────────────────────────────────────────────
class Order {
    private final int    orderId;
    private final String customerName;
    private final double amount;

    public Order(int orderId, String customerName, double amount) {
        this.orderId      = orderId;
        this.customerName = customerName;
        this.amount       = amount;
    }

    public int    getOrderId()      { return orderId; }
    public String getCustomerName() { return customerName; }
    public double getAmount()       { return amount; }

    @Override
    public String toString() {
        return String.format("Order(%d, %-8s $%6.2f)", orderId, customerName + ",", amount);
    }
}

// ─── QuickSort (queue-based, from course QuickSort.java) ─────────────────────
class QuickSort {
    public static <K> void quickSort(Queue<K> S, Comparator<K> comp) {
        int n = S.size();
        if (n < 2) return;

        K pivot = S.first();
        Queue<K> L = new LinkedQueue<>();
        Queue<K> E = new LinkedQueue<>();
        Queue<K> G = new LinkedQueue<>();

        while (!S.isEmpty()) {
            K element = S.dequeue();
            int c = comp.compare(element, pivot);
            if      (c < 0) L.enqueue(element);
            else if (c == 0) E.enqueue(element);
            else             G.enqueue(element);
        }

        quickSort(L, comp);
        quickSort(G, comp);

        while (!L.isEmpty()) S.enqueue(L.dequeue());
        while (!E.isEmpty()) S.enqueue(E.dequeue());
        while (!G.isEmpty()) S.enqueue(G.dequeue());
    }
}

// ═════════════════════════════════════════════════════════════════════════════
public class Exercise3_QuickSort {

    static void sep(String title) {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  " + title);
        System.out.println("══════════════════════════════════════════");
    }

    static Queue<Order> makeOrderQueue() {
        Queue<Order> q = new LinkedQueue<>();
        q.enqueue(new Order(1003, "Charlie", 250.00));
        q.enqueue(new Order(1001, "Alice",   450.75));
        q.enqueue(new Order(1005, "Eve",      80.50));
        q.enqueue(new Order(1002, "Bob",     320.00));
        q.enqueue(new Order(1004, "Diana",   450.75));
        return q;
    }

    static void printQueue(Queue<Order> q) {
        Queue<Order> temp = new LinkedQueue<>();
        while (!q.isEmpty()) {
            Order o = q.dequeue();
            System.out.println("  " + o);
            temp.enqueue(o);
        }
        while (!temp.isEmpty()) q.enqueue(temp.dequeue());
    }

    public static void main(String[] args) {

        // ── Part A: Characters ────────────────────────────────────────────────
        sep("PART A — QuickSort on Queue<Character>");

        // Exact queue from the exercise sheet
        Queue<Character> q1 = new LinkedQueue<>();
        q1.enqueue('A'); q1.enqueue('z'); q1.enqueue('y');
        System.out.println("Before: " + q1);
        Comparator<Character> natural = (c1, c2) -> c1.compareTo(c2);
        QuickSort.quickSort(q1, natural);
        System.out.println("After (natural): " + q1);   // (A, y, z)

        // Mixed case — natural Unicode order
        Queue<Character> q2 = new LinkedQueue<>();
        for (char c : new char[]{'z','A','m','B','a','Z','M','b'}) q2.enqueue(c);
        System.out.println("\nBefore (mixed): " + q2);
        QuickSort.quickSort(q2, natural);
        System.out.println("After  (natural Unicode): " + q2);  // (A,B,M,Z,a,b,m,z)

        // Case-insensitive
        Queue<Character> q3 = new LinkedQueue<>();
        for (char c : new char[]{'z','A','m','B','a','Z','M','b'}) q3.enqueue(c);
        Comparator<Character> caseInsensitive =
            (c1, c2) -> Character.toLowerCase(c1) - Character.toLowerCase(c2);
        QuickSort.quickSort(q3, caseInsensitive);
        System.out.println("After  (case-insensitive): " + q3);

        // Digits
        Queue<Character> q4 = new LinkedQueue<>();
        for (char c : new char[]{'5','2','8','1','9','3'}) q4.enqueue(c);
        QuickSort.quickSort(q4, natural);
        System.out.println("Digits sorted: " + q4);   // (1,2,3,5,8,9)

        // ── Part B: Order objects ─────────────────────────────────────────────
        sep("PART B — QuickSort on Queue<Order>");

        System.out.println("Original:"); printQueue(makeOrderQueue());

        System.out.println("\n-- By amount ascending --");
        Queue<Order> byAmtQ = makeOrderQueue();
        QuickSort.quickSort(byAmtQ, (o1, o2) -> Double.compare(o1.getAmount(), o2.getAmount()));
        printQueue(byAmtQ);

        System.out.println("\n-- By name A-Z --");
        Queue<Order> byNameQ = makeOrderQueue();
        QuickSort.quickSort(byNameQ, (o1, o2) -> o1.getCustomerName().compareTo(o2.getCustomerName()));
        printQueue(byNameQ);

        System.out.println("\n-- By orderId --");
        Queue<Order> byIdQ = makeOrderQueue();
        QuickSort.quickSort(byIdQ, (o1, o2) -> Integer.compare(o1.getOrderId(), o2.getOrderId()));
        printQueue(byIdQ);

        sep("Key Observation — E queue and stability");
        System.out.println("Tied elements (e.g. Alice & Diana both $450.75) go into E queue.");
        System.out.println("E is never recursed — FIFO order preserved. QuickSort is STABLE on a Queue.");
    }
}
