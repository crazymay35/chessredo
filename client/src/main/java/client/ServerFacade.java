package client;

import model.*;
import com.google.gson.Gson;
import model.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverURL;
    private final Gson gson = new Gson();

    public ServerFacade(int port) {
        this.serverURL = "http://localhost:" + port;
    }

    public AuthData register(UserData user) throws ResponseException {
        var request = buildRequest("POST","/user", user, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }
    public AuthData login(UserLoginRequest user) throws ResponseException {
        var request = buildRequest("POST","/session", user, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }
    public void logout(String authToken) throws ResponseException {
        var request = buildRequest("DELETE","/session", null, authToken);
        var response = sendRequest(request);
        handleResponse(response,null);
    }

    public GameData[] listGames(String authToken) throws ResponseException {
        var request = buildRequest("GET","/game", null, authToken);
        var response = sendRequest(request);
        record ListGamesResponse(GameData[] games) {}
        var listResponse = handleResponse(response, ListGamesResponse.class);
        return listResponse.games();
    }
    public GameData createGame(GameCreateRequest game, String authToken) throws ResponseException {
        var request = buildRequest("POST","/game",game,authToken);
        var response = sendRequest(request);
        return handleResponse(response,GameData.class);
    }
    public void joinGame(GameJoinRequest game, String authToken) throws ResponseException {
        var request = buildRequest("PUT","/game",game,authToken);
        var response = sendRequest(request);
        handleResponse(response,null);
    }

    public void clear() throws ResponseException {
        var request = buildRequest("DELETE","/db",null,null);
        var response = sendRequest(request);
        handleResponse(response,null);
    }
    private HttpRequest buildRequest(String method,String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder().uri(URI.create(serverURL + path)).method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type","application/json");
        }
        if (authToken != null && !authToken.isBlank()) {
            request.setHeader("Authorization", authToken);
        }
        return request.build();
    }
    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(gson.toJson(request));
        }
        else {
            return BodyPublishers.noBody();
        }
    }
    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        var body = response.body();

        if (!isSuccessful(status)) {
            String errorMsg = body;
            try {
                var map = gson.fromJson(body, java.util.Map.class);
                if (map != null && map.containsKey("message")) {
                    errorMsg = (String) map.get("message");
                }
            } catch (Exception ignored) {}

            throw new ResponseException(status, errorMsg);
        }
        if (responseClass != null) {
            return gson.fromJson(body, responseClass);
        }
        return null;
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
