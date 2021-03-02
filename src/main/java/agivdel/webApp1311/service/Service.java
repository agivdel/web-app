package agivdel.webApp1311.service;

import agivdel.webApp1311.utils.ConnectionPoolHikariCP;
import agivdel.webApp1311.password.PBKDF2;
import agivdel.webApp1311.utils.PropertiesReader;
import agivdel.webApp1311.dao.UserDao;
import agivdel.webApp1311.entities.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Service {

    interface Transaction<T> {
        T run(Connection connection) throws Exception;
    }



    public boolean authentication(String username, String password) {
        //есть в базе такие логин и соленый пароль?
        User user = findUser(username);
        String saltedHash = saltPassword(password);
        return true;
    }

    public boolean authentication(User user) throws Exception {
        User storedUser = findUser(user);
        return user.equals(storedUser) && comparePasswords(user, storedUser);
    }

    public boolean isUserExists(User user) throws Exception {
        User storedUser = findUser(user);
        return storedUser != null;
    }

    public boolean isUserExists(String username) throws Exception {
        User storedUser = findUser(username);
        return storedUser != null;
    }

    public boolean signUp(User user) throws Exception {
        UserDao userDao = new UserDao();
        ConnectionPoolHikariCP pool = new ConnectionPoolHikariCP();
        Connection con = pool.getConnection();
        try {
            con.setAutoCommit(false);
            userDao.insertUser(con, user);
            userDao.insertBalance(con, user.getId(), startBalance());
            userDao.updatePassword(con, user.getId(), saltPassword(user));
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("transaction rollback error");
            }
            throw new Exception("database access error");
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                throw new Exception("transaction rollback error");
            }
            e.printStackTrace();
        } finally {
            pool.close(con);
        }
        return true;
    }

    public User findUser(String username) {
        return new User();
    }

    public User findUser(User user) throws Exception {
        UserDao userDao = new UserDao();
        ConnectionPoolHikariCP pool = new ConnectionPoolHikariCP();
        Connection con = pool.getConnection();
        User storedUser = null;
        try {
            con.setAutoCommit(false);
            storedUser = userDao.selectUser(con, user.getUsername());
            Long balance = userDao.selectBalance(con, storedUser.getId());
            storedUser.setBalance(balance);
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("transaction rollback error");
            }
            throw new Exception("database access error");
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                throw new Exception("transaction rollback error");
            }
            e.printStackTrace();
        } finally {
            pool.close(con);
        }
        return storedUser;
    }

    public User pay(User user) throws Exception {
        UserDao userDao = new UserDao();
        ConnectionPoolHikariCP pool = new ConnectionPoolHikariCP();
        Connection con = pool.getConnection();
        User storedUser = null;
        try {
            con.setAutoCommit(false);
            storedUser = userDao.selectUser(con, user.getUsername());
            Long balance = userDao.selectBalance(con, storedUser.getId());
            long subtotal = balance - paymentUnit();
            if (subtotal <= lowerLimit()) {
                throw  new Exception("There are not enough funds on your account");
            }
            userDao.updateBalance(con, storedUser.getId(), subtotal);
            userDao.insertPayment(con, storedUser.getId(), paymentUnit());
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("transaction rollback error");
            }
            throw new Exception("database access error");
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                throw new Exception("transaction rollback error");
            }
            e.printStackTrace();
        } finally {
            pool.close(con);
        }
        return storedUser;
    }

    private boolean comparePasswords(String password, String storedPassword) {
        try {
            return PBKDF2.compare(password, storedPassword);
        } catch (Exception e) {
            System.err.println("failed to hash the password. Re-enter the data for registration");
        }
        return false;
    }

    private boolean comparePasswords(User user, User storedUser) {
        try {
            return PBKDF2.compare(user.getPassword(), storedUser.getPassword());
        } catch (Exception e) {
            System.err.println("failed to hash the password. Re-enter the data for registration");
        }
        return false;
    }

    private String saltPassword(String password) {
        try {
            return PBKDF2.getSaltedHash(password);
        } catch (Exception e) {
            e.printStackTrace();//TODO
        }
        return password;
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