import java.util.*;

public class ex11<T> {
    private Queue<T> q1 = new LinkedList<>(), q2 = new LinkedList<>();

    public void push(T item) {
        q2.offer(item);

        while(!q1.isEmpty()){
            q2.offer(q1.poll());
        }

        //swap q1 and q2
        Queue<T> temp = q1;
        q1 = q2;
        q2 = temp;

    }

    public T pop() {
        if (q1.isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }

        return q1.poll();
    }

    public static void main(String[] args) {
        ex11<Integer> s = new ex11<>();
        s.push(1); s.push(2); s.push(3);

        System.out.println(s.pop()); // 3
        System.out.println(s.pop()); // 2
    }
}