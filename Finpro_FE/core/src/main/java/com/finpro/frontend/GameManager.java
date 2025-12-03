package com.finpro.frontend;

import com.finpro.frontend.models.Player;
import com.finpro.frontend.services.BackendService;
import com.finpro.frontend.states.GameStateManager;

/**
 * Simple singleton to keep current player and backend service.
 * States can call GameManager.getInstance() to read/write player or call backend.
 */
public class GameManager {
    private static final GameManager INSTANCE = new GameManager();

    private Player currentPlayer;
    private BackendService backendService;
    private GameStateManager gsm; // optional, set by MainGame

    private GameManager() {
        backendService = new BackendService();
    }

    public static GameManager getInstance() {
        return INSTANCE;
    }

    public BackendService getBackendService() {
        return backendService;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player p) {
        this.currentPlayer = p;
    }

    public void setGsm(GameStateManager gsm) {
        this.gsm = gsm;
    }

    public GameStateManager getGsm() {
        return gsm;
    }

    /**
     * Convenience method: register player via backend and set currentPlayer on success.
     * Caller must handle UI changes (or you can push states here if you want).
     */
    public void registerPlayer(String username, BackendService.RequestCallback callback) {
        backendService.register(username, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                // response is JSON player object; simplest parse get id & username
                // naive parse (better use Gson)
                String id = parseIdFromJson(response);
                Player p = new Player(id, username);
                setCurrentPlayer(p);
                callback.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    // very small helper parse for simple JSON like {"id":"...","username":"..."}
    private String parseIdFromJson(String json) {
        if (json == null) return null;
        // naive extract between "id":" and next "
        try {
            int idx = json.indexOf("\"id\"");
            if (idx == -1) idx = json.indexOf("\"playerId\"");
            if (idx == -1) return null;
            int colon = json.indexOf(":", idx);
            int firstQuote = json.indexOf("\"", colon);
            int secondQuote = json.indexOf("\"", firstQuote + 1);
            return json.substring(firstQuote + 1, secondQuote);
        } catch (Exception e) {
            return null;
        }
    }
}
