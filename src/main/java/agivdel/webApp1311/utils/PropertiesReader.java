package agivdel.webApp1311.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    public static Properties getProperties() {
        Properties property = new Properties();
        try {
            InputStream input = PropertiesReader.class.getClassLoader().getResourceAsStream("application.properties");
            property.load(input);
        } catch (IOException e) {
            System.err.println("failed to load the file 'application.properties'");
            e.printStackTrace();
        }
        return property;
    }
}