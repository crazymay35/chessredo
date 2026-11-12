package client;

import model.GameCreateRequest;
import model.GameJoinRequest;
import model.UserData;
import model.UserLoginRequest;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }
    @BeforeEach
    public void clear() throws ResponseException {
        facade.clear();
    }

    @Test
    public void registerSuccess() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        var authData = facade.register(user);
        assertNotNull(authData.authToken());
        assertEquals("test",authData.username());
    }
    @Test
    public void registerFailureDuplicate() {
        UserData user = new UserData("test","password","test@gmail.com");
        assertThrows(ResponseException.class, () -> {
            facade.register(user);
            facade.register(user);
        });
    }
    @Test
    public void registerFailureSameUsername() {
        UserData user = new UserData("test","password","test@gmail.com");
        UserData other = new UserData("test","other","other@gmail.com");
        assertThrows(ResponseException.class, () -> {
            facade.register(user);
            facade.register(other);
        });
    }

    @Test
    public void loginSuccess() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        facade.register(user);
        UserLoginRequest login = new UserLoginRequest(user.username(),user.password());
        var authData = facade.login(login);
        assertNotNull(authData.authToken());
    }
    @Test
    public void loginFailureUserDNE() {
        UserLoginRequest login = new UserLoginRequest("test","password");
        assertThrows(ResponseException.class, () -> facade.login(login));
    }

    @Test
    public void logoutSuccess() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        var auth = facade.register(user);
        assertNotNull(auth.authToken());
        assertDoesNotThrow(() -> facade.logout(auth.authToken()));
        assertThrows(ResponseException.class, () -> facade.logout(auth.authToken()));
    }
    @Test
    public void logoutFailureAuthDNE() {
        String fakeAuth = "fakeAuth";
        assertThrows(ResponseException.class, () -> facade.logout(fakeAuth));
    }

    @Test
    public void createGameSuccess() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        var authData = facade.register(user);
        GameCreateRequest game = new GameCreateRequest("chess");
        var gameData = facade.createGame(game, authData.authToken());
        assertNotNull(gameData);
    }
    @Test
    public void createGameFailureAuthDNE() {
        GameCreateRequest game = new GameCreateRequest("chess");
        assertThrows(ResponseException.class, () -> facade.createGame(game, "fakeAuth"));
    }
    @Test
    public void createGameFailureGameNull() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        var authData = facade.register(user);
        assertThrows(ResponseException.class, () -> facade.createGame(null, authData.authToken()));
    }

    @Test
    public void joinGameSuccess() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        var authData = facade.register(user);
        GameCreateRequest gameRequest = new GameCreateRequest("chess");
        var gameData = facade.createGame(gameRequest, authData.authToken());
        var joinGame = new GameJoinRequest("BLACK",gameData.gameID());
        assertDoesNotThrow(() -> facade.joinGame(joinGame, authData.authToken()));
    }
    @Test
    public void joinGameFailureColorDNE() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        var authData = facade.register(user);
        GameCreateRequest gameRequest = new GameCreateRequest("chess");
        var gameData = facade.createGame(gameRequest, authData.authToken());
        var joinGame = new GameJoinRequest("color",gameData.gameID());
        assertThrows(ResponseException.class, () -> facade.joinGame(joinGame, authData.authToken()));
    }
    @Test
    public void joinGameFailureColorNull() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        var authData = facade.register(user);
        GameCreateRequest gameRequest = new GameCreateRequest("chess");
        var gameData = facade.createGame(gameRequest, authData.authToken());
        var joinGame = new GameJoinRequest(null,gameData.gameID());
        assertThrows(ResponseException.class, () -> facade.joinGame(joinGame, authData.authToken()));
    }
    @Test
    public void joinGameFailureGameIdDNE() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        var authData = facade.register(user);
        GameCreateRequest gameRequest = new GameCreateRequest("chess");
        facade.createGame(gameRequest, authData.authToken());
        var joinGame = new GameJoinRequest("BLACK",123);
        assertThrows(ResponseException.class, () -> facade.joinGame(joinGame, authData.authToken()));
    }
    @Test
    public void joinGameFailureGameNull() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        var authData = facade.register(user);
        GameCreateRequest gameRequest = new GameCreateRequest("chess");
        assertThrows(ResponseException.class, () -> facade.joinGame(null, authData.authToken()));
    }
    @Test
    public void joinGameFailureAuthDNE() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        var authData = facade.register(user);
        GameCreateRequest gameRequest = new GameCreateRequest("chess");
        var gameData = facade.createGame(gameRequest, authData.authToken());
        var joinGame = new GameJoinRequest("BLACK",gameData.gameID());
        assertThrows(ResponseException.class, () -> facade.joinGame(joinGame, "fakeAuth"));
    }

    @Test
    public void listGamesSuccess() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        var authData = facade.register(user);
        GameCreateRequest gameRequest = new GameCreateRequest("chess");
        facade.createGame(gameRequest, authData.authToken());
        assertNotNull(facade.listGames(authData.authToken()));
    }
    @Test
    public void listGamesSuccessEmpty() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        var authData = facade.register(user);
        var games = facade.listGames(authData.authToken());
        assertNotNull(games);
        assertEquals(0, games.length);
    }
    @Test
    public void listGamesFailure() {
        assertThrows(ResponseException.class, () -> facade.listGames("fakeAuth"));
    }

    /*@Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }*/

}
