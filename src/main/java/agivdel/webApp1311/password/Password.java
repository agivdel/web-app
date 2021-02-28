package agivdel.webApp1311.password;

public interface Password {
    void adjust(String details);
    String getSaltedHash(String password) throws Exception;
    boolean compare(String password, String stored) throws Exception;
}