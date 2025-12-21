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
    private Texture logoTexture;
    private Texture startGameBGTexture;

    public MenuState(GameStateManager gsm, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.player = gsm.getPlayer();
        //this.player = new Player("puti", "jdu834", 1);
        this.buttonManager = buttonManager;

        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(Color.WHITE);

        backgroundTexture = new Texture("menu/background.png");
        startGameBGTexture = new Texture("menu/startGameBG.png");
        logoTexture = new Texture("menu/logo.png");

        // Load button textures
        buttonTexture = new Texture("button_normal.png");
        buttonHoverTexture = new Texture("button_hover.png");

        // Create "Start Game" button using ButtonManager
        float buttonWidth = 250;
        float buttonHeight = 80;
        float centerX = Gdx.graphics.getWidth() / 2f - buttonWidth / 2f;
        float buttonY = Gdx.graphics.getHeight() / 2f - buttonHeight / 2f - 320;

        startGameButton = buttonManager.createButton(
            "START GAME",
            centerX,
            buttonY,
            buttonWidth,
            buttonHeight,
            buttonTexture,
            buttonHoverTexture
        );

        // Create houses with ButtonManager
        datingHouse = new DatingHouse(-20, 260, buttonManager);
        dressingHouse = new DressingHouse(840, 360);

        System.out.println("Active buttons before load: " + buttonManager.getActiveCount());
    }

    @Override
    public void update(float delta) {
        if (player != null) {
            // Player sudah login - tampilkan houses
            datingHouse.update();
            dressingHouse.update();

            // Check house clicks
            if (Gdx.input.justTouched()) {
                if (dressingHouse.isHovered()) {
                    buttonManager.releaseButton(startGameButton);
                    gsm.setState(new DressingHouseState(gsm, buttonManager));
                    return;
                } else if (datingHouse.isHovered()) {
                    //buttonManager.releaseButton(startGameButton);
                    gsm.setState(new DatingHouseState(gsm, buttonManager));
                    buttonManager.releaseButton(startGameButton);
                    System.out.println("Dating House clicked!");
                    return;
                }
            }
        } else {
            // Player belum login - tampilkan button Start Game
            startGameButton.update();
            if (startGameButton.isClicked()) {
                gsm.setState(new StartGameState(gsm, buttonManager));
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        if (player == null) {
            float logoWidth = 800;
            float logoHeight = 800;
            float logoX = Gdx.graphics.getWidth() / 2f - logoWidth / 2f;
            float logoY = Gdx.graphics.getHeight() / 2f - logoHeight / 2f + 40;

            // Drae BG
            batch.draw(startGameBGTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            // Draw logo
            batch.draw(logoTexture, logoX, logoY, logoWidth, logoHeight);
            startGameButton.render(batch, font);
        } else {
            // View after login - Player info
            font.getData().setScale(1.5f);
            font.draw(batch, "Welcome: " + player.getUsername(), 100, 400);
            font.draw(batch, "ID: " + player.getId(), 100, 370);
            font.draw(batch, "Level: " + player.getLevel(), 100, 340);
            font.draw(batch, "Coin: " + player.getFashionCoin(), 100, 310);
            font.getData().setScale(2f);

            // Render houses
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            datingHouse.render(batch);
            dressingHouse.render(batch);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        buttonManager.releaseButton(startGameButton);
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }

        if (font != null) {
            font.dispose();
        }

        if (startGameBGTexture != null) {
            startGameBGTexture.dispose();
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
    }
}
