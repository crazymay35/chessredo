package client;

import chess.ChessGame;
import model.GameCreateRequest;
import model.GameData;
import model.GameJoinRequest;

import java.util.HashMap;
import java.util.Scanner;

public class PostLoginClient {
    private final ServerFacade server;
    private final String authToken;

    PostLoginClient(ServerFacade server, String authToken) {
        this.server = server;
        this.authToken = authToken;
    }
    public boolean run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Chess - Post Login ===");
        System.out.println("type 'help' to see available commands");

        while (true) {
            String line = scanner.nextLine();
            String[] tokens = line.trim().split("\\s");
            if (tokens.length == 0 || tokens[0].isBlank()) {
                continue;
            }
            String command = tokens[0].toLowerCase();

            try {
                switch (command) {
                    case "help" -> help();
                    case "create" -> create(tokens);
                    case "list" -> list();
                    case "play" -> play(tokens);
                    case "observe" -> observe(tokens);
                    case "logout" -> {
                        return logout(authToken);
                    }
                    default -> System.out.println("unknown command. Type 'help'");
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //help
    private void help() {
        System.out.println("help - display possible commands");
        System.out.println("create <GAMENAME> - create new game");
        System.out.println("list - list all games");
        System.out.println("play <GAMEID> <WHITE|BLACK> - join game");
        System.out.println("observe <GAMEID> - observe game");
        System.out.println("logout - logout");
    }
    //create (gameName)
    private void create(String[] tokens) throws ResponseException {
        if (tokens.length < 2) {
            System.out.println("create <GAMENAME>");
            return;
        }
        GameCreateRequest game = new GameCreateRequest(tokens[1]);
        server.createGame(game,authToken);
        System.out.println("game created");
    }
    //list
    private void list() throws ResponseException {
        GameData[] games = server.listGames(authToken);
        if (games == null || games.length == 0) {
            System.out.println("no games found");
            return;
        }
        System.out.println("--- Current Games ---");
        for (GameData game: games) {
            String white = (game.whiteUsername() != null) ? game.whiteUsername() : "<EMPTY>";
            String black = (game.blackUsername() != null) ? game.blackUsername() : "<EMPTY>";
            System.out.printf("ID: %-13s Name: %-13s White: %-13s Black: %-13s\n", String.format("%07d",game.gameID()), game.gameName(), white, black);
        }
    }
    //play (playerColor, gameID)
    private void play(String[] tokens) throws ResponseException {
        if (tokens.length < 3) {
            System.out.println("play <GAMEID> <WHITE|BLACK>");
            return;
        }
        int id = Integer.parseInt(tokens[1]);
        String color = tokens[2].toUpperCase();
        if (!color.equals("WHITE") && !color.equals("BLACK")) {
            System.out.println("Error: color must be WHITE or BLACK");
            return;
        }
        GameJoinRequest game = new GameJoinRequest(color,id);
        server.joinGame(game,authToken);
        System.out.println("joined game");
        var playGame = getGame(id);
        assert playGame != null;
        BoardUI.drawBoard(playGame.getBoard());
    }
    //observe (gameID)
    private void observe(String[] tokens) throws ResponseException {
        if (tokens.length < 2) {
            System.out.println("observe <GAMEID>");
            return;
        }
        int id = Integer.parseInt(tokens[1]);
        System.out.println("observing game");
        var game = getGame(id);
        assert game != null;
        BoardUI.drawBoard(game.getBoard());
    }
    //logout
    private boolean logout(String authToken) throws ResponseException {
        server.logout(authToken);
        System.out.println("logged out");
        return true;
    }

    private ChessGame getGame(int findID) throws ResponseException {
        GameData[] games = server.listGames(authToken);
        for (var game : games) {
            if (game.gameID() == findID) {
                return game.game();
            }
        }
        return null;
    }
}
