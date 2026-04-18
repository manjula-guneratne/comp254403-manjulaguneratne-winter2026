package ex1;

import java.util.ArrayList;
import java.util.Comparator;

// ── bring all supporting types into the package ──────────────────────────────

interface Position<E> {
    E getElement() throws IllegalStateException;
}

interface Entry<K, V> {
    K getKey();
    V getValue();
}

interface Map<K, V> {
    int size();
    boolean isEmpty();
    V get(K key);
    V put(K key, V value);
    V remove(K key);
    Iterable<K> keySet();
    Iterable<V> values();
    Iterable<Entry<K, V>> entrySet();
}

interface SortedMap<K, V> extends Map<K, V> {
    Entry<K, V> firstEntry();
    Entry<K, V> lastEntry();
    Entry<K, V> ceilingEntry(K key) throws IllegalArgumentException;
    Entry<K, V> floorEntry(K key)   throws IllegalArgumentException;
    Entry<K, V> lowerEntry(K key)   throws IllegalArgumentException;
    Entry<K, V> higherEntry(K key)  throws IllegalArgumentException;
    Iterable<Entry<K, V>> subMap(K fromKey, K toKey) throws IllegalArgumentException;
}

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
    public int size()      { return size; }
    public boolean isEmpty(){ return size == 0; }
    public E first()       { return isEmpty() ? null : head.element; }
    public void addLast(E e) {
        Node<E> n = new Node<>(e, null);
        if (isEmpty()) head = n; else tail.next = n;
        tail = n; size++;
    }
    public E removeFirst() {
        if (isEmpty()) return null;
        E val = head.element; head = head.next; size--;
        if (size == 0) tail = null;
        return val;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        Node<E> w = head;
        while (w != null) { sb.append(w.element); if (w != tail) sb.append(", "); w = w.next; }
        return sb.append(")").toString();
    }
}

// ── LinkedQueue ───────────────────────────────────────────────────────────────

class LinkedQueue<E> implements Queue<E> {
    private SinglyLinkedList<E> list = new SinglyLinkedList<>();
    public int size()         { return list.size(); }
    public boolean isEmpty()  { return list.isEmpty(); }
    public void enqueue(E e)  { list.addLast(e); }
    public E first()          { return list.first(); }
    public E dequeue()        { return list.removeFirst(); }
    public String toString()  { return list.toString(); }
}

// ── AbstractMap ───────────────────────────────────────────────────────────────

abstract class AbstractMap<K, V> implements Map<K, V> {
    public boolean isEmpty() { return size() == 0; }

    protected static class MapEntry<K, V> implements Entry<K, V> {
        private K k; private V v;
        public MapEntry(K key, V value) { k = key; v = value; }
        public K getKey()   { return k; }
        public V getValue() { return v; }
        protected void setKey(K key) { k = key; }
        protected V setValue(V value) { V old = v; v = value; return old; }
        public String toString() { return "<" + k + ", " + v + ">"; }
    }

    public Iterable<K> keySet() {
        ArrayList<K> keys = new ArrayList<>();
        for (Entry<K, V> e : entrySet()) keys.add(e.getKey());
        return keys;
    }
    public Iterable<V> values() {
        ArrayList<V> vals = new ArrayList<>();
        for (Entry<K, V> e : entrySet()) vals.add(e.getValue());
        return vals;
    }
}

// ── DefaultComparator ─────────────────────────────────────────────────────────

class DefaultComparator<E> implements Comparator<E> {
    @SuppressWarnings("unchecked")
    public int compare(E a, E b) { return ((Comparable<E>) a).compareTo(b); }
}

// ── AbstractSortedMap ─────────────────────────────────────────────────────────

abstract class AbstractSortedMap<K, V> extends AbstractMap<K, V>
        implements SortedMap<K, V> {
    private Comparator<K> comp;
    protected AbstractSortedMap(Comparator<K> c) { comp = c; }
    protected AbstractSortedMap() { this(new DefaultComparator<>()); }
    protected int compare(Entry<K,V> a, Entry<K,V> b) { return comp.compare(a.getKey(), b.getKey()); }
    protected int compare(K a, Entry<K,V> b)           { return comp.compare(a, b.getKey()); }
    protected int compare(Entry<K,V> a, K b)           { return comp.compare(a.getKey(), b); }
    protected int compare(K a, K b)                    { return comp.compare(a, b); }
    protected boolean checkKey(K key) {
        try { return comp.compare(key, key) == 0; }
        catch (ClassCastException e) { throw new IllegalArgumentException("Incompatible key"); }
    }
}

