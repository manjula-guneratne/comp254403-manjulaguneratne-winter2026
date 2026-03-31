package ex1;

//Node class
class Node{
    int key;
    Node left, right;

    Node(int item){
        key = item;
        left = right = null;
    }
}

public class Solution5_1 {

    Node root;

    public Solution5_1(){
        root = null;
    }

    //Insert a new node with a given key
    public void insert(int key){
        root = insertRec(root,key);
    }

    //Recrusive function to insert a new key into the Tree
    private Node insertRec(Node root, int key){
        if(root == null){
            root = new Node(key);
            return root;
        }

        //Otherwise
        if(key < root.key)
            root.left = insertRec(root.left, key);
        else if(key > root.key)
            root.right = insertRec(root.right, key);

        return root;
    }

    //print the tree inorder
    public void inorder(){
        inorderRec(root);
    }

    //Function to do inorder traversal of the tree
    private void inorderRec(Node root){
        if(root != null){
            inorderRec(root.left);
            System.out.println(root.key + " ");
            inorderRec(root.right);
        }
    }

    //Function to search for a key in tree
    public boolean search(int key){
        return searchRec(root, key);
    }

    //Function to search for a key in tree
    private boolean searchRec(Node root, int key){
        if(root == null)
            return false;

        if(root.key == key)
            return true;

        if(key < root.key)
            return searchRec(root.left, key);
        else
            return searchRec(root.right, key);
    }

    //Find Minimum value in the tree
    public int findMin(){
        return findMinRec(root);
    }

    //Function to find the minimum value in a tree
    private int findMinRec(Node root){
        if(root == null)
            throw new IllegalStateException("Tree is empty");

        if(root.left == null)
            return root.key;

        return findMinRec(root.left);
    }

    //Find Maximum value in the tree
    public int findMax() {
        return findMaxRec(root);
    }

    //Function to find the Maximum value in tree
    private int findMaxRec(Node root){
        if(root == null)
            throw new IllegalStateException("Tree is Empty");

        if(root.right == null)
            return root.key;

        return findMaxRec(root.right);
    }

    public static void main(String[] args) {

        Solution5_1 tree = new Solution5_1();

        // Insert some nodes
        tree.insert(50);
        tree.insert(30);
        tree.insert(20);
        tree.insert(40);
        tree.insert(70);
        tree.insert(60);
        tree.insert(80);

        // Print inorder traversal of the tree
        System.out.println("Inorder traversal:");
        tree.inorder();
        // Output: 20 30 40 50 60 70 80

        // Search for a key
        int searchKey = 40;
        if (tree.search(searchKey))
            System.out.println("\nKey " + searchKey + " found in the tree.");
        else
            System.out.println("\nKey " + searchKey + " not found in the tree.");

        // Find minimum and maximum values
        System.out.println("Minimum value in the tree: " + tree.findMin());
        System.out.println("Maximum value in the tree: " + tree.findMax());

    }
}
