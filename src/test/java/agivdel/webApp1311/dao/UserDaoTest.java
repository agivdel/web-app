package agivdel.webApp1311.dao;

import agivdel.webApp1311.entities.Payment;
import agivdel.webApp1311.entities.User;
import agivdel.webApp1311.password.PBKDF2;
import agivdel.webApp1311.utils.ConnectionCreator;
import org.junit.jupiter.api.*;

import javax.naming.NameNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class UserDaoTest {
    private static Connection con;
    private static UserDao userDao;

//    @Rule
//    public ExpectedException expectedEx = ExpectedException.none();
//    private void expectedException(String message) {
//        expectedEx.expect(IllegalArgumentException.class);
//        expectedEx.expectMessage(message);
//    }
//    expectedException(String.format("The book.id %d already added.", book1.id));


    private void clearDB() throws SQLException {
        con.createStatement().execute("DELETE FROM users");
        con.createStatement().execute("DELETE FROM balances");
        con.createStatement().execute("DELETE FROM payments");
    }

    @BeforeEach
    void init() throws SQLException {
        userDao = new UserDao();
        con = new ConnectionCreator().getConnection();
        clearDB();
    }

    @AfterEach
    void close() {
        new ConnectionCreator().close(con);
    }

    @Test
    void addUser_return_positive_Int() throws Exception {
        String username = "Bob";
        int userId = userDao.insertUser(con, username);

        assertTrue(userId >= 0);
    }

    @Test
    void updatePassword_selectUser_return_the_same_Id_and_password() throws Exception {
        String username = "Bob";
        String password = "Bob123";
        int userId = userDao.insertUser(con, username);
        userDao.updatePassword(con, userId, password);
        User storedUser = userDao.selectUser(con, username);

        assertEquals(userId, storedUser.getId());
        assertEquals(storedUser.getPassword(), password);
    }

    @Test
    void selectUser_exception_when_user_not_found_in_database() {
        String username = "Bob";
        Exception exception = assertThrows(NameNotFoundException.class, () -> {
            userDao.selectUser(con, username);
            throw new NameNotFoundException();
        });

        assertEquals("username '" + username + "' was not found", exception.getMessage());
    }

    @Test
    void insertBalance_updateBalance_return_the_same_value() throws Exception {
        String username = "Bob";
        int userId = userDao.insertUser(con, username);
        userDao.insertBalance(con, userId, 800);
        Long storedBalance = userDao.selectBalance(con, userId).getValue();

        assertEquals(800, storedBalance);
    }

    @Test
    void selectBalance_exception_when_userId_not_found_in_database() {
        int userId = 1;
        Exception exception = assertThrows(NameNotFoundException.class, () -> {
            userDao.selectBalance(con, userId);
            throw new NameNotFoundException();
        });

        assertEquals("userId " + userId + " was not found", exception.getMessage());
    }

    @Test
    void insertPayments_updatePayments_return_the_same_value() throws Exception {
        String username = "Bob";
        int userId = userDao.insertUser(con, username);
        userDao.insertPayment(con, userId, 110);
        userDao.insertPayment(con, userId, 110);
        List<Payment> payments = userDao.selectPayments(con, userId);

        assertEquals(userId, payments.get(0).getUserId());
        assertEquals(userId, payments.get(1).getUserId());
        assertEquals(110, payments.get(0).getPayment());
        assertEquals(110, payments.get(1).getPayment());
    }

    @Test
    void SelectAllColumnsFromPayments() throws Exception {
        String username = "Bob";
        int userId = userDao.insertUser(con, username);
        int paymentId = userDao.insertPayment(con, userId, 110);
        List<Payment> payments = userDao.selectPayments(con, userId);

        assertEquals(paymentId, payments.get(0).getId());
        assertEquals(userId, payments.get(0).getUserId());
        assertEquals(110, payments.get(0).getPayment());
        assertNotNull(payments.get(0).getPaymentTime());
    }
}