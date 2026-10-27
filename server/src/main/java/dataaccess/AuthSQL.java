package dataaccess;

import model.AuthData;
import java.sql.*;

public class AuthSQL implements InterfaceAuthSQL {
    public AuthSQL() throws DataAccessException {

    }

    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
