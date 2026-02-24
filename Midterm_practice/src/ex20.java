public class ex20 {
    private int[] data;
    private int head = 0, tail = 0, size = 0;

    public ex20(int capacity) {
        data = new int[capacity];
    }

    // Queue is full when size equals capacity
    public boolean isFull() {
        return size == data.length;
    }

    // Queue is empty when size is zero
    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(int x) throws IllegalStateException {
        if (isFull()) {
            throw new IllegalStateException("Queue is full");
        }

        data[tail] = x;
        tail = (tail + 1) % data.length; // wrap around
        size++;
    }

    public int dequeue() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        int value = data[head];
        head = (head + 1) % data.length; // wrap around
        size--;
        return value;
    }

    public static void main(String[] args) {
        ex20 q = new ex20(3);

        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);

        System.out.println(q.isFull());  // true
        System.out.println(q.dequeue()); // 1

        q.enqueue(4); // wraps to index 0

        System.out.println(q.dequeue()); // 2
    }
}