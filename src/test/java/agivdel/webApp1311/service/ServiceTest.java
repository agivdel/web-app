package agivdel.webApp1311.service;

import agivdel.webApp1311.entities.Balance;
import agivdel.webApp1311.entities.User;
import agivdel.webApp1311.utils.ConnectionCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
    private Connection con;
    private Service service;

    private void clearDB() throws SQLException {
        con.createStatement().execute("DELETE FROM users");
        con.createStatement().execute("DELETE FROM balances");
        con.createStatement().execute("DELETE FROM payments");
    }

    @BeforeEach
    public void init() throws SQLException {
        service = new Service();
        con = new ConnectionCreator().getConnection();
        clearDB();
    }

    @AfterEach
    public void close() {
        new ConnectionCreator().close(con);
    }


    @Test
    public void SignUp_CorrectReturnTrue() throws Exception {
        String username = "Bob";
        String password = "Bob123";
        assertTrue(service.signUp(username, password));
    }

    @Test
    public void SignUp_CorrectStartBalance() throws Exception {
        String username = "Bob";
        String password = "Bob123";
        service.signUp(username, password);
        User storedUser = service.findUser(username);
        Balance storedBalance = service.findBalance(storedUser.getId());
        assertEquals(800, storedBalance.getValue());
    }

    @Test
    public void SignUp_CorrectSetSaltedHashPassword() throws Exception {
        String username = "Bob";
        String password = "Bob123";
        service.signUp(username, password);
        User storedUser = service.findUser(username);
        assertNotEquals("Bob123", storedUser.getPassword());
    }

    @Test
    public void findUser_FromEmptyDBReturnsNull() throws Exception {
        String username = "Arkady";
        String password = "ArkadyTheGreat3";
        User storedUser = service.findUser(username);
        assertNull(storedUser.getUsername());
        assertEquals(0, storedUser.getId());
    }

    @Test
    public void findUser_WhenUserExistsInDB() throws Exception {
        String username = "Arkady";
        String password = "ArkadyTheGreat3";
        User user = new User(username, password);
        service.signUp(username, password);
        User storedUser = service.findUser(username);
        assertEquals(user, storedUser);
    }

    @Test
    public void authentication_WithTheSameUserReturnsTrue() throws Exception {
        String username = "Shchekn";
        String password = "SuperDog";
        assertTrue(service.signUp(username, password));
        assertTrue(service.authentication(username, password));
    }

    @Test
    public void authentication_WithDifferentUsersReturnsFalse() throws Exception {
        String username = "Shchekn";
        String password = "SuperDog";
        assertTrue(service.signUp(username, password));
        String otherPassword = "SuperDog222";
        assertFalse(service.authentication(username, otherPassword));
    }

    @Test
    public void pay_CorrectPayment() throws Exception {
        String username = "Bob";
        String password = "Bob123";
        service.signUp(username, password);
        int userId = service.findUser(username).getId();

        service.pay(username);
        Balance storedBalance = service.findBalance(userId);
        assertEquals(690, storedBalance.getValue());

        service.pay(username);
        storedBalance = service.findBalance(userId);
        assertEquals(580, storedBalance.getValue());
    }

    @Test
    public void pay_BalanceCannotBeLessThanLowerLimit() throws Exception {
        String username = "Bob";
        String password = "Bob123";
        service.signUp(username, password);
        User storedUser = service.findUser(username);

        long lowerLimit = 0;
        long startBalance = 800;
        long paymentUnit = 110;
        int x = 10;
        while (x >= 0) {
            service.pay(username);
            x--;
        }
        long balance = (startBalance - lowerLimit) % paymentUnit;
        long storedBalance = service.findBalance(storedUser.getId()).getValue();
        assertEquals(balance, storedBalance);
    }
}