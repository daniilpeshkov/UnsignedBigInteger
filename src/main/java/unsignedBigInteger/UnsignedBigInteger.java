package unsignedBigInteger;

import org.jetbrains.annotations.Contract;

import java.util.*;

public class UnsignedBigInteger implements java.lang.Comparable<UnsignedBigInteger> {

    public enum Base {BASE10, BASE256}

    private byte[] mag;

    private int digitsCount;

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

    private static byte[] plusMags(byte[] mag1, byte[] mag2, Base base) {
        int magLength = Math.max(mag1.length, mag2.length);
        byte[] newMag = new byte[magLength + 1];
        int addition = 0;
        int newDigitsCount;
        for( newDigitsCount = 0;newDigitsCount < magLength; newDigitsCount++) {
            int tmp = (newDigitsCount < mag1.length? mag1[newDigitsCount]: 0)
                    + (newDigitsCount < mag2.length? mag2[newDigitsCount]: 0) + addition;
            if (base == Base.BASE10) {
                addition = tmp / 10;
                newMag[newDigitsCount] = (byte) (tmp % 10);
            } else if (base == Base.BASE256) {
                tmp += 256;
                addition = tmp / 256;
                newMag[newDigitsCount] = (byte) (tmp % 256 - 128);
            }
        }
        if (addition != 0) {
            newDigitsCount++;
            newMag[newDigitsCount - 1] = 1;
        }
        return newMag;
    }

    public static byte[] multiply(byte[] mag1, byte[] mag2, Base base) {
        int mag1Len = countMeaningDigits(mag1);
        int mag2Len = countMeaningDigits(mag2);
        byte[] newMag = new byte[mag1Len + mag2Len];//new byte[Math.max(digitsCount, other.digitsCount) * 2];
        for (int i = 0; i < mag1Len; i++) {
            for (int j = 0; j < mag2Len; j++) {
                int curPos = i + j;
                if (base == Base.BASE10) {
                    newMag[curPos] += mag1[i] * mag2[j];
                    newMag[curPos + 1] += newMag[curPos] / 10;
                    newMag[curPos] %= 10;
                } else if (base == Base.BASE256) {

                }
            }
        }
        return newMag;
    }

    private static int countMeaningDigits(byte[] mag) {
        int start = mag.length - 1;
        while (start > 1 && mag[start - 1] == 0)
            start--;
        return start;
    }

    public static UnsignedBigInteger valueOf(int val) {
        if (val < 0) throw new IllegalArgumentException("number is negative");
        Queue<Integer> digits = new ArrayDeque<>();
        while(val > 0) {
            digits.add(val % 256 - 128);
            val /= 256;
        }
        byte[] mag = new byte[digits.size()];
        int digitsCount = 0;
        if (digits.isEmpty()) {
            return new UnsignedBigInteger(new byte[]{0}, 1);
        } else {
            while (!digits.isEmpty()) {
                mag[digitsCount] = digits.poll().byteValue();

                digitsCount++;
            }
            return new UnsignedBigInteger(mag, digitsCount);
        }
    }


    @Contract("null -> fail")
    public static UnsignedBigInteger valueOf(String number) {
        if (number == null || number.isEmpty()) throw new IllegalArgumentException("number is empty or null");
        else {
            int digitsCount = number.length();
            byte[] mag = new byte[digitsCount];
            for (int i = digitsCount - 1; i >= 0; i--) {
                int tmp = number.charAt(i) - '0';
                if (tmp > 9 || tmp < 0) throw new NumberFormatException("is not number");
                mag[mag.length - i - 1] = (byte) tmp;
            }
            return new UnsignedBigInteger(mag, digitsCount);
        }
    }

    private UnsignedBigInteger(byte[] mag, int digitsCount) {
        this.mag = mag;
        this.digitsCount = digitsCount;
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
        byte[] newMag = plusMags(mag, other.mag, Base.BASE256);
        return new UnsignedBigInteger(newMag, countMeaningDigits(newMag));
    }
    

    public UnsignedBigInteger minus(UnsignedBigInteger other) {
        if (this.less(other))
            throw new IllegalArgumentException(toString() + " is less than " + other.toString());
        else {
            byte[] newMag = new byte[digitsCount];
            int loan = 0;
            for (int i = 0; i < digitsCount || loan != 0; i++) {
                newMag[i] = (byte) (mag[i] - (i < other.digitsCount ? other.mag[i] : 0) - loan);
                if (newMag[i] >= 0) loan = 0;
                else {
                    loan = 1;
                    newMag[i] += 10;
                }
            }
            return new UnsignedBigInteger(newMag, countMeaningDigits(newMag));
        }
    }

    public UnsignedBigInteger multiply(UnsignedBigInteger other) {
        byte[] newMag = new byte[digitsCount + other.digitsCount];//new byte[Math.max(digitsCount, other.digitsCount) * 2];
        for (int i = 0; i < digitsCount; i++) {
            for (int j = 0; j < other.digitsCount; j++) {
                int curPos = i + j;
                newMag[curPos] += mag[i] * other.mag[j];
                newMag[curPos + 1] += newMag[curPos] / 10;
                newMag[curPos] %= 10;
            }
        }
        return new UnsignedBigInteger(newMag, countMeaningDigits(newMag));
    }

    public UnsignedBigInteger divide(UnsignedBigInteger other) {
        byte[] magCopy = Arrays.copyOf(mag, digitsCount);
        int offset = magCopy.length - 1;//digitsCount - other.digitsCount;
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
        byte[] magCopy = Arrays.copyOf(mag, digitsCount);
        int offset = magCopy.length - 1;//digitsCount - other.digitsCount;

        while (compareMags(magCopy, 0, other.mag, 0) >= 0 || offset >= 0) {
            while (compareMags(magCopy, offset, other.mag, 0) < 0 && offset > 0) {
                offset--;
            }
            while (compareMags(magCopy, offset, other.mag, 0) >= 0) {
                subtractMags(magCopy, offset, other.mag, 0);
            }
            offset--;
        }

        return new UnsignedBigInteger(magCopy, countMeaningDigits(magCopy));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        else if (o instanceof UnsignedBigInteger) {
            return this.compareTo((UnsignedBigInteger) o) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (int i = 0; i < digitsCount; i++) {
            hashCode += mag[i] * 199;
        }
        return hashCode;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = digitsCount - 1; i >= 0; i--) {
            stringBuilder.append(mag[i] + 128 + " ");
        }
        return stringBuilder.toString();
    }

}
