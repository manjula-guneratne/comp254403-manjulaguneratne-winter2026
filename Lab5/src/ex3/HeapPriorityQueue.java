package ex3;

import java.util.ArrayList;
import java.util.Comparator;

/* ---------- Entry Interface ---------- */
interface Entry<K,V> {
    K getKey();
    V getValue();
}

/* ---------- Entry Implementation ---------- */
class PQEntry<K,V> implements Entry<K,V> {
    private K key;
    private V value;

    public PQEntry(K k, V v) {
        key = k;
        value = v;
    }

    public K getKey() { return key; }
    public V getValue() { return value; }

    protected void setKey(K k) { key = k; }
    protected void setValue(V v) { value = v; }
}

/* ---------- Abstract PQ ---------- */
abstract class AbstractPriorityQueue<K,V> {

    protected Comparator<K> comp;

    protected AbstractPriorityQueue() {
        this.comp = (k1, k2) -> ((Comparable<K>)k1).compareTo(k2);
    }

    protected AbstractPriorityQueue(Comparator<K> c) {
        comp = c;
    }

    protected int compare(Entry<K,V> a, Entry<K,V> b) {
        return comp.compare(a.getKey(), b.getKey());
    }

    protected void checkKey(K key) throws IllegalArgumentException {
        try {
            comp.compare(key, key);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid key");
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public abstract int size();
}

/* ---------- Heap PQ ---------- */
public class HeapPriorityQueue<K,V> extends AbstractPriorityQueue<K,V> {

    protected ArrayList<Entry<K,V>> heap = new ArrayList<>();

    public HeapPriorityQueue() { super(); }

    public HeapPriorityQueue(Comparator<K> comp) { super(comp); }

    protected int parent(int j) { return (j-1)/2; }
    protected int left(int j) { return 2*j+1; }
    protected int right(int j) { return 2*j+2; }

    protected boolean hasLeft(int j) { return left(j) < heap.size(); }
    protected boolean hasRight(int j) { return right(j) < heap.size(); }

    protected void swap(int i, int j) {
        Entry<K,V> temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    protected void upheap(int j) {
        if (j == 0) return;

        int p = parent(j);

        if (compare(heap.get(j), heap.get(p)) < 0) {
            swap(j, p);
            upheap(p);  // recursion
        }
    }

    protected void downheap(int j) {
        if (!hasLeft(j)) return;

        int smallChild = left(j);

        if (hasRight(j) &&
                compare(heap.get(right(j)), heap.get(left(j))) < 0) {
            smallChild = right(j);
        }

        if (compare(heap.get(smallChild), heap.get(j)) < 0) {
            swap(j, smallChild);
            downheap(smallChild);
        }
    }

    @Override
    public int size() { return heap.size(); }

    public Entry<K,V> min() {
        if (heap.isEmpty()) return null;
        return heap.get(0);
    }

    public Entry<K,V> insert(K key, V value) {
        checkKey(key);

        Entry<K,V> newest = new PQEntry<>(key, value);
        heap.add(newest);

        upheap(heap.size() - 1);

        return newest;
    }

    public Entry<K,V> removeMin() {
        if (heap.isEmpty()) return null;

        Entry<K,V> answer = heap.get(0);
        swap(0, heap.size()-1);

        heap.remove(heap.size()-1);

        if (!heap.isEmpty())
            downheap(0);

        return answer;
    }

    public static void main(String[] args) {

        HeapPriorityQueue<Integer,String> pq = new HeapPriorityQueue<>();

        pq.insert(47, "A");
        pq.insert(75, "C");
        pq.insert(28, "B");
        pq.insert(51, "D");
        pq.insert(31, "F");
        pq.insert(22, "G");
        pq.insert(15, "H");

        System.out.println("Heap contents:");
        for (int i = 0; i < pq.size(); i++) {
            System.out.println("(" + pq.heap.get(i).getKey() +
                    ", " + pq.heap.get(i).getValue() + ")");
        }

        System.out.println("Min: " + pq.min().getKey());
    }
}