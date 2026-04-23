// ─────────────────────────────────────────────────────────────────────────────
//  Exercise1_ComputeDepths.java
//  Requires SharedClasses.java in the same src/ folder.
//  (Queue, SinglyLinkedList, LinkedQueue are defined there.)
// ─────────────────────────────────────────────────────────────────────────────

interface Position<E> {
    E getElement() throws IllegalStateException;
}

interface Tree<E> extends Iterable<E> {
    Position<E> root();
    Position<E> parent(Position<E> p) throws IllegalArgumentException;
    Iterable<Position<E>> children(Position<E> p) throws IllegalArgumentException;
    int numChildren(Position<E> p) throws IllegalArgumentException;
    boolean isInternal(Position<E> p) throws IllegalArgumentException;
    boolean isExternal(Position<E> p) throws IllegalArgumentException;
    boolean isRoot(Position<E> p) throws IllegalArgumentException;
    int size();
    boolean isEmpty();
    java.util.Iterator<E> iterator();
    Iterable<Position<E>> positions();
}

abstract class AbstractTree<E> implements Tree<E> {
    @Override public boolean isInternal(Position<E> p) { return numChildren(p) > 0; }
    @Override public boolean isExternal(Position<E> p) { return numChildren(p) == 0; }
    @Override public boolean isRoot(Position<E> p)     { return p == root(); }
    @Override public boolean isEmpty()                  { return size() == 0; }

    @Override public int numChildren(Position<E> p) {
        int count = 0;
        for (Position<E> c : children(p)) count++;
        return count;
    }

    @Override public int size() {
        int count = 0;
        for (Position<E> p : positions()) count++;
        return count;
    }

    public int depth(Position<E> p) {
        if (isRoot(p)) return 0;
        return 1 + depth(parent(p));
    }

    public int height(Position<E> p) {
        int h = 0;
        for (Position<E> c : children(p))
            h = Math.max(h, 1 + height(c));
        return h;
    }

    private void preorderSubtree(Position<E> p, java.util.List<Position<E>> snap) {
        snap.add(p);
        for (Position<E> c : children(p)) preorderSubtree(c, snap);
    }

    public Iterable<Position<E>> preorder() {
        java.util.List<Position<E>> snap = new java.util.ArrayList<>();
        if (!isEmpty()) preorderSubtree(root(), snap);
        return snap;
    }

    public Iterable<Position<E>> breadthfirst() {
        java.util.List<Position<E>> snap = new java.util.ArrayList<>();
        if (!isEmpty()) {
            Queue<Position<E>> fringe = new LinkedQueue<>();  // from SharedClasses.java
            fringe.enqueue(root());
            while (!fringe.isEmpty()) {
                Position<E> p = fringe.dequeue();
                snap.add(p);
                for (Position<E> c : children(p)) fringe.enqueue(c);
            }
        }
        return snap;
    }

    @Override public Iterable<Position<E>> positions() { return preorder(); }

    @Override public java.util.Iterator<E> iterator() {
        java.util.Iterator<Position<E>> posIter = positions().iterator();
        return new java.util.Iterator<E>() {
            public boolean hasNext() { return posIter.hasNext(); }
            public E next()          { return posIter.next().getElement(); }
        };
    }
}

interface BinaryTree<E> extends Tree<E> {
    Position<E> left(Position<E> p)    throws IllegalArgumentException;
    Position<E> right(Position<E> p)   throws IllegalArgumentException;
    Position<E> sibling(Position<E> p) throws IllegalArgumentException;
}

