package Week8.Practice;

//Node class
Class Node{
    int key;
    Node left, right;

    public void Node(int item){
        key = item;
        left = right = null;
    }
}

public class Binary_class_basic {

    Node root;

    public Binary_class_basic(){
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

    public static void main(String[] args) {



    }
}
