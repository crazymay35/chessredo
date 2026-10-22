package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTests {
    @Test
    public void clearSuccess() throws Exception {
        AuthDAO authDAO = new AuthDAO();
        UserDAO userDAO = new UserDAO();
        GameDAO gameDAO = new GameDAO();

        Collection<Object> mixed = new ArrayList<>();

        UserService userService = new UserService((UserDAO) userDAO, (AuthDAO) authDAO);
        GameService gameService = new GameService((AuthDAO) authDAO, (GameDAO) gameDAO);

        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        GameCreateRequest gameReq = new GameCreateRequest("game");
        int gameID = gameService.createGame(authData.authToken(),gameReq);

        mixed.add(userDAO.getUser("username"));
        mixed.add(authDAO.getAuthToken(authData.authToken()));
        mixed.add(gameDAO.getGame(gameID));

        for (Object item : mixed) {
            assertNotNull(item);
        }

        ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
        clearService.clear();
        mixed.clear();

        mixed.add(userDAO.getUser("username"));
        mixed.add(authDAO.getAuthToken(authData.authToken()));
        mixed.add(gameDAO.getGame(gameID));

        for (Object item : mixed) {
            assertNull(item);
        }
    }
}