// ── LinkedBinaryTree (minimal subset needed by TreeMap) ───────────────────────

interface BinaryTree<E> {
    Position<E> root();
    Position<E> parent(Position<E> p);
    Position<E> left(Position<E> p);
    Position<E> right(Position<E> p);
    Position<E> sibling(Position<E> p);
    boolean isRoot(Position<E> p);
    boolean isInternal(Position<E> p);
    boolean isExternal(Position<E> p);
    int size();
    Iterable<Position<E>> inorder();
    E set(Position<E> p, E e);
    Position<E> addRoot(E e);
    Position<E> addLeft(Position<E> p, E e);
    Position<E> addRight(Position<E> p, E e);
    E remove(Position<E> p);
}

class LinkedBinaryTree<E> implements BinaryTree<E> {
    protected static class Node<E> implements Position<E> {
        E element; Node<E> parent, left, right;
        Node(E e, Node<E> p, Node<E> l, Node<E> r) { element=e; parent=p; left=l; right=r; }
        public E getElement() { return element; }
    }
    protected Node<E> root = null;
    private int size = 0;

    protected Node<E> createNode(E e, Node<E> p, Node<E> l, Node<E> r) { return new Node<>(e,p,l,r); }

    @SuppressWarnings("unchecked")
    protected Node<E> validate(Position<E> p) {
        if (!(p instanceof Node)) throw new IllegalArgumentException("Invalid position");
        Node<E> node = (Node<E>) p;
        if (node.parent == node) throw new IllegalArgumentException("Position no longer valid");
        return node;
    }

    public int size()  { return size; }
    public Position<E> root()   { return root; }
    public Position<E> parent(Position<E> p) { return validate(p).parent; }
    public Position<E> left(Position<E> p)   { return validate(p).left; }
    public Position<E> right(Position<E> p)  { return validate(p).right; }
    public Position<E> sibling(Position<E> p) {
        Node<E> node = validate(p), par = node.parent;
        if (par == null) return null;
        return (node == par.left) ? par.right : par.left;
    }
    public boolean isRoot(Position<E> p)     { return validate(p).parent == null; }
    public boolean isInternal(Position<E> p) { Node<E> n=validate(p); return n.left!=null||n.right!=null; }
    public boolean isExternal(Position<E> p) { return !isInternal(p); }

    public E set(Position<E> p, E e)         { Node<E> n=validate(p); E old=n.element; n.element=e; return old; }

    public Position<E> addRoot(E e) {
        if (root != null) throw new IllegalStateException("Tree not empty");
        root = createNode(e, null, null, null); size=1; return root;
    }
    public Position<E> addLeft(Position<E> p, E e) {
        Node<E> par=validate(p);
        if (par.left != null) throw new IllegalArgumentException("Left child exists");
        par.left = createNode(e, par, null, null); size++; return par.left;
    }
    public Position<E> addRight(Position<E> p, E e) {
        Node<E> par=validate(p);
        if (par.right != null) throw new IllegalArgumentException("Right child exists");
        par.right = createNode(e, par, null, null); size++; return par.right;
    }
    public E remove(Position<E> p) {
        Node<E> node=validate(p);
        if (node.left!=null && node.right!=null) throw new IllegalArgumentException("Two children");
        Node<E> child = (node.left!=null) ? node.left : node.right;
        if (child != null) child.parent = node.parent;
        if (node == root) root = child;
        else { Node<E> par=node.parent; if (node==par.left) par.left=child; else par.right=child; }
        size--;
        E old = node.element;
        node.element=null; node.left=null; node.right=null; node.parent=node;
        return old;
    }

    private void inorderSubtree(Position<E> p, ArrayList<Position<E>> snap) {
        Node<E> n = validate(p);
        if (n.left  != null) inorderSubtree(n.left,  snap);
        snap.add(p);
        if (n.right != null) inorderSubtree(n.right, snap);
    }
    public Iterable<Position<E>> inorder() {
        ArrayList<Position<E>> snap = new ArrayList<>();
        if (root != null) inorderSubtree(root, snap);
        return snap;
    }
}

// ════════════════════════════════════════════════════════════════════════════════
// Solution7_1 — the main class required by the assignment
// ════════════════════════════════════════════════════════════════════════════════

public class Solution7_1<K, V> extends AbstractSortedMap<K, V> {

    // ── internal BST ─────────────────────────────────────────────────────────
    protected LinkedBinaryTree<Entry<K, V>> tree = new LinkedBinaryTree<>();

