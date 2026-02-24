
import java.util.*;

public class ex10<T> {
    private Stack<T> s1 = new Stack<>(), s2 = new Stack<>();

    public void enqueue(T item) {
        s1.push(item);
    }
    public T dequeue() {

        //Check if both stacks are empty
        if(s1.empty() && s2.empty()){
            throw new NoSuchElementException("Queue is empty");
        }

        //Moving to a new stack inorder to pop
        if(s2.isEmpty()){
            while (!s1.isEmpty()){
                s2.push(s1.pop());
            }
        }

        //Removing the oldest element
        return s2.pop();
    }

    public static void main(String[] args) {
        ex10<Integer> q = new ex10<>();
        q.enqueue(1); q.enqueue(2); q.enqueue(3);
        System.out.println(q.dequeue()); // 1
        System.out.println(q.dequeue()); // 2
    }
}