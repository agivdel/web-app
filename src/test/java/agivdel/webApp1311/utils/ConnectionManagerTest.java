package agivdel.webApp1311.utils;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConnectionManagerTest {

    @Test
    public void getDBConnectionTest() throws SQLException {
        Connection con = new ConnectionManager().getConnection();

        assertTrue(con.isValid(1));
        assertFalse(con.isClosed());

        new ConnectionManager().close(con);
    }
}