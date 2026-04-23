import java.util.Objects;

public class Exercise5_HashTableOpen<K, V> {

    public Exercise5_HashTableOpen(Entry<K, V>[] table, int size, int capacity) {
        this.table = table;
        this.size = size;
        this.capacity = capacity;
    }

    private static class Entry<K, V>{
        K k; V v;
        boolean tombstone;
        Entry(K k, V v){
            this.k = k;
            this.v = v;
            this.tombstone = false;
        }
    }
    private Entry<K, V>[] table;
    private int size = 0;
    private final int capacity;

    public Exercise5_HashTableOpen(int cap){
        capacity = cap;
        table = (Entry<K, V>[]) new Entry[cap];
    }

    private int index(K k) {
        return (Objects.hashCode(k) & 0x7fffffff) % capacity;
    }

    public V put(K k, V v){
        int start = index(k);
        int tombstoneIdx = -1;

        for(int i=0; i<capacity; i++){
            int slot = (start+i)%capacity;
            Entry<K, V> e = table[slot];

            if(e == null){
                int insertAt = (tombstoneIdx != -1) ? tombstoneIdx : slot;
                table[insertAt] = new Entry<>(k,v);
                size++;
                return null;
            }
            if(e.tombstone){
                if(tombstoneIdx ==-1) tombstoneIdx = slot;  //remove
                continue;
            }
            if(Objects.equals(e.k, k)){
                V old = e.v;
                e.v = v;    //update
                return old;
            }
        }
        throw new RuntimeException("Hash table is full");
    }

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

    public V remove(K k){
        int start = index(k);
        for (int i=0; i<capacity; i++){
            int slot = (start + i) % capacity;
            Entry<K, V> e = table[slot];

            if (e == null) return null;
            if (e.tombstone) continue;
            if (Objects.equals(e.k, k)) {
                V old = e.v;
                e.tombstone = true;  //mark as deleted
                e.k = null;
                e.v = null;
                size--;
                return old;
            }
        }
        return null;
    }

    public int size() {return size;}
    public boolean isEmpty() {return size == 0;}

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

    public static void main(String[] args){
        Exercise5_HashTableOpen<String,Integer> h=new Exercise5_HashTableOpen<>(5);
        h.put("A",10); h.put("F",20); // collides with "A" in small table
        System.out.println(h.get("A")); // 10
        System.out.println(h.get("F")); // 20
        System.out.println(h.get("Z")); // null
    }
}

