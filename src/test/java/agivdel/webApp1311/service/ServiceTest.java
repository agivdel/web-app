package agivdel.webApp1311.service;

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
        assertEquals(800, storedUser.getBalance());
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
        service.pay(username);
        User storedUser = service.findUser(username);
        Long storedBalance = storedUser.getBalance();
        assertEquals(690, storedBalance);

        service.pay(username);
        storedUser = service.findUser(username);
        storedBalance = storedUser.getBalance();
        assertEquals(580, storedBalance);
    }

    @Test
    public void pay_BalanceCannotBeLessThanLowerLimit() throws Exception {
        String username = "Bob";
        String password = "Bob123";
        service.signUp(username, password);
        long lowerLimit = 0;
        long startBalance = 800;
        long paymentUnit = 110;
        int x = 10;
        while (x >= 0) {
            service.pay(username);
            x--;
        }
        long balance = (startBalance - lowerLimit) % paymentUnit;
        Long storedBalance = service.findUser(username).getBalance();
        assertEquals(balance, storedBalance);
    }
}