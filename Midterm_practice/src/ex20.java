public class ex20 {
    private int[] data;
    private int head = 0, tail = 0, size = 0;

    public ex20(int capacity) {
        data = new int[capacity];
    }

    public boolean isFull() {
        return size == data.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(int x) throws IllegalStateException {
        if (isFull())
            throw new IllegalStateException("Queue is full");

        data[tail] = x;

        // move tail circularly
        tail = (tail + 1) % data.length;

        size++;
    }

    public int dequeue() throws IllegalStateException {
        if (isEmpty())
            throw new IllegalStateException("Queue is empty");

        int value = data[head];

        // move head circularly
        head = (head + 1) % data.length;

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

        q.enqueue(4); // wraps around

        System.out.println(q.dequeue()); // 2
    }
}