package agivdel.webApp1311.entities;

public class User {
    private int id;
    private String username;
    private String password;
    private Long balance;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = null;
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.balance = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", " + balance + "}";
    }
}