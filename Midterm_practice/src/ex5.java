
//IterativeBinarySearch
public class ex5{
    public static int search(String[] a, String key) {

        if((a.length)==0) return -1; //empty array

        int lo = 0;
        int hi = a.length - 1;

        while(lo <= hi) {
            int m = (lo+hi) / 2;

            int cmp = key.compareTo(a[m]);

            System.out.println("cmp output: "+ cmp);  //testing

            // The locaiton of the key is found
            if(cmp == 0)
                return m;

            //smaller
            if(cmp < 0)
                hi = m-1;


            //larger
            if(cmp > 0)
                lo = m+1;
        }
        return -1;
    }

    public static void main(String[] args) {
        String[] a = {"apple","banana","cherry","date"};
        System.out.println(search(a,"cherry")); // 2
        System.out.println(search(a,"fig"));    // -1
    }
}
