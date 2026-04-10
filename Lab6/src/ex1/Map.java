package ex1;

public interface Map<K, V> {

    interface Entry<K, V> {
        K getKey();
        V getValue();
    }

    int size();
    boolean isEmpty();
    V get(K key);
    V put(K key, V value);
    V remove(K key);
    Iterable<K> keySet();
    Iterable<V> values();
    Iterable<Entry<K,V>> entrySet();
}