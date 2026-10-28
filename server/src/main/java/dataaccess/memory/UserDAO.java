package dataaccess.memory;

import dataaccess.InterfaceUserDAO;
import model.UserData;
import java.util.HashMap;

public class UserDAO implements InterfaceUserDAO {
    private final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void createUser(UserData user) {
        users.put(user.username(),user);
    }

    @Override
    public void clear() {
        users.clear();
    }
}
