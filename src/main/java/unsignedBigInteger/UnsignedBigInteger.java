package unsignedBigInteger;

import java.util.*;

public class UnsignedBigInteger implements java.lang.Comparable<UnsignedBigInteger> {

    private byte[] mag;

    private int bitsCount;

    public static UnsignedBigInteger max(UnsignedBigInteger a, UnsignedBigInteger b) {
        if (a.compareTo(b) >= 0) return a;
        else return b;
    }

    public static UnsignedBigInteger min(UnsignedBigInteger a, UnsignedBigInteger b) {
        if (a.compareTo(b) <= 0) return a;
        else return b;
    } 

    private static int compareMags(byte[] mag1, int offset1,
                                   byte[] mag2, int offset2) {
        int maxLength = Math.max(mag1.length - offset1, mag2.length - offset2);
        int answer = 0;
        for (int i = 0; i < maxLength; i++) {
            byte a = (i + offset1 < mag1.length? mag1[offset1 + i]: 0);
            byte b = (offset2 + i < mag2.length? mag2[offset2 + i]: 0);
            if (a != b) {
                if (a > b) answer = 1;
                else answer = -1;
            }
        }
        return answer;
    }

    private static void subtractMags(byte[] mag1, int offset1,
                                     byte[] mag2, int offset2) {
        int loan = 0;
        for (int i = 0; i + offset1 < mag1.length || loan != 0; i++) {
            mag1[i + offset1] = (byte) (mag1[i + offset1]
                    - (i + offset2 < mag2.length ? mag2[i + offset2] : 0) - loan);
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


    public static UnsignedBigInteger valueOf(String number) {
        if (number == null || number.isEmpty()) throw new IllegalArgumentException("number is empty or null");
        else {
            int bitsCount = number.length();
            byte[] mag = new byte[bitsCount];
            for (int i = bitsCount - 1; i >= 0; i--) {
                int tmp = number.charAt(i) - '0';
                if (tmp > 9 || tmp < 0) throw new NumberFormatException("is not number");
                mag[mag.length - i - 1] = (byte) tmp;
            }
            return new UnsignedBigInteger(mag, bitsCount);
        }
    }

    private UnsignedBigInteger(byte[] mag, int bitsCount) {
        this.mag = mag;
        this.bitsCount = bitsCount;
    }

    @Override
    public int compareTo(UnsignedBigInteger other) {
        return compareMags(mag,0, other.mag,0);
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
        byte[] magCopy = Arrays.copyOf(mag, bitsCount);
        int offset = magCopy.length - 1;//bitsCount - other.bitsCount;
        Stack<Byte> newMagReversed = new Stack<>();

        while (compareMags(magCopy, offset, other.mag, 0) < 0 && offset > 0) {
            offset--;
        }

        while (compareMags(magCopy, 0, other.mag, 0) >= 0 || offset >= 0) {
            int curDigit = 0;
            while (compareMags(magCopy, offset, other.mag, 0) < 0 && offset > 0) {
                offset--;
                newMagReversed.push((byte) 0);
            }
            while (compareMags(magCopy, offset, other.mag, 0) >= 0) {
                subtractMags(magCopy, offset, other.mag, 0);
                curDigit++;
            }
            newMagReversed.push((byte) curDigit);
            offset--;
        }

        byte[] newMag = new byte[newMagReversed.size()];
        for (int i = 0; i < newMag.length && !newMagReversed.empty(); i++) {
            newMag[i] = newMagReversed.pop();
        }

        return new UnsignedBigInteger(newMag, newMag.length);
    }

    public UnsignedBigInteger mod(UnsignedBigInteger other) {
        byte[] magCopy = Arrays.copyOf(mag, bitsCount);
        int offset = magCopy.length - 1;//bitsCount - other.bitsCount;

        while (compareMags(magCopy, 0, other.mag, 0) >= 0 || offset >= 0) {
            while (compareMags(magCopy, offset, other.mag, 0) < 0 && offset > 0) {
                offset--;
            }
            while (compareMags(magCopy, offset, other.mag, 0) >= 0) {
                subtractMags(magCopy, offset, other.mag, 0);
            }
            offset--;
        }

        return new UnsignedBigInteger(magCopy, countMeaningBits(magCopy, bitsCount));
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
        int hashCode = 0;
        for (int i = 0; i < bitsCount; i++) {
            hashCode += mag[i] * 199;
        }
        return hashCode;
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
