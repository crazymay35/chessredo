package dataaccess.sql;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class GameSQL implements InterfaceGameSQL{

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        return 0;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
