package unsignedBigInteger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UnsignedBigIntegerOld implements java.lang.Comparable<UnsignedBigIntegerOld>, Cloneable {

    private List<Byte> number;

    public static UnsignedBigIntegerOld max(UnsignedBigIntegerOld a, UnsignedBigIntegerOld b) {
        if (a.compareTo(b) >= 0) return a;
        else return b;
    }

    public static UnsignedBigIntegerOld min(UnsignedBigIntegerOld a, UnsignedBigIntegerOld b) {
        if (a.compareTo(b) <= 0) return a;
        else return b;
    }

    public UnsignedBigIntegerOld(String number) {
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

    public UnsignedBigIntegerOld(long i) {
        if (i < 0) throw new IllegalArgumentException("number is negative");
        number = new LinkedList<>();
        while (i > 0) {
            number.add((byte) (i % 10));
            i /= 10;
        }
        if (number.isEmpty())number.add((byte) 0);
    }

    public UnsignedBigIntegerOld(int i) {
        if (i < 0) throw new IllegalArgumentException("number is negative");
        number = new LinkedList<>();
        while (i > 0) {
            number.add((byte) (i % 10));
            i /= 10;
        }
        if (number.isEmpty())number.add((byte) 0);
    }

    private UnsignedBigIntegerOld(List<Byte> number) {
        this.number = number;
    }

    @Override
    public int compareTo(UnsignedBigIntegerOld o) {
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

    public boolean greater(UnsignedBigIntegerOld i) {
        return (this.compareTo(i) > 0);
    }

    public boolean greaterInclusive(UnsignedBigIntegerOld i) {
        return (this.compareTo(i) >= 0);
    }

    public boolean less(UnsignedBigIntegerOld i) {
        return (this.compareTo(i) < 0);
    }

    public boolean lessInclusive(UnsignedBigIntegerOld i) {
        return (this.compareTo(i) <= 0);
    }

    public UnsignedBigIntegerOld plus(UnsignedBigIntegerOld o) {
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
        return new UnsignedBigIntegerOld(newArr);
    }

    public UnsignedBigIntegerOld plus(int i) {
        return plus(new UnsignedBigIntegerOld(i));
    }

    public UnsignedBigIntegerOld plus(long i) {
        return plus(new UnsignedBigIntegerOld(i));
    }

    public UnsignedBigIntegerOld increment() {
        int i = 0;
        while (number.get(i) + 1 == 10) {
            number.set(i, (byte) 0);
            if (i == number.size() - 1) number.add((byte) 0);
            i++;
        }
        number.set(i, (byte) (number.get(i) + 1));
        return this;
    }

    public UnsignedBigIntegerOld multiply(UnsignedBigIntegerOld i) {
        UnsignedBigIntegerOld max;
        UnsignedBigIntegerOld min;
        if (number.size() >= i.number.size()) {
            max = this;
            min = i;
        } else {
            max = i;
            min = this;
        }
        UnsignedBigIntegerOld tmp = new UnsignedBigIntegerOld("0");
        UnsignedBigIntegerOld k = new UnsignedBigIntegerOld("0");
        while (k.less(min)) {
            tmp = tmp.plus(max);
            k.increment();
        }
        return tmp;
    }

    public UnsignedBigIntegerOld multiply(int i) {
        return this.multiply(new UnsignedBigIntegerOld(i));
    }

    public UnsignedBigIntegerOld multiply(long i) {
        return this.multiply(new UnsignedBigIntegerOld(i));
    }

    public UnsignedBigIntegerOld minus(UnsignedBigIntegerOld o) {
        if (this.lessInclusive(o)) return new UnsignedBigIntegerOld("0");
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
            return new UnsignedBigIntegerOld(newNumber);
        }
    }

    public UnsignedBigIntegerOld minus(int o) {
        return this.minus(new UnsignedBigIntegerOld(o));
    }

    public UnsignedBigIntegerOld minus(long o) {
        return this.minus(new UnsignedBigIntegerOld(o));
    }

    public UnsignedBigIntegerOld divide(UnsignedBigIntegerOld o) {
        if (o.compareTo(new UnsignedBigIntegerOld("1")) == 0)
            return this.clone();
        else if(o.compareTo(new UnsignedBigIntegerOld("0")) == 0)
            throw new ArithmeticException("dividing by zero");
        else if (o.lessInclusive(this)) {
            UnsignedBigIntegerOld k = new UnsignedBigIntegerOld(new LinkedList<>());
            k.number.add((byte) 0);
            UnsignedBigIntegerOld tmp = this;
            while (tmp.greaterInclusive(o)) {
                tmp = tmp.minus(o);
                k.increment();
            }
            return k;
        } else return new UnsignedBigIntegerOld("0");
    }

    public UnsignedBigIntegerOld divide(int o) {
        return this.divide(new UnsignedBigIntegerOld(o));
    }

    public UnsignedBigIntegerOld divide(long o) {
        return this.divide(new UnsignedBigIntegerOld(o));
    }

    public UnsignedBigIntegerOld mod(UnsignedBigIntegerOld i) {
        return this.minus(i.multiply(this.divide(i)));
    }

    public UnsignedBigIntegerOld mod(int o) {
        return this.mod(new UnsignedBigIntegerOld(o));
    }

    public UnsignedBigIntegerOld mod(long o) {
        return this.mod(new UnsignedBigIntegerOld(o));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UnsignedBigIntegerOld) {
            return this.compareTo((UnsignedBigIntegerOld) o) == 0;
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
    public UnsignedBigIntegerOld clone() {
        List<Byte> tmp = new ArrayList<>(number.size());
        tmp.addAll(number);
        return new UnsignedBigIntegerOld(tmp);
    }
}
