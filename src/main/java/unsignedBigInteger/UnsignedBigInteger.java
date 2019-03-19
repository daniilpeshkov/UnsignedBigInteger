package unsignedBigInteger;

import java.util.*;
import java.util.stream.Stream;

public class UnsignedBigInteger implements java.lang.Comparable<UnsignedBigInteger> {

    private static class DivisionResult {
        int[] mod;
        int[] result;

        DivisionResult(int[] result, int[] mod) {
            this.mod = mod;
            this.result = result;
        }
    }

    private int[] mag;
    
    public enum Base {BASE10, BASE}
    
    private static int BASE = 1048576; // 2^20
    private static int[] BASE_IN_10 = {6,7,5,8,4,0,1};
    
    private int digitsCount;

    public static UnsignedBigInteger max(UnsignedBigInteger a, UnsignedBigInteger b) {
        if (a.compareTo(b) >= 0) return a;
        else return b;
    }

    public static UnsignedBigInteger min(UnsignedBigInteger a, UnsignedBigInteger b) {
        if (a.compareTo(b) <= 0) return a;
        else return b;
    } 

    private static int compareMags(int[] mag1, int offset1,
                                   int[] mag2, int offset2) {
        int maxLength = Math.max(mag1.length - offset1, mag2.length - offset2);
        int answer = 0;
        for (int i = 0; i < maxLength; i++) {
            int a = (i + offset1 < mag1.length? mag1[offset1 + i]: 0);
            int b = (offset2 + i < mag2.length? mag2[offset2 + i]: 0);
            if (a != b) {
                if (a > b) answer = 1;
                else answer = -1;
            }
        }
        return answer;
    }

    private static int[] subtractMags(int[] mag1, int offset1,
                                     int[] mag2, int offset2, Base base) {
        int[] newMag = Arrays.copyOf(mag1, mag1.length);
        int loan = 0;
        for (int i = 0; i + offset1 < mag1.length || loan != 0; i++) {
            newMag[i + offset1] = mag1[i + offset1]
                    - (i + offset2 < mag2.length ? mag2[i + offset2] : 0) - loan;
            if (newMag[i + offset1] >= 0) loan = 0;
            else {
                loan = 1;
                newMag[i + offset1] += (base == Base.BASE? BASE: 10);
            }
        }
        return newMag;
    }

    private static int[] plusMags(int[] mag1, int[] mag2, Base base) {
        int magLength = Math.max(mag1.length, mag2.length);
        int[] newMag = new int[magLength + 1];
        int addition = 0;
        int newDigitsCount;
        for( newDigitsCount = 0;newDigitsCount < magLength; newDigitsCount++) {
            int tmp = (newDigitsCount < mag1.length? mag1[newDigitsCount]: 0)
                    + (newDigitsCount < mag2.length? mag2[newDigitsCount]: 0) + addition;
            if (base == Base.BASE10) {
                addition = tmp / 10;
                newMag[newDigitsCount] = tmp % 10;
            } else if (base == Base.BASE) {
                addition = tmp / BASE;
                newMag[newDigitsCount] = tmp % BASE;
            }
        }
        if (addition != 0) {
            newDigitsCount++;
            newMag[newDigitsCount - 1] = addition;
        }
        return newMag;
    }

    private static int[] multiplyMags(int[] mag1, int[] mag2, Base base) {
        int mag1Len = countMeaningDigits(mag1);
        int mag2Len = countMeaningDigits(mag2);
        int[] newMag = new int[mag1Len + mag2Len];
        for (int i = 0; i < mag1Len; i++) {
            for (int j = 0; j < mag2Len; j++) {
                int curPos = i + j;
                if (base == Base.BASE10) {
                    newMag[curPos] += mag1[i] * mag2[j];
                    newMag[curPos + 1] += newMag[curPos] / 10;
                    newMag[curPos] %= 10;
                } else if (base == Base.BASE) {
                    newMag[curPos] += mag1[i] * mag2[j];
                    newMag[curPos + 1] += newMag[curPos] / BASE;
                    newMag[curPos] %= BASE;
                }
            }
        }
        return newMag;
    }

