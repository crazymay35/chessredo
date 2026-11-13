package client;

import chess.*;
import model.AuthData;

public class ClientMain {
    public static void main(String[] args) {
        int port = 8080;
        var server = new ServerFacade(port);
        while (true) {
            var preLoginClient = new PreLoginClient(server);
            AuthData auth =  preLoginClient.run();
            if (auth != null) {
                var postLoginClient = new PostLoginClient(server, auth.authToken());
                boolean loggedOut = postLoginClient.run();
                if (!loggedOut) {
                    break;
                }
            }
        }
    }
}
