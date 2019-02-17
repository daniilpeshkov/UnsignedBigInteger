package unsignedBigInteger;

import java.util.ArrayList;

public class UnsignedBigInteger implements java.lang.Comparable<UnsignedBigInteger> {

    private ArrayList<Byte> number;

    public UnsignedBigInteger(String number) {
        if (number == null || number.isEmpty()) throw new IllegalArgumentException("number is empty or null");
        else {
            this.number = new ArrayList<>(number.length());
            for (int i = number.length() - 1; i >= 0; i--) {
                int tmp = number.charAt(i) - '0';
                if (tmp > 9 || tmp < 0) throw new NumberFormatException("is not number");
                this.number.add((byte) tmp);
            }
        }
    }

    private UnsignedBigInteger(ArrayList<Byte> number) {
        this.number = number;
    }

    @Override
    public int compareTo(UnsignedBigInteger o) {
        if(number.size() != o.number.size()) {
            return (number.size() > o.number.size()? 1: -1);
        }
        else {
            for(int i = 0; i < number.size(); i++) {
                if (number.get(i) != o.number.get(i)) {
                    if (number.get(i) > o.number.get(i)) return 1;
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
        int maxLength = Math.max(number.size(), o.number.size());
        int addition = 0;
        ArrayList<Byte> newNumber = new ArrayList<>(maxLength + 1);
        for(int i =0; i < maxLength; i++) {
            int tmp = (i < number.size()? number.get(i): 0) + (i < o.number.size()? o.number.get(i): 0) + addition;
            addition = tmp / 10;
            newNumber.add((byte) (tmp - addition * 10));
        }
        if (addition != 0) {
            newNumber.set(maxLength, (byte) addition);
        }
        return new UnsignedBigInteger(newNumber);
    }

    public UnsignedBigInteger increment() {
        int i = 0;
        while (number.get(i) + 1 == 10) {
            number.set(i, (byte) 0);
            if (i == number.size() - 1) number.add((byte) 0);
            i++;
        }
        number.set(i, (byte) (number.get(i) + 1));
        return this;
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
        return number.hashCode() + 1;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = number.size() - 1; i >= 0; i--) {
            stringBuilder.append(number.get(i));
        }
        return stringBuilder.toString();
    }
}
