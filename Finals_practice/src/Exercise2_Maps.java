import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

// ─── Entry interface ──────────────────────────────────────────────────────────
interface Entry<K, V> {
    K getKey();
    V getValue();
}

// ─── Map interface ────────────────────────────────────────────────────────────
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

// ─── SortedMap interface ──────────────────────────────────────────────────────
interface SortedMap<K, V> extends Map<K, V> {
    Entry<K, V> firstEntry();
    Entry<K, V> lastEntry();
    Entry<K, V> ceilingEntry(K key) throws IllegalArgumentException;
    Entry<K, V> floorEntry(K key)   throws IllegalArgumentException;
    Entry<K, V> lowerEntry(K key)   throws IllegalArgumentException;
    Entry<K, V> higherEntry(K key)  throws IllegalArgumentException;
    Iterable<Entry<K, V>> subMap(K fromKey, K toKey) throws IllegalArgumentException;
}

// ─── DefaultComparator ───────────────────────────────────────────────────────
class DefaultComparator<E> implements Comparator<E> {
    @SuppressWarnings("unchecked")
    public int compare(E a, E b) { return ((Comparable<E>) a).compareTo(b); }
}

// ─── AbstractMap ─────────────────────────────────────────────────────────────
abstract class AbstractMap<K, V> implements Map<K, V> {

    @Override public boolean isEmpty() { return size() == 0; }

    protected static class MapEntry<K, V> implements Entry<K, V> {
        private K k; private V v;
        public MapEntry(K key, V value) { k = key; v = value; }
        public K getKey()   { return k; }
        public V getValue() { return v; }
        protected void setKey(K key) { k = key; }
        protected V setValue(V value) { V old = v; v = value; return old; }
        public String toString() { return "<" + k + ", " + v + ">"; }
    }

    private class KeyIterator implements Iterator<K> {
        private Iterator<Entry<K, V>> entries = entrySet().iterator();
        public boolean hasNext() { return entries.hasNext(); }
        public K next() { return entries.next().getKey(); }
        public void remove() { throw new UnsupportedOperationException(); }
    }

    private class ValueIterator implements Iterator<V> {
        private Iterator<Entry<K, V>> entries = entrySet().iterator();
        public boolean hasNext() { return entries.hasNext(); }
        public V next() { return entries.next().getValue(); }
        public void remove() { throw new UnsupportedOperationException(); }
    }

    @Override public Iterable<K> keySet() { return () -> new KeyIterator(); }
    @Override public Iterable<V> values()  { return () -> new ValueIterator(); }
}

// ─── AbstractSortedMap ────────────────────────────────────────────────────────
abstract class AbstractSortedMap<K, V> extends AbstractMap<K, V> implements SortedMap<K, V> {
    private Comparator<K> comp;
    protected AbstractSortedMap(Comparator<K> c) { comp = c; }
    protected AbstractSortedMap() { this(new DefaultComparator<>()); }

    protected int compare(Entry<K, V> a, Entry<K, V> b) { return comp.compare(a.getKey(), b.getKey()); }
    protected int compare(K a, Entry<K, V> b)           { return comp.compare(a, b.getKey()); }
    protected int compare(Entry<K, V> a, K b)           { return comp.compare(a.getKey(), b); }
    protected int compare(K a, K b)                     { return comp.compare(a, b); }

    protected boolean checkKey(K key) {
        try { return (comp.compare(key, key) == 0); }
        catch (ClassCastException e) { throw new IllegalArgumentException("Incompatible key"); }
    }
}

// ─── SortedTableMap ──────────────────────────────────────────────────────────
class SortedTableMap<K, V> extends AbstractSortedMap<K, V> {

    private ArrayList<MapEntry<K, V>> table = new ArrayList<>();

    public SortedTableMap() { super(); }
    public SortedTableMap(Comparator<K> comp) { super(comp); }

    /** Binary search: smallest index j where table[j].key >= key, or high+1 if none. */
    private int findIndex(K key, int low, int high) {
        if (high < low) return high + 1;
        int mid = (low + high) / 2;
        int cmp = compare(key, table.get(mid));
        if (cmp == 0) return mid;
        else if (cmp < 0) return findIndex(key, low, mid - 1);
        else              return findIndex(key, mid + 1, high);
    }

