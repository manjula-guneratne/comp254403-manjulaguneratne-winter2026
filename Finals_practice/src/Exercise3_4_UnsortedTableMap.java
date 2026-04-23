import java.util.ArrayList;

/**
 * Exercises 3 & 4 combined — UnsortedTableMap with
 *   containsKey, getOrDefault, and remove.
 */
public class Exercise3_4_UnsortedTableMap<K, V> {

    // ─── Internal entry class ─────────────────────────────────────────────────
    private static class Entry<K, V> {
        K k; V v;
        Entry(K k, V v) { this.k = k; this.v = v; }
        public String toString() { return "(" + k + "=" + v + ")"; }
    }

    private final ArrayList<Entry<K, V>> table = new ArrayList<>();

    // ─── findIndex — private utility used by ALL public methods ──────────────
    /**
     * Returns the index of the entry with key k, or -1 if not found.
     * All public methods call this ONCE — no redundant scans.
     * Time: O(n)
     */
    private int findIndex(K k) {
        for (int i = 0; i < table.size(); i++) {
            K ek = table.get(i).k;
            // null-safe equality check
            if (k == null ? ek == null : k.equals(ek)) return i;
        }
        return -1;
    }

    // ─── put (baseline — provided in starter) ────────────────────────────────
    /** Inserts or updates key k with value v. Returns the old value, or null. */
    public V put(K k, V v) {
        int i = findIndex(k);
        if (i == -1) {
            table.add(new Entry<>(k, v));
            return null;
        }
        V old = table.get(i).v;
        table.get(i).v = v;
        return old;
    }

    public V get(K k) {
        int i = findIndex(k);
        return (i == -1) ? null : table.get(i).v;
    }

    public int size()        { return table.size(); }
    public boolean isEmpty() { return table.isEmpty(); }

    // ─── Exercise 3A: containsKey ─────────────────────────────────────────────
    /**
     * Returns true if key k exists in the map.
     * Single call to findIndex — O(n).
     */
    public boolean containsKey(K k) {
        return findIndex(k) != -1;        // -1 means absent; anything else means present
    }

    // ─── Exercise 3B: getOrDefault ────────────────────────────────────────────
    /**
     * Returns the value for k if present, otherwise returns defaultVal.
     * Single call to findIndex — O(n).
     */
    public V getOrDefault(K k, V defaultVal) {
        int i = findIndex(k);
        return (i == -1) ? defaultVal : table.get(i).v;
    }

    // ─── Exercise 4: remove ───────────────────────────────────────────────────
    /**
     * Removes the entry with key k and returns its old value.
     * Returns null if k was not present.
     *
     * O(1) deletion trick: swap the target entry with the LAST entry,
     * then remove the last slot.  No shifting needed — order doesn't matter
     * in an UnsortedTableMap.
     *
     * Time: O(n) for the scan, O(1) for the deletion itself.
     */
    public V remove(K k) {
        int i = findIndex(k);
        if (i == -1) return null;                          // key not found

        V old = table.get(i).v;
        int last = table.size() - 1;
        if (i != last)
            table.set(i, table.get(last));                 // overwrite slot i with last entry
        table.remove(last);                                // remove the (now-duplicate) last slot
        return old;
    }

    // ─── toString for easy inspection ─────────────────────────────────────────
    public String toString() { return table.toString(); }

    // ═════════════════════════════════════════════════════════════════════════
    //  main — tests for Exercises 3 & 4
    // ═════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {

        // ── Exercise 3: containsKey & getOrDefault ────────────────────────────
        System.out.println("════ Exercise 3 — containsKey & getOrDefault ════");

        Exercise3_4_UnsortedTableMap<String, Integer> m = new Exercise3_4_UnsortedTableMap<>();
        m.put("a", 1);
        m.put("b", 2);
        m.put("c", 3);

        System.out.println("Map: " + m);

        System.out.println("\ncontainsKey(\"a\") -> " + m.containsKey("a"));  // true
        System.out.println("containsKey(\"b\") -> " + m.containsKey("b"));  // true
        System.out.println("containsKey(\"z\") -> " + m.containsKey("z"));  // false
        System.out.println("containsKey(null)-> " + m.containsKey(null));  // false

        System.out.println("\ngetOrDefault(\"a\", 99) -> " + m.getOrDefault("a", 99)); // 1
        System.out.println("getOrDefault(\"z\", 99) -> " + m.getOrDefault("z", 99)); // 99
        System.out.println("getOrDefault(null,99)  -> " + m.getOrDefault(null, 99)); // 99

        // PASS checks (from exercise sheet)
        System.out.println("\n--- PASS checks (Exercise 3) ---");
        Exercise3_4_UnsortedTableMap<String, Integer> m2 = new Exercise3_4_UnsortedTableMap<>();
        m2.put("a", 1);
        System.out.println("containsKey(\"a\") = true  : " + (m2.containsKey("a") == true));
        System.out.println("containsKey(\"b\") = false : " + (m2.containsKey("b") == false));
        System.out.println("getOrDefault(\"b\",99) = 99: " + (m2.getOrDefault("b", 99) == 99));

        // ── Exercise 4: remove ────────────────────────────────────────────────
        System.out.println("\n════ Exercise 4 — remove ════");

        Exercise3_4_UnsortedTableMap<String, Integer> m3 = new Exercise3_4_UnsortedTableMap<>();
        m3.put("x", 7);
        m3.put("y", 8);
        m3.put("z", 9);
        System.out.println("Map before removes: " + m3);

        System.out.println("\nremove(\"x\") -> " + m3.remove("x"));  // 7
        System.out.println("Map after remove(x): " + m3);

        System.out.println("remove(\"z\") -> " + m3.remove("z"));  // 9
        System.out.println("Map after remove(z): " + m3);

        System.out.println("remove(\"missing\") -> " + m3.remove("missing"));  // null

        System.out.println("Remaining size: " + m3.size());  // 1  ("y" left)

        // PASS checks (from exercise sheet)
        System.out.println("\n--- PASS checks (Exercise 4) ---");
        Exercise3_4_UnsortedTableMap<String, Integer> m4 = new Exercise3_4_UnsortedTableMap<>();
        m4.put("x", 7); m4.put("y", 8);
        System.out.println("remove(\"x\") = 7   : " + (m4.remove("x") == 7));
        System.out.println("remove(\"z\") = null: " + (m4.remove("z") == null));

        // ── Edge cases ────────────────────────────────────────────────────────
        System.out.println("\n════ Edge cases ════");

        // Update existing key
        Exercise3_4_UnsortedTableMap<String, Integer> m5 = new Exercise3_4_UnsortedTableMap<>();
        m5.put("k", 10);
        Object old = m5.put("k", 20);   // update — should return 10
        System.out.println("put duplicate returns old: " + old);       // 10
        System.out.println("get after update: "         + m5.get("k")); // 20

        // Remove only entry
        m5.remove("k");
        System.out.println("isEmpty after removing only entry: " + m5.isEmpty()); // true

        System.out.println("\nAll tests complete.");
    }
}
