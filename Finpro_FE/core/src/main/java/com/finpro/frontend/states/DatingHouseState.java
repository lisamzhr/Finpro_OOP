package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.finpro.frontend.models.BoyButton;
import com.finpro.frontend.strategies.EasyDatingStrategy;
import com.finpro.frontend.strategies.HardDatingStrategy;
import com.finpro.frontend.strategies.MediumDatingStrategy;

public class DatingHouseState implements GameState {
    private Texture background;
    private BitmapFont font;
    private GameStateManager gsm;

    // Boy selection buttons
    private BoyButton alexButton;
    private BoyButton brianButton;
    private BoyButton chrisButton;

    public DatingHouseState(GameStateManager gsm) {
        this.gsm = gsm;

        background = new Texture("dating/BackgroundDatingState.png");
        font = new BitmapFont();

        // Create boy buttons (posisi sesuaikan dengan layout kamu)
        float centerX = Gdx.graphics.getWidth() / 2f;
        alexButton = new BoyButton("Alex", "dating/alex_profile.png",
            centerX - 700, 200, "ALEX");
        brianButton = new BoyButton("Brian", "dating/brian_profile.png",
            centerX - 200, 200, "BRIAN");
        chrisButton = new BoyButton("Chris", "dating/chris_profile.png",
            centerX + 300, 200, "CHRIS");
    }

    @Override
    public void update(float delta) {
        // Check button clicks
        if (alexButton.isClicked()) {
            gsm.push(new StoryState(gsm, new EasyDatingStrategy(), "ALEX"));
        }
        if (brianButton.isClicked()) {
            gsm.push(new StoryState(gsm, new MediumDatingStrategy(), "BRIAN"));
        }
        if (chrisButton.isClicked()) {
            gsm.push(new StoryState(gsm, new HardDatingStrategy(), "CHRIS"));
        }

        alexButton.update();
        brianButton.update();
        chrisButton.update();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        // Draw background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw title
        font.getData().setScale(2f);

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
        alexButton.dispose();
        brianButton.dispose();
        chrisButton.dispose();
    }
}
