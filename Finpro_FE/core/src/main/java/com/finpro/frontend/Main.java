package com.finpro.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.finpro.frontend.states.GameStateManager;
import com.finpro.frontend.states.MenuState;
import com.finpro.frontend.factory.ButtonFactory;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private GameStateManager gsm;
    private ButtonManager buttonManager;
    private BitmapFont defaultFont;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gsm = new GameStateManager();

        // Create shared ButtonManager for all states
        defaultFont = new BitmapFont();
        ButtonFactory buttonFactory = new ButtonFactory(defaultFont);
        buttonManager = new ButtonManager(buttonFactory);

        System.out.println("=== Game Started ===");
        System.out.println("ButtonManager created");

        // Push MenuState with buttonManager
        gsm.push(new MenuState(gsm, null, buttonManager));

        // Save gsm to GameManager if needed
        if (GameManager.getInstance() != null) {
            GameManager.getInstance().setGsm(gsm);
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        gsm.update(delta);
        gsm.render(batch);
    }

    @Override
    public void resize(int width, int height) {
        // Handle resize if needed
    }

    @Override
    public void dispose() {
        System.out.println("=== Game Closing ===");
        System.out.println("Active buttons before cleanup: " + buttonManager.getActiveCount());

        batch.dispose();
        gsm.dispose();

        // Dispose ButtonManager - this is the ONLY place it should be disposed
        if (buttonManager != null) {
            buttonManager.dispose();
        }

        if (defaultFont != null) {
            defaultFont.dispose();
        }

        System.out.println("Game disposed successfully");
    }
}
