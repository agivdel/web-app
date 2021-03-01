package agivdel.webApp1311.dao;

import agivdel.webApp1311.entities.User;
import agivdel.webApp1311.utils.ConnectionManager;
import org.junit.jupiter.api.*;

import java.sql.Connection;

public class UserDaoTest {
    private static Connection con;
    private static UserDao userDao;

    @BeforeEach
    public void init() {
        userDao = new UserDao();
        con = new ConnectionManager().getConnection();
    }

    @AfterEach
    public void close() {
        new ConnectionManager().close(con);
    }

    @Test
    public void addUserIdEqualsFindUserId() throws Exception {
        User user = new User("Bob", "Bob123");
        userDao.insertUser(con, user);

        User storedUser = userDao.selectUser(con, user.getUsername());
        Assertions.assertEquals(user, storedUser);
    }

    @Test
    public void updatePasswordEqualsFindUserPassword() throws Exception {
        User user = new User("Arkady", "ArkadyTheGreat");
        userDao.insertUser(con, user);
        userDao.updatePassword(con, user.getId(), user.getPassword());

        User storedUser = userDao.selectUser(con, user.getUsername());
        Assertions.assertEquals(user.getPassword(), storedUser.getPassword());
    }

    @Test
    public void updateBalanceEqualsFindBalance() throws Exception {
        User user = new User("Shchekn", "SuperDog");
        userDao.insertUser(con, user);
        userDao.insertBalance(con, user.getId(), 800);

        Long storedBalance = userDao.selectBalance(con, user.getId());
        Assertions.assertEquals(800, storedBalance);
    }
}