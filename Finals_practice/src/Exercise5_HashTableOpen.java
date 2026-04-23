import java.util.Objects;

public class Exercise5_HashTableOpen<K, V> {

    // ─── Internal entry ───────────────────────────────────────────────────────
    private static class Entry<K, V> {
        K k; V v;
        boolean tombstone;           // marks a "deleted" slot
        Entry(K k, V v) { this.k = k; this.v = v; this.tombstone = false; }
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V>[] table;
    private int size = 0;
    private final int capacity;

    @SuppressWarnings("unchecked")
    public Exercise5_HashTableOpen(int cap) {
        capacity = cap;
        table = (Entry<K, V>[]) new Entry[cap];
    }

    // ─── Hash function ────────────────────────────────────────────────────────
    /** Maps key to a slot index in [0, capacity). */
    private int index(K k) {
        return (Objects.hashCode(k) & 0x7fffffff) % capacity;
    }

    // ─── put (insert or update) ───────────────────────────────────────────────
    /**
     * Inserts key k with value v using linear probing.
     *
     * Probe sequence: index(k), index(k)+1, index(k)+2, ... (wrap around)
     * Stop when:
     *   (a) slot is null            → insert here
     *   (b) slot has same key       → update value
     *   (c) slot is a tombstone     → record position; keep probing for same key
     *
     * If a tombstone was found before an empty slot, reuse the tombstone position
     * so deleted slots are recycled.
     *
     * Time: O(1) expected with a good hash and low load factor.
     */
    public V put(K k, V v) {
        int start = index(k);
        int tombstoneIdx = -1;               // first tombstone found (reuse candidate)

        for (int i = 0; i < capacity; i++) {
            int slot = (start + i) % capacity;
            Entry<K, V> e = table[slot];

            if (e == null) {
                // Empty slot — key definitely not in table
                int insertAt = (tombstoneIdx != -1) ? tombstoneIdx : slot;
                table[insertAt] = new Entry<>(k, v);
                size++;
                return null;                 // no previous value
            }
            if (e.tombstone) {
                if (tombstoneIdx == -1) tombstoneIdx = slot;  // remember first tombstone
                continue;
            }
            if (Objects.equals(e.k, k)) {
                V old = e.v;
                e.v = v;                     // update existing entry
                return old;
            }
        }

        // Table full (should not happen if load factor is kept low)
        throw new RuntimeException("Hash table is full");
    }

    // ─── get ──────────────────────────────────────────────────────────────────
    /**
     * Returns the value for key k, or null if not present.
     *
     * Probe until:
     *   - null slot → key is definitely not here (stop)
     *   - tombstone → skip (key may be further along)
     *   - matching key → return value
     *
     * Time: O(1) expected.
     */
    public V get(K k) {
        int start = index(k);
        for (int i = 0; i < capacity; i++) {
            int slot = (start + i) % capacity;
            Entry<K, V> e = table[slot];

            if (e == null)           return null;          // definitive miss
            if (e.tombstone)         continue;             // skip deleted slot
            if (Objects.equals(e.k, k)) return e.v;       // found
        }
        return null;
    }

    // ─── remove ───────────────────────────────────────────────────────────────
    /**
     * Removes key k and marks its slot as a tombstone.
     * Tombstones are necessary because removing a slot would break
     * the probe chain for keys that probed past it.
     */
    public V remove(K k) {
        int start = index(k);
        for (int i = 0; i < capacity; i++) {
            int slot = (start + i) % capacity;
            Entry<K, V> e = table[slot];

            if (e == null) return null;
            if (e.tombstone) continue;
            if (Objects.equals(e.k, k)) {
                V old = e.v;
                e.tombstone = true;          // mark as deleted (don't set to null!)
                e.k = null; e.v = null;
                size--;
                return old;
            }
        }
        return null;
    }

    public int size()        { return size; }
    public boolean isEmpty() { return size == 0; }

