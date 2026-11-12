package client;

import model.AuthData;
import model.UserData;

import java.util.Scanner;

public class PreLoginClient {
    private final ServerFacade server;
    PreLoginClient(ServerFacade server) {
        this.server = server;
    }
    public AuthData run() {
        Scanner scanner = new Scanner(System.in);
        help();
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
                    case "quit" -> { quit(); }
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
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - create a new account");
        System.out.println("login <USERNAME> <PASSWORD> - log into account");
        System.out.println("quit - exit program");
        System.out.println("help - display possible commands");
    }
    //quit
    private void quit() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
    //login (username, password)

    //register (username, password, email)
    private AuthData register(String[] tokens) throws ResponseException {
        if (tokens.length < 4) {
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL>");
            return null;
        }
        UserData user = new UserData(tokens[1],tokens[2],tokens[3]);
        AuthData auth = server.register(user);
        System.out.println("registered and logged in");
        return auth;
    }

}
