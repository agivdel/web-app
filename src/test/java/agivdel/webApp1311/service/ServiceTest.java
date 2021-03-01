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
        assertTrue(service.signUp(user));
    }

    @Test
    public void SignUp_CorrectStartBalance() throws Exception {
        User user = new User("Bob", "Bob123");
        service.signUp(user);
        Long storedBalance = service.findUser(user).getBalance();
        assertEquals(800, storedBalance);
    }

    @Test
    public void SignUp_CorrectSetSaltedHashPassword() throws Exception {
        User user = new User("Bob", "Bob123");
        service.signUp(user);
        String storedPassword = service.findUser(user).getPassword();
        assertNotEquals("Bob123", storedPassword);
    }

    @Test
    public void findUser_FromEmptyDBReturnsNull() throws Exception {
        User user = new User("Arkady", "ArkadyTheGreat");
        assertNull(service.findUser(user).getUsername());
        assertEquals(0, service.findUser(user).getId());
    }

    @Test
    public void findUser_WhenUserExistsInDB() throws Exception {
        User user = new User("Arkady", "ArkadyTheGreat");
        assertTrue(service.signUp(user));
        assertEquals(user, service.findUser(user));
    }

    @Test
    public void authentication_WithTheSameUserReturnsTrue() throws Exception {
        User user = new User("Shchekn", "SuperDog");
        assertTrue(service.signUp(user));
        assertTrue(service.authentication(user));
    }

    @Test
    public void authentication_WithDifferentUsersReturnsFalse() throws Exception {
        User user = new User("Shchekn", "SuperDog");
        User user2 = new User("Schekn", "SuperDog222");
        assertTrue(service.signUp(user));
        assertFalse(service.authentication(user2));
    }

    @Test
    public void pay_CorrectPayment() throws Exception {
        User user = new User("Bob", "Bob123");
        service.signUp(user);
        service.pay(user);
        Long storedBalance = service.findUser(user).getBalance();
        assertEquals(690, storedBalance);

        service.pay(user);
        Long storedBalance2 = service.findUser(user).getBalance();
        assertEquals(580, storedBalance2);
    }

    @Test
    public void pay_BalanceCannotBeLessThanLowerLimit() throws Exception {
        User user = new User("Bob", "Bob123");
        service.signUp(user);
        int x = 9;
        while (x >= 0) {
            service.pay(user);
            x--;
        }
        Long storedBalance = service.findUser(user).getBalance();
        assertEquals(30, storedBalance);

    }
}