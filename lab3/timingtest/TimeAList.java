package timingtest;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.*;
/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        int start = 1000;
        for (int i = 0; i <= 8; i ++){
            Ns.addLast(start);
            Stopwatch sw = new Stopwatch();
            timeAListConstruction(start);
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
            start *= 2;
        }
        printTimingTable(Ns, times, Ns);
    }

    public static void timeAListConstruction(int N) {
        // TODO: YOUR CODE HERE
        AList<Integer> test = new AList<>();
        for (int j = 0; j < N; j ++){
            test.addLast(j);
        }
    }
}
