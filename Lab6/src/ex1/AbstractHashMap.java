package ex1;

import java.util.ArrayList;
import java.util.Random;

/**
 * AbstractHashMap with a user-configurable maximum load factor.
 * Default remains 0.5 if not specified.
 */
public abstract class AbstractHashMap<K,V> extends AbstractMap<K,V> {
  protected int n = 0;
  protected int capacity;
  private int prime;
  private long scale, shift;
  private double maxLoad;   // *** NEW: configurable load factor ***

  /** Full constructor: capacity, prime, maxLoad. */
  public AbstractHashMap(int cap, int p, double maxLoad) {
    if (maxLoad <= 0 || maxLoad >= 1)
      throw new IllegalArgumentException("maxLoad must be in (0, 1)");
    this.maxLoad = maxLoad;
    prime = p;
    capacity = cap;
    Random rand = new Random();
    scale = rand.nextInt(prime - 1) + 1;
    shift = rand.nextInt(prime);
    createTable();
  }

  /** capacity + prime, default maxLoad = 0.5 */
  public AbstractHashMap(int cap, int p) { this(cap, p, 0.5); }

  /** capacity + maxLoad, default prime */
  public AbstractHashMap(int cap, double maxLoad) { this(cap, 109345121, maxLoad); }

  /** capacity only */
  public AbstractHashMap(int cap) { this(cap, 109345121, 0.5); }

  /** All defaults */
  public AbstractHashMap() { this(17); }

  /** Returns the current maximum load factor. */
  public double getMaxLoad() { return maxLoad; }

  /** Updates the maximum load factor (resizes immediately if required). */
  public void setMaxLoad(double maxLoad) {
    if (maxLoad <= 0 || maxLoad >= 1)
      throw new IllegalArgumentException("maxLoad must be in (0, 1)");
    this.maxLoad = maxLoad;
    if (n > capacity * maxLoad) resize(2 * capacity - 1);
  }

  @Override public int size() { return n; }

  @Override
  public V get(K key) { return bucketGet(hashValue(key), key); }

  @Override
  public V remove(K key) { return bucketRemove(hashValue(key), key); }

  @Override
  public V put(K key, V value) {
    V answer = bucketPut(hashValue(key), key, value);
    if (n > capacity * maxLoad)          // *** use configurable maxLoad ***
      resize(2 * capacity - 1);
    return answer;
  }

  private int hashValue(K key) {
    return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity);
  }

  private void resize(int newCap) {
    ArrayList<Entry<K,V>> buffer = new ArrayList<>(n);
    for (Entry<K,V> e : entrySet()) buffer.add(e);
    capacity = newCap;
    createTable();
    n = 0;
    for (Entry<K,V> e : buffer) put(e.getKey(), e.getValue());
  }

  protected abstract void createTable();
  protected abstract V bucketGet(int h, K k);
  protected abstract V bucketPut(int h, K k, V v);
  protected abstract V bucketRemove(int h, K k);
}
