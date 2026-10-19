package dataaccess;

import model.AuthData;

public interface InterfaceAuthDAO {
    AuthData getAuthToken(String authToken);
    void createAuth(String authToken);
    void deleteAuth(String authToken);
    void clear();
}
