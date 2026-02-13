package ex3;

import java.util.Arrays;
import java.util.Random;

class Uniqueness {

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

    public static boolean sink;

    public static void main(String[] args) {

        int n = 1000;
        double runtime = 0;
        Random rand = new Random();

        /*
        while (true) {

            int[] array = new int[n];

            for (int i = 0; i < n; i++) {
                array[i] = i + 1;
            }

            //shuffle an array
            for (int i = array.length - 1; i > 0; i--) {
                int j = rand.nextInt(i + 1);
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }

            //warm up
            unique1(array);

            double totaltime = 0;
            double tempRunTime;
            for (int i = 0; i < 5; i++) {
                //Start timer
                long startTime = System.nanoTime();
                //Algorithm testing
                sink = unique1(array);
                //Stop timer
                long endTime = System.nanoTime();

                tempRunTime = (endTime - startTime) / 1_000_000_000.0;
                totaltime = totaltime + tempRunTime;
            }

            runtime = totaltime / 5;

            System.out.println("n = " + n + " time = " + runtime + "s");

            if (runtime > 60) {
                break;
            } else {
                // double input size
                n = n * 2;
            }
        }
         */

        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = i + 1;
        }


    }
}


