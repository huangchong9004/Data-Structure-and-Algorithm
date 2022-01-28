package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    public static class numComparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return a - b;
        }
    }
    public static class lengthComparator implements Comparator<String> {
        public int compare(String a, String b) {
            return a.length() - b.length();
        }
    }
    @Test
    public void bigLLDequeTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        numComparator nc = new numComparator();
        lengthComparator lc = new lengthComparator();
        //MaxArrayDeque<Integer> lld1 = new MaxArrayDeque<Integer>(nc);
        MaxArrayDeque<String> lld1 = new MaxArrayDeque<String>(lc);
        for (int i = 0; i < 100; i++) {
            //System.out.println(i);
            lld1.addLast(Integer.toString(i+1));
        }

        assertEquals("expected value:", "100", (String) lld1.max(lc));
    }
}
