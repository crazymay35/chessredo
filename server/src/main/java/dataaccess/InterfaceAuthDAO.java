package dataaccess;

import model.AuthData;

public interface InterfaceAuthDAO {
    AuthData getAuthToken(String authToken);
    void createAuth(AuthData auth);
    void deleteAuth(String authToken);
    void clear();
}
