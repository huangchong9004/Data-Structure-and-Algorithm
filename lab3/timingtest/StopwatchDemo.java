package timingtest;

import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */

public class StopwatchDemo {
    /** Computes the nth Fibonacci number using a slow naive recursive strategy.*/
    private static int fib(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }
    public static boolean findSum(int[] A, int x) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A.length; j++) {
                if (A[i] + A[j] == x) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean findSumFaster(int[] A, int x) {
        int start = 0;
        int end = A.length - 1;
        while (end > start) {
            if (A[start] + A[end] == x) {
                return true;
            } else if (A[start] + A[end] > x) {
                end -= 1;
            } else {
                start += 1;
            }
        }
        return false;
    }
    public static void main(String[] args) {
        int N =10000000;
        int[] A = new int[N];
        for (int i = 0; i < N; i++) {
            A[i] = i;
        }
        int x = N - 500000;
        Stopwatch sw = new Stopwatch();
        findSum(A, x);
        double timeInSeconds = sw.elapsedTime();
        Stopwatch sw2 = new Stopwatch();
        findSumFaster(A, x);
        double timeInSeconds2 = sw.elapsedTime();
        System.out.println("The findSum needs " + timeInSeconds);
        System.out.println("The findSumFaster needs " + timeInSeconds2);
    }
}
