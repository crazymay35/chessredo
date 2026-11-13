package client;

import model.AuthData;
import model.GameCreateRequest;
import model.GameData;

import java.util.HashMap;
import java.util.Scanner;

public class PostLoginClient {
    private final ServerFacade server;
    private final String authToken;
    private final HashMap<Integer,Integer> displayedGames = new HashMap<>();
    PostLoginClient(ServerFacade server, String authToken) {
        //System.out.println("now in post login client");
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
                    case "help" -> { help(); }
                    case "create" -> { create(tokens); }
                    case "list" -> { }
                    case "play" -> {}
                    case "observe" -> {}
                    case "logout" -> { }
                    default -> { System.out.println("unknown command. Type 'help'"); }
                }
            }
            /*catch (ResponseException e) {
                System.out.println("Error: " + e.getMessage());
            }*/
            catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage());
            }
        }
    }

    //help
    private void help() {
        System.out.println("help - display possible commands");
        System.out.println("create <GAMENAME> - create new game");
        System.out.println("list - list all games");
        System.out.println("play <WHITE|BLACK> <GAMEID> - join game");
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
        displayedGames.clear();
        if (games == null || games.length == 0) {
            System.out.println("no games found");
            return;
        }
        System.out.println("\n--- Current Games ---");
        int index = 1;
        for (GameData game: games) {
            displayedGames.put(index, game.gameID());
            String white = (game.whiteUsername() != null) ? game.whiteUsername() : "<EMPTY";
            String black = (game.blackUsername() != null) ? game.blackUsername() : "<EMPTY";
            System.out.printf("%d Name: %s | White: %s | Black: %s", index, game.gameName(), white, black);
            index++;
        }
        System.out.println();
    }
    //play (playerColor, gameID)

    //observe (gameID)
    //logout
}
