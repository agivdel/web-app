package agivdel.webApp1311.service;

import agivdel.webApp1311.entities.Balance;
import agivdel.webApp1311.entities.User;
import agivdel.webApp1311.password.PBKDF2;
import agivdel.webApp1311.utils.ConnectionCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

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
    public void signUp_return_true_after_signup() throws Exception {
        String username = "Bob";
        String password = "Bob123";

        assertTrue(service.signUp(username, password));
    }

    @Test
    public void signUp_correct_start_balance() throws Exception {
        String username = "Bob";
        String password = "Bob123";
        service.signUp(username, password);
        User storedUser = service.findUser(username);
        Balance storedBalance = service.findBalance(storedUser.getId());

        assertEquals(800, storedBalance.getValue());
    }

    @Test
    public void signUp_inserted_saltedHash_not_equals_password() throws Exception {
        String username = "Bob";
        String password = "Bob123";
        service.signUp(username, password);
        User storedUser = service.findUser(username);

        assertNotEquals("Bob123", storedUser.getPassword());
    }

    @Test
    public void findUser_returns_null_from_empty_DB() throws Exception {
        String username = "Bob";
        User storedUser = service.findUser(username);

        assertNull(storedUser);
    }

    @Test
    public void findUser_returns_User_when_it_exists_in_DB() throws Exception {
        String username = "Arkady";
        String password = "ArkadyTheGreat3";
        service.signUp(username, password);
        User storedUser = service.findUser(username);
        boolean isEqual = PBKDF2.compare(password, storedUser.getPassword());

        assertEquals(username, storedUser.getUsername());
        assertTrue(isEqual);
    }

    @Test
    public void authentication_returns_true_for_the_same_username_and_password() throws Exception {
        String username = "Shchekn";
        String password = "SuperDog";

        assertTrue(service.signUp(username, password));
        assertTrue(service.authentication(username, password));
    }

    @Test
    public void authentication_returns_false_for_the_same_username_and_other_password() throws Exception {
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

        long afterFirstPayment = service.pay(username);
        Balance storedBalance = service.findBalance(userId);
        assertEquals(afterFirstPayment, storedBalance.getValue());

        long afterSecondPayment = service.pay(username);
        storedBalance = service.findBalance(userId);
        assertEquals(afterSecondPayment, storedBalance.getValue());
    }

    @Test
    public void pay_balance_cannot_be_lower_than_the_lowerLimit() throws Exception {
        String username = "Bob";
        String password = "Bob123";
        service.signUp(username, password);

        long startBalance = 800;
        long unit = 110;
        long lowerLimit = 0;

        String path = "src/main/resources/applicationBlank.properties";

        try {
            Properties property = new Properties();
            property.load(new FileInputStream(path));
            property.setProperty("payment.startBalance", String.valueOf(startBalance));
            property.setProperty("payment.unit", String.valueOf(unit));
            property.setProperty("payment.lowerLimit", String.valueOf(lowerLimit));
            property.store(new FileOutputStream(path), "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        long balance = 0;
        for (int i = 0; i < 10; i++) {
            balance = service.pay(username);
        }

        assertEquals(balance, startBalance % unit);
    }
}