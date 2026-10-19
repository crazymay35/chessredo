package dataaccess;
import  model.UserData;

public interface InterfaceUserDAO {
    UserData getUser(String username);
    void createUser(UserData user);
    void clear();
}
