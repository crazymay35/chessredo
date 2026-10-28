package dataaccess;

import model.AuthData;
import java.sql.*;

public class AuthSQL implements InterfaceAuthDAO {
    public AuthSQL() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection con = DatabaseManager.getConnection()) {
            String createTableStatement =
                    """
                    CREATE TABLE IF NOT EXISTS auth (
                        authToken VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        PRIMARY KEY (authToken)
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
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        String statement = "SELECT * FROM auth WHERE authToken=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.setString(1, authToken);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String token = rs.getString("authToken");
                    String username = rs.getString("username");
                    return new AuthData(token, username);
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to get authToken: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        String statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.setString(1,auth.authToken());
            ps.setString(2,auth.username());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable create auth: %s", e.getMessage()));
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String statement = "DELETE FROM auth WHERE authToken=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.setString(1, authToken);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to delete auth: %s", e.getMessage()));
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE auth";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to clear auth table: %s", e.getMessage()));
        }
    }
}
