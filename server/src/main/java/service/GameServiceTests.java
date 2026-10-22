package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    private UserService userService;
    private GameService gameService;
    private InterfaceAuthDAO authDAO;
    private InterfaceUserDAO userDAO;
    private InterfaceGameDAO gameDAO;

    @BeforeEach
    public void setup() {
        authDAO = new AuthDAO();
        userDAO = new UserDAO();
        gameDAO = new GameDAO();
        userService = new UserService((UserDAO) userDAO, (AuthDAO) authDAO);
        gameService = new GameService((AuthDAO) authDAO, (GameDAO) gameDAO);
    }

    //list games
    @Test
    public void listEmptyGamesSuccess() throws Exception {
        //UserData sendUser = new UserData("username","password","user@gmail.com");
        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        gameService.listGames(authData.authToken());
    }
    @Test
    public void listGameSuccess() throws Exception {
        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        GameCreateRequest gameReq1 = new GameCreateRequest("game1");
        GameCreateRequest gameReq2 = new GameCreateRequest("game2");
        GameCreateRequest gameReq3 = new GameCreateRequest("game3");
        gameService.createGame(authData.authToken(),gameReq1);
        gameService.createGame(authData.authToken(),gameReq2);
        gameService.createGame(authData.authToken(),gameReq3);
        Collection<GameData> gamesList = gameService.listGames(authData.authToken());
        assertNotNull(gamesList);
        assertEquals(3,gamesList.size());
    }
    @Test
    public void listGameFailureInvalidAuthToken() {
        AuthData fakeAuth = new AuthData("authToken","username");
        assertThrows(UnauthorizedException.class, () ->  {
            gameService.listGames(fakeAuth.authToken());
        });
    }

    //create game
    @Test
    public void createGameSuccess() throws Exception {
        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        GameCreateRequest gameRequest = new GameCreateRequest("game");
        int gameID = gameService.createGame(authData.authToken(),gameRequest);
        assertTrue(gameID > 0);
        assertNotNull(gameDAO.getGame(gameID));
        assertEquals("game", gameDAO.getGame(gameID).gameName());
    }
    @Test
    public void createGameFailureInvalidAuthToken() {
        AuthData fakeAuth = new AuthData("authToken","username");
        GameCreateRequest gameRequest = new GameCreateRequest("game");
        assertThrows(UnauthorizedException.class, () ->  {
            gameService.createGame(fakeAuth.authToken(),gameRequest);
        });
    }
    @Test
    public void CreateGameFailureMissingFields() throws Exception {
        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        GameCreateRequest gameRequest = new GameCreateRequest(null);
        assertThrows(BadRequestException.class, ()-> {
            gameService.createGame(authData.authToken(),gameRequest);
        });
    }

    @Test
    public void JoinGameSuccessWhite() throws Exception {
        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        GameCreateRequest gameCreate = new GameCreateRequest("game");
        int gameID = gameService.createGame(authData.authToken(),gameCreate);
        GameJoinRequest gameJoin = new GameJoinRequest("WHITE",gameID);
        gameService.joinGame(authData.authToken(), gameJoin);
        GameData updated = gameDAO.getGame(gameID);
        assertEquals("username",updated.whiteUsername());
    }
    @Test
    public void JoinGameSuccessBlack() throws Exception {
        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        GameCreateRequest gameCreate = new GameCreateRequest("game");
        int gameID = gameService.createGame(authData.authToken(),gameCreate);
        GameJoinRequest gameJoin = new GameJoinRequest("BLACK",gameID);
        gameService.joinGame(authData.authToken(), gameJoin);
        GameData updated = gameDAO.getGame(gameID);
        assertEquals("username",updated.blackUsername());
    }
    @Test
    void JoinGameFailureInvalidAuthToken() throws Exception {
        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        AuthData fakeAuth = new AuthData("authToken","username");
        GameCreateRequest gameCreate = new GameCreateRequest("game");
        int gameID = gameService.createGame(authData.authToken(),gameCreate);
        GameJoinRequest gameRequest = new GameJoinRequest("WHITE",gameID);
        assertThrows(UnauthorizedException.class, () ->  {
            gameService.joinGame(fakeAuth.authToken(),gameRequest);
        });
    }
    @Test
    void joinGameFailureMissingFields() throws Exception {
        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        GameCreateRequest gameCreate = new GameCreateRequest("game");
        gameService.createGame(authData.authToken(),gameCreate);
        GameJoinRequest gameRequest = new GameJoinRequest(null,0);
        assertThrows(BadRequestException.class, () ->  {
            gameService.joinGame(authData.authToken(),gameRequest);
        });
    }
    @Test
    public void joinGameFailureGameDNE() throws Exception {
        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        GameCreateRequest gameCreate = new GameCreateRequest("game");
        gameService.createGame(authData.authToken(),gameCreate);
        GameJoinRequest gameRequest = new GameJoinRequest("BLACK",0);
        assertThrows(BadRequestException.class, () ->  {
            gameService.joinGame(authData.authToken(),gameRequest);
        });
    }
    @Test
    public void joinGameFailureGameWhiteTaken() throws Exception {
        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        GameCreateRequest gameCreate = new GameCreateRequest("game");
        int gameID = gameService.createGame(authData.authToken(),gameCreate);
        GameJoinRequest gameJoin = new GameJoinRequest("WHITE",gameID);
        gameService.joinGame(authData.authToken(), gameJoin);
        assertThrows(AlreadyTakenException.class, ()-> {
            gameService.joinGame(authData.authToken(), gameJoin);
        });
    }
    @Test
    public void joinGameFailureGameBlackTaken() throws Exception {
        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        GameCreateRequest gameCreate = new GameCreateRequest("game");
        int gameID = gameService.createGame(authData.authToken(),gameCreate);
        GameJoinRequest gameJoin = new GameJoinRequest("BLACK",gameID);
        gameService.joinGame(authData.authToken(), gameJoin);
        assertThrows(AlreadyTakenException.class, ()-> {
            gameService.joinGame(authData.authToken(), gameJoin);
        });
    }
    @Test
    public void joinGameFailureGameFieldWrong() throws Exception {
        AuthData authData = userService.register(new UserData("username","password","user@gmail.com"));
        GameCreateRequest gameCreate = new GameCreateRequest("game");
        int gameID = gameService.createGame(authData.authToken(),gameCreate);
        GameJoinRequest gameJoin = new GameJoinRequest("color",gameID);
        assertThrows(BadRequestException.class, ()-> {
            gameService.joinGame(authData.authToken(), gameJoin);
        });
    }
}
