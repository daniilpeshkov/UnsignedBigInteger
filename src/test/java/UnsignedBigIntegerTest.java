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
        UnsignedBigInteger a = new UnsignedBigInteger("99");
        UnsignedBigInteger b = new UnsignedBigInteger("99");
        assertEquals(new UnsignedBigInteger("198"), a.plus(b));
        assertEquals(new UnsignedBigInteger("111"), a.plus(12));

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
    public void multiply() {
        UnsignedBigInteger a = new UnsignedBigInteger("2");
        UnsignedBigInteger b = new UnsignedBigInteger("2");
        assertEquals(new UnsignedBigInteger("4"), a.multiply(b));
        a = new UnsignedBigInteger("99");
        b = new UnsignedBigInteger("99");
        assertEquals(new UnsignedBigInteger("9801"), a.multiply(b));


    }


}
