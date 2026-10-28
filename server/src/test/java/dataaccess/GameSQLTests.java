package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameSQLTests {
    private GameSQL gameSQL;

    @BeforeEach
    public void setUp() throws DataAccessException {
        gameSQL = new GameSQL();
        gameSQL.clear();
    }
    @AfterEach
    public void tearDown() throws DataAccessException {
        gameSQL.clear();
    }
    @Test
    public void databaseAuthSQLSuccess() {
        assertNotNull(gameSQL);
    }
    /*@Test
    /*public void createAndGetGameSuccess() throws DataAccessException {
        AuthData authSend = new AuthData("authToken", "username");
        authSQL.createAuth(authSend);
        AuthData authReceive = authSQL.getAuthToken("authToken");
        assertNotNull(authReceive);
        assertEquals(authSend.authToken(), authReceive.authToken());
        assertEquals(authSend.username(), authReceive.username());
    }*/
    /*public void create() {
        ChessGame game = new ChessGame();
        GameData gameSend = new GameData(1,"white","black","name", game);
    }*/
}