    public Solution7_1()                  { super();      tree.addRoot(null); }
    public Solution7_1(Comparator<K> comp){ super(comp);  tree.addRoot(null); }

    @Override public int size() { return (tree.size() - 1) / 2; }

    private void expandExternal(Position<Entry<K,V>> p, Entry<K,V> entry) {
        tree.set(p, entry);
        tree.addLeft(p,  null);
        tree.addRight(p, null);
    }

    // ── convenience shorthands ───────────────────────────────────────────────
    protected Position<Entry<K,V>> root()                             { return tree.root(); }
    protected Position<Entry<K,V>> parent(Position<Entry<K,V>> p)    { return tree.parent(p); }
    protected Position<Entry<K,V>> left(Position<Entry<K,V>> p)      { return tree.left(p); }
    protected Position<Entry<K,V>> right(Position<Entry<K,V>> p)     { return tree.right(p); }
    protected Position<Entry<K,V>> sibling(Position<Entry<K,V>> p)   { return tree.sibling(p); }
    protected boolean isRoot(Position<Entry<K,V>> p)                 { return tree.isRoot(p); }
    protected boolean isExternal(Position<Entry<K,V>> p)             { return tree.isExternal(p); }
    protected boolean isInternal(Position<Entry<K,V>> p)             { return tree.isInternal(p); }
    protected void set(Position<Entry<K,V>> p, Entry<K,V> e)         { tree.set(p, e); }
    protected Entry<K,V> remove(Position<Entry<K,V>> p)              { return tree.remove(p); }

    // ════════════════════════════════════════════════════════════════════════
    // EXERCISE 1 — iterative treeSearch (no recursion)
    //
    // The original (Code Fragment 11.3) calls itself recursively, consuming
    // one Java call-stack frame per tree level.  On a degenerate (linear)
    // tree this causes a StackOverflowError.
    //
    // Fix: because BST search NEVER branches (it goes left OR right, never
    // both), the recursion is tail-recursion and can be replaced by a plain
    // while-loop.  The loop variable `current` walks down the tree just as
    // the recursive calls did, but uses O(1) stack space instead of O(h).
    // ════════════════════════════════════════════════════════════════════════
    private Position<Entry<K,V>> treeSearch(Position<Entry<K,V>> p, K key) {
        Position<Entry<K,V>> current = p;           // start at the given root

        while (!isExternal(current)) {              // sentinel leaf → stop
            int comp = compare(key, current.getElement());

            if      (comp == 0) return current;     // exact match found
            else if (comp <  0) current = left(current);   // go left
            else                current = right(current);  // go right
        }

        return current;     // key not found; return terminal sentinel leaf
    }

    // ── Map operations (all delegate to the iterative search) ────────────────

    @Override
    public V get(K key) throws IllegalArgumentException {
        checkKey(key);
        Position<Entry<K,V>> p = treeSearch(root(), key);
        if (isExternal(p)) return null;
        return p.getElement().getValue();
    }

    @Override
    public V put(K key, V value) throws IllegalArgumentException {
        checkKey(key);
        Entry<K,V> newEntry = new MapEntry<>(key, value);
        Position<Entry<K,V>> p = treeSearch(root(), key);
        if (isExternal(p)) {
            expandExternal(p, newEntry);
            return null;
        } else {
            V old = p.getElement().getValue();
            set(p, newEntry);
            return old;
        }
    }

    @Override
    public V remove(K key) throws IllegalArgumentException {
        checkKey(key);
        Position<Entry<K,V>> p = treeSearch(root(), key);
        if (isExternal(p)) return null;
        V old = p.getElement().getValue();
        if (isInternal(left(p)) && isInternal(right(p))) {
            Position<Entry<K,V>> rep = treeMax(left(p));
            set(p, rep.getElement());
            p = rep;
        }
        Position<Entry<K,V>> leaf = isExternal(left(p)) ? left(p) : right(p);
        remove(leaf);
        remove(p);
        return old;
    }

    // ── SortedMap navigation helpers ─────────────────────────────────────────

    protected Position<Entry<K,V>> treeMin(Position<Entry<K,V>> p) {
        Position<Entry<K,V>> walk = p;
        while (isInternal(walk)) walk = left(walk);
        return parent(walk);
    }
    protected Position<Entry<K,V>> treeMax(Position<Entry<K,V>> p) {
        Position<Entry<K,V>> walk = p;
        while (isInternal(walk)) walk = right(walk);
        return parent(walk);
    }

