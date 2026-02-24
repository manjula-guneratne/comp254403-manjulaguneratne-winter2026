import java.util.*;

public class ex15 {

    public static int eval(String expr) {

        Stack<Integer> stack = new Stack<>();

        // Split expression into tokens
        String[] tokens = expr.split(" ");

        // Traverse from RIGHT to LEFT
        for (int i = tokens.length - 1; i >= 0; i--) {

            String token = tokens[i];

            // If token is a number → push
            if (Character.isDigit(token.charAt(0))) {
                stack.push(Integer.parseInt(token));
            }
            // If token is an operator → pop, compute, push result
            else {
                int a = stack.pop();
                int b = stack.pop();

                int result = 0;

                switch (token) {
                    case "+":
                        result = a + b;
                        break;
                    case "-":
                        result = a - b;
                        break;
                    case "*":
                        result = a * b;
                        break;
                    case "/":
                        result = a / b;
                        break;
                }

                stack.push(result);
            }
        }

        return stack.pop();
    }

    public static void main(String[] args) {
        String expr = "- + 7 * 4 5 + 2 0";
        System.out.println(eval(expr)); // 25
    }
}