abstract class AbstractBinaryTree<E> extends AbstractTree<E> implements BinaryTree<E> {
    @Override public Position<E> sibling(Position<E> p) {
        Position<E> parent = parent(p);
        if (parent == null) return null;
        return (p == left(parent)) ? right(parent) : left(parent);
    }
    @Override public int numChildren(Position<E> p) {
        int c = 0;
        if (left(p)  != null) c++;
        if (right(p) != null) c++;
        return c;
    }
    @Override public Iterable<Position<E>> children(Position<E> p) {
        java.util.List<Position<E>> snap = new java.util.ArrayList<>(2);
        if (left(p)  != null) snap.add(left(p));
        if (right(p) != null) snap.add(right(p));
        return snap;
    }
    private void inorderSubtree(Position<E> p, java.util.List<Position<E>> snap) {
        if (left(p)  != null) inorderSubtree(left(p),  snap);
        snap.add(p);
        if (right(p) != null) inorderSubtree(right(p), snap);
    }
    public Iterable<Position<E>> inorder() {
        java.util.List<Position<E>> snap = new java.util.ArrayList<>();
        if (!isEmpty()) inorderSubtree(root(), snap);
        return snap;
    }
    @Override public Iterable<Position<E>> positions() { return inorder(); }
}

class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {
    protected static class Node<E> implements Position<E> {
        E element; Node<E> parent, left, right;
        Node(E e, Node<E> p, Node<E> l, Node<E> r) { element=e; parent=p; left=l; right=r; }
        public E getElement() { return element; }
    }
    protected Node<E> root = null;
    private int size = 0;

    protected Node<E> validate(Position<E> p) {
        if (!(p instanceof Node)) throw new IllegalArgumentException("Invalid node");
        Node<E> node = (Node<E>) p;
        if (node.parent == node) throw new IllegalArgumentException("Defunct node");
        return node;
    }
    @Override public int size()               { return size; }
    @Override public Position<E> root()       { return root; }
    @Override public Position<E> parent(Position<E> p) { return validate(p).parent; }
    @Override public Position<E> left(Position<E> p)   { return validate(p).left; }
    @Override public Position<E> right(Position<E> p)  { return validate(p).right; }

    public Position<E> addRoot(E e) {
        if (!isEmpty()) throw new IllegalStateException("Tree not empty");
        root = new Node<>(e, null, null, null); size = 1; return root;
    }
    public Position<E> addLeft(Position<E> p, E e) {
        Node<E> parent = validate(p);
        if (parent.left != null) throw new IllegalArgumentException("Left child exists");
        Node<E> child = new Node<>(e, parent, null, null);
        parent.left = child; size++; return child;
    }
    public Position<E> addRight(Position<E> p, E e) {
        Node<E> parent = validate(p);
        if (parent.right != null) throw new IllegalArgumentException("Right child exists");
        Node<E> child = new Node<>(e, parent, null, null);
        parent.right = child; size++; return child;
    }
    public E set(Position<E> p, E e) {
        Node<E> node = validate(p); E tmp = node.element; node.element = e; return tmp;
    }
}

// ═════════════════════════════════════════════════════════════════════════════
public class Exercise1_ComputeDepths {

    public static <E> int[] computeDepths(AbstractTree<E> T, int n) {
        int[] depths = new int[n];
        int i = 0;
        for (Position<E> p : T.preorder()) {
            depths[i] = T.depth(p);
            i++;
        }
        return depths;
    }

    public static void main(String[] args) {
        //        ICET
        //       /    \
        //  Software  Networking
        //   /    \
        // SET     IG
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>();
        Position<String> root       = tree.addRoot("ICET");
        Position<String> software   = tree.addLeft(root,      "Software");
        Position<String> networking = tree.addRight(root,     "Networking");
        Position<String> set        = tree.addLeft(software,  "SET");
        Position<String> ig         = tree.addRight(software, "IG");

        int n = tree.size();
        int[] depths = computeDepths(tree, n);

        System.out.println("=== computeDepths() (preorder) ===");
        int i = 0;
        for (Position<String> p : tree.preorder())
            System.out.printf("  %-12s depth = %d%n", p.getElement(), depths[i++]);

        System.out.println("\nExpected:");
        System.out.println("  ICET        depth = 0");
        System.out.println("  Software    depth = 1");
        System.out.println("  SET         depth = 2");
        System.out.println("  IG          depth = 2");
        System.out.println("  Networking  depth = 1");

        // PASS check
        boolean pass = depths[0]==0 && depths[1]==1 && depths[2]==2
                    && depths[3]==2 && depths[4]==1;
        System.out.println("\nAll depths correct: " + pass);
    }
}
