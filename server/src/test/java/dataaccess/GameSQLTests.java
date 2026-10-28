package dataaccess;

import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
