package agivdel.webApp1311.dao;

import agivdel.webApp1311.entities.Payment;
import agivdel.webApp1311.entities.User;
import agivdel.webApp1311.utils.ConnectionCreator;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class UserDaoTest {
    private static Connection con;
    private static UserDao userDao;

    private void clearDB() throws SQLException {
        con.createStatement().execute("DELETE FROM users");
        con.createStatement().execute("DELETE FROM balances");
        con.createStatement().execute("DELETE FROM payments");
    }

    @BeforeEach
    public void init() throws SQLException {
        userDao = new UserDao();
        con = new ConnectionCreator().getConnection();
        clearDB();
    }

    @AfterEach
    public void close() {
        new ConnectionCreator().close(con);
    }

    @Test
    public void addUserIdEqualsFindUserId() throws Exception {
        User user = new User("Bob", "Bob123");
        userDao.insertUser(con, user.getUsername());

        User storedUser = userDao.selectUser(con, user.getUsername());
        assertEquals(user, storedUser);
    }

    @Test
    public void updatePasswordEqualsFindUserPassword() throws Exception {
        User user = new User("Arkady", "ArkadyTheGreat");
        userDao.insertUser(con, user.getUsername());
        userDao.updatePassword(con, user.getId(), user.getPassword());

        User storedUser = userDao.selectUser(con, user.getUsername());
        assertEquals(user.getPassword(), storedUser.getPassword());
    }

    @Test
    public void updateBalanceEqualsFindBalance() throws Exception {
        User user = new User("Shchekn", "SuperDog");
        userDao.insertUser(con, user.getUsername());
        userDao.insertBalance(con, user.getId(), 800);

        Long storedBalance = userDao.selectBalance(con, user.getId());
        assertEquals(800, storedBalance);
    }

    @Test
    public void insertPaymentEqualsSelectPayment() throws Exception {
        User user = new User("Toyvo", "Luden");
        userDao.insertUser(con, user.getUsername());
        userDao.insertPayment(con, user.getId(), 110L);
        userDao.insertPayment(con, user.getId(), 110L);

        List<Payment> payments = userDao.selectPayments(con, user.getId());
        assertEquals(user.getId(), payments.get(0).getUserId());
        assertEquals(user.getId(), payments.get(1).getUserId());

        assertEquals(110L, payments.get(0).getPayment());
        assertEquals(110L, payments.get(1).getPayment());
    }

    @Test
    public void SelectAllColumnsFromPayments() throws Exception {
        User user = new User("Toyvo", "Luden");
        userDao.insertUser(con, user.getUsername());
        userDao.insertPayment(con, user.getId(), 110L);

        List<Payment> payments = userDao.selectPayments(con, user.getId());
        assertNotEquals(0, payments.get(0).getId());
        assertNotEquals(0, payments.get(0).getUserId());
        assertNotNull(payments.get(0).getPayment());
        assertNotNull(payments.get(0).getPaymentTime());
    }
}