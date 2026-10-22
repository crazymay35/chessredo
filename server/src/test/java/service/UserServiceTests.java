package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private UserService userService;

    @BeforeEach
    public void setup() {
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();
        userService =  new UserService(userDAO, authDAO);
    }

    //register
    @Test
    public void registerSuccess() throws Exception {
        UserData sendUser = new UserData("username","password","user@gmail.com");
        AuthData returnUser = userService.register(sendUser);
        assertNotNull(returnUser.authToken());
        assertEquals(sendUser.username(),returnUser.username());
    }
    @Test
    public void registerFailureMissingFields() {
        UserData sendUser = new UserData(null,null,null);
        assertThrows(BadRequestException.class, ()-> userService.register(sendUser));
    }
    @Test
    public void registerFailureDuplicate() {
        UserData sendUser = new UserData("username","password","user@gmail.com");
        assertDoesNotThrow(() -> userService.register(sendUser));
        assertThrows(AlreadyTakenException.class, ()-> userService.register(sendUser));
    }

    //login
    @Test
    public void loginSuccess() throws Exception {
        UserData registerUser = new UserData("username","password","user@gmail.com");
        userService.register(registerUser);
        UserLoginRequest sendUser = new UserLoginRequest("username","password");
        AuthData returnUser = userService.login(sendUser);
        assertNotNull(returnUser.authToken());
        assertEquals("username",returnUser.username());
    }
    @Test
    public void loginFailureMissingFields() {
        UserLoginRequest sendUser = new UserLoginRequest(null,null);
        assertThrows(BadRequestException.class, ()-> userService.login(sendUser));
    }
    @Test
    public void loginFailureUserDNE() {
        UserLoginRequest sendUser = new UserLoginRequest("username","password");
        assertThrows(UnauthorizedException.class, ()-> userService.login(sendUser));
    }
    @Test
    public void loginFailurePassword() throws Exception {
        UserData validUser = new UserData("username","correctPassword", "user@gmail.com");
        userService.register(validUser);
        UserLoginRequest badLoginRequest = new UserLoginRequest("username","wrongPassword");
        assertThrows(UnauthorizedException.class, () -> userService.login(badLoginRequest));
    }

    //logout
    @Test
    public void logoutSuccess() throws Exception {
        UserData sendUser = new UserData("username","password","user@gmail.com");
        AuthData returnUser = userService.register(sendUser);
        assertNotNull(returnUser.authToken());
        userService.logout(returnUser.authToken());
        assertThrows(UnauthorizedException.class, () -> userService.logout(returnUser.authToken()));
    }
    @Test
    public void logoutFailureInvalidAuthToken() {
        AuthData fakeAuth = new AuthData("authToken","username");
        assertThrows(UnauthorizedException.class, () -> userService.logout(fakeAuth.authToken()));
    }
    @Test
    public  void logoutFailureDuplicate() throws Exception {
        UserData sendUser = new UserData("username","password","user@gmail.com");
        AuthData returnUser = userService.register(sendUser);
        userService.logout(returnUser.authToken());
        assertThrows(UnauthorizedException.class, () -> userService.logout(returnUser.authToken()));
    }
}
