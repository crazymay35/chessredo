package dataaccess;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

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
    public void createDatabase() {
        String authToken = "testAuth";
        String username = "test";

    }
}
