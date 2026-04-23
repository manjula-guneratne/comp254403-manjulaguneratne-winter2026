import java.util.ArrayList;

public class Exercise3_4_UnsortedTableMap<K,V> {

    private static class Entry<K,V>{
        K k;
        V v;
        Entry(K k,V v){this.k=k;this.v=v;}
        public String toString() { return "(" + k + "=" + v + ")"; }
    }

    private final ArrayList<Entry<K, V>> table = new ArrayList<>();

    private int findIndex(K k){
        for(int i=0;i<table.size();i++) {
            K ek = table.get(i).k;
            if (k == null ? ek == null : k.equals(ek)) return i;
        }
        return -1;
    }

    public V put(K k,V v){ // baseline
        int i=findIndex(k);
        if(i==-1){
            table.add(new Entry<>(k,v));
            return null;
        }
        V old=table.get(i).v;
        table.get(i).v=v;
        return old;
    }

    public V get(K k) {
        int i=findIndex(k);
        return (i==-1) ? null : table.get(i).v;
    }

    public int size() {
        return table.size();
    }
    public boolean isEmpty() {
        return table.isEmpty();
    }

    public boolean containsKey(K k){
        return findIndex(k) != -1;
    }
    public V getOrDefault(K k, V defaultVal){
        int i = findIndex(k);
        return (i == -1) ? defaultVal : table.get(i).v;
    }

    public V remove(K k){
        int i = findIndex(k);
        if(i == -1) return null;

        V old = table.get(i).v;
        int last = table.size() -1;
        if(i != last)
            table.set(i, table.get(last));
        table.remove(last);
        return old;
    }

    @Override
    public String toString() {
        return table.toString();
    }

    public static void main(String[] argsS){
        Exercise3_4_UnsortedTableMap<String,Integer> m =
                new Exercise3_4_UnsortedTableMap<>();
        m.put("a",1);
        System.out.println(m.containsKey("a")); // true
        System.out.println(m.containsKey("b")); // false
        System.out.println(m.getOrDefault("b",99)); // 99
    }
}