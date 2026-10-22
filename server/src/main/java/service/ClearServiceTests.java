package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTests {
    private UserService userService;
    private GameService gameService;
    private InterfaceUserDAO userDAO;
    private InterfaceAuthDAO authDAO;
    private InterfaceGameDAO gameDAO;

    @BeforeEach
    public void setup() {
        authDAO = new AuthDAO();
        userDAO = new UserDAO();
        gameDAO = new GameDAO();
        userService = new UserService((UserDAO) userDAO, (AuthDAO) authDAO);
        gameService = new GameService((AuthDAO) authDAO, (GameDAO) gameDAO);
    }

    @Test
    public void clearSuccess() throws Exception {
        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        GameCreateRequest gameReq = new GameCreateRequest("game");
        int gameID = gameService.createGame(authData.authToken(),gameReq);
        assertNotNull(userDAO.getUser("username"));
        assertNotNull(authDAO.getAuthToken(authData.authToken()));
        assertNotNull(gameDAO.getGame(gameID));

        ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
        clearService.clear();

        assertNull(userDAO.getUser("username"));
        assertNull(authDAO.getAuthToken(authData.authToken()));
        assertNull(gameDAO.getGame(gameID));
    }
}
