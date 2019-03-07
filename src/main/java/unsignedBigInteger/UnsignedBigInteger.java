package unsignedBigInteger;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class UnsignedBigInteger implements java.lang.Comparable<UnsignedBigInteger> {

    private byte[] mag;

    private int bitsCount;

    public static UnsignedBigInteger ONE = UnsignedBigInteger.valueOf(1);

    public static UnsignedBigInteger ZERO = UnsignedBigInteger.valueOf(0);

    public static UnsignedBigInteger max(UnsignedBigInteger a, UnsignedBigInteger b) {
        if (a.compareTo(b) >= 0) return a;
        else return b;
    }

    public static UnsignedBigInteger min(UnsignedBigInteger a, UnsignedBigInteger b) {
        if (a.compareTo(b) <= 0) return a;
        else return b;
    } 

    private static int compareMags(byte[] mag1, int offset1, int length1,
                                   byte[] mag2, int offset2, int length2) {
        int maxLength = Math.max(length1, length2);
        int answer = 0;
        for (int i = 0; i < maxLength; i++) {
            byte a = (i < length1? mag1[offset1 + i]: 0);
            byte b = (offset2 + i < length2? mag2[offset2 + i]: 0);
            if (a != b) {
                if (a > b) answer = 1;
                else answer = -1;
            }
        }
        return answer;
    }

    private static void subtractMags(byte[] mag1, int offset1, int length1,
                                       byte[] mag2, int offset2, int length2) {
        int loan = 0;
        for (int i = 0; i < length1 || loan != 0; i++) {
            mag1[i + offset1] = (byte) (mag1[i + offset1]
                    - (i < length2 ? mag2[i + offset2] : 0) - loan);
            if (mag1[i + offset1] >= 0) loan = 0;
            else {
                loan = 1;
                mag1[i + offset1] += 10;
            }
        }
    }

    private static int countMeaningBits(byte[] mag, int start) {
        while (start > 1 && mag[start - 1] == 0)
            start--;
        return start;
    }

    public UnsignedBigInteger(String number) {
        if (number == null || number.isEmpty()) throw new IllegalArgumentException("number is empty or null");
        else {
            bitsCount = number.length();
            this.mag = new byte[bitsCount];
            for (int i = bitsCount - 1; i >= 0; i--) {
                int tmp = number.charAt(i) - '0';
                if (tmp > 9 || tmp < 0) throw new NumberFormatException("is not number");
                mag[mag.length - i - 1] = (byte) tmp;
            }
        }
    }

    private UnsignedBigInteger(byte[] mag, int bitsCount) {
        this.mag = mag;
        this.bitsCount = bitsCount;
    }

    public static UnsignedBigInteger valueOf(int val) {
        if (val < 0) throw new IllegalArgumentException("number is negative");
        Queue<Integer> bits = new ArrayDeque<>();
        while(val > 0) {
            bits.add(val % 10);
            val /= 10;
        }
        byte[] mag = new byte[bits.size()];
        int bitsCount = 0;
        if (bits.isEmpty()) {
            return new UnsignedBigInteger(new byte[]{0}, 1);
        } else {
            while (!bits.isEmpty()) {
                mag[bitsCount] = bits.poll().byteValue();

                bitsCount++;
            }
            return new UnsignedBigInteger(mag, bitsCount);
        }
    }

    @Override
    public int compareTo(@NotNull UnsignedBigInteger other) {
        return compareMags(mag,0, bitsCount, other.mag,0, other.bitsCount);
    }
    
    public boolean greater(UnsignedBigInteger other) {
        return compareTo(other) > 0;
    }

    public boolean greaterInclusive(UnsignedBigInteger other) {
        return compareTo(other) >= 0;
    }

    public boolean less(UnsignedBigInteger i) {
        return compareTo(i) < 0;
    }

    public boolean lessInclusive(UnsignedBigInteger other) {
        return compareTo(other) <= 0;
    }

    public UnsignedBigInteger plus(UnsignedBigInteger other) {
        int magLength = Math.max(bitsCount, other.bitsCount);
        byte[] newMag = new byte[magLength + 1];
        int addition = 0;
        int newBitsCount;
        for( newBitsCount = 0;newBitsCount < magLength; newBitsCount++) {
            newMag[newBitsCount] = (byte) ((newBitsCount < mag.length?mag[newBitsCount]:0)
                    + (newBitsCount < other.mag.length?other.mag[newBitsCount]:0) + addition);
            addition = newMag[newBitsCount] / 10;
            newMag[newBitsCount] -= addition * 10;
        }
        if (addition != 0) {
            newBitsCount++;
            newMag[newBitsCount - 1] = 1;
        }
        return new UnsignedBigInteger(newMag, newBitsCount);
    }
    

    public UnsignedBigInteger minus(UnsignedBigInteger other) {
        if (this.less(other))
            throw new IllegalArgumentException(toString() + " is less than " + other.toString());
        else {
            byte[] newMag = new byte[bitsCount];
            int loan = 0;
            for (int i = 0; i < bitsCount || loan != 0; i++) {
                newMag[i] = (byte) (mag[i] - (i < other.bitsCount ? other.mag[i] : 0) - loan);
                if (newMag[i] >= 0) loan = 0;
                else {
                    loan = 1;
                    newMag[i] += 10;
                }
            }
            return new UnsignedBigInteger(newMag, countMeaningBits(newMag, newMag.length));
        }
    }

    public UnsignedBigInteger multiply(UnsignedBigInteger other) {
        byte[] newMag = new byte[Math.max(bitsCount, other.bitsCount) * 2];
        for (int i = 0; i < bitsCount; i++) {
            for (int j = 0; j < other.bitsCount; j++) {
                int curPos = i + j;
                newMag[curPos] += mag[i] * other.mag[j];
                newMag[curPos + 1] += newMag[curPos] / 10;
                newMag[curPos] %= 10;
            }
        }
        return new UnsignedBigInteger(newMag, countMeaningBits(newMag, newMag.length));
    }

    public UnsignedBigInteger divide(UnsignedBigInteger other) {
        if (less(other)) return ZERO;
        else if(equals(other)) return ONE;
        else if(other.equals(ONE)) return this;
        else {
            return ZERO;
        }

    }

    public UnsignedBigInteger mod(UnsignedBigInteger other) {
        return minus(other.multiply(divide(other)));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UnsignedBigInteger) {
            return this.compareTo((UnsignedBigInteger) o) == 0;
        } else {
            throw new IllegalArgumentException("incorrect type");
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mag) + bitsCount * 3;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = bitsCount - 1; i >= 0; i--) {
            stringBuilder.append(mag[i]);
        }
        return stringBuilder.toString();
    }

}
