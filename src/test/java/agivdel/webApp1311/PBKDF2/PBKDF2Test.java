package agivdel.webApp1311.PBKDF2;

import agivdel.webApp1311.password.PBKDF2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class PBKDF2Test {

    @Test
    public void saltedHashNotEqualsPassword() throws Exception {
        String password = "1234";
        String saltedHash = PBKDF2.getSaltedHash(password);
        Assertions.assertNotEquals(password, saltedHash);
    }

    @Test
    public void twoHashesFromOnePasswordEquals() throws Exception {
        String password = "1234";
        String saltedHash = PBKDF2.getSaltedHash(password);
        Assertions.assertTrue(PBKDF2.compare(password, saltedHash));
    }

    @Test
    public void twoSaltedHashesFromOnePasswordsNotEquals() throws Exception {
        String password = "1234";
        String saltedHashFirst = PBKDF2.getSaltedHash(password);
        String saltedHashSecond = PBKDF2.getSaltedHash(password);
        Assertions.assertNotEquals(saltedHashFirst, saltedHashSecond);
    }
}