package ex1;

import java.util.Random;

public class Ex1_LoadFactorTest {

  private static int countEntries(ChainHashMap<Integer,Integer> map, int[] keys) {
    int found = 0;
    for (int k : keys) if (map.get(k) != null) found++;
    return found;
  }

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
  }
}
