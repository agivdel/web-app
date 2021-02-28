package agivdel.webApp1311.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Balance {
    private long value;
    private LocalDateTime time;//TODO так ли это нужно?

    public Balance() {
    }

    public Balance(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Balance)) return false;
        Balance balance = (Balance) o;
        return value == balance.value;
    }

    //TODO так ли он нужен?
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
//        return "balance=" + value / 100 + "." + Math.abs(value) % 100 + " USD";
// если начальная сумма меньше платежа, то при переходе через 0 знак минуса теряется (следущая операция уже верная)
        return "balance=" + value + " cents";
    }
}