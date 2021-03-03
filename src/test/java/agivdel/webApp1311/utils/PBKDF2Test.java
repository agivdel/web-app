package agivdel.webApp1311.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PBKDF2Test {

    @Test
    void getSaltedHash_hash_not_equals_password() throws Exception {
        String password = "1234";
        String saltedHash = PBKDF2.getSaltedHash(password);

        assertNotEquals(password, saltedHash);
    }

    @Test
    void getSaltedHash_exception_when_password_is_null() {
        Exception exceptionNull = assertThrows(IllegalArgumentException.class, () -> {
            PBKDF2.getSaltedHash(null);
            throw new IllegalArgumentException();
        });

        assertEquals("empty passwords are not supported", exceptionNull.getMessage());
    }

    @Test
    void getSaltedHash_exception_when_password_is_empty() {
        Exception exceptionEmpty = assertThrows(IllegalArgumentException.class, () -> {
            PBKDF2.getSaltedHash("");
            throw new IllegalArgumentException();
        });

        assertEquals("empty passwords are not supported", exceptionEmpty.getMessage());
    }

    @Test
    void compare_exception_when_the_second_parameter_is_String() {
        String password = "1234";
        String passwordTwo = "qwer";
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            PBKDF2.compare(password, passwordTwo);
            throw new IllegalStateException();
        });

        assertEquals("the stored password have the form 'salt$hash'", exception.getMessage());
    }

    @Test
    void getSaltedHash_two_saltedHashes_fro_one_password_are_equals() throws Exception {
        String password = "1234";
        String saltedHashFirst = PBKDF2.getSaltedHash(password);
        String saltedHashSecond = PBKDF2.getSaltedHash(password);

        assertNotEquals(saltedHashFirst, saltedHashSecond);
    }

    @Test
    void compare_two_hashes_from_one_password_are_equals() throws Exception {
        String password = "1234";
        String saltedHash = PBKDF2.getSaltedHash(password);

        assertTrue(PBKDF2.compare(password, saltedHash));
    }
}