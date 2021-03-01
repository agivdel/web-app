package agivdel.webApp1311.utils;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConnectionPoolTest {

    @Test
    public void getTomcatConnectionPoolTest() throws SQLException {
        Connection con = new ConnectionPoolTomcat().getConnection();

        assertTrue(con.isValid(1));
        assertFalse(con.isClosed());

        new ConnectionPoolTomcat().close(con);
    }

    @Test
    public void getHikariConnectionPoolTest() throws SQLException {
        Connection con = new ConnectionPoolHikariCP().getConnection();

        assertTrue(con.isValid(1));
        assertFalse(con.isClosed());

        new ConnectionPoolHikariCP().close(con);

    }
}