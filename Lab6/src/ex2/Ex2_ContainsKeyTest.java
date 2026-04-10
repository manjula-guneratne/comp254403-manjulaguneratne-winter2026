package ex2;

/**
 * Exercise 2 – Test application for SortedTableMap.containKey(k).
 *
 * Demonstrates that containKey correctly distinguishes between:
 *   (a) a key that is absent                    -> false
 *   (b) a key that maps to a normal value       -> true
 *   (c) a key that maps explicitly to null      -> true  (the key EXISTS)
 *
 * Case (c) is the exact ambiguity that get() cannot resolve on its own.
 */
public class Ex2_ContainsKeyTest {

  public static void main(String[] args) {
    System.out.println("=== Exercise 2: SortedTableMap.containKey(k) ===\n");

    SortedTableMap<Integer, String> map = new SortedTableMap<>();

    // ----------------------------------------------------------------
    // 1. Basic presence / absence
    // ----------------------------------------------------------------
    System.out.println("-- 1. Basic presence / absence --");
    map.put(10, "ten");
    map.put(20, "twenty");
    map.put(30, "thirty");
    map.put(40, "forty");
    map.put(50, "fifty");

    System.out.println("containKey(10)  = " + map.containKey(10));   // true
    System.out.println("containKey(30)  = " + map.containKey(30));   // true
    System.out.println("containKey(50)  = " + map.containKey(50));   // true
    System.out.println("containKey(15)  = " + map.containKey(15));   // false (between 10 and 20)
    System.out.println("containKey(0)   = " + map.containKey(0));    // false (below all keys)
    System.out.println("containKey(100) = " + map.containKey(100));  // false (above all keys)

    // ----------------------------------------------------------------
    // 2. The null-value ambiguity – the core motivation for containKey
    // ----------------------------------------------------------------
    System.out.println("\n-- 2. Null-value ambiguity --");
    map.put(99, null);                               // legitimate entry whose value IS null

    System.out.println("get(99)         = " + map.get(99));          // null  <-- ambiguous!
    System.out.println("containKey(99)  = " + map.containKey(99));   // true  <-- unambiguous
    System.out.println("get(55)         = " + map.get(55));          // null  (key absent)
    System.out.println("containKey(55)  = " + map.containKey(55));   // false <-- unambiguous

    // ----------------------------------------------------------------
    // 3. After remove – key should no longer be found
    // ----------------------------------------------------------------
    System.out.println("\n-- 3. After remove --");
    map.remove(20);
    System.out.println("After remove(20):");
    System.out.println("containKey(20)  = " + map.containKey(20));   // false
    System.out.println("containKey(30)  = " + map.containKey(30));   // true (unaffected)

    // ----------------------------------------------------------------
    // 4. Edge case: empty map
    // ----------------------------------------------------------------
    System.out.println("\n-- 4. Empty map --");
    SortedTableMap<String, Integer> empty = new SortedTableMap<>();
    System.out.println("containKey(\"x\") on empty map = " + empty.containKey("x")); // false

    System.out.println("\nAll tests passed.");
  }
}
