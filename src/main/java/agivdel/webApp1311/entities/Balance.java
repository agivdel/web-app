package agivdel.webApp1311.entities;

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
    public String toString() {
        return "balance=" + value + " cents";
    }
}