package dataaccess.sql;

import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserSQLTests {
    private UserSQL userSQL;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userSQL = new UserSQL();
        userSQL.clear();
    }
    @AfterEach
    public void tearDown() throws DataAccessException {
        userSQL.clear();
    }
    @Test
    public void databaseUserSQLSuccess() {
        assertNotNull(userSQL);
    }
    @Test
    public void createAndGetUserSuccess() throws DataAccessException {
        UserData userSend = new UserData("username", "password","email");
        userSQL.createUser(userSend);
        UserData userReceive = userSQL.getUser("username");
        assertNotNull(userReceive);
        assertEquals(userSend, userReceive);
    }
    @Test
    public void createUserFailureDuplicate() {
        UserData user = new UserData("username", "password","email");
        assertDoesNotThrow(() -> userSQL.createUser(user));
        assertThrows(DataAccessException.class, () -> userSQL.createUser(user));
    }
    @Test
    public void createUserFailureMissingFields() {
        UserData user = new UserData(null,null,null);
        assertThrows(DataAccessException.class, () -> userSQL.createUser(user));
    }
    @Test
    public void getUserFailureUserDNE() throws DataAccessException {
        String username = "username";
        assertNull(userSQL.getUser(username));
    }
    @Test
    public void getUserFailureUserMissingFields() throws DataAccessException {
        assertNull(userSQL.getUser(null));
    }
    @Test
    public void clearSuccess() throws DataAccessException {
        userSQL.createUser(new UserData("username1", "password1","email1"));
        userSQL.createUser(new UserData("username2", "password2","email2"));
        userSQL.clear();
        assertNull(userSQL.getUser("username1"));
        assertNull(userSQL.getUser("username2"));
    }
}
