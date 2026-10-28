package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class GameService {
    private final InterfaceAuthDAO authDAO;
    private final InterfaceGameDAO gameDAO;

    public GameService(InterfaceAuthDAO authDAO, InterfaceGameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Collection<GameData> listGames(String authToken) throws Exception {
        if (authDAO.getAuthToken(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return gameDAO.listGames();
    }

    public int createGame(String authToken, GameCreateRequest game) throws Exception {
        if (authDAO.getAuthToken(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (game.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        int gameID = ThreadLocalRandom.current().nextInt(1,10000000);
        ChessGame chessGame = new ChessGame();
        chessGame.getBoard().resetBoard();
        GameData gameData = new GameData(gameID,null,null,game.gameName(),chessGame);
        gameDAO.createGame(gameData);
        return gameID;
    }

    public void joinGame(String authToken, GameJoinRequest game) throws Exception {
        AuthData authData = authDAO.getAuthToken(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (game.playerColor() == null) {
            throw new BadRequestException("Error: bad request");
        }
        GameData gameData = gameDAO.getGame(game.gameID());
        if (gameData == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (game.playerColor().equals("BLACK")) {
            if (gameData.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            GameData newGameData = gameData.setBlackUsername(authData.username());
            gameDAO.updateGame(newGameData);
        }
        else if (game.playerColor().equals("WHITE")) {
            if (gameData.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            GameData newGameData = gameData.setWhiteUsername(authData.username());
            gameDAO.updateGame(newGameData);
        }
        else {
            throw new BadRequestException("Error: bad request");
        }
    }
}
