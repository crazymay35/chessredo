package client;

import client.ResponseException;
import client.ServerFacade;
import model.UserData;
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
    public void registerFailureDuplicate() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        assertThrows(ResponseException.class, () -> {
            facade.register(user);
            facade.register(user);
        });
    }
    @Test
    public void registerFailureSameUsername() throws ResponseException {
        UserData user = new UserData("test","password","test@gmail.com");
        UserData other = new UserData("test","other","other@gmail.com");
        assertThrows(ResponseException.class, () -> {
            facade.register(user);
            facade.register(other);
        });
    }

    /*@Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }*/

}
