package com.finpro.frontend.models;

import com.finpro.frontend.observers.PlayerListener;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private String id;
    private String username;
    private int level;
    private float fashionCoin;

    // Observer list
    private List<PlayerListener> listeners = new ArrayList<>();

    public Player(String id, String username, int level) {
        this.id = id;
        this.username = username;
        this.level = level;
        fashionCoin = 5;
    }

    //Observer Pattern
    public void addListener(PlayerListener listener) {
        listeners.add(listener);
    }
    public void removeListener(PlayerListener listener) {
        listeners.remove(listener);
    }
    private void notifyListeners(String eventType) {
        for (PlayerListener listener : listeners) {
            listener.onPlayerUpdated(this, eventType);
        }
    }

    //GETTERS
    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public int getLevel() {
        return level;
    }
    public float getFashionCoin() {
        return fashionCoin;
    }

    //Setter + notify
    public void setUsername(String username) {
        this.username = username;
        notifyListeners("USERNAME_CHANGED");
    }
    public void setLevel(int level) {
        this.level = level;
        notifyListeners("LEVEL_CHANGED");
    }
    public void setFashionCoin(float fashionCoin) {
        this.fashionCoin = fashionCoin;
        notifyListeners("COIN_CHANGED");
    }
}
