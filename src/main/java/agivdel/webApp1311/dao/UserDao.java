package agivdel.webApp1311.dao;

import agivdel.webApp1311.entities.User;

import java.lang.String;
import java.sql.*;

public class UserDao {
    private static final String INSERT_INTO_USERS_USERNAME = "INSERT INTO users (username) VALUES (?)";
    private static final String UPDATE_USERS_SET_PASSWORD = "UPDATE users SET password=? WHERE id=?";
    private static final String SELECT_ID_PASSWORD_FROM_USERS = "SELECT id, password FROM users WHERE username=?";
    private static final String INSERT_INTO_BALANCES_ID_BALANCE = "INSERT INTO balances (id, balance) VALUES (?,?)";
    private static final String UPDATE_BALANCES_SET_BALANCE = "UPDATE balances SET balance=? WHERE id=?";
    private static final String SELECT_BALANCE_FROM_BALANCES = "SELECT balance FROM balances WHERE id=?";

    public void insertUser(Connection con, User user) throws Exception {
        // Перед возвратом Connection con в пул все Statement'ы и ResultSet'ы,
        // полученные с помощью этого соединения, автоматически закрываются в соответствии с API
        PreparedStatement pstUsers = con.prepareStatement(INSERT_INTO_USERS_USERNAME, new String[]{"id"});
        pstUsers.setString(1, user.getUsername());
        pstUsers.executeUpdate();

        ResultSet resultSet = pstUsers.getGeneratedKeys();
        if (!resultSet.next()) {
            throw new Exception("database access error");
        }
        user.setId(resultSet.getInt("id"));
    }

    public void updatePassword(Connection con, int userId, String password) throws SQLException {
        PreparedStatement pstUsers = con.prepareStatement(UPDATE_USERS_SET_PASSWORD);
        pstUsers.setString(1, password);
        pstUsers.setInt(2, userId);
        pstUsers.executeUpdate();
    }

    public User selectUser(Connection con, String username) throws Exception {
        PreparedStatement pstUser = con.prepareStatement(SELECT_ID_PASSWORD_FROM_USERS);
        pstUser.setString(1, username);

        ResultSet resultSetUser = pstUser.executeQuery();
        if (!resultSetUser.next()) {
            throw new Exception("database access error");
        }
        int userId = resultSetUser.getInt(1);
        String password = resultSetUser.getString(2);
        return new User(userId, username, password);
    }

    public void insertBalance(Connection con, int userId, long balance) throws SQLException {
        PreparedStatement pstBalances = con.prepareStatement(INSERT_INTO_BALANCES_ID_BALANCE);
        pstBalances.setInt(1, userId);
        pstBalances.setLong(2, balance);
        pstBalances.executeUpdate();
    }

    public void updateBalance(Connection con, int userId, long balance) throws SQLException {
        PreparedStatement pstBalances = con.prepareStatement(UPDATE_BALANCES_SET_BALANCE);
        pstBalances.setLong(1, balance);
        pstBalances.setInt(2, userId);
        pstBalances.executeUpdate();
    }

    public Long selectBalance(Connection con, int userId) throws Exception {
        PreparedStatement pstBalance = con.prepareStatement(SELECT_BALANCE_FROM_BALANCES);
        pstBalance.setInt(1, userId);

        ResultSet resultSetBalance = pstBalance.executeQuery();
        if (!resultSetBalance.next()) {
            throw new Exception("database access error");
        }
        return resultSetBalance.getLong(1);
    }
}