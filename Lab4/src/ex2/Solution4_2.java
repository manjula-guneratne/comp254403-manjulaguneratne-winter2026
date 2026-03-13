package ex2;


class Node {
    int data;
    Node next;

    Node(int data){
        this.data = data;
        this.next = null;
    }
}

public class Solution4_2 {
    private Node top;

    //constructor
    public Solution4_2(){
        top = null;
    }

    public void push(int value){
        Node newNode = new Node(value);
        newNode.next = top;
        top = newNode;
    }

    public int pop(){
        if(isEmpty()){
            throw new RuntimeException("Empty stack");
        }
        int value = top.data;
        top = top.next;
        return value;
    }

    //show the entire stack
    public void printStack(){
        Node current = top;
        System.out.print("Stack: ");
        while(current != null){
            System.out.print(current.data+" ");
            current = current.next;
        }
        System.out.println();
    }

    public boolean isEmpty(){
        return top == null;
    }

    public static void transfer(Solution4_2 S, Solution4_2 T) {
        while (!S.isEmpty()) {
            T.push(S.pop());
        }
    }

    public static void main(String[] args) {

        Solution4_2 stack_S = new Solution4_2();
        Solution4_2 stack_T = new Solution4_2();

        stack_S.push(10);
        stack_S.push(20);
        stack_S.push(30);

        System.out.println("Stack S values");
        stack_S.printStack();

        transfer(stack_S, stack_T);

        System.out.println("Stack T values after Transfer");
        stack_T.printStack();
    }
}
