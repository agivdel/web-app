package agivdel.webApp1311.dao;

import agivdel.webApp1311.entities.User;

import java.lang.String;
import java.sql.*;

public class UserDao {

    public void addUser(Connection con, User user) throws Exception {
        String userSqlInsert = "INSERT INTO users (username) VALUES (?)";

        PreparedStatement pstUsers = con.prepareStatement(userSqlInsert, new String[]{"id"});
        pstUsers.setString(1, user.getUsername());
        pstUsers.executeUpdate();

        ResultSet resultSet = pstUsers.getGeneratedKeys();
        if (!resultSet.next()) {
            throw new Exception("database access error");
        }
        user.setId(resultSet.getInt("id"));
    }

    public void updateBalance(Connection con, int userId, long balance) throws Exception {
        String balancesSqlUpdate = "UPDATE balances SET balance=? WHERE id=?";

        PreparedStatement pstBalances = con.prepareStatement(balancesSqlUpdate);
        pstBalances.setLong(1, balance);
        pstBalances.setInt(2, userId);
        pstBalances.executeUpdate();
    }

    public void updatePassword(Connection con, int userId, String password) throws SQLException {
        String userSqlInsert = "UPDATE users SET password=? WHERE id=?";

        PreparedStatement pstUsers = con.prepareStatement(userSqlInsert);
        pstUsers.setString(1, password);
        pstUsers.setInt(2, userId);
        pstUsers.executeUpdate();
    }

    public User findUser(Connection con, String username) throws Exception {
        String userSqlSelect = "SELECT id, password FROM users WHERE username=?";

        PreparedStatement pstUser = con.prepareStatement(userSqlSelect);
        pstUser.setString(1, username);
        ResultSet resultSetUser = pstUser.executeQuery();
        if (!resultSetUser.next()) {
            throw new Exception("database access error");
        }
        int userId = resultSetUser.getInt(1);
        String password = resultSetUser.getString(2);
        return new User(userId, username, password);
    }

    public Long findBalance(Connection con, int userId) throws Exception {
        String balanceSqlSelect = "SELECT balance FROM balances WHERE id=?";

        PreparedStatement pstBalance = con.prepareStatement(balanceSqlSelect);
        pstBalance.setInt(1, userId);
        ResultSet resultSetBalance = pstBalance.getResultSet();
        if (!resultSetBalance.next()) {
            throw new Exception("database access error");
        }
        return resultSetBalance.getLong(1);
    }
}