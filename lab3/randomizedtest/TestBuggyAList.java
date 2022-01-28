package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> lst1= new AListNoResizing<>();
        BuggyAList<Integer> lst2 = new BuggyAList<>();
        for (int i = 4; i <= 6; i ++){
            lst1.addLast(i);
            lst2.addLast(i);
        }
        int[] expected = new int[3];
        int[] test = new int[3];
        for (int j = 0; j <= 2; j++){
            expected[j] = lst1.removeLast();
            test[j] = lst2.removeLast();
        }
        assertArrayEquals(expected, test);
    }
    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> M = new BuggyAList<>();
        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                M.addLast(randVal);
                //System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int sizeM = M.size();
                assertEquals(size, sizeM);
               //System.out.println("size: " + size);
            } else if (operationNumber == 2) {
                // size
                if (L.size() > 0){
                    int last = L.getLast();
                    int lastM = M.getLast();
                    assertEquals(last,lastM);
                    //System.out.println("Last is : " + last);
                }
            } else if (operationNumber == 3) {
                // size
                if (L.size() > 0){
                    int last = L.removeLast();
                    int lastM = M.removeLast();
                    assertEquals(last,lastM);
                    //System.out.println("Last: " + last + " is now removed");
                }
            }
        }
    }
}
