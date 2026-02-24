
public class ex9 {
    public static long fact(int n) {
        // Base case
        if(n <= 1)
            return 1;

        return n * fact(n-1);
    }

    public static void main(String[] args) {
        long t1 = System.nanoTime();
        long result = fact(20);
        long t2 = System.nanoTime();
        System.out.println("20! = " + result);
        System.out.println("Time: " + (t2-t1)/1e6 + " ms");
    }
}