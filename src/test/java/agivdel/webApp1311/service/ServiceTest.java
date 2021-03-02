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
        User user = new User("Bob", "Bob123");
        assertTrue(service.signUp(user.getUsername(), user.getPassword()));
    }

    @Test
    public void SignUp_CorrectStartBalance() throws Exception {
        User user = new User("Bob", "Bob123");
        service.signUp(user.getUsername(), user.getPassword());
        User storedUser = service.findUser(user.getUsername());
        assertEquals(800, storedUser.getBalance());
    }

    @Test
    public void SignUp_CorrectSetSaltedHashPassword() throws Exception {
        User user = new User("Bob", "Bob123");
        service.signUp(user.getUsername(), user.getPassword());
        User storedUser = service.findUser(user.getUsername());
        assertNotEquals("Bob123", storedUser.getPassword());
    }

    @Test
    public void findUser_FromEmptyDBReturnsNull() throws Exception {
        User user = new User("Arkady", "ArkadyTheGreat");
        User storedUser = service.findUser(user.getUsername());
        assertNull(storedUser.getUsername());
        assertEquals(0, storedUser.getId());
    }

    @Test
    public void findUser_WhenUserExistsInDB() throws Exception {
        User user = new User("Arkady", "ArkadyTheGreat");
        assertTrue(service.signUp(user.getUsername(), user.getPassword()));
        assertEquals(user, service.findUser(user.getUsername()));
    }

    @Test
    public void authentication_WithTheSameUserReturnsTrue() throws Exception {
        User user = new User("Shchekn", "SuperDog");
        assertTrue(service.signUp(user.getUsername(), user.getPassword()));
        assertTrue(service.authentication(user.getUsername(), user.getPassword()));
    }

    @Test
    public void authentication_WithDifferentUsersReturnsFalse() throws Exception {
        User user = new User("Shchekn", "SuperDog");
        User user2 = new User("Schekn", "SuperDog222");
        assertTrue(service.signUp(user.getUsername(), user.getPassword()));
        assertFalse(service.authentication(user2.getUsername(), user2.getPassword()));
    }

    @Test
    public void pay_CorrectPayment() throws Exception {
        User user = new User("Bob", "Bob123");
        service.signUp(user.getUsername(), user.getPassword());
        service.pay(user.getUsername());
        Long storedBalance = service.findUser(user.getUsername()).getBalance();
        assertEquals(690, storedBalance);

        service.pay(user.getUsername());
        Long storedBalance2 = service.findUser(user.getUsername()).getBalance();
        assertEquals(580, storedBalance2);
    }

    @Test
    public void pay_BalanceCannotBeLessThanLowerLimit() throws Exception {
        User user = new User("Bob", "Bob123");
        service.signUp(user.getUsername(), user.getPassword());
        long lowerLimit = 0;
        long startBalance = 800;
        long paymentUnit = 110;
        int x = 10;
        while (x >= 0) {
            service.pay(user.getUsername());
            x--;
        }
        long balance = (startBalance - lowerLimit) % paymentUnit;
        Long storedBalance = service.findUser(user.getUsername()).getBalance();
        assertEquals(balance, storedBalance);
    }
}