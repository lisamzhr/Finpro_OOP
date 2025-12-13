package com.finpro.frontend.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class GameStateManager {
    private final Stack<GameState> states;

    public GameStateManager() {
        this.states = new Stack<>();
    }
    public void push(GameState state){
        states.push(state);
    }
    public void pop(){
        states.pop();
    }
    public void set(GameState state){
        states.pop();
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
