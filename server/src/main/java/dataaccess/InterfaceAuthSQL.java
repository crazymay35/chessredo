package dataaccess;

import model.AuthData;

public interface InterfaceAuthSQL {
    AuthData getAuthToken(String authToken) throws DataAccessException;
    void createAuth(AuthData auth) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
}
