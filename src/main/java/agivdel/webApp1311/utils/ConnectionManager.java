package agivdel.webApp1311.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

    private Properties getProperties() {
        Properties property = new Properties();
        try {
            FileInputStream input = new FileInputStream("src/main/resources/application.properties");
            property.load(input);
        } catch (IOException e) {
            System.err.println("failed to load the file 'application.properties'");
            e.printStackTrace();
        }
        return property;
    }

    public Connection getConnection() {
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
            System.err.println("SQLException: " + e.getSQLState());
        } catch (ClassNotFoundException e) {
            System.err.println("database driver not found");
        }
        return connection;
    }

    public void close(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}