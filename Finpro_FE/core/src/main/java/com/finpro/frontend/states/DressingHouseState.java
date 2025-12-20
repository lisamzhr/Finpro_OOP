package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.models.DressingHouse;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.models.Skin;

public class DressingHouseState implements GameState {

    private GameStateManager gsm;
    private DressingHouse dressingHouse;
    private Player player;
    private ButtonManager buttonManager;

    private Texture background;
    private Button saveBtn;
    private Texture buttonTexture;
    private Texture buttonHoverTexture;
    private BitmapFont font;

    public DressingHouseState(GameStateManager gsm, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.player = gsm.getPlayer();
        this.buttonManager = buttonManager;

        background = new Texture("bg/dressingroom.jpeg");
        dressingHouse = new DressingHouse(0, 0);
        font = new BitmapFont();

        // Load button textures
        try {
            buttonTexture = new Texture("button_normal.png");
            buttonHoverTexture = new Texture("button_hover.png");
            System.out.println("✅ Button textures loaded: " + buttonTexture.getWidth() + "x" + buttonTexture.getHeight());
        } catch (Exception e) {
            System.err.println("❌ ERROR loading button textures: " + e.getMessage());
        }

        // Create save button - POSISI KIRI BAWAH SUPAYA JELAS KELIATAN
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float buttonWidth = 200;
        float buttonHeight = 80;
        float buttonX = screenWidth / 2 - buttonWidth / 2; // Centered
        float buttonY = 50; // 50px dari bawah

        saveBtn = buttonManager.createButton(
            "SAVE",
            buttonX,
            buttonY,
            buttonWidth,
            buttonHeight,
            buttonTexture,
            buttonHoverTexture
        );

        // ✅ DEBUG: Check if button created
        if (saveBtn == null) {
            System.err.println("❌ ERROR: Save button is NULL!");
        } else {
            System.out.println("✅ Save button created at: x=" + buttonX + ", y=" + buttonY + ", w=" + buttonWidth + ", h=" + buttonHeight);
        }

        // Debug: Check player skin on load
        if (player != null) {
            System.out.println("✅ DressingHouse - Player: " + player.getUsername() + " | Skin ID: " + player.getSelectedSkinId());
        } else {
            System.err.println("❌ ERROR: Player is NULL!");
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    private void handleInput() {
        if (saveBtn != null) {
            saveBtn.update();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            dressingHouse.previousSkin();
            System.out.println("⬅️ Previous skin: " + dressingHouse.getCurrentSkinIndex());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            dressingHouse.nextSkin();
            System.out.println("➡️ Next skin: " + dressingHouse.getCurrentSkinIndex());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            saveSkinAndExit();
        }

        if (saveBtn != null && saveBtn.isClicked()) {
            saveSkinAndExit();
        }
    }

    private void saveSkinAndExit() {
        if (player != null) {
            int newSkinId = dressingHouse.getCurrentSkinIndex();
            player.setSelectedSkinId(newSkinId);
            System.out.println("💾 Skin saved! New Skin ID: " + newSkinId);
        } else {
            System.err.println("❌ ERROR: Cannot save skin - Player is NULL!");
        }

        // Release button sebelum pindah state
        if (saveBtn != null && buttonManager != null) {
            buttonManager.releaseButton(saveBtn);
            saveBtn = null;
        }

        gsm.setState(new MenuState(gsm, buttonManager));
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Skin currentSkin = dressingHouse.getCurrentSkin();

        sb.begin();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Draw background
        sb.draw(background, 0, 0, screenWidth, screenHeight);

        // Draw player skin (centered)
        float skinWidth = 320;
        float skinHeight = 650;
        float skinX = (screenWidth - skinWidth) / 2;
        float skinY = (screenHeight - skinHeight) / 2;
        sb.draw(currentSkin.getTexture(), skinX, skinY, skinWidth, skinHeight);

        // Draw save button
        if (saveBtn != null) {
            saveBtn.render(sb, font);
        } else {
            // ✅ DEBUG: Gambar rectangle manual kalau button null
            font.getData().setScale(2f);
            font.draw(sb, "ERROR: Button NULL!", screenWidth / 2 - 100, 100);
        }

        // ✅ DEBUG: Tampilkan info di layar
        font.getData().setScale(1.5f);
        font.draw(sb, "Current Skin: " + dressingHouse.getCurrentSkinIndex(), 50, screenHeight - 50);
        font.draw(sb, "Press LEFT/RIGHT to change skin", 50, screenHeight - 80);
        font.draw(sb, "Press ENTER or click SAVE", 50, screenHeight - 110);

        sb.end();
    }

    @Override
    public void dispose() {
        System.out.println("🗑️ DressingHouseState disposing...");

        if (background != null) background.dispose();
        if (buttonTexture != null) buttonTexture.dispose();
        if (buttonHoverTexture != null) buttonHoverTexture.dispose();
        if (font != null) font.dispose();

        // Release button back to pool
        if (saveBtn != null && buttonManager != null) {
            buttonManager.releaseButton(saveBtn);
            saveBtn = null;
        }

        System.out.println("✅ DressingHouseState disposed");
    }
}
