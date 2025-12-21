package com.finpro.frontend;

import com.finpro.frontend.models.Player;
import com.finpro.frontend.services.BackendService;
import com.finpro.frontend.states.GameStateManager;

import javax.swing.text.LabelView;

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

    public void registerPlayer(String username, BackendService.RequestCallback callback) {
        backendService.register(username, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                String id = extractValue(response, "playerId");
                String lvl = extractValue(response, "level");
                Player p = new Player(id, username, Integer.valueOf(lvl));
                setCurrentPlayer(p);
                callback.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    private String extractValue(String json, String key) {
        try {
            String search = "\"" + key + "\":";
            int start = json.indexOf(search);
            if (start == -1) return "";
            start += search.length();
            // Skip spasi
            while (start < json.length() && (json.charAt(start) == ' ')) {
                start++;
            }
            // Jika value diawali tanda kutip → STRING
            if (json.charAt(start) == '\"') {
                start++;
                int end = json.indexOf("\"", start);
                return json.substring(start, end);
            }
            // Jika bukan kutip → NUMBER
            int end = start;
            while (end < json.length() &&
                (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) {
                end++;
            }
            return json.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }

}
