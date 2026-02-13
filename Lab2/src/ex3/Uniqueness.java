package ex3;

import java.util.Arrays;
import java.util.Random;

class Uniqueness {

    // To collect both functions
    @FunctionalInterface
    interface UniquenessAlgorithm {
        boolean apply(int[] data);
    }

    /**
     * Returns true if there are no duplicate elements in the array.
     */
    public static boolean unique1(int[] data) {
        int n = data.length;
        for (int j = 0; j < n - 1; j++)
            for (int k = j + 1; k < n; k++)
                if (data[j] == data[k])
                    return false;                    // found duplicate pair
        return true;                           // if we reach this, elements are unique
    }

    /**
     * Returns true if there are no duplicate elements in the array.
     */
    public static boolean unique2(int[] data) {
        int n = data.length;
        int[] temp = Arrays.copyOf(data, n);   // make copy of data
        Arrays.sort(temp);                     // and sort the copy
        for (int j = 0; j < n - 1; j++)
            if (temp[j] == temp[j + 1])            // check neighboring entries
                return false;                      // found duplicate pair
        return true;                           // if we reach this, elements are unique
    }

    public static void binerySearch(String name, UniquenessAlgorithm algo) {

        final double LIMIT = 60.0;
        int low = 0;
        int high = 0;

        int n = 1000;
        double runtime = 0;
        Random rand = new Random();

        while (true) {

            int[] array = new int[n];

            for (int i = 0; i < n; i++) {
                array[i] = i + 1;
            }

            // shuffle array (Fisher–Yates)
            for (int i = array.length - 1; i > 0; i--) {
                int j = rand.nextInt(i + 1);
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }

            // warm up JVM
            algo.apply(array);

            double totaltime = 0;

            for (int i = 0; i < 5; i++) {

                long startTime = System.nanoTime();
                sink = algo.apply(array);   // <-- uses passed function
                long endTime = System.nanoTime();

                double tempRunTime = (endTime - startTime) / 1_000_000_000.0;
                totaltime += tempRunTime;
            }

            runtime = totaltime / 5;
            System.out.println(name + " | n = " + n + " time = " + runtime + "s");

            if (runtime <= LIMIT){
                low = n;
                n = n*2;
            }
            else {
                high = n;
                break;
            }
        }

        System.out.println("Initial bounds found: low = " + low + ", high = " + high);
    }

    public static void doublingPhase(String name, UniquenessAlgorithm algo) {

        int n = 1000;
        double runtime = 0;
        Random rand = new Random();

        while (true) {

            int[] array = new int[n];

            for (int i = 0; i < n; i++) {
                array[i] = i + 1;
            }

            // shuffle array (Fisher–Yates)
            for (int i = array.length - 1; i > 0; i--) {
                int j = rand.nextInt(i + 1);
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }

            // warm up JVM
            algo.apply(array);

            double totaltime = 0;

            for (int i = 0; i < 5; i++) {

                long startTime = System.nanoTime();
                sink = algo.apply(array);   // <-- uses passed function
                long endTime = System.nanoTime();

                double tempRunTime = (endTime - startTime) / 1_000_000_000.0;
                totaltime += tempRunTime;
            }

            runtime = totaltime / 5;
            System.out.println(name + " | n = " + n + " time = " + runtime + "s");

            if (runtime > 60) break;
            n = n * 2;
        }
    }

    public static boolean sink;

    public static void main(String[] args) {

        doublingPhase("unique1 O(n^2)", Uniqueness::unique1);
        System.out.println();
        doublingPhase("unique2 O(n log n)", Uniqueness::unique2);

        System.out.println();

        binerySearch("unique1 O(n^2)", Uniqueness::unique1);
        System.out.println();
        binerySearch("unique2 O(n log n)", Uniqueness::unique2);
    }
}


