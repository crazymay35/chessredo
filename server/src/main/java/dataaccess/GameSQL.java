package dataaccess;

import model.AuthData;
import model.GameData;

import java.sql.*;
import java.util.Collection;
import java.util.List;

public class GameSQL implements InterfaceGameSQL{
    public GameSQL() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection con = DatabaseManager.getConnection()) {
            String createTableStatement =
                    """
                    CREATE TABLE IF NOT EXISTS game (
                        gameID INT NOT NULL,
                        whiteUsername VARCHAR(255),
                        blackUsername VARCHAR(255),
                        gameName VARCHAR(255) NOT NULL,
                        game TEXT NOT NULL,
                        PRIMARY KEY (gameID)
                    );
                    """;
            try (var statement = con.prepareStatement(createTableStatement)) {
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        String statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?)";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.setInt(1,game.gameID());
            ps.setString(2,game.whiteUsername());
            ps.setString(3, game.blackUsername());
            ps.setString(4,game.gameName());
            ps.setString(5, String.valueOf(game.game()));
            ps.executeUpdate();
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("gameID");
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable create game: %s", e.getMessage()));
        }
        return 0;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
