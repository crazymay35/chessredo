package dataaccess;
import  model.GameData;

import java.util.Collection;

public interface InterfaceGameDAO {
    GameData getGame(int gameID);
    Collection<GameData> listGames();
    int createGame(GameData game);
    void updateGame(GameData game);
    void clear();
}
