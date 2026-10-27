package dataaccess.sql;

import dataaccess.DataAccessException;
import model.AuthData;

public class AuthSQL implements InterfaceAuthSQL {

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
