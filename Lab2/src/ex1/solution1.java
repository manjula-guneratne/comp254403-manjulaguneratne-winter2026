package ex1;

public class Solution1 {

    public static void sol_A(){

        int n = 1000;    //T(n) = 1
        System.out.println("Hey - your input is: " + n);   //T(n) = 2
        System.out.println("Hmm.. I'm doing more stuff with: " + n);    //T(n) = 2
        System.out.println("And more: " + n);    //T(n) = 2

        //        T(n) = 7
        //        O(1)     Constant-time algorithm
    }

    public static void sol_B(){

        int n = 5;
        for (int i = 1; i < n; i = i * 2){   //T(n) = n
            System.out.println("Hey - I'm busy looking at: " + i);   //T(n) = 2
        }

        //(n) = n + 2
        //O(n)     Linear-time algorithm

    }

    public static void sol_C(){

        int n = 5;
        for (int i = 1; i <= n; i++){            //T(n) = n
            for(int j = 1; j < n; j = j * 2) {       //log(n) = n
                System.out.println("Hey - I'm busy looking at: " + i + " and " + j); //T(n) = 3
            }
        }

        //T(n)=nlog(n) + 3
        //O(nlog(n))     logrithmic-time algorithm
    }

    public static double[] prefixAverage1(double[] x) {
        int n = x.length;                //T(n) = 1
        double[] a = new double[n];      //T(n) = 1
        for (int j=0; j < n; j++) {      //T(n) = n
            double total = 0;            //T(n) = 1
            for (int i=0; i <= j; i++)   //T(n) = n
            total += x[i];               //T(n) = 1
            a[j] = total / (j+1);        //T(n) = 1
        }
        return a;                        //T(n) = 1
    }

    //T(n)=n2 + 6
    //O(n2)     Quadratic-time algorithm

    public static double[] prefixAverage2(double[] x) {
        int n = x.length;              //T(n) = 1
        double[] a = new double[n];    //T(n) = 1
        double total = 0;              //T(n) = 1
        for (int j=0; j < n; j++) {    //T(n) = n
            total += x[j];             //T(n) = 1
            a[j] = total / (j+1);      //T(n) = 1
        }
        return a;                      //T(n) = 1
    }

    //T(n)=n + 6
    //O(n)     Linear-time algorithm


    public  static  void main(String[] args){

    }
}
