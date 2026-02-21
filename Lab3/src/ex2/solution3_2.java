package ex2;
import java.util.Scanner;

public class solution3_2 {

    public static boolean isPalindrome(String s) {

        if (s.length() <= 1)  //Base case
            return true;

        if (s.charAt(0) != s.charAt(s.length() - 1))  //Mismatch
            return false;

        //Recursion case
        return isPalindrome(s.substring(1, s.length() - 1));
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Enter a string: ");
        String word = input.nextLine();

        if (isPalindrome(word))
            System.out.println("It is a palindrome.");
        else
            System.out.println("It is NOT a palindrome.");

        input.close();
    }
}
