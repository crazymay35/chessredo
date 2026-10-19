package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameDAO implements InterfaceGameDAO{
    private HashMap<Integer, GameData> games = new HashMap<>();
    int nextGameID = 0;

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    @Override
    public int createGame(GameData game) {
        int id = nextGameID++;
        GameData newGame = new GameData(id,game.whiteUsername(),game.blackUsername(),game.gameName(),game.game());
        games.put(id,newGame);
        return id;
    }

    @Override
    public void updateGame(GameData game) {
        games.put(game.gameID(),game);
    }

    @Override
    public void clear() {
        games.clear();
    }
}
