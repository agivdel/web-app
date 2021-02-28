package agivdel.webApp1311.password;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

public class PBKDF2 implements Password {
    private int saltLength;
    private int keyLength;
    private int iterations;
    private String algorithm;
    private String pseudoRNG;

    public PBKDF2() {
    }

    @Override
    public void adjust(String details) {
        String[] properties = details.split("\\$");
        this.saltLength = Integer.parseInt(properties[0]);
        this.keyLength = Integer.parseInt(properties[1]);
        this.iterations = Integer.parseInt(properties[2]);
        this.algorithm = properties[3];
        this.pseudoRNG = properties[4];
    }

    @Override
    public String getSaltedHash(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance(pseudoRNG).generateSeed(saltLength);
        return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
    }

    @Override
    public boolean compare(String password, String storedPassword) throws Exception {
        String[] saltAndPassword = storedPassword.split("\\$");
        if (saltAndPassword.length != 2) {
            throw new IllegalStateException("The stored password have the form 'salt$hash'");
        }
        String hashOfInput = hash(password, Base64.decodeBase64(saltAndPassword[0]));
        return hashOfInput.equals(saltAndPassword[1]);
    }

    private String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("Empty passwords are not supported");
        }
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength));
        return Base64.encodeBase64String(key.getEncoded());
    }
}