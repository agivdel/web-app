package agivdel.webApp1311.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
    public static Properties getProperties() {
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
}