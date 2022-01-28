package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
   @Test
    public void SquarePrimesTest(){
       IntList lst = IntList.of(5,6,11,13,14,17,18);
       //System.out.println(lst.toString());
       boolean modified = IntListExercises.squarePrimes(lst);
       //System.out
       assertEquals("25 -> 6 -> 121 -> 169 -> 14 -> 289 -> 18",lst.toString());
   }
}
