package agivdel.webApp1311.dao;

import agivdel.webApp1311.utils.ConnectionManager;
import agivdel.webApp1311.entities.User;

import java.lang.String;
import java.sql.*;

public class UserDao {

    public boolean addUser(User user, long startBalance) {
        String userSqlInsert = "INSERT INTO users (username, user_password) VALUES (?,?)";
        String paymentSqlInsert = "INSERT INTO payments (user_id, balance) VALUES (?,?)";
        Connection con = ConnectionManager.getConnection();
        try (PreparedStatement pstUser = con.prepareStatement(userSqlInsert, new String[]{"id"});
             PreparedStatement pstPayment = con.prepareStatement(paymentSqlInsert)) {

            con.setAutoCommit(false);

            pstUser.setString(1, user.getUsername());
            pstUser.setString(2, user.getPassword());
            pstUser.executeUpdate();

            ResultSet rs = pstUser.getGeneratedKeys();
            if (!rs.next()) {
                return false;
            }
            int user_id = rs.getInt("id");

            pstPayment.setInt(1, user_id);
            pstPayment.setLong(2, startBalance);
            pstPayment.executeUpdate();

            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getSQLState());
            try {
                con.rollback();
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getSQLState());
            }
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getSQLState());
            }
        }
        //TODO con нигде не закрывается!
        return false;
    }

    public User findUser(String username) {
        String userSqlSelect = "SELECT id, password FROM users WHERE username=?";
        String paymentSqlSelect = "SELECT balance FROM payments WHERE id = (SELECT MAX(id) FROM payments WHERE user_id=?)";
        User user = new User();//создаем пустого юзера (id=0)
        Connection con = ConnectionManager.getConnection();
        try (PreparedStatement pstUser = con.prepareStatement(userSqlSelect);
             PreparedStatement pstPayment = con.prepareStatement(paymentSqlSelect)) {

            pstUser.setString(1, username);
            ResultSet resultSetUser = pstUser.executeQuery();
            if (!resultSetUser.next()) {
                return user;//возвращаем пустого юзера (id=0)
            }
            int userId = resultSetUser.getInt(1);
            String password = resultSetUser.getString(2);
            user = new User(userId, username, password);

            pstPayment.setInt(1, user.getId());
            ResultSet resultSetPayment = pstPayment.executeQuery();
            if (!resultSetPayment.next()) {
                return user;
            }
            long balance = resultSetPayment.getLong(1);
            user.setBalance(balance);

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getSQLState());
        }
        return user;
    }

    public long pay(User user, long paymentUnit, long lowerLimit) {
        String paymentSqlSelect = "SELECT balance FROM payments WHERE id = (SELECT MAX(id) FROM payments WHERE user_id=?)";
        String paymentSqlInsert = "INSERT INTO payments (user_id, balance) VALUES (?,?)";
        //TODO оформить двойные записи вместо одинарных
        long currentBalance = 0;
        Connection con = ConnectionManager.getConnection();
        try (PreparedStatement pstPaymentOld = con.prepareStatement(paymentSqlSelect);
             PreparedStatement pstPaymentNew = con.prepareStatement(paymentSqlInsert)) {

            con.setAutoCommit(false);

            pstPaymentOld.setInt(1, user.getId());
            ResultSet resultSet = pstPaymentOld.executeQuery();

            if (!resultSet.next()) {
                return -1;
            }
            currentBalance = resultSet.getInt(1);
            long subtotal = currentBalance - paymentUnit;
            if (subtotal <= lowerLimit) {
                return -1;
            }
            currentBalance = subtotal;

            //а можно всегда возвращать currentBalance
            //в случае нехватки средств на счету делать return сразу же текущим значением currentBalance, а не с -1
            //при этом нужно выдавать исключение с сообщением о нехватке средств, пробрасывая его на самый вверх

            pstPaymentNew.setInt(1, user.getId());
            pstPaymentNew.setLong(2, currentBalance);
            pstPaymentNew.executeUpdate();

            con.commit();
            return currentBalance;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getSQLState());
            try {
                con.rollback();
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getSQLState());
            }
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getSQLState());
            }
        }
        return -1;
    }
}