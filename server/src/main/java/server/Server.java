package server;

import io.javalin.*;
import java.util.ArrayList;

public class Server {

    private final Javalin javalin;
    ArrayList<String> users = new ArrayList<>();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        // Register your endpoints and exception handlers here.
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
