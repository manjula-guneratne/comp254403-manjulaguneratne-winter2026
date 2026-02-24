

public class ex4 {
    public static int binarySearch(int[] a, int key, int lo, int hi) {

       if(hi >= lo){
           int m = (lo+(hi-1))/2;

           // Base case
           if(a[m] == key)
               return m;

           //Left side
           if(a[m] > key)
               return binarySearch(a,key, lo, m-1);

           //right side
           if(a[m] < key)
               return binarySearch(a,key, m+1, hi);
       }

        return -1;
    }

    public static void main(String[] args) {
        int[] a = {1,3,5,7,9};
        System.out.println(binarySearch(a,5,0,a.length-1)); // EXPECTED: 2
        System.out.println(binarySearch(a,2,0,a.length-1)); // EXPECTED: -1
    }
}