    private static DivisionResult divideMags(int[] mag1, int[] mag2, Base base) {
        int[] magCopy = Arrays.copyOf(mag1, mag1.length);
        int offset = magCopy.length - 1;
        Stack<Integer> newMagReversed = new Stack<>();

        while (compareMags(magCopy, offset, mag2, 0) < 0 && offset > 0) {
            offset--;
        }

        while (compareMags(magCopy, 0, mag2, 0) >= 0 || offset >= 0) {
            int curDigit = 0;
            while (compareMags(magCopy, offset, mag2, 0) < 0 && offset > 0) {
                offset--;
                newMagReversed.push(0);
            }
            while (compareMags(magCopy, offset, mag2, 0) >= 0) {
                magCopy = subtractMags(magCopy, offset, mag2, 0, base);
                curDigit++;
            }
            newMagReversed.push(curDigit);
            offset--;
        }

        int[] newMag = new int[newMagReversed.size()];
        for (int i = 0; i < newMag.length && !newMagReversed.empty(); i++) {
            newMag[i] = newMagReversed.pop();
        }

        return new DivisionResult(newMag, magCopy);
    }

    private static int countMeaningDigits(int[] mag) {
        int start = mag.length - 1;
        while (start > 1 && mag[start] == 0)
            start--;
        return start + 1;
    }

    public static UnsignedBigInteger valueOf(int val) {
        if (val < 0) throw new IllegalArgumentException("number is negative");
        Queue<Integer> digits = new ArrayDeque<>();
        while(val > 0) {
            digits.add(val % BASE);
            val /= BASE;
        }
        int[] mag = new int[digits.size()];
        int digitsCount = 0;
        if (digits.isEmpty()) {
            return new UnsignedBigInteger(new int[]{0}, 1);
        } else {
            while (!digits.isEmpty()) {
                mag[digitsCount] = digits.poll();

                digitsCount++;
            }
            return new UnsignedBigInteger(mag, digitsCount);
        }
    }

    public static UnsignedBigInteger valueOf(String number) {
        //TODO
        if (number == null || number.isEmpty()) throw new IllegalArgumentException("number is empty or null");
        else {
            int[] decimalNum = Stream.of(new StringBuilder(number).reverse().toString().split(""))
                    .mapToInt(Integer::parseInt).toArray();

            Queue<Integer> digits = new ArrayDeque<>();
            while(compareMags(decimalNum, 0, new int[] {0}, 0) != 0) {
                DivisionResult result = divideMags(decimalNum,BASE_IN_10, Base.BASE10);
                int length = countMeaningDigits(result.mod);
                int curDigit = result.mod[length - 1];
                for (int i = length - 2; i >= 0; i--) {
                    curDigit *= 10;
                    curDigit += result.mod[i];
                }
                digits.add(curDigit);
                decimalNum = result.result;
            }

            int[] mag = new int[digits.size()];
            int digitsCount = 0;

            while (!digits.isEmpty()) {
                mag[digitsCount] = digits.poll();
                digitsCount++;
            }

            return new UnsignedBigInteger(mag, countMeaningDigits(mag));
        }
    }

    private UnsignedBigInteger(int[] mag, int digitsCount) {
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
        int[] newMag = plusMags(mag, other.mag, Base.BASE);
        return new UnsignedBigInteger(newMag, countMeaningDigits(newMag));
    }
    

    public UnsignedBigInteger minus(UnsignedBigInteger other) {
        if (this.less(other))
            throw new IllegalArgumentException(toString() + " is less than " + other.toString());
        else {
            int[] newMag = subtractMags(mag, 0, other.mag, 0, Base.BASE);
            return new UnsignedBigInteger(newMag, countMeaningDigits(newMag));
        }
    }

    public UnsignedBigInteger multiply(UnsignedBigInteger other) {
        int[] newMag = multiplyMags(mag, other.mag, Base.BASE);
        return new UnsignedBigInteger(newMag, countMeaningDigits(newMag));
    }

    public UnsignedBigInteger divide(UnsignedBigInteger other) {
        DivisionResult result = divideMags(mag, other.mag, Base.BASE);
        return new UnsignedBigInteger(result.result, countMeaningDigits(result.result));
    }

    public UnsignedBigInteger mod(UnsignedBigInteger other) {
        DivisionResult result = divideMags(mag, other.mag, Base.BASE);
        return new UnsignedBigInteger(result.mod, countMeaningDigits(result.mod));
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
        int[] decimalNum = new int[] {mag[digitsCount - 1]};
        int[] base = new int[] {BASE};

        for (int i = digitsCount - 2; i >= 0; i--) {
            decimalNum = multiplyMags(decimalNum, base, Base.BASE10);
            decimalNum = plusMags(decimalNum, new int[]{mag[i]}, Base.BASE10);
        }

        StringBuilder stringBuilder = new StringBuilder();
        int digitsCnt = countMeaningDigits(decimalNum);
        for(int i = digitsCnt - 1; i >= 0; i--) {
            stringBuilder.append(decimalNum[i]);
        }
        return stringBuilder.toString();
    }

}
