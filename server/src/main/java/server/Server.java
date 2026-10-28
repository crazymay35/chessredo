package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.json.JavalinGson;
import model.*;
import service.*;

import java.util.Collection;
import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
            config.jsonMapper(new JavalinGson());
        });

        // Register your endpoints and exception handlers here. (switch between DAO and SQL)
        InterfaceUserDAO userDAO;
        InterfaceAuthDAO authDAO;
        InterfaceGameDAO gameDAO;
        try {
            userDAO = new UserSQL();
            authDAO = new AuthSQL();
            gameDAO = new GameSQL();
        }
        catch (dataaccess.DataAccessException e) {
            throw new RuntimeException("Failed to initialize database DAOs", e);
        }
        UserService userService = new UserService(userDAO,authDAO);
        GameService gameService = new GameService(authDAO,gameDAO);
        ClearService clearService = new ClearService(userDAO,authDAO,gameDAO);

        Gson gson = new Gson();
        javalin.exception(UnauthorizedException.class, (e,ctx) -> {
            ctx.status(401);
            ctx.json(Map.of("message", e.getMessage()));
        });
        javalin.exception(BadRequestException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.json(Map.of("message", e.getMessage()));
        });
        javalin.exception(AlreadyTakenException.class, (e,ctx) -> {
            ctx.status(403);
            ctx.json(Map.of("message", e.getMessage()));
        });

        //DELETE /db
        javalin.delete("/db", ctx -> {
            clearService.clear();
            ctx.status(200);
            ctx.json("{}");
        });

        //POST /user
        javalin.post("/user", ctx -> {
            UserData user = gson.fromJson(ctx.body(), UserData.class);
            AuthData auth = userService.register(user);
            ctx.status(200);
            ctx.json(auth);
        });

        //POST /session
        javalin.post("/session", ctx -> {
            UserLoginRequest user = gson.fromJson(ctx.body(), UserLoginRequest.class);
            AuthData auth = userService.login(user);
            ctx.status(200);
            ctx.json(auth);
        });
        //DELETE /session
        javalin.delete("/session", ctx -> {
            String authToken = ctx.header("authorization");
            userService.logout(authToken);
            ctx.status(200);
            ctx.json(Map.of());
        });

        //POST /game
        javalin.post("/game", ctx -> {
            String authToken = ctx.header("authorization");
            GameCreateRequest game = gson.fromJson(ctx.body(), GameCreateRequest.class);
            int gameID = gameService.createGame(authToken,game);
            record GameCreateResponse(int gameID) {}
            ctx.status(200);
            ctx.json(new GameCreateResponse(gameID));
        });
        //PUT /game
        javalin.put("/game", ctx -> {
            String authToken = ctx.header("authorization");
            GameJoinRequest game = gson.fromJson(ctx.body(), GameJoinRequest.class);
            gameService.joinGame(authToken,game);
            ctx.status(200);
            ctx.json(gson.toJson(Map.of()));
        });
        //GET /game
        javalin.get("/game", ctx -> {
            String authToken = ctx.header("authorization");
            Collection <GameData> gamesList = gameService.listGames(authToken);
            record GameListResponse(Collection<GameData> games) {}
            ctx.status(200);
            ctx.json(new GameListResponse(gamesList));
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
