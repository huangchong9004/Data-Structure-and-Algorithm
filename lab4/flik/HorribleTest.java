package flik;

import org.junit.Test;
import static org.junit.Assert.*;


public class HorribleTest {
    @Test
    public void testHorrible() {
        int i = 0;
        for (int j = 0; i < 500; ++i, ++j) {
            System.out.println(i);
            System.out.println(j);
            assertTrue(Flik.isSameNumber(i, j));
        }
    }
}
