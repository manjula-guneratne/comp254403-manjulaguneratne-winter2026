// ─────────────────────────────────────────────────────────────────────────────
//  SharedClasses.java
//  Put this in the SAME src/ folder as all your Exercise files.
//  Exercise1 and Exercise3 both need Queue, SinglyLinkedList, LinkedQueue —
//  defining them here once avoids "duplicate class" errors.
// ─────────────────────────────────────────────────────────────────────────────

interface Queue<E> {
    int size();
    boolean isEmpty();
    void enqueue(E e);
    E first();
    E dequeue();
}

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
        Node<E> newest = new Node<>(e, null);
        if (isEmpty()) head = newest; else tail.next = newest;
        tail = newest;
        size++;
    }

    public E removeFirst() {
        if (isEmpty()) return null;
        E ans = head.element;
        head = head.next;
        size--;
        if (size == 0) tail = null;
        return ans;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        Node<E> walk = head;
        while (walk != null) {
            sb.append(walk.element);
            if (walk != tail) sb.append(", ");
            walk = walk.next;
        }
        return sb.append(")").toString();
    }
}

class LinkedQueue<E> implements Queue<E> {
    private final SinglyLinkedList<E> list = new SinglyLinkedList<>();
    public int size()        { return list.size(); }
    public boolean isEmpty() { return list.isEmpty(); }
    public void enqueue(E e) { list.addLast(e); }
    public E first()         { return list.first(); }
    public E dequeue()       { return list.removeFirst(); }
    public String toString() { return list.toString(); }
}
