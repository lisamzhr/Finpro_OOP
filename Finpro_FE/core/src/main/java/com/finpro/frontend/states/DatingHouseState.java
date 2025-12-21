package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.finpro.frontend.GameManager;
import com.finpro.frontend.MusicManager;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.strategies.EasyDatingStrategy;
import com.finpro.frontend.strategies.HardDatingStrategy;
import com.finpro.frontend.strategies.MediumDatingStrategy;

public class DatingHouseState implements GameState {
    private Texture background;
    private BitmapFont font;
    private GameStateManager gsm;
    private Player player;
    private ButtonManager buttonManager;
    private GameManager gameManager;

    // Boy selection buttons
    private Button alexButton;
    private Button brianButton;
    private Button chrisButton;

    // Boy profile textures
    private Texture alexProfile;
    private Texture brianProfile;
    private Texture chrisProfile;

    // Hover textures
    private Texture alexProfileHover;
    private Texture brianProfileHover;
    private Texture chrisProfileHover;

    //back button
    private Button backButton;
    private Texture buttonTexture;
    private Texture buttonHoverTexture;

    // ✅ NEW: Error message display
    private String errorMessage = "";
    private float errorMessageTimer = 0;

    public DatingHouseState(GameStateManager gsm, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.player = gsm.getPlayer();
        this.buttonManager = buttonManager;
        gameManager = GameManager.getInstance();

        background = new Texture("dating/BackgroundDatingState.png");
        font = new BitmapFont();

        // Load boy profile textures
        alexProfile = new Texture("dating/alex_profile.png");
        brianProfile = new Texture("dating/brian_profile.png");
        chrisProfile = new Texture("dating/chris_profile.png");

        // Load hover textures
        alexProfileHover = new Texture("dating/alex_profile.png");
        brianProfileHover = new Texture("dating/brian_profile.png");
        chrisProfileHover = new Texture("dating/chris_profile.png");

        //back to menu
        buttonTexture = new Texture("dressing/homeButton.png");
        buttonHoverTexture = new Texture("dressing/homeButton.png");
        backButton = buttonManager.createButtonNoText(
            50,
            100,
            buttonTexture.getWidth(),
            buttonTexture.getHeight(),
            buttonTexture,
            buttonHoverTexture
        );

        MusicManager.getInstance().playMusic(MusicManager.DATING_MUSIC);
        // Create boy buttons using ButtonManager
        float centerX = Gdx.graphics.getWidth() / 2f;

        alexButton = buttonManager.createButton(
            "",
            centerX - 700,
            200,
            alexProfile.getWidth(),
            alexProfile.getHeight(),
            alexProfile,
            alexProfileHover
        );

        brianButton = buttonManager.createButton(
            "",
            centerX - 200,
            200,
            brianProfile.getWidth(),
            brianProfile.getHeight(),
            brianProfile,
            brianProfileHover
        );

        chrisButton = buttonManager.createButton(
            "",
            centerX + 300,
            200,
            chrisProfile.getWidth(),
            chrisProfile.getHeight(),
            chrisProfile,
            chrisProfileHover
        );

        System.out.println("DatingHouse - Active buttons: " + buttonManager.getActiveCount());

        // Debug: Check player
        if (player != null) {
            System.out.println("DatingHouse - Player: " + player.getUsername() + " | Skin ID: " + player.getSelectedSkinId());
        }
    }

    // ✅ Helper methods for skin validation
    private static boolean isSkinCompatible(String boyName, int skinId) {
        switch (boyName) {
            case "ALEX":
                return skinId == 0 || skinId == 1; // Casual, Formal
            case "BRIAN":
                return skinId == 2 || skinId == 3; // Sport, Traditional
            case "CHRIS":
                return skinId == 4 || skinId == 5; // Modern, Elegant
            default:
                return false;
        }
    }

    private static String getErrorMessage(String boyName) {
        switch (boyName) {
            case "ALEX":
                return "Alex likes Casual or Formal style!";
            case "BRIAN":
                return "Brian prefers Sport or Traditional style!";
            case "CHRIS":
                return "Chris loves Modern or Elegant style!";
            default:
                return "Choose the right outfit!";
        }
    }

    private void showError(String message) {
        this.errorMessage = message;
        this.errorMessageTimer = 3.0f; // Show for 3 seconds
        System.out.println("Error: " + message);
    }

    @Override
    public void update(float delta) {
        // Update all buttons
        alexButton.update();
        brianButton.update();
        chrisButton.update();
        backButton.update();

        // ✅ Countdown error message timer
        if (errorMessageTimer > 0) {
            errorMessageTimer -= delta;
            if (errorMessageTimer <= 0) {
                errorMessage = "";
            }
        }

        //PRIORITAS: Cek back button DULU sebelum yang lain
        if (backButton.isClicked()) {
            System.out.println("Back button clicked! Returning to menu...");
            gsm.set(new MenuState(gsm, buttonManager)); // ✅ GANTI dari pop() ke set()
            return;
        }

        // ✅ Get player's current skin
        int playerSkinId = player.getSelectedSkinId();

        // ✅ Check button clicks with validation
        if (alexButton.isClicked()) {
            if (player.getLevel() == 1) {
                if (isSkinCompatible("ALEX", playerSkinId)) {
                    gsm.push(new StoryState(gsm, new EasyDatingStrategy(), "ALEX", buttonManager));
                } else {
                    showError(getErrorMessage("ALEX"));
                }
            } else {
                showError("Complete previous level first!");
            }
        }

        if (brianButton.isClicked()) {
            if (player.getLevel() == 2) {
                if (isSkinCompatible("BRIAN", playerSkinId)) {
                    gsm.push(new StoryState(gsm, new MediumDatingStrategy(), "BRIAN", buttonManager));
                } else {
                    showError(getErrorMessage("BRIAN"));
                }
            } else {
                showError("Complete previous level first!");
            }
        }

        if (chrisButton.isClicked()) {
            if (player.getLevel() == 3) {
                if (isSkinCompatible("CHRIS", playerSkinId)) {
                    gsm.push(new StoryState(gsm, new HardDatingStrategy(), "CHRIS", buttonManager));
                } else {
                    showError(getErrorMessage("CHRIS"));
                }
            } else {
                showError("Complete previous level first!");
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        // Draw background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw boy buttons
        alexButton.render(batch, font);
        brianButton.render(batch, font);
        chrisButton.render(batch, font);

        //back button
        backButton.render(batch, font);

        //Render error message if exists
        if (!errorMessage.isEmpty()) {
            font.getData().setScale(1.5f);
            font.setColor(Color.RED);
            font.draw(batch, errorMessage,
                Gdx.graphics.getWidth()/2 - 250,
                150);
            font.setColor(Color.WHITE);
            font.getData().setScale(1f);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();

        // Dispose textures
        alexProfile.dispose();
        brianProfile.dispose();
        chrisProfile.dispose();
        alexProfileHover.dispose();
        brianProfileHover.dispose();
        chrisProfileHover.dispose();

        // Release buttons back to pool
        if (buttonTexture != null) {
            buttonTexture.dispose();
        }
        if (buttonHoverTexture != null) {
            buttonHoverTexture.dispose();
        }

        if (alexButton != null) {
            buttonManager.releaseButton(alexButton);
            alexButton = null;
        }
        if (brianButton != null) {
            buttonManager.releaseButton(brianButton);
            brianButton = null;
        }
        if (chrisButton != null) {
            buttonManager.releaseButton(chrisButton);
            chrisButton = null;
        }
        if (backButton != null) {
            buttonManager.releaseButton(backButton);
            backButton = null;
        }
    }
}