    private int findIndex(K key) { return findIndex(key, 0, table.size() - 1); }

    @Override public int size() { return table.size(); }

    @Override public V get(K key) {
        checkKey(key);
        int j = findIndex(key);
        if (j == size() || compare(key, table.get(j)) != 0) return null;
        return table.get(j).getValue();
    }

    @Override public V put(K key, V value) {
        checkKey(key);
        int j = findIndex(key);
        if (j < size() && compare(key, table.get(j)) == 0)
            return table.get(j).setValue(value);
        table.add(j, new MapEntry<>(key, value));
        return null;
    }

    @Override public V remove(K key) {
        checkKey(key);
        int j = findIndex(key);
        if (j == size() || compare(key, table.get(j)) != 0) return null;
        return table.remove(j).getValue();
    }

    private Entry<K, V> safeEntry(int j) {
        if (j < 0 || j >= table.size()) return null;
        return table.get(j);
    }

    @Override public Entry<K, V> firstEntry()  { return safeEntry(0); }
    @Override public Entry<K, V> lastEntry()   { return safeEntry(table.size() - 1); }

    @Override public Entry<K, V> ceilingEntry(K key) {
        return safeEntry(findIndex(key));
    }

    @Override public Entry<K, V> floorEntry(K key) {
        int j = findIndex(key);
        if (j == size() || !key.equals(table.get(j).getKey())) j--;
        return safeEntry(j);
    }

    @Override public Entry<K, V> lowerEntry(K key) {
        return safeEntry(findIndex(key) - 1);
    }

    @Override public Entry<K, V> higherEntry(K key) {
        int j = findIndex(key);
        if (j < size() && key.equals(table.get(j).getKey())) j++;
        return safeEntry(j);
    }

    private Iterable<Entry<K, V>> snapshot(int startIndex, K stop) {
        ArrayList<Entry<K, V>> buf = new ArrayList<>();
        int j = startIndex;
        while (j < table.size() && (stop == null || compare(stop, table.get(j)) > 0))
            buf.add(table.get(j++));
        return buf;
    }

    @Override public Iterable<Entry<K, V>> entrySet() { return snapshot(0, null); }

    @Override public Iterable<Entry<K, V>> subMap(K fromKey, K toKey) {
        return snapshot(findIndex(fromKey), toKey);
    }
}

// ─── UnsortedTableMap (used as ChainHashMap bucket) ──────────────────────────
class UnsortedTableMap<K, V> extends AbstractMap<K, V> {
    private ArrayList<MapEntry<K, V>> table = new ArrayList<>();

    private int findIndex(K key) {
        int n = table.size();
        for (int j = 0; j < n; j++)
            if (table.get(j).getKey().equals(key)) return j;
        return -1;
    }

    @Override public int size() { return table.size(); }

    @Override public V get(K key) {
        int j = findIndex(key);
        return (j == -1) ? null : table.get(j).getValue();
    }

    @Override public V put(K key, V value) {
        int j = findIndex(key);
        if (j == -1) { table.add(new MapEntry<>(key, value)); return null; }
        return table.get(j).setValue(value);
    }

    @Override public V remove(K key) {
        int j = findIndex(key);
        if (j == -1) return null;
        V ans = table.get(j).getValue();
        if (j != size() - 1) table.set(j, table.get(size() - 1));
        table.remove(size() - 1);
        return ans;
    }

    @Override public Iterable<Entry<K, V>> entrySet() { return new ArrayList<>(table); }
}

// ─── AbstractHashMap ─────────────────────────────────────────────────────────
abstract class AbstractHashMap<K, V> extends AbstractMap<K, V> {
    protected int n = 0;
    protected int capacity;
    private int prime;
    private long scale, shift;

