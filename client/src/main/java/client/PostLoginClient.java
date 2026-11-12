package client;

import model.AuthData;

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
                    case "help" -> { }
                    case "create" -> { }
                    case "list" -> { }
                    case "play" -> {}
                    case "observe" -> {}
                    case "logout" -> { }
                    case "quit" -> {}
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
    //create (gameName)
    //list
    //play (playerColor, gameID)
    //observe (gameID)
    //logout
    //quit
}
