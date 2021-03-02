package agivdel.webApp1311.entities;

public class Balance {
    private int userId;
    private long value;

    public Balance() {
    }

    public Balance(int id, long value) {
        this.userId = id;
        this.value = value;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Balance{user_id=" + userId + ", " + value + " cents}";
    }
}