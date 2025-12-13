package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.strategies.EasyDatingStrategy;
import com.finpro.frontend.strategies.HardDatingStrategy;
import com.finpro.frontend.strategies.MediumDatingStrategy;

public class DatingHouseState implements GameState {
    private Texture background;
    private BitmapFont font;
    private GameStateManager gsm;
    private ButtonManager buttonManager;

    // Boy selection buttons
    private Button alexButton;
    private Button brianButton;
    private Button chrisButton;

    // Boy profile textures
    private Texture alexProfile;
    private Texture brianProfile;
    private Texture chrisProfile;

    // Hover textures (optional - can be same as normal or slightly different)
    private Texture alexProfileHover;
    private Texture brianProfileHover;
    private Texture chrisProfileHover;

    public DatingHouseState(GameStateManager gsm, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.buttonManager = buttonManager;

        background = new Texture("dating/BackgroundDatingState.png");
        font = new BitmapFont();

        // Load boy profile textures
        alexProfile = new Texture("dating/alex_profile.png");
        brianProfile = new Texture("dating/brian_profile.png");
        chrisProfile = new Texture("dating/chris_profile.png");

        // Load hover textures (you can create highlighted versions or use same textures)
        alexProfileHover = new Texture("dating/alex_profile.png");
        brianProfileHover = new Texture("dating/brian_profile.png");
        chrisProfileHover = new Texture("dating/chris_profile.png");

        // Create boy buttons using ButtonManager
        float centerX = Gdx.graphics.getWidth() / 2f;

        alexButton = buttonManager.createButton(
            "Alex",
            centerX - 700,
            200,
            alexProfile.getWidth(),
            alexProfile.getHeight(),
            alexProfile,
            alexProfileHover
        );

        brianButton = buttonManager.createButton(
            "Brian",
            centerX - 200,
            200,
            brianProfile.getWidth(),
            brianProfile.getHeight(),
            brianProfile,
            brianProfileHover
        );

        chrisButton = buttonManager.createButton(
            "Chris",
            centerX + 300,
            200,
            chrisProfile.getWidth(), chrisProfile.getHeight(),
            chrisProfile,
            chrisProfileHover
        );
    }

    @Override
    public void update(float delta) {
        // Update all buttons
        alexButton.update();
        brianButton.update();
        chrisButton.update();

        // Check button clicks
        if (alexButton.isClicked()) {
            gsm.push(new StoryState(gsm, new EasyDatingStrategy(), "ALEX", buttonManager));
        }
        if (brianButton.isClicked()) {
            gsm.push(new StoryState(gsm, new MediumDatingStrategy(), "BRIAN", buttonManager));
        }
        if (chrisButton.isClicked()) {
            gsm.push(new StoryState(gsm, new HardDatingStrategy(), "CHRIS", buttonManager));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        // Draw background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw title (optional)
        font.getData().setScale(2f);
        // Add your title text here if needed
        font.getData().setScale(1f);

        // Draw buttons
        alexButton.render(batch, font);
        brianButton.render(batch, font);
        chrisButton.render(batch, font);

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
    }
}
