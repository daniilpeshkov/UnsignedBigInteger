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
        if (number.isEmpty())number.add((byte) 0);
    }

    public UnsignedBigInteger(int i) {
        if (i < 0) throw new IllegalArgumentException("number is negative");
        number = new LinkedList<>();
        while (i > 0) {
            number.add((byte) (i % 10));
            i /= 10;
        }
        if (number.isEmpty())number.add((byte) 0);
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
            for(int i = number.size() - 1; i >= 0 ; i--) {
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

    public UnsignedBigInteger multiply(int i) {
        return this.multiply(new UnsignedBigInteger(i));
    }

    public UnsignedBigInteger multiply(long i) {
        return this.multiply(new UnsignedBigInteger(i));
    }

    public UnsignedBigInteger minus(UnsignedBigInteger o) {
        if (this.lessInclusive(o)) return new UnsignedBigInteger("0");
        else {
            int minSize = o.number.size();
            List<Byte> newNumber = new ArrayList<>(number.size());
            int loan = 0;
            for (int i = 0; i < number.size() || loan != 0; i++) {
                byte tmp = (byte) (number.get(i) - (i < minSize ? o.number.get(i) : 0) - loan);
                if (tmp >= 0) loan = 0;
                else {
                    loan = 1;
                    tmp += 10;
                }
                newNumber.add(tmp);
            }
            while (newNumber.get(newNumber.size() - 1) == 0) newNumber.remove(newNumber.size() - 1);
            return new UnsignedBigInteger(newNumber);
        }
    }

    public UnsignedBigInteger minus(int o) {
        return this.minus(new UnsignedBigInteger(o));
    }

    public UnsignedBigInteger minus(long o) {
        return this.minus(new UnsignedBigInteger(o));
    }

    public UnsignedBigInteger divide(UnsignedBigInteger o) {
        if (o.compareTo(new UnsignedBigInteger("1")) == 0)
            return this.clone();
        else if(o.compareTo(new UnsignedBigInteger("0")) == 0)
            throw new ArithmeticException("dividing by zero");
        else if (o.lessInclusive(this)) {
            UnsignedBigInteger k = new UnsignedBigInteger(new LinkedList<>());
            k.number.add((byte) 0);
            UnsignedBigInteger tmp = this;
            while (tmp.greaterInclusive(o)) {
                tmp = tmp.minus(o);
                k.increment();
            }
            return k;
        } else return new UnsignedBigInteger("0");
    }

    public UnsignedBigInteger divide(int o) {
        return this.divide(new UnsignedBigInteger(o));
    }

    public UnsignedBigInteger divide(long o) {
        return this.divide(new UnsignedBigInteger(o));
    }

    public UnsignedBigInteger mod(UnsignedBigInteger i) {
        return this.minus(i.multiply(this.divide(i)));
    }

    public UnsignedBigInteger mod(int o) {
        return this.mod(new UnsignedBigInteger(o));
    }

    public UnsignedBigInteger mod(long o) {
        return this.mod(new UnsignedBigInteger(o));
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

    @Override
    public UnsignedBigInteger clone() {
        List<Byte> tmp = new ArrayList<>(number.size());
        tmp.addAll(number);
        return new UnsignedBigInteger(tmp);
    }
}
