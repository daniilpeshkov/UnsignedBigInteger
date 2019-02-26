import org.junit.*;
import static org.junit.Assert.*;

import unsignedBigInteger.UnsignedBigIntegerOld;
import unsignedBigInteger.UnsignedBigInteger;

public class UnsignedBigIntegerTest {

    @Test
    public void constructor() {
        try {
            new UnsignedBigInteger(null);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
        UnsignedBigInteger a = new UnsignedBigInteger("2334");
        System.out.println(a);
    }

    @Test
    public void plusOld() {
        for (int i = 1; i < 9000; i++) {
            for (int j = 1; j < i ; j++) {
                assertEquals(new UnsignedBigIntegerOld(i + j), new UnsignedBigIntegerOld(i)
                        .plus(new UnsignedBigIntegerOld(j)));
            }
        }
    }
    @Test
    public void plus() {
        for (int i = 1; i < 9000; i++) {
            for (int j = 1; j < i ; j++) {
                assertEquals(UnsignedBigInteger.unsignedBigIntegerFrom(i + j), UnsignedBigInteger
                        .unsignedBigIntegerFrom(i).plus(UnsignedBigInteger
                                .unsignedBigIntegerFrom(j)));
            }
        }
    }

    @Test
    public void minMaxOld() {
        UnsignedBigIntegerOld a = new UnsignedBigIntegerOld("12");
        UnsignedBigIntegerOld b = new UnsignedBigIntegerOld("120");
        assertEquals(a, UnsignedBigIntegerOld.min(a, b));
        assertEquals(b, UnsignedBigIntegerOld.max(a, b));
    }

    @Test
    public void incrementOld() {
        UnsignedBigIntegerOld a = new UnsignedBigIntegerOld("99");
        assertEquals(new UnsignedBigIntegerOld("100"), a.increment());

        UnsignedBigIntegerOld b = new UnsignedBigIntegerOld("9999");
        assertEquals(new UnsignedBigIntegerOld("10000"), b.increment());
    }

    @Test
    public void compareTo() {
        assertEquals(0, UnsignedBigInteger.unsignedBigIntegerFrom(12)
                .compareTo(UnsignedBigInteger.unsignedBigIntegerFrom(12)));
    }

    @Test
    public void multiplyOld() {
        for (int i = 1; i < 900; i++) {
            for (int j = 1; j <= i ; j++) {
                assertEquals(new UnsignedBigIntegerOld(i * j), new UnsignedBigIntegerOld(i).multiply(j));
            }
        }
    }

    @Test
    public void minusOld() {
        for (int i = 1; i < 9000; i++) {
            for (int j = 1; j < i; j++) {
                assertEquals(new UnsignedBigIntegerOld(i - j),
                        new UnsignedBigIntegerOld(i).minus(new UnsignedBigIntegerOld(j)));
            }
        }
    }

    @Test
    public void minus() {
        for (int i = 1; i < 9000; i++) {
            for (int j = 1; j < i ; j++) {
                assertEquals(UnsignedBigInteger.unsignedBigIntegerFrom(i - j),
                        UnsignedBigInteger.unsignedBigIntegerFrom(i).minus(UnsignedBigInteger.unsignedBigIntegerFrom(j)));
            }
         }
    }

    @Test
    public void divideOld() {
        boolean a = false;
        try {
            new UnsignedBigIntegerOld(2).divide(0);
        } catch (ArithmeticException ex) {
            a = true;
        }
        assertTrue(a);
        for (int i = 1; i < 9000; i++) {
            for (int j = 1; j < i ; j++) {
                assertEquals(new UnsignedBigIntegerOld(i / j), new UnsignedBigIntegerOld(i).divide(j));
            }
        }
    }

    @Test
    public void modOld() {
        for (int i = 1; i < 9000; i++) {
            for (int j = 1; j < i ; j++) {
                assertEquals(new UnsignedBigIntegerOld(i % j), new UnsignedBigIntegerOld(i).mod(j));
            }
        }
    }
}
