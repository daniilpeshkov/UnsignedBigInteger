import org.junit.*;
import static org.junit.Assert.*;

import unsignedBigInteger.UnsignedBigInteger;

public class UnsignedBigIntegerTest {

    @Test
    public void constructor() {

        UnsignedBigInteger a = UnsignedBigInteger.valueOf("128");

        System.out.println(a);

    }

    @Test
    public void plus() {
        for (int i = 1; i <= 9000 ; i++) {
            for (int j = 1; j <= 9000 ; j++) {
                assertEquals(UnsignedBigInteger.valueOf(i + j),
                        UnsignedBigInteger.valueOf(i).plus(UnsignedBigInteger.valueOf(j)));
            }
        }
    }

    @Test
    public void multiply() {
        for (int i = 1; i <= 9000; i++) {
            for (int j = 1; j <= 9000 ; j++) {
                assertEquals(UnsignedBigInteger.valueOf(i * j),
                        UnsignedBigInteger.valueOf(i).multiply(UnsignedBigInteger.valueOf(j)));
            }
        }
    }

    @Test
    public void minus() {
        for (int i = 1; i <= 9000; i++) {
            for (int j = 0; j <= i ; j++) {
                assertEquals(UnsignedBigInteger.valueOf(i - j),
                        UnsignedBigInteger.valueOf(i).minus(UnsignedBigInteger.valueOf(j)));
            }
        }
    }

    @Test
    public void divide() {
        for (int i = 1; i <= 9000; i++) {
            for (int j = 1; j <= 9000 ; j++) {
                assertEquals(UnsignedBigInteger.valueOf(i / j),
                        UnsignedBigInteger.valueOf(i).divide(UnsignedBigInteger.valueOf(j)));
            }
        }
    }

    @Test
    public void mod() {
        for (int i = 1; i <= 9000; i++) {
            for (int j = 1; j <= 9000 ; j++) {
                assertEquals(UnsignedBigInteger.valueOf(i % j),
                        UnsignedBigInteger.valueOf(i).mod(UnsignedBigInteger.valueOf(j)));
            }
        }
    }
}
