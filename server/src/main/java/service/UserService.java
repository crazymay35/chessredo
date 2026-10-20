package service;

import dataaccess.*;
import model.*;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    //register
        //checks if missing fields
        //checks if user already exists
        //registers user
        //returns AuthData
    //UserData is json{username:"name",password:"pass",email:"e@mail.com"}
    public AuthData register(UserData user) throws Exception {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (userDAO.getUser(user.username()) != null) {
            throw new AlreadyTakenException("Error: username already taken");
        }
        userDAO.createUser(user);
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken,user.username());
        authDAO.createAuth(authData);
        return authData;
    }

    //login
        //check if missing fields
        //check if user exists
        //check if passwords match
        //return AuthData
    //LoginRequest is json{username:"name",password:"pass"}
    public AuthData login(UserLoginData user) throws Exception {
        if (user.username() == null || user.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        UserData tryUser = userDAO.getUser(user.username());
        if (tryUser == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (!tryUser.password().equals(user.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken,user.username());
        authDAO.createAuth(authData);
        return authData;
    }
    //logout
        //checks if authToken exists
        //returns nothing
    public void logout(AuthData user) throws Exception {
        if (authDAO.getAuthToken(user.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.deleteAuth(user.authToken());
    }
}
