package com.finpro.frontend.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.frontend.models.Player;

import java.util.Stack;

public class GameStateManager {
    private final Stack<GameState> states;
    private Player player;

    public GameStateManager() {
        this.states = new Stack<>();
    }
    public void push(GameState state){
        states.push(state);
    }
    public void pop(){
        if (!states.isEmpty()) {
            GameState oldState = states.pop();
            oldState.dispose();
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
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
    public void update(float delta){
        states.peek().update(delta);
    }
    public void render(SpriteBatch batch){
        states.peek().render(batch);
    }
    public void dispose() {
        while (!states.isEmpty()) {
            GameState state = states.pop();
            state.dispose();
        }
    }

    public void setState(GameState state) {
        if (!states.isEmpty()) {
            GameState oldState = states.pop();
            oldState.dispose();
        }
        states.push(state);
    }

}
