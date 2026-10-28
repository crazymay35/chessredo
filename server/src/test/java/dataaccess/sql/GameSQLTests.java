package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @Test
    public void createAndGetGameSuccess() throws DataAccessException {
        GameData gameSend = new GameData(1,"white","black","name", new ChessGame());
        gameSQL.createGame(gameSend);
        GameData gameReceive = gameSQL.getGame(1);
        assertNotNull(gameReceive);
        assertEquals(gameSend,gameReceive);
    }
    @Test
    public void createGameFailureDuplicate() {
        GameData game = new GameData(1,"white","black","name", new ChessGame());
        assertDoesNotThrow(() -> gameSQL.createGame(game));
        assertThrows(DataAccessException.class, () -> gameSQL.createGame(game));
    }
    @Test
    public void createGameFailureMissingFields() {
        GameData game = new GameData(0,null,null,null, null);
        assertThrows(DataAccessException.class, () -> gameSQL.createGame(game));
    }
    @Test
    public void getGameFailureGameDNE() throws DataAccessException {
        int gameID = 0;
        assertNull(gameSQL.getGame(gameID));
    }
    @Test
    public void listGameSuccess() throws DataAccessException {
        GameData game1 = new GameData(1,"white1","black1","name1", new ChessGame());
        GameData game2 = new GameData(2,"white2","black2","name2", new ChessGame());
        gameSQL.createGame(game1);
        gameSQL.createGame(game2);
        Collection<GameData> games = gameSQL.listGames();
        assertEquals(2, games.size());
        assertTrue(games.contains(game1));
        assertTrue(games.contains(game2));
    }
    @Test
    public void listGameSuccessEmpty() throws DataAccessException {
        Collection<GameData> games = gameSQL.listGames();
        assertNotNull(games);
        assertTrue(games.isEmpty());
    }
    @Test
    public void updateGameSuccess() throws DataAccessException {
        GameData game = new GameData(1,null,null,"name", new ChessGame());
        gameSQL.createGame(game);
        GameData newGame = new GameData(1,"white","black","newName", new ChessGame());
        gameSQL.updateGame(newGame);
        GameData returnGame = gameSQL.getGame(1);
        assertEquals("white", returnGame.whiteUsername());
        assertEquals("black", returnGame.blackUsername());
        assertEquals("newName", returnGame.gameName());
    }
    @Test
    public void clearSuccess() throws DataAccessException {
        gameSQL.createGame(new GameData(1,"white1","black1","name1", new ChessGame()));
        gameSQL.createGame(new GameData(2,"white2","black2","name2", new ChessGame()));
        gameSQL.clear();
        assertNull(gameSQL.getGame(1));
        assertNull(gameSQL.getGame(2));
    }
}
