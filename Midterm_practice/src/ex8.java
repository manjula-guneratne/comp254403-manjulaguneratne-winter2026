import java.util.*;

public class ex8 {
    static class MyQueue<T> {
        private LinkedList<T> list = new LinkedList<>();
        public void enqueue(T item){ list.addLast(item); }
        public T dequeue(){ return list.removeFirst(); }
    }

    public static void main(String[] args) {
        MyQueue<Integer> queue = new MyQueue<>();
        long t1 = System.nanoTime();

        //Adding
        for(int i=0; i<1_000_000; i++){
            queue.enqueue(i);
        }

        long t2 = System.nanoTime();

        //Remove
        for(int i=0; i<1_000_000; i++){
            queue.dequeue();
        }

        long t3 = System.nanoTime();
        System.out.println("Enqueue time: " + (t2-t1)/1e6 + " ms");
        System.out.println("Dequeue time: " + (t3-t2)/1e6 + " ms");
    }
}