    @Override public Entry<K,V> firstEntry() { return isEmpty() ? null : treeMin(root()).getElement(); }
    @Override public Entry<K,V> lastEntry()  { return isEmpty() ? null : treeMax(root()).getElement(); }

    @Override public Entry<K,V> ceilingEntry(K key) {
        checkKey(key);
        Position<Entry<K,V>> p = treeSearch(root(), key);
        if (isInternal(p)) return p.getElement();
        while (!isRoot(p)) { if (p == left(parent(p))) return parent(p).getElement(); else p = parent(p); }
        return null;
    }
    @Override public Entry<K,V> floorEntry(K key) {
        checkKey(key);
        Position<Entry<K,V>> p = treeSearch(root(), key);
        if (isInternal(p)) return p.getElement();
        while (!isRoot(p)) { if (p == right(parent(p))) return parent(p).getElement(); else p = parent(p); }
        return null;
    }
    @Override public Entry<K,V> lowerEntry(K key) {
        checkKey(key);
        Position<Entry<K,V>> p = treeSearch(root(), key);
        if (isInternal(p) && isInternal(left(p))) return treeMax(left(p)).getElement();
        while (!isRoot(p)) { if (p == right(parent(p))) return parent(p).getElement(); else p = parent(p); }
        return null;
    }
    @Override public Entry<K,V> higherEntry(K key) {
        checkKey(key);
        Position<Entry<K,V>> p = treeSearch(root(), key);
        if (isInternal(p) && isInternal(right(p))) return treeMin(right(p)).getElement();
        while (!isRoot(p)) { if (p == left(parent(p))) return parent(p).getElement(); else p = parent(p); }
        return null;
    }

    @Override public Iterable<Entry<K,V>> entrySet() {
        ArrayList<Entry<K,V>> buf = new ArrayList<>(size());
        for (Position<Entry<K,V>> p : tree.inorder())
            if (isInternal(p)) buf.add(p.getElement());
        return buf;
    }
    @Override public Iterable<Entry<K,V>> subMap(K fromKey, K toKey) {
        ArrayList<Entry<K,V>> buf = new ArrayList<>(size());
        if (compare(fromKey, toKey) < 0) subMapRecurse(fromKey, toKey, root(), buf);
        return buf;
    }
    private void subMapRecurse(K from, K to, Position<Entry<K,V>> p, ArrayList<Entry<K,V>> buf) {
        if (isInternal(p)) {
            if (compare(p.getElement(), from) < 0)
                subMapRecurse(from, to, right(p), buf);
            else {
                subMapRecurse(from, to, left(p), buf);
                if (compare(p.getElement(), to) < 0) { buf.add(p.getElement()); subMapRecurse(from, to, right(p), buf); }
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // main — tests the iterative treeSearch through all map operations
    // ════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        System.out.println("=== Exercise 1: Iterative treeSearch ===\n");

        Solution7_1<Integer, String> map = new Solution7_1<>();

        // build the tree from the lab's own example
        map.put(6, "A");
        map.put(2, "B");
        map.put(4, "C");
        map.put(1, "D");
        map.put(9, "E");
        map.put(8, "F");

        System.out.println("-- get --");
        System.out.println("get(4)  -> " + map.get(4));   // C
        System.out.println("get(9)  -> " + map.get(9));   // E
        System.out.println("get(7)  -> " + map.get(7));   // null (not present)

        System.out.println("\n-- navigation --");
        System.out.println("higherEntry(2)  -> " + map.higherEntry(2));   // <4, C>
        System.out.println("lowerEntry(9)   -> " + map.lowerEntry(9));    // <8, F>
        System.out.println("floorEntry(5)   -> " + map.floorEntry(5));    // <4, C>
        System.out.println("ceilingEntry(5) -> " + map.ceilingEntry(5));  // <6, A>
        System.out.println("firstEntry()    -> " + map.firstEntry());     // <1, D>
        System.out.println("lastEntry()     -> " + map.lastEntry());      // <9, E>

        System.out.println("\n-- entrySet (inorder) --");
        System.out.println(map.entrySet());

        System.out.println("\n-- remove(4) --");
        map.remove(4);
        System.out.println("entrySet after: " + map.entrySet());
        System.out.println("get(4) -> " + map.get(4));    // null

        System.out.println("\n-- put update --");
        map.put(6, "UPDATED");
        System.out.println("get(6) -> " + map.get(6));    // UPDATED

        System.out.println("\n-- all values --");
        for (String v : map.values()) System.out.println("  " + v);
    }
}