package unsignedBigInteger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UnsignedBigInteger implements java.lang.Comparable<UnsignedBigInteger> {

    private List<Byte> number;

    public static UnsignedBigInteger max(UnsignedBigInteger a, UnsignedBigInteger b) {
        if (a.compareTo(b) >= 0) return a;
        else return b;
    }

    public static UnsignedBigInteger min(UnsignedBigInteger a, UnsignedBigInteger b) {
        if (a.compareTo(b) <= 0) return a;
        else return b;
    }

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

    public UnsignedBigInteger(long i) {
        if (i < 0) throw new IllegalArgumentException("number is negative");
        number = new LinkedList<>();
        while (i > 0) {
            number.add((byte) (i % 10));
            i /= 10;
        }
    }

    public UnsignedBigInteger(byte i) {
        if (i < 0) throw new IllegalArgumentException("number is negative");
        number = new LinkedList<>();
        while (i > 0) {
            number.add((byte) (i % 10));
            i /= 10;
        }
    }

    public UnsignedBigInteger(int i) {
        if (i < 0) throw new IllegalArgumentException("number is negative");
        number = new LinkedList<>();
        while (i > 0) {
            number.add((byte) (i % 10));
            i /= 10;
        }
    }

    private UnsignedBigInteger(List<Byte> number) {
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

    public boolean greater(UnsignedBigInteger i) {
        return (this.compareTo(i) > 0);
    }

    public boolean greaterInclusive(UnsignedBigInteger i) {
        return (this.compareTo(i) >= 0);
    }

    public boolean less(UnsignedBigInteger i) {
        return (this.compareTo(i) < 0);
    }

    public boolean lessInclusive(UnsignedBigInteger i) {
        return (this.compareTo(i) <= 0);
    }

    public UnsignedBigInteger plus(UnsignedBigInteger o) {
        int maxLength = Math.max(number.size(), o.number.size());
        int addition = 0;
        ArrayList<Byte> newArr = new ArrayList<>(maxLength + 1);
        for(int i =0; i < maxLength; i++) {
            int tmp = (i < number.size()? number.get(i): 0) + (i < o.number.size()? o.number.get(i): 0) + addition;
            addition = tmp / 10;
            newArr.add((byte) (tmp - addition * 10));
        }
        if (addition != 0) {
            newArr.add((byte) addition);
        }
        return new UnsignedBigInteger(newArr);
    }

    public UnsignedBigInteger plus(int i) {
        return plus(new UnsignedBigInteger(i));
    }

    public UnsignedBigInteger plus(long i) {
        return plus(new UnsignedBigInteger(i));
    }

    public UnsignedBigInteger plus(byte i) {
        return plus(new UnsignedBigInteger(i));
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

    public UnsignedBigInteger multiply(UnsignedBigInteger i) {
        UnsignedBigInteger max;
        UnsignedBigInteger min;
        if (number.size() >= i.number.size()) {
            max = this;
            min = i;
        } else {
            max = i;
            min = this;
        }
        UnsignedBigInteger tmp = new UnsignedBigInteger("0");
        UnsignedBigInteger k = new UnsignedBigInteger("0");
        while (k.less(min)) {
            tmp = tmp.plus(max);
            k.increment();
        }
        return tmp;
    }

    public UnsignedBigInteger subtract(UnsignedBigInteger i) {
        if (this.lessInclusive(i)) return new UnsignedBigInteger("0");
        else {
             return new UnsignedBigInteger("2");//TODO
        }
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
