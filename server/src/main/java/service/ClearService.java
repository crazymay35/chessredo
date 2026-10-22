package service;

import dataaccess.*;

public class ClearService {
    private final InterfaceUserDAO userDAO;
    private final InterfaceAuthDAO authDAO;
    private final InterfaceGameDAO gameDAO;

    public ClearService(InterfaceUserDAO userDAO, InterfaceAuthDAO authDAO, InterfaceGameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void clear() {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }
}
