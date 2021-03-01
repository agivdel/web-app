package agivdel.webApp1311.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConnectionManagerTest {
    private static Connection con;

    @BeforeEach
    public void init() {
        con = new ConnectionManager().getConnection();
    }

    @AfterEach
    public void close() {
        new ConnectionManager().close(con);
    }

    @Test
    public void getDBConnectionTest() throws SQLException {
        assertTrue(con.isValid(1));
        assertFalse(con.isClosed());
    }
}