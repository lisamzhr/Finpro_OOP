package com.finpro.frontend.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.observers.PlayerDataSaver;

import java.util.Stack;

public class GameStateManager {
    private final Stack<GameState> states;
    private Player player;
    private PlayerDataSaver playerDataSaver;

    public GameStateManager() {
        this.states = new Stack<>();
        this.playerDataSaver = new PlayerDataSaver();
    }

    public boolean hasPlayer() {
        return player != null;
    }

    public void push(GameState state) {
        states.push(state);
    }

    public void pop() {
        if (!states.isEmpty()) {
            GameState oldState = states.pop();
            oldState.dispose();
        }
    }

    public void setPlayer(Player player) {
        if (this.player != null) {
            this.player.removeListener(playerDataSaver);
        }
        this.player = player;
        if (player != null) {
            player.addListener(playerDataSaver);
        }

    }

    public Player getPlayer() {
        return player;
    }

    public void set(GameState state){
        if (!states.isEmpty()) {
            GameState oldState = states.pop();
            oldState.dispose();
        }
        states.push(state);
    }
    public void dispose() {
        if (player != null) {
            player.removeListener(playerDataSaver);
        }
        while (!states.isEmpty()) {
            GameState state = states.pop();
            state.dispose();
        }
        player = null;
    }

    public void setState(GameState state) {
        if (!states.isEmpty()) {
            GameState oldState = states.pop();
            oldState.dispose();
        }
        states.push(state);
    }
    public void update(float delta) {
        if (!states.isEmpty()) {
            states.peek().update(delta);
        }
    }

    public void render(SpriteBatch batch) {
        if (!states.isEmpty()) {
            states.peek().render(batch);
        }
    }

}
