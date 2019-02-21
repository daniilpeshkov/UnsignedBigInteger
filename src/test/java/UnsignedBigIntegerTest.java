import org.junit.*;
import static org.junit.Assert.*;

import unsignedBigInteger.UnsignedBigInteger;

public class UnsignedBigIntegerTest {


    @Test
    public void constructor() {
        try {
            new UnsignedBigInteger(null);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void plus() {
        for (int i = 1; i < 9000; i++) {
            for (int j = 1; j < i ; j++) {
                assertEquals(new UnsignedBigInteger(i + j), new UnsignedBigInteger(i).plus(j));
            }
        }
    }

    @Test
    public void minMax() {
        UnsignedBigInteger a = new UnsignedBigInteger("12");
        UnsignedBigInteger b = new UnsignedBigInteger("120");
        assertEquals(a, UnsignedBigInteger.min(a, b));
        assertEquals(b, UnsignedBigInteger.max(a, b));
    }

    @Test
    public void increment() {
        UnsignedBigInteger a = new UnsignedBigInteger("99");
        assertEquals(new UnsignedBigInteger("100"), a.increment());

        UnsignedBigInteger b = new UnsignedBigInteger("9999");
        assertEquals(new UnsignedBigInteger("10000"), b.increment());
    }

    @Test
    public void compareTo() {
        assertEquals(1,new UnsignedBigInteger(20).compareTo(new UnsignedBigInteger(19)));
    }

    @Test
    public void multiply() {
        for (int i = 1; i < 900; i++) {
            for (int j = 1; j <= i ; j++) {
                assertEquals(new UnsignedBigInteger(i * j), new UnsignedBigInteger(i).multiply(j));
            }
        }
    }

    @Test
    public void minus() {
        assertEquals(new UnsignedBigInteger(11), new UnsignedBigInteger(11).divide(1));
        for (int i = 1; i < 9000; i++) {
            for (int j = 1; j < i ; j++) {
                assertEquals(new UnsignedBigInteger(i - j), new UnsignedBigInteger(i).minus(j));
            }
         }
    }

    @Test
    public void divide() {
        boolean a = false;
        try {
            new UnsignedBigInteger(2).divide(0);
        } catch (ArithmeticException ex) {
            a = true;
        }
        assertTrue(a);
        for (int i = 1; i < 9000; i++) {
            for (int j = 1; j < i ; j++) {
                assertEquals(new UnsignedBigInteger(i / j), new UnsignedBigInteger(i).divide(j));
            }
        }
    }

    @Test
    public void mod() {
        for (int i = 1; i < 9000; i++) {
            for (int j = 1; j < i ; j++) {
                assertEquals(new UnsignedBigInteger(i % j), new UnsignedBigInteger(i).mod(j));
            }
        }
    }
}
