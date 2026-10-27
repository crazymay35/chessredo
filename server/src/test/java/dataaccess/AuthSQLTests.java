package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthSQLTests {
    private AuthSQL authSQL;

    @BeforeEach
    public void setUp() throws DataAccessException {
        authSQL = new AuthSQL();
        authSQL.clear();
    }
    @AfterEach
    public void tearDown() throws DataAccessException {
        authSQL.clear();
    }
    @Test
    public void setAuthSQLSuccess() {
        assertNotNull(authSQL);
    }
    @Test
    public void createAndGetAuthSuccess() throws DataAccessException {
        AuthData authSend = new AuthData("authToken", "username");
        authSQL.createAuth(authSend);
        AuthData authReceive = authSQL.getAuthToken("authToken");
        assertNotNull(authReceive);
        assertEquals(authSend.authToken(), authReceive.authToken());
        assertEquals(authSend.username(), authReceive.username());
    }
    @Test
    public void createAuthFailureDuplicate() {
        AuthData auth = new AuthData("authToken", "username");
        assertDoesNotThrow(() -> authSQL.createAuth(auth));
        assertThrows(DataAccessException.class, () -> authSQL.createAuth(auth));
    }
    @Test
    public void createAuthFailureMissingFields() {
        AuthData auth = new AuthData(null,null);
        assertThrows(DataAccessException.class, () -> authSQL.createAuth(auth));
    }
    @Test
    public void getAuthFailureAuthDNE() throws DataAccessException {
        String authToken = "authToken";
        assertNull(authSQL.getAuthToken(authToken));
    }
    @Test
    public void getAuthFailureAuthMissingFields() throws DataAccessException {
        assertNull(authSQL.getAuthToken(null));
    }
    @Test
    public void deleteAuthSuccess() throws DataAccessException {
        AuthData authSend = new AuthData("authToken", "username");
        authSQL.createAuth(authSend);
        assertNotNull(authSQL.getAuthToken("authToken"));
        authSQL.deleteAuth("authToken");
        assertNull(authSQL.getAuthToken("authToken"));
    }
    @Test
    public void clearSuccess() throws DataAccessException {
        authSQL.createAuth(new AuthData("authToken1", "username1"));
        authSQL.createAuth(new AuthData("authToken2", "username2"));
        authSQL.clear();
        assertNull(authSQL.getAuthToken("authToken1"));
        assertNull(authSQL.getAuthToken("authToken2"));
    }

}
