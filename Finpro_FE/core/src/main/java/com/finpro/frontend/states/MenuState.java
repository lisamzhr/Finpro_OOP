package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.frontend.models.DatingHouse;
import com.finpro.frontend.models.DressingHouse;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.factory.ButtonFactory;

public class MenuState implements GameState {

    private GameStateManager gsm;
    private Player player;
    private BitmapFont font;
    private DatingHouse datingHouse;
    private DressingHouse dressingHouse;
    private Texture backgroundTexture;
    private ButtonManager buttonManager;
    private Button startGameButton;
    private Texture buttonTexture;
    private Texture buttonHoverTexture;

    public MenuState(GameStateManager gsm, Player player) {
        this.gsm = gsm;
        this.player = player; //yang bener ini
        //this.player = new Player("LKJH", "lili", 1); //buat trial

        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(Color.WHITE);

        backgroundTexture = new Texture("menu/background.png");

        // Initialize ButtonManager with font
        ButtonFactory buttonFactory = new ButtonFactory(font);
        buttonManager = new ButtonManager(buttonFactory);

        // Load button textures
        buttonTexture = new Texture("button_normal.png");
        buttonHoverTexture = new Texture("button_hover.png");

        // Create "Start Game" button using ButtonManager
        float buttonWidth = 250;
        float buttonHeight = 80;
        float centerX = Gdx.graphics.getWidth() / 2f - buttonWidth / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f - buttonHeight / 2f;

        startGameButton = buttonManager.createButton(
            "START GAME",
            centerX,
            centerY,
            buttonWidth,
            buttonHeight,
            buttonTexture,
            buttonHoverTexture
        );

        // Create houses with ButtonManager
        datingHouse = new DatingHouse(-20, 260, buttonManager);
        dressingHouse = new DressingHouse(840, 360);
    }

    @Override
    public void update(float delta) {
        // If player exists (already logged in), update houses
        if (player != null) {
            datingHouse.update();
            dressingHouse.update();
            return;
        }

        // If not logged in, wait for "Start Game" button click
        startGameButton.update();

        if (startGameButton.isClicked()) {
            gsm.set(new StartGameState(gsm));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // Draw background
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (player == null) {
            // Initial view: "Start Game" button
            startGameButton.render(batch, font);
        } else {
            // View after login

            // Player info
            font.getData().setScale(1.5f);
            font.draw(batch, "Welcome: " + player.getUsername(), 100, 400);
            font.draw(batch, "ID: " + player.getId(), 100, 370);
            font.draw(batch, "Level: " + player.getLevel(), 100, 340);
            font.draw(batch, "Coin: " + player.getFashionCoin(), 100, 310);
            font.getData().setScale(2f);

            // Render houses
            datingHouse.render(batch);
            dressingHouse.render(batch);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();

        if (font != null) {
            font.dispose();
        }

        if (buttonTexture != null) {
            buttonTexture.dispose();
        }

        if (buttonHoverTexture != null) {
            buttonHoverTexture.dispose();
        }

        if (datingHouse != null) {
            datingHouse.dispose();
        }

        if (dressingHouse != null) {
            dressingHouse.dispose();
        }

        // Release button back to pool
        if (startGameButton != null && buttonManager != null) {
            buttonManager.releaseButton(startGameButton);
            startGameButton = null;
        }

        // Dispose ButtonManager
        if (buttonManager != null) {
            buttonManager.dispose();
        }
    }
}
