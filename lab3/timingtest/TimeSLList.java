package timingtest;
import edu.princeton.cs.algs4.Stopwatch;
import org.checkerframework.checker.units.qual.A;

/**
 * Created by hug.
 */
public class TimeSLList {
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

        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        int k = 10000;
        int start = 1000;
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        for (int i = 0; i < 8; i ++){
            SLList<Integer> slst = new SLList<>();
            for (int j = 0; j <= start; j ++){
                slst.addFirst(j);
            }
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j <= k; j ++){
                slst.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            Ns.addLast(start);
            times.addLast(timeInSeconds);
            opCounts.addLast(k);
            start *= 2;
        }
        printTimingTable(Ns,times,opCounts);
    }

}
