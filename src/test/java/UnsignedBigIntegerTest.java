import org.junit.*;
import org.junit.runners.JUnit4;
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
        UnsignedBigInteger a = new UnsignedBigInteger("12");
        UnsignedBigInteger b = new UnsignedBigInteger("12");
        assertEquals(new UnsignedBigInteger("24"), a.plus(b));

    }

    @Test
    public void increment(){
        UnsignedBigInteger a = new UnsignedBigInteger("99");
        assertEquals(new UnsignedBigInteger("100"), a.increment());

        UnsignedBigInteger b = new UnsignedBigInteger("9999");
        assertEquals(new UnsignedBigInteger("10000"), b.increment());
    }


}
