package agivdel.webApp1311.entities;

import java.util.Objects;

public class Balance {
    private long value;

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

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "balance=" + value + " cents";
    }
}