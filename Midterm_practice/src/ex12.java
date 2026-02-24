
public class ex12 {
    public static boolean isPalin(String s, int lo, int hi) {
        //Base case
        if(lo>=hi)
            return true;

        //if characters do not meet or cross
        if(s.charAt(lo) != s.charAt(hi))
            return false;

        return isPalin(s,lo+1, hi-1);
    }

    public static void main(String[] args) {
        System.out.println(isPalin("racecar", 0, 6)); // true
        System.out.println(isPalin("hello", 0, 4));   // false
    }
}