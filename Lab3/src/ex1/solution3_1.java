package ex1;

public class solution3_1 {

    public static int multiply(int m, int n) {
        if (n == 0)    //Base stop case
            return 0;
        return m + multiply(m, n - 1);
    }

    public static void main(String[] args) {

        int result = multiply(3, 2);

        System.out.println("Result: " + result);
    }

}
