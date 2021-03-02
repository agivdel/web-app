package agivdel.webApp1311.entities;

public class Balance {
    private int id;
    private long value;

    public Balance() {
    }

    public Balance(int id, long value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}