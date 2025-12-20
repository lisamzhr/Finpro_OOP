package com.finpro.frontend.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.frontend.models.Player;

import java.util.Stack;

public class GameStateManager {
    private final Stack<GameState> states;
    private Player player; // ✅ Single source of truth untuk player

    public GameStateManager() {
        this.states = new Stack<>();
    }

    // ✅ Player Management Methods
    /**
     * Get the current player instance
     * @return Player object, or null if not initialized
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set player instance (called after login/register)
     * @param player Player object to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Initialize default player for testing/development
     */
    public void initializeDefaultPlayer() {
        this.player = new Player("puti", "jdu834", 1);
        System.out.println("Default player initialized: " + player.getUsername());
    }

    /**
     * Check if player is initialized
     * @return true if player exists, false otherwise
     */
    public boolean hasPlayer() {
        return player != null;
    }

    // ✅ State Management Methods
    public void push(GameState state) {
        states.push(state);
    }

    public void pop() {
        if (!states.isEmpty()) {
            GameState oldState = states.pop();
            oldState.dispose();
        }
    }

    public void set(GameState state) {
        if (!states.isEmpty()) {
            GameState oldState = states.pop();
            oldState.dispose();
        }
        states.push(state);
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

    public void dispose() {
        while (!states.isEmpty()) {
            GameState state = states.pop();
            state.dispose();
        }
        // Clean up player reference
        player = null;
    }
}
