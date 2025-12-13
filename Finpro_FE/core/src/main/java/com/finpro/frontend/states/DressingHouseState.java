package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    public DressingHouseState(GameStateManager gsm, Player player, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.player = player;
        this.buttonManager = buttonManager; 

        background = new Texture("bg/dressingroom.jpeg");
        dressingHouse = new DressingHouse(0, 0);
        saveBtn = new SimpleButton("SAVE", 260, 100, 200, 80);
        font = new BitmapFont();
    }

    @Override
    public void update(float dt) {
        handleInput();
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
            saveSkinAndExit();
        }

        if (saveBtn.isClicked()) {
            saveSkinAndExit();
        }
    }

    private void saveSkinAndExit() {
        player.setSelectedSkinId(dressingHouse.getCurrentSkinIndex());
        gsm.setState(new MenuState(gsm, player, buttonManager));
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f); // Dark gray
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Skin currentSkin = dressingHouse.getCurrentSkin();

        sb.begin();
        
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        sb.draw(background, 0, 0, screenWidth, screenHeight);

        // Draw player skin (centered)
        float skinWidth = 320;
        float skinHeight = 650;
        float skinX = (screenWidth - skinWidth) / 2;
        float skinY = (screenHeight - skinHeight) / 2;
        sb.draw(currentSkin.getTexture(), skinX, skinY, skinWidth, skinHeight);

        // Draw save button
        saveBtn.render(sb, font);

        sb.end();

        currentSkin.dispose();
    }

    @Override
    public void dispose() {
        if (background != null) background.dispose();
        if (font != null) font.dispose();
    }
}
