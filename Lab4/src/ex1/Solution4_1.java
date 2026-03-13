package ex1;


// Position Interface
interface Position<E> {
    E getElement();
}

// Node class implementing Position
class Node<E> implements Position<E> {
    private E element;
    Node<E> prev;
    Node<E> next;

    public Node(E e, Node<E> p, Node<E> n) {
        element = e;
        prev = p;
        next = n;
    }

    public E getElement() {
        return element;
    }
}

// PositionalList Implementation
class Solution4_1<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public Solution4_1() {
        head = new Node<>(null, null, null);
        tail = new Node<>(null, head, null);
        head.next = tail;
    }

    // return first position
    public Position<E> first() {
        if (size == 0) return null;
        return head.next;
    }

    // return position after p
    public Position<E> after(Position<E> p) {
        Node<E> node = (Node<E>) p;
        if (node.next == tail) return null;
        return node.next;
    }

    // add element at end
    public Position<E> addLast(E e) {
        Node<E> newest = new Node<>(e, tail.prev, tail);
        tail.prev.next = newest;
        tail.prev = newest;
        size++;
        return newest;
    }

    public int indexOf(Position<E> p) {

        Position<E> current = first();
        int index = 0;

        while (current != null) {

            if (current == p) {
                return index;
            }

            current = after(current);
            index++;
        }

        return -1;
    }


    public static void main(String[] args) {

        Solution4_1<String> list = new Solution4_1<>();

        Position<String> p1 = list.addLast("A");
        Position<String> p2 = list.addLast("B");
        Position<String> p3 = list.addLast("C");
        Position<String> p4 = list.addLast("D");

        int index = list.indexOf(p3);

        System.out.println("Index of C = " + index);
    }
}

