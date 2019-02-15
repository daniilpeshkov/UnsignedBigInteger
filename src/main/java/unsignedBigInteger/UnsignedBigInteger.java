package unsignedBigInteger;

import java.util.Arrays;

public class UnsignedBigInteger implements java.lang.Comparable<UnsignedBigInteger> {

    private byte[] number;
    private int length;

    public UnsignedBigInteger(String number) {
        this.number = new byte[number.length()];
        length = number.length();
        for(int i = number.length() - 1; i >=0 ; i--) {
            int tmp = number.charAt(i) - '0';
            if (tmp > 9 || tmp < 0) throw new NumberFormatException("is not number");
            this.number[number.length() - 1 - i] = (byte) tmp;
        }
    }

    private UnsignedBigInteger(byte[] number, int length) {
        this.number = number;
        this.length = length;
    }

    @Override
    public int compareTo(UnsignedBigInteger o) {
        if(number.length != o.number.length) {
            return (number.length > o.number.length? 1: -1);
        }
        else {
            for(int i = 0; i < number.length; i++) {
                if (number[i] != o.number[i]) {
                    if (number[i] > o.number[i]) return 1;
                    else return -1;
                }
            }
        }
        return 0;
    }

    public boolean greaterThan(UnsignedBigInteger o) {
        return (this.compareTo(o) > 0);
    }

    public boolean lessThan(UnsignedBigInteger o) {
        return (this.compareTo(o) < 0);
    }

    public UnsignedBigInteger plus(UnsignedBigInteger o) {
        int maxLength = Math.max(length, o.length);
        int addition = 0;
        byte[] newNumber = new byte[maxLength + 1];
        for(int i =0; i < maxLength; i++) {
            int tmp = (i < number.length? number[i]: 0) + (i < o.number.length? o.number[i]: 0) + addition;
            addition = tmp / 10;
            newNumber[i] = (byte) (tmp - addition * 10);
        }
        if (addition != 0) {
            newNumber[maxLength] = (byte) addition;
            maxLength += 1;
        }

        return new UnsignedBigInteger(newNumber, maxLength);
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
        return Arrays.hashCode(number) + 1;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = length - 1; i >= 0; i--) {
            stringBuilder.append(number[i]);
        }
        return stringBuilder.toString();
    }
}
