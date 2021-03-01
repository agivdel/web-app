package agivdel.webApp1311.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPoolHikariCP {

    public Connection getConnection() {
        Connection con = null;
        try {
            DataSource dataSource = getDatasource();
            con = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public DataSource getDatasource() {
        Properties property = PropertiesReader.getProperties();
        HikariConfig config = new HikariConfig();
        config.setUsername(property.getProperty("database.username"));
        config.setPassword(property.getProperty("database.password"));
        config.setJdbcUrl(property.getProperty("database.url"));
        return new HikariDataSource(config);
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