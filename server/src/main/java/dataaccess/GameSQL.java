package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class GameSQL implements InterfaceGameDAO{
    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public int createGame(GameData game) {
        return 0;
    }

    @Override
    public void updateGame(GameData game) {

    }

    @Override
    public void clear() {

    }
}
