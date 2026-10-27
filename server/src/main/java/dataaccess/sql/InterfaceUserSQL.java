package dataaccess.sql;

import dataaccess.DataAccessException;
import model.UserData;

public interface InterfaceUserSQL {
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    void clear() throws DataAccessException;
}