    public AbstractHashMap(int cap, int p) {
        prime = p; capacity = cap;
        Random rand = new Random();
        scale = rand.nextInt(prime - 1) + 1;
        shift = rand.nextInt(prime);
        createTable();
    }
    public AbstractHashMap(int cap) { this(cap, 109345121); }
    public AbstractHashMap()       { this(17); }

    @Override public int size() { return n; }
    @Override public V get(K key)           { return bucketGet(hashValue(key), key); }
    @Override public V remove(K key)        { return bucketRemove(hashValue(key), key); }

    @Override public V put(K key, V value) {
        V ans = bucketPut(hashValue(key), key, value);
        if (n > capacity / 2) resize(2 * capacity - 1);
        return ans;
    }

    private int hashValue(K key) {
        return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity);
    }

    private void resize(int newCap) {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>(n);
        for (Entry<K, V> e : entrySet()) buffer.add(e);
        capacity = newCap;
        createTable();
        n = 0;
        for (Entry<K, V> e : buffer) put(e.getKey(), e.getValue());
    }

    protected abstract void createTable();
    protected abstract V bucketGet(int h, K k);
    protected abstract V bucketPut(int h, K k, V v);
    protected abstract V bucketRemove(int h, K k);
}

// ─── ChainHashMap ─────────────────────────────────────────────────────────────
class ChainHashMap<K, V> extends AbstractHashMap<K, V> {
    @SuppressWarnings("unchecked")
    private UnsortedTableMap<K, V>[] table;

    public ChainHashMap()         { super(); }
    public ChainHashMap(int cap)  { super(cap); }

    @Override @SuppressWarnings("unchecked")
    protected void createTable() {
        table = (UnsortedTableMap<K, V>[]) new UnsortedTableMap[capacity];
    }

    @Override protected V bucketGet(int h, K k) {
        UnsortedTableMap<K, V> bucket = table[h];
        return (bucket == null) ? null : bucket.get(k);
    }

    @Override protected V bucketPut(int h, K k, V v) {
        UnsortedTableMap<K, V> bucket = table[h];
        if (bucket == null) bucket = table[h] = new UnsortedTableMap<>();
        int oldSize = bucket.size();
        V ans = bucket.put(k, v);
        n += (bucket.size() - oldSize);
        return ans;
    }

    @Override protected V bucketRemove(int h, K k) {
        UnsortedTableMap<K, V> bucket = table[h];
        if (bucket == null) return null;
        int oldSize = bucket.size();
        V ans = bucket.remove(k);
        n -= (oldSize - bucket.size());
        return ans;
    }

    @Override public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>();
        for (int h = 0; h < capacity; h++)
            if (table[h] != null)
                for (Entry<K, V> e : table[h].entrySet()) buffer.add(e);
        return buffer;
    }
}

// ═════════════════════════════════════════════════════════════════════════════
//  EXERCISE 2 — Main class
// ═════════════════════════════════════════════════════════════════════════════
public class Exercise2_Maps {

    // ── helper: print a separator ─────────────────────────────────────────────
    static void sep(String title) {
        System.out.println("\n──────────────────────────────────────────");
        System.out.println("  " + title);
        System.out.println("──────────────────────────────────────────");
    }

