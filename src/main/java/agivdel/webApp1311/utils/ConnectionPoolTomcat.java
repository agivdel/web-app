package agivdel.webApp1311.utils;

import org.apache.tomcat.jdbc.pool.DataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolTomcat {
    public Connection getConnection() {
        Connection con = null;
        try {
            Context envContext = (Context) new InitialContext().lookup("java:comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("jdbc/webApp1311");
            con = dataSource.getConnection();
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}