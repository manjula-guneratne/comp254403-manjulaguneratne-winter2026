public class ex14 {

    public static double pow(double x, int n) {

        // Base case
        if (n == 0)
            return 1;

        // Handle negative exponent (reciprocal rule)
        if (n < 0)
            return 1 / pow(x, -n);

        // Compute half power once
        double half = pow(x, n / 2);

        // If n is even
        if (n % 2 == 0)
            return half * half;

        // If n is odd  <-- to get all the n values when n/2. e.g. n=9
        else
            return x * half * half;
    }

    public static void main(String[] args) {
        System.out.println(pow(2,10));  // 1024.0
        System.out.println(pow(5,-2));  // 0.04
    }
}