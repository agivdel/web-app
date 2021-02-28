package agivdel.webApp1311.entities;

import java.util.Objects;

public class User {
    private int id;
    private String username;
    private String password;
    private Balance balance;

    public User() {
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.balance = new Balance(801);//изначально баланс делаем некорректным, верное значение ставим только внутри метода addNewUser()
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = new Balance(801);
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
        return balance.getValue();
    }

    public void setBalance(long balance) {
        this.balance.setValue(balance);
    }

    //TODO так ли он нужен, если мы не проверяем баланс, а пароли сравнимаем после хеширования?
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id && username.equals(user.username) && password.equals(user.password) && balance.equals(user.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, balance);
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