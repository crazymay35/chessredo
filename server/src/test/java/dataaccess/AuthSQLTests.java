package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthSQLTests {
    private AuthSQL authSQL;

    @BeforeEach
    public void setUp() throws DataAccessException {
        authSQL = new AuthSQL();
        //authSQL.clear();
    }
    @AfterEach
    public void tearDown() throws DataAccessException {
        //authSQL.clear();
    }
    @Test
    public void setAuthSQLSuccess() {
        assertNotNull(authSQL);
    }

    @Test
    public void createAuthSuccess() throws DataAccessException {
        AuthData authSend = new AuthData("authToken", "username");
        authSQL.createAuth(authSend);

    }
}
