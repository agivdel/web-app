package agivdel.webApp1311.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    public static Connection getConnection() {
        Connection connection = null;
        Properties property = PropertiesReader.getProperties();
        try {
            Class.forName(property.getProperty("database.driverClassName"));
            connection = DriverManager.getConnection(
                    property.getProperty("database.url"),
                    property.getProperty("database.username"),
                    property.getProperty("database.password")
            );
        } catch (SQLException e) {
            System.err.println("0");
            System.err.println("SQLException: " + e.getSQLState());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("database driver not found");
        }
        return connection;
    }
}