    /** Debug view of the internal table. */
    public void dump() {
        System.out.println("  Table (capacity=" + capacity + ", size=" + size + "):");
        for (int i = 0; i < capacity; i++) {
            Entry<K, V> e = table[i];
            if (e == null)         System.out.println("    [" + i + "] null");
            else if (e.tombstone)  System.out.println("    [" + i + "] TOMBSTONE");
            else                   System.out.println("    [" + i + "] " + e.k + " -> " + e.v
                                                      + "  (hash=" + index(e.k) + ")");
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  main
    // ═════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {

        System.out.println("════ Exercise 5 — Open Addressing (Linear Probing) ════\n");

        // ── Test 1: basic from the exercise sheet (capacity 5) ────────────────
        Exercise5_HashTableOpen<String, Integer> h1 = new Exercise5_HashTableOpen<>(5);
        h1.put("A", 10);
        h1.put("F", 20);   // likely collides with "A" in a small table

        System.out.println("After inserting A=10, F=20:");
        h1.dump();
        System.out.println("get(A) = " + h1.get("A"));   // 10
        System.out.println("get(F) = " + h1.get("F"));   // 20
        System.out.println("get(Z) = " + h1.get("Z"));   // null

        System.out.println("\nPASS get(A)=10   : " + (h1.get("A").equals(10)));
        System.out.println("PASS get(F)=20   : " + (h1.get("F").equals(20)));
        System.out.println("PASS get(Z)=null : " + (h1.get("Z") == null));

        // ── Test 2: update existing key ───────────────────────────────────────
        System.out.println("\n--- Test 2: update ---");
        Exercise5_HashTableOpen<String, Integer> h2 = new Exercise5_HashTableOpen<>(11);
        h2.put("hello", 1);
        h2.put("world", 2);
        Integer old = h2.put("hello", 99);   // update
        System.out.println("Old value of 'hello': " + old);       // 1
        System.out.println("New value of 'hello': " + h2.get("hello")); // 99

        // ── Test 3: remove + tombstone ────────────────────────────────────────
        System.out.println("\n--- Test 3: remove + tombstone ---");
        Exercise5_HashTableOpen<Integer, String> h3 = new Exercise5_HashTableOpen<>(7);
        h3.put(1, "one");
        h3.put(8, "eight");  // hash(8) % 7 == 1, same as hash(1) % 7 → collision
        h3.put(15,"fifteen");// another collision at slot 1

        System.out.println("Before remove:");
        h3.dump();

        System.out.println("remove(1): " + h3.remove(1));    // "one"
        System.out.println("After remove(1) [tombstone placed]:");
        h3.dump();

        // get(8) must still work even though slot 1 now has a tombstone
        System.out.println("get(8)  still works: " + h3.get(8));    // "eight"
        System.out.println("get(15) still works: " + h3.get(15));   // "fifteen"
        System.out.println("get(1)  now null   : " + h3.get(1));    // null

        // ── Test 4: large table ───────────────────────────────────────────────
        System.out.println("\n--- Test 4: large table ---");
        Exercise5_HashTableOpen<Integer, Integer> h4 = new Exercise5_HashTableOpen<>(101);
        for (int i = 0; i < 50; i++) h4.put(i, i * i);
        System.out.println("Inserted 50 entries, size = " + h4.size());
        boolean allCorrect = true;
        for (int i = 0; i < 50; i++)
            if (!h4.get(i).equals(i * i)) { allCorrect = false; break; }
        System.out.println("All values correct: " + allCorrect);

        // ── Concept reminder ─────────────────────────────────────────────────
        System.out.println("\n--- Key concept: why tombstones? ---");
        System.out.println("If we set a deleted slot to null, get() would stop probing early");
        System.out.println("and falsely report keys NOT FOUND (even if they were inserted");
        System.out.println("after the now-deleted slot). Tombstones tell get() to keep probing.");
    }
}
