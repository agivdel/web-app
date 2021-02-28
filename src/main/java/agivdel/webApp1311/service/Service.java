package agivdel.webApp1311.service;

import agivdel.webApp1311.password.PBKDF2;
import agivdel.webApp1311.utils.PropertiesManager;
import agivdel.webApp1311.dao.UserDao;
import agivdel.webApp1311.entities.User;

import java.util.Properties;

public class Service {
    public boolean authentication(User user) {
        User storedUser = getUserByName(user);
        return user.equals(storedUser) && comparePasswords(user, storedUser);
    }

    public boolean isUserExists(User user) {
        User storedUser = getUserByName(user);
        return storedUser.getId() != 0;
    }

    public User getUserByName(User user) {
        return new UserDao().findUser(user.getUsername());
    }

    public boolean signUp(User user) {
        user.setPassword(saltPassword(user));
        return new UserDao().addUser(user, startBalance());
    }

    public long pay(User user) {
        long newBalance = new UserDao().pay(user, paymentUnit(), lowerLimit());
        if (newBalance == -1) {
            throw new IllegalArgumentException("There are not enough funds on your account ");
        }
        return newBalance;
    }

    private boolean comparePasswords(User user, User storedUser) {
        try {
            return PBKDF2.compare(user.getPassword(), storedUser.getPassword());
        } catch (Exception e) {
            System.err.println("failed to hash the password. Re-enter the data for registration");
        }
        return false;
    }

    private String saltPassword(User user) {
        try {
            return PBKDF2.getSaltedHash(user.getPassword());
        } catch (Exception e) {
            System.err.println("failed to hash the password. Re-enter the data for registration");
        }
        return user.getPassword();
    }


    /**
     * To be able to change the initial account balance, payment amount and account balance.
     */
    private long startBalance() {
        Properties property = PropertiesManager.getProperties();
        return Long.parseLong(property.getProperty("payment.startBalance"));
    }

    private long paymentUnit() {
        Properties property = PropertiesManager.getProperties();
        return Long.parseLong(property.getProperty("payment.unit"));
    }

    private long lowerLimit() {
        Properties property = PropertiesManager.getProperties();
        return Long.parseLong(property.getProperty("payment.lowerLimit"));
    }
}