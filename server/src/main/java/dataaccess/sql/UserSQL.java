package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.InterfaceUserDAO;
import model.UserData;

import java.sql.*;

public class UserSQL implements InterfaceUserDAO {
    public UserSQL() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection con = DatabaseManager.getConnection()) {
            String createTableStatement =
                    """
                    CREATE TABLE IF NOT EXISTS user (
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        PRIMARY KEY (username)
                    );
                    """;
            try (var statement = con.prepareStatement(createTableStatement)) {
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Error: unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String statement = "SELECT * FROM user WHERE username=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(name,password,email);
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Error: unable to get user: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.setString(1,user.username());
            ps.setString(2, user.password());
            ps.setString(3, user.email());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Error: unable create user: %s", e.getMessage()));
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE user";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Error: unable to clear user table: %s", e.getMessage()));
        }
    }
}
