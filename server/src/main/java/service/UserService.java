package service;

import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {
    private final InterfaceUserDAO userDAO;
    private final InterfaceAuthDAO authDAO;

    public UserService(InterfaceUserDAO userDAO, InterfaceAuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData user) throws Exception {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (userDAO.getUser(user.username()) != null) {
            throw new AlreadyTakenException("Error: username already taken");
        }
        String hashedPassword = BCrypt.hashpw(user.password(),BCrypt.gensalt());
        UserData secureUser = new UserData(user.username(),hashedPassword,user.email());
        userDAO.createUser(secureUser);
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken,user.username());
        authDAO.createAuth(authData);
        return authData;
    }

    public AuthData login(UserLoginRequest user) throws Exception {
        if (user.username() == null || user.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        UserData tryUser = userDAO.getUser(user.username());
        if (tryUser == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (!BCrypt.checkpw(user.password(),tryUser.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken,user.username());
        authDAO.createAuth(authData);
        return authData;
    }

    public void logout(String authToken) throws Exception {
        if (authDAO.getAuthToken(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }
}
