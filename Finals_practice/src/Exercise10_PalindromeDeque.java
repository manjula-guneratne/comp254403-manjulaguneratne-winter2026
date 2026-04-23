import java.util.ArrayDeque;
import java.util.Deque;

public class Exercise10_PalindromeDeque {

    /**
     * Returns true if s is a palindrome, ignoring case and non-letter characters.
     *
     * Algorithm:
     *   1. Scan s; for each Character.isLetter(c), push toLowerCase(c) onto a Deque.
     *   2. While deque has more than 1 element:
     *        compare removeFirst() vs removeLast().
     *        If they differ → not a palindrome.
     *   3. If we exit the loop without a mismatch → palindrome.
     *
     * Time:  O(n)   n = length of s.
     * Space: O(k)   k = number of letters in s (deque storage).
     */
    public static boolean isPalindrome(String s) {
        Deque<Character> dq = new ArrayDeque<>();

        // Step 1: filter and normalise
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c))
                dq.addLast(Character.toLowerCase(c));
        }

        // Step 2: compare both ends, moving inward
        while (dq.size() > 1) {
            char front = dq.removeFirst();
            char back  = dq.removeLast();
            if (front != back) return false;   // mismatch → not a palindrome
        }

        return true;   // all matched (or 0/1 letter → trivially true)
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  main
    // ═════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {

        System.out.println("════ Exercise 10 — Palindrome Check with Deque ════\n");

        // ── Test 1 & 2: from exercise sheet ───────────────────────────────────
        String s1 = "A man, a plan, a canal: Panama";
        String s2 = "Data Structures";
        System.out.println("\"" + s1 + "\"");
        System.out.println("  isPalindrome = " + isPalindrome(s1));  // true

        System.out.println("\n\"" + s2 + "\"");
        System.out.println("  isPalindrome = " + isPalindrome(s2));  // false

        // PASS checks
        System.out.println("\n--- PASS checks ---");
        System.out.println("Panama   PASS: " + (isPalindrome(s1) == true));
        System.out.println("DataStr  PASS: " + (isPalindrome(s2) == false));

        // ── More test cases ───────────────────────────────────────────────────
        String[][] tests = {
            {"racecar",                  "true"},
            {"Racecar",                  "true"},   // case-insensitive
            {"Was it a car or a cat I saw?", "true"},
            {"hello",                    "false"},
            {"a",                        "true"},   // single letter
            {"",                         "true"},   // empty string
            {"Aa",                       "true"},   // two letters, same
            {"Ab",                       "false"},  // two letters, different
            {"Never odd or even",        "true"},
            {"Not a palindrome",         "false"},
        };

        System.out.println("\n--- Additional tests ---");
        boolean allPass = true;
        for (String[] tc : tests) {
            boolean expected = tc[1].equals("true");
            boolean got      = isPalindrome(tc[0]);
            String  status   = (got == expected) ? "PASS" : "FAIL";
            if (got != expected) allPass = false;
            System.out.printf("  %-35s -> %-5s  [%s]%n", "\"" + tc[0] + "\"", got, status);
        }
        System.out.println("\nAll tests passed: " + allPass);

        // ── Step-by-step trace ────────────────────────────────────────────────
        System.out.println("\n--- Trace: \"racecar\" ---");
        System.out.println("After filtering: deque = [r, a, c, e, c, a, r]");
        System.out.println("  front=r, back=r  -> match, deque=[a, c, e, c, a]");
        System.out.println("  front=a, back=a  -> match, deque=[c, e, c]");
        System.out.println("  front=c, back=c  -> match, deque=[e]");
        System.out.println("  size==1, stop. Return TRUE.");

        System.out.println("\n--- Trace: \"hello\" ---");
        System.out.println("After filtering: deque = [h, e, l, l, o]");
        System.out.println("  front=h, back=o  -> MISMATCH. Return FALSE immediately.");

        // ── Key concept ───────────────────────────────────────────────────────
        System.out.println("\n--- Key concept: why a Deque? ---");
        System.out.println("A Deque supports O(1) removal from BOTH ends.");
        System.out.println("removeFirst() = front char, removeLast() = back char.");
        System.out.println("A Stack + Queue combo would also work but needs two structures.");
        System.out.println("ArrayDeque is Java's built-in Deque: no boxing overhead.");
    }
}
