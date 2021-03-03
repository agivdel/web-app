package agivdel.webApp1311.service;

import agivdel.webApp1311.entities.Balance;
import agivdel.webApp1311.utils.ConnectionCreator;
import agivdel.webApp1311.utils.ConnectionPoolHikariCP;
import agivdel.webApp1311.password.PBKDF2;
import agivdel.webApp1311.utils.ConnectionPoolTomcat;
import agivdel.webApp1311.utils.PropertiesReader;
import agivdel.webApp1311.dao.UserDao;
import agivdel.webApp1311.entities.User;
import org.apache.tomcat.jdbc.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Service {

    public boolean authentication(String username, String password) throws Exception {
        User storedUser = findUser(username);
        return PBKDF2.compare(password, storedUser.getPassword());
    }

    public boolean isUserExists(String username) throws Exception {
        User storedUser = findUser(username);
        return storedUser != null;
    }

    public boolean signUp(String username, String password) throws Exception {//TODO возвращать id?
        UserDao userDao = new UserDao();
        return doTransaction(con -> {
            int userId = userDao.insertUser(con, username);
            userDao.insertBalance(con, userId, startBalance());
            String saltedHash = PBKDF2.getSaltedHash(password);
            userDao.updatePassword(con, userId, saltedHash);
            return true;
        });
    }

    public User findUser(String username) throws Exception {
        UserDao userDao = new UserDao();
        return doTransaction(con -> userDao.selectUser(con, username));
    }

    public Balance findBalance(int userId) throws Exception {
        UserDao userDao = new UserDao();
        return doTransaction(con -> userDao.selectBalance(con, userId));
    }

    public long pay(String username) throws Exception {//TODO заменить на int userId?
        UserDao userDao = new UserDao();
        return doTransaction(con -> {
            User storedUser = userDao.selectUser(con, username);
            Balance storedBalance = userDao.selectBalance(con, storedUser.getId());
            long subtotal = storedBalance.getValue() - paymentUnit();
            if (subtotal <= lowerLimit()) {
                throw  new Exception("there are not enough funds on your account");
            }
            userDao.updateBalance(con, storedUser.getId(), subtotal);
            userDao.insertPayment(con, storedUser.getId(), paymentUnit());
            return subtotal;
        });
    }

    interface Transaction<T> {
        T run(Connection con) throws Exception;
    }

    private <T> T doTransaction(Transaction<T> transaction) throws Exception {
//        ConnectionPoolHikariCP pool = new ConnectionPoolHikariCP();//TODO проблемы с пулом
//        ConnectionPoolTomcat pool = new ConnectionPoolTomcat();
//        Connection con = pool.getConnection();
        ConnectionCreator creator = new ConnectionCreator();//
        Connection con = creator.getConnection();
        try {
            con.setAutoCommit(false);
            T result = transaction.run(con);
            con.commit();
            return result;
        } catch (Exception e) {
            Exception exception = new Exception(e.getMessage());
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                exception.addSuppressed(ex);
            }
            throw exception;
        } finally {
//            pool.close(con);
            creator.close(con);//
        }
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