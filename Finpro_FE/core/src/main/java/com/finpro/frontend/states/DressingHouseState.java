package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.models.DressingHouse;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.models.SimpleButton;
import com.finpro.frontend.models.Skin;

public class DressingHouseState implements GameState {

    private GameStateManager gsm;
    private DressingHouse dressingHouse;
    private Player player;
    private ButtonManager buttonManager;

    private Texture background;
    private SimpleButton saveBtn;
    private BitmapFont font;
    private BitmapFont smallFont;

    private String statusMessage = "";
    private float statusTimer = 0;

    public DressingHouseState(GameStateManager gsm, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.player = gsm.getPlayer();
        this.buttonManager = buttonManager;

        background = new Texture("bg/dressingroom.png");
        dressingHouse = new DressingHouse(0, 0);

        saveBtn = new SimpleButton("SELECT", 260, 100, 200, 80);

        font = new BitmapFont();
        smallFont = new BitmapFont();
        smallFont.getData().setScale(1.2f);
    }

    @Override
    public void update(float dt) {
        handleInput();

        if (statusTimer > 0) {
            statusTimer -= dt;
            if (statusTimer <= 0) {
                statusMessage = "";
            }
        }
    }

    private void handleInput() {
        saveBtn.update();

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            dressingHouse.previousSkin();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            dressingHouse.nextSkin();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            selectSkin();
        }

        if (saveBtn.isClicked()) {
            selectSkin();
        }
    }


    private void selectSkin() {
        int currentSkinId = dressingHouse.getCurrentSkinIndex();

        if (player.canAffordSkin(currentSkinId)) {
            player.setSelectedSkinId(currentSkinId);
            gsm.setState(new MenuState(gsm, buttonManager));
        } else {
            Skin skin = dressingHouse.getCurrentSkin();
            showStatus("Need " + skin.getPrice() + " coins! You have " + (int)player.getFashionCoin());
            skin.dispose();
        }
    }

    private void showStatus(String message) {
        this.statusMessage = message;
        this.statusTimer = 3.0f;
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Skin currentSkin = dressingHouse.getCurrentSkin();
        int currentSkinId = dressingHouse.getCurrentSkinIndex();
        boolean canAfford = player.canAffordSkin(currentSkinId);

        sb.begin();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        sb.draw(background, 0, 0, screenWidth, screenHeight);

        // Draw player skin (centered)
        float skinWidth = 600;
        float skinHeight = 900;
        float skinX = (screenWidth - skinWidth) / 2;
        float skinY = (screenHeight - skinHeight) / 2;
        sb.draw(currentSkin.getTexture(), skinX, skinY, skinWidth, skinHeight);

        // Display skin info
        font.getData().setScale(2.0f);
        font.setColor(Color.WHITE);
        font.draw(sb, currentSkin.getName(), screenWidth/2 - 60, screenHeight - 80);

        // Display lock/unlock status
        if (canAfford) {
            smallFont.setColor(Color.GREEN);
            smallFont.draw(sb, "AVAILABLE", screenWidth/2 - 50, screenHeight - 170);
        } else {
            smallFont.setColor(Color.RED);
            smallFont.draw(sb, "LOCKED", screenWidth/2 - 40, screenHeight - 170);
        }

        // Display player coins
        smallFont.setColor(Color.WHITE);
        smallFont.draw(sb, "Your Coins: " + (int)player.getFashionCoin(), 50, screenHeight - 50);

        // Skin counter
        int currentIndex = dressingHouse.getCurrentSkinIndex() + 1;
        smallFont.draw(sb, "Skin " + currentIndex + " / 6", screenWidth/2 - 40, 250);

        // Draw save button
        saveBtn.render(sb, font);

        // Display status message
        if (!statusMessage.isEmpty()) {
            font.getData().setScale(1.3f);
            font.setColor(Color.RED);
            font.draw(sb, statusMessage, 150, 180);
        }

        font.setColor(Color.WHITE);
        font.getData().setScale(1f);

        sb.end();
    }

    @Override
    public void dispose() {
        if (background != null) background.dispose();
        if (font != null) font.dispose();
        if (smallFont != null) smallFont.dispose();
    }
}
