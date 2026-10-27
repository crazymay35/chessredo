package dataaccess;

import model.UserData;

public class UserSQL implements InterfaceUserSQL{

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
