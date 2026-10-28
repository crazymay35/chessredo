package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class GameSQL implements InterfaceGameSQL{
    private static final Gson GSON = new Gson();
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
            throw new DataAccessException(String.format("unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String statement = "SELECT * FROM game WHERE gameID=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.setInt(1, gameID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    String jsonGame = rs.getString("game");
                    ChessGame game = GSON.fromJson(jsonGame, ChessGame.class);
                    return new GameData(id,whiteUsername,blackUsername,gameName,game);
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to get game: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> games = new ArrayList<>();
        String statement = "SELECT * FROM game";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                String jsonGame = rs.getString("game");
                ChessGame game = GSON.fromJson(jsonGame, ChessGame.class);
                games.add(new GameData(id,whiteUsername,blackUsername,gameName,game));
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable list games: %s", e.getMessage()));
        }
        return games;
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        String statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.setInt(1,game.gameID());
            ps.setString(2,game.whiteUsername());
            ps.setString(3, game.blackUsername());
            ps.setString(4,game.gameName());
            ps.setString(5, GSON.toJson(game.game()));
            ps.executeUpdate();
            return game.gameID();
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable create game: %s", e.getMessage()));
        }
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        String statement = "UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.setString(1,game.whiteUsername());
            ps.setString(2, game.blackUsername());
            ps.setString(3,game.gameName());
            ps.setString(4, GSON.toJson(game.game()));
            ps.setInt(5,game.gameID());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update game: %s", e.getMessage()));
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE game";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to clear game table: %s", e.getMessage()));
        }
    }
}
