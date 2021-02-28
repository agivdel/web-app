package agivdel.webApp1311.password;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

public class PBKDF2 {
    private static final int saltLength = 32;
    private static final int keyLength = 512;
    private static final int iterations = 64000;
    private static final String algorithm = "PBKDF2WithHmacSHA1";
    private static final String pseudoRNG = "SHA1PRNG";

    public static String getSaltedHash(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance(pseudoRNG).generateSeed(saltLength);
        return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
    }

    public static boolean compare(String password, String storedPassword) throws Exception {
        String[] saltAndPassword = storedPassword.split("\\$");
        if (saltAndPassword.length != 2) {
            throw new IllegalStateException("The stored password have the form 'salt$hash'");
        }
        String hashOfInput = hash(password, Base64.decodeBase64(saltAndPassword[0]));
        return hashOfInput.equals(saltAndPassword[1]);
    }

    private static String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("Empty passwords are not supported");
        }
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength));
        return Base64.encodeBase64String(key.getEncoded());
    }
}