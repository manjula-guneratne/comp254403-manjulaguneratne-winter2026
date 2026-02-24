import java.util.*;

public class ex7 {
    static class MyStack<T> {
        private List<T> list = new ArrayList<>();
        public void push(T item){ list.add(item); }
        public T pop(int i){ return list.remove(list.size()-1); }
    }

    public static void main(String[] args) {
        MyStack<Integer> stack = new MyStack<>();
        long t1 = System.nanoTime();
        //      PUSH
        for (int i=0; i<1000000; i++){
            stack.push(i);
        }

        long t2 = System.nanoTime();
        //      POP
        for (int i=0; i<1000000; i++){
            stack.pop(i);
        }

        long t3 = System.nanoTime();

        double pushTimeMs = (t2-t1)/1_000_000.0;
        double popTimeMs = (t3-t2)/1_000_000.0;

        System.out.println("Push time: " + pushTimeMs + " ms");
        System.out.println("Pop time:  " + popTimeMs + " ms");
    }
}