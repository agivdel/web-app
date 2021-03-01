package agivdel.webApp1311.dao;

import agivdel.webApp1311.entities.Payment;
import agivdel.webApp1311.entities.User;

import java.lang.String;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final String INSERT_USERNAME = "INSERT INTO users (username) VALUES (?)";
    private static final String UPDATE_PASSWORD_WHERE_ID = "UPDATE users SET password=? WHERE id=?";
    private static final String SELECT_ID_PASSWORD_WHERE_USERNAME = "SELECT id, password FROM users WHERE username=?";
    private static final String INSERT_ID_BALANCE = "INSERT INTO balances (id, balance) VALUES (?,?)";
    private static final String UPDATE_BALANCE_WHERE_ID = "UPDATE balances SET balance=? WHERE id=?";
    private static final String SELECT_BALANCE_WHERE_ID = "SELECT balance FROM balances WHERE id=?";
    private static final String INSERT_ID_PAYMENT = "INSERT INTO payments (user_id, payment) VALUES (?,?)";
    private static final String SELECT_ALL_FROM_PAYMENTS_WHERE_USER_ID = "SELECT * FROM payments WHERE user_id=?";

    public void insertUser(Connection con, User user) throws Exception {
        // Перед возвратом Connection con в пул все Statement'ы и ResultSet'ы,
        // полученные с помощью этого соединения, автоматически закрываются в соответствии с API
        PreparedStatement pstUsers = con.prepareStatement(INSERT_USERNAME, new String[]{"id"});
        pstUsers.setString(1, user.getUsername());
        pstUsers.executeUpdate();

        ResultSet resultSet = pstUsers.getGeneratedKeys();
        if (!resultSet.next()) {
            throw new Exception("database access error");
        }
        user.setId(resultSet.getInt("id"));
    }

    public void updatePassword(Connection con, int userId, String password) throws SQLException {
        PreparedStatement pstUsers = con.prepareStatement(UPDATE_PASSWORD_WHERE_ID);
        pstUsers.setString(1, password);
        pstUsers.setInt(2, userId);
        pstUsers.executeUpdate();
    }

    public User selectUser(Connection con, String username) throws SQLException {
        PreparedStatement pstUser = con.prepareStatement(SELECT_ID_PASSWORD_WHERE_USERNAME);
        pstUser.setString(1, username);

        ResultSet resultSetUser = pstUser.executeQuery();
        if (!resultSetUser.next()) {
            return new User();//возврат user c id=0
        }
        int userId = resultSetUser.getInt(1);
        String password = resultSetUser.getString(2);
        return new User(userId, username, password);
    }

    public void insertBalance(Connection con, int userId, long balance) throws SQLException {
        PreparedStatement pstBalances = con.prepareStatement(INSERT_ID_BALANCE);
        pstBalances.setInt(1, userId);
        pstBalances.setLong(2, balance);
        pstBalances.executeUpdate();
    }

    public void updateBalance(Connection con, int userId, long balance) throws SQLException {
        PreparedStatement pstBalances = con.prepareStatement(UPDATE_BALANCE_WHERE_ID);
        pstBalances.setLong(1, balance);
        pstBalances.setInt(2, userId);
        pstBalances.executeUpdate();
    }

    public Long selectBalance(Connection con, int userId) throws Exception {
        PreparedStatement pstBalance = con.prepareStatement(SELECT_BALANCE_WHERE_ID);
        pstBalance.setInt(1, userId);

        ResultSet resultSetBalance = pstBalance.executeQuery();
        if (!resultSetBalance.next()) {
            throw new Exception("this userId was not found");
        }
        return resultSetBalance.getLong(1);
    }

    public void insertPayment(Connection con, int userId, long payment) throws SQLException {
        PreparedStatement pstBalances = con.prepareStatement(INSERT_ID_PAYMENT);
        pstBalances.setInt(1, userId);
        pstBalances.setLong(2, payment);
        pstBalances.executeUpdate();
    }

    public List<Payment> selectPayments(Connection con, int userId) throws Exception {
        PreparedStatement pstPayment = con.prepareStatement(SELECT_ALL_FROM_PAYMENTS_WHERE_USER_ID);
        pstPayment.setInt(1, userId);

        ResultSet resultSetPayment = pstPayment.executeQuery();
        List<Payment> payments = new ArrayList<>();
        while (resultSetPayment.next()) {
            Payment payment = new Payment(
                    resultSetPayment.getInt(1),
                    resultSetPayment.getInt(2),
                    resultSetPayment.getLong(3),
                    resultSetPayment.getTimestamp(4)
            );
            payments.add(payment);
        }
        return payments;
    }
}