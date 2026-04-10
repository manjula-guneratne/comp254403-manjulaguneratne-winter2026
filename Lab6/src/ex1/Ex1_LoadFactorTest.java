package ex1;

import java.util.Random;

/**
 * Exercise 1 – Test application for configurable load factor in ChainHashMap.
 *
 * Inserts 500 random integer keys and measures:
 *  - number of resize events (approximated by capacity growth)
 *  - average probe length (via a simple timing proxy)
 * for load factors: 0.25, 0.50, 0.75, 0.90
 */
public class Ex1_LoadFactorTest {

  /** Counts how many entries are actually stored (sanity check). */
  private static int countEntries(ChainHashMap<Integer,Integer> map, int[] keys) {
    int found = 0;
    for (int k : keys) if (map.get(k) != null) found++;
    return found;
  }

  /** Inserts 'numKeys' random integers and returns the final map capacity. */
  private static int runExperiment(double loadFactor, int numKeys, int seed) {
    ChainHashMap<Integer,Integer> map = new ChainHashMap<>(17, loadFactor);
    Random rng = new Random(seed);
    long start = System.nanoTime();

    for (int i = 0; i < numKeys; i++) {
      int key = rng.nextInt(numKeys * 5);   // some collisions expected
      map.put(key, i);
    }

    long elapsed = System.nanoTime() - start;
    System.out.printf("  loadFactor=%.2f | size=%4d | capacity=%4d | actual load=%.3f | time=%,d ns%n",
        loadFactor,
        map.size(),
        map.capacity,
        (double) map.size() / map.capacity,
        elapsed);

    return map.capacity;
  }

  public static void main(String[] args) {
    final int NUM_KEYS = 500;
    final int SEED     = 42;
    double[] loadFactors = {0.25, 0.50, 0.75, 0.90};

    System.out.println("=== Exercise 1: ChainHashMap with Configurable Load Factor ===");
    System.out.println("Inserting " + NUM_KEYS + " random keys into ChainHashMap\n");

    for (double lf : loadFactors)
      runExperiment(lf, NUM_KEYS, SEED);

    // ----------------------------------------------------------------
    // Demonstrate get/put/remove still work correctly
    // ----------------------------------------------------------------
    System.out.println("\n--- Correctness check (loadFactor = 0.75) ---");
    ChainHashMap<String,Integer> demo = new ChainHashMap<>(8, 0.75);
    String[] words = {"apple","banana","cherry","date","elderberry","fig","grape"};
    for (int i = 0; i < words.length; i++) demo.put(words[i], i + 1);

    System.out.println("get(\"cherry\") = " + demo.get("cherry"));   // expected 3
    System.out.println("get(\"fig\")    = " + demo.get("fig"));      // expected 6
    demo.remove("banana");
    System.out.println("After remove(\"banana\"), get(\"banana\") = " + demo.get("banana")); // null

    // ----------------------------------------------------------------
    // Demonstrate setMaxLoad at runtime
    // ----------------------------------------------------------------
    System.out.println("\n--- Runtime setMaxLoad demo ---");
    ChainHashMap<Integer,Integer> live = new ChainHashMap<>(17, 0.9);
    System.out.printf("Before inserts:  capacity=%d, maxLoad=%.2f%n", live.capacity, live.getMaxLoad());
    for (int i = 0; i < 30; i++) live.put(i, i);
    System.out.printf("After 30 inserts (maxLoad=0.9): capacity=%d, size=%d%n", live.capacity, live.size());
    live.setMaxLoad(0.3);   // tighten – should trigger immediate resize
    System.out.printf("After setMaxLoad(0.3):          capacity=%d, size=%d%n", live.capacity, live.size());
  }
}
