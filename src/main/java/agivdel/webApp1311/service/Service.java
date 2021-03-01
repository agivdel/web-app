package agivdel.webApp1311.service;

import agivdel.webApp1311.utils.ConnectionPool;
import agivdel.webApp1311.password.PBKDF2;
import agivdel.webApp1311.utils.PropertiesReader;
import agivdel.webApp1311.dao.UserDao;
import agivdel.webApp1311.entities.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Service {

    public boolean authentication(User user) throws Exception {
        User storedUser = findUser(user);
        return user.equals(storedUser) && comparePasswords(user, storedUser);
    }

    public boolean isUserExists(User user) throws Exception {
        User storedUser = findUser(user);
        return storedUser.getId() != 0;
    }

    public boolean signUp(User user) throws Exception {
        UserDao userDao = new UserDao();
        ConnectionPool pool = new ConnectionPool();
        Connection con = pool.getConnection();
        try {
            con.setAutoCommit(false);
            userDao.addUser(con, user);
            userDao.updateBalance(con, user.getId(), startBalance());
            userDao.updatePassword(con, user.getId(), saltPassword(user));
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("transaction rollback error ");
            }
            throw new Exception("database access error");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.close(con);
        }
        return false;
    }

    public User findUser(User user) throws Exception {
        UserDao userDao = new UserDao();
        ConnectionPool pool = new ConnectionPool();
        Connection con = pool.getConnection();
        User storedUser = null;
        try {
            con.setAutoCommit(false);//TODO нужно или нет?
            storedUser = userDao.findUser(con, user.getUsername());
            Long balance = userDao.findBalance(con, storedUser.getId());
            storedUser.setBalance(balance);
            con.commit();//TODO нужно или нет?
        } catch (SQLException ex) {
            try {
                con.rollback();//TODO нужно или нет?
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("transaction rollback error ");
            }
            throw new Exception("database access error");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.close(con);
        }
        return storedUser;
    }

    public User pay(User user) throws Exception {
        UserDao userDao = new UserDao();
        ConnectionPool pool = new ConnectionPool();
        Connection con = pool.getConnection();
        User storedUser = null;
        try {
            con.setAutoCommit(false);
            storedUser = userDao.findUser(con, user.getUsername());
            Long balance = userDao.findBalance(con, storedUser.getId());
            long subtotal = balance - paymentUnit();
            if (subtotal <= lowerLimit()) {
                throw  new Exception("There are not enough funds on your account");
            }
            userDao.updateBalance(con, storedUser.getId(), subtotal);
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("transaction rollback error ");
            }
            throw new Exception("database access error");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.close(con);
        }
        return storedUser;
    }

    private boolean comparePasswords(User user, User storedUser) {
        try {
            return PBKDF2.compare(user.getPassword(), storedUser.getPassword());
        } catch (Exception e) {
            System.err.println("failed to hash the password. Re-enter the data for registration");
        }
        return false;
    }

    private String saltPassword(User user) {
        try {
            return PBKDF2.getSaltedHash(user.getPassword());
        } catch (Exception e) {
            System.err.println("failed to hash the password. Re-enter the data for registration");
        }
        return user.getPassword();
    }

    /**
     * To be able to change the initial account balance, payment amount and account balance.
     */
    private long startBalance() {
        Properties property = PropertiesReader.getProperties();
        return Long.parseLong(property.getProperty("payment.startBalance"));
    }

    private long paymentUnit() {
        Properties property = PropertiesReader.getProperties();
        return Long.parseLong(property.getProperty("payment.unit"));
    }

    private long lowerLimit() {
        Properties property = PropertiesReader.getProperties();
        return Long.parseLong(property.getProperty("payment.lowerLimit"));
    }
}