    public static void main(String[] args) {

        // ══════════════════════════════════════════════════════════════════════
        //  PART A — SortedTableMap
        // ══════════════════════════════════════════════════════════════════════
        sep("PART A — SortedTableMap<String, Integer>");

        SortedTableMap<String, Integer> stm = new SortedTableMap<>();

        // 1. Populate
        stm.put("mango",     3);
        stm.put("apple",     7);
        stm.put("banana",    2);
        stm.put("cherry",    5);
        stm.put("date",      1);
        stm.put("elderberry",4);
        System.out.println("Inserted 6 entries. Size = " + stm.size());

        // 2. Basic get
        System.out.println("\n-- get() lookups --");
        System.out.println("apple       -> " + stm.get("apple"));    // 7
        System.out.println("mango       -> " + stm.get("mango"));    // 3
        System.out.println("kiwi (miss) -> " + stm.get("kiwi"));     // null

        // 3. Update (put on existing key)
        stm.put("apple", 10);
        System.out.println("apple after update -> " + stm.get("apple")); // 10

        // 4. SortedMap navigation methods
        System.out.println("\n-- SortedMap navigation --");
        System.out.println("firstEntry()              -> " + stm.firstEntry());
        System.out.println("lastEntry()               -> " + stm.lastEntry());
        System.out.println("ceilingEntry(\"avocado\")  -> " + stm.ceilingEntry("avocado")); // banana
        System.out.println("floorEntry(\"cherry\")     -> " + stm.floorEntry("cherry"));   // cherry
        System.out.println("lowerEntry(\"cherry\")     -> " + stm.lowerEntry("cherry"));   // banana
        System.out.println("higherEntry(\"cherry\")    -> " + stm.higherEntry("cherry"));  // date

        // 5. subMap [banana, date)  — inclusive start, exclusive end
        System.out.println("\n-- subMap(\"banana\", \"date\") --");
        for (Entry<String, Integer> e : stm.subMap("banana", "date"))
            System.out.println("  " + e.getKey() + " -> " + e.getValue());
        // banana->2, cherry->5

        // 6. entrySet() — always sorted by key
        System.out.println("\n-- entrySet() (sorted order guaranteed) --");
        for (Entry<String, Integer> e : stm.entrySet())
            System.out.printf("  %-12s -> %d%n", e.getKey(), e.getValue());

        // 7. remove
        stm.remove("date");
        System.out.println("\nAfter remove(\"date\"), size = " + stm.size()); // 5

        // ══════════════════════════════════════════════════════════════════════
        //  PART B — ChainHashMap
        // ══════════════════════════════════════════════════════════════════════
        sep("PART B — ChainHashMap<String, Integer>");

        ChainHashMap<String, Integer> chm = new ChainHashMap<>();

        // 1. Populate
        chm.put("STU001", 88);
        chm.put("STU002", 74);
        chm.put("STU003", 91);
        chm.put("STU004", 65);
        chm.put("STU005", 82);
        System.out.println("Inserted 5 entries. Size = " + chm.size());

        // 2. get
        System.out.println("\n-- get() lookups --");
        System.out.println("STU001 -> " + chm.get("STU001")); // 88
        System.out.println("STU003 -> " + chm.get("STU003")); // 91
        System.out.println("STU999 -> " + chm.get("STU999")); // null

        // 3. Update
        chm.put("STU002", 79);
        System.out.println("STU002 after update -> " + chm.get("STU002")); // 79

        // 4. Containment check
        String id = "STU004";
        Integer grade = chm.get(id);
        if (grade != null) System.out.println(id + " exists, grade = " + grade);

        // 5. entrySet — order NOT guaranteed (hash-based)
        System.out.println("\n-- entrySet() (order not guaranteed) --");
        for (Entry<String, Integer> e : chm.entrySet())
            System.out.println("  " + e.getKey() + " -> " + e.getValue());

        // 6. remove
        chm.remove("STU004");
        System.out.println("After remove(STU004), size = " + chm.size()); // 4

        // 7. Word frequency counter  (mirrors WordCount.java)
        sep("PART B2 — Word frequency counter (like WordCount.java)");
        ChainHashMap<String, Integer> freq = new ChainHashMap<>();
        String[] words = {"data","structures","data","algorithms","data","structures","java"};
        for (String w : words) {
            Integer count = freq.get(w);
            freq.put(w, count == null ? 1 : count + 1);
        }
        System.out.println("Word frequencies:");
        for (Entry<String, Integer> e : freq.entrySet())
            System.out.printf("  %-14s -> %d%n", e.getKey(), e.getValue());
        // data->3, structures->2, algorithms->1, java->1

        // ══════════════════════════════════════════════════════════════════════
        //  COMPARISON
        // ══════════════════════════════════════════════════════════════════════
        sep("Summary — SortedTableMap vs ChainHashMap");
        System.out.println("  SortedTableMap : get/put/remove = O(log n), entrySet sorted");
        System.out.println("  ChainHashMap   : get/put/remove = O(1) expected, no order");
    }
}
