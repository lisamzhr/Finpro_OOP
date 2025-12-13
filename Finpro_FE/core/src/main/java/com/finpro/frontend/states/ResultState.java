package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.strategies.DatingStrategy;

public class ResultState implements GameState {
    protected GameStateManager gsm;
    private Texture background;
    private Texture resultImage;
    private BitmapFont font;
    private DatingStrategy strategy;
    private String boyId;
    private int totalPoints;
    private boolean passed;
    private String resultMessage;
    private ButtonManager buttonManager;

    private Button backButton;
    private Texture buttonTexture;
    private Texture buttonHoverTexture;

    public ResultState(GameStateManager gsm, DatingStrategy strategy,
                       String boyId, int totalPoints, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.strategy = strategy;
        this.boyId = boyId;
        this.totalPoints = totalPoints;
        this.buttonManager = buttonManager;
        this.passed = strategy.isPass(totalPoints);

        background = new Texture("dating/" + boyId.toLowerCase() + "_Background_Chall.png");
        resultImage = new Texture(passed ? "dating/background.png" : "dating/background.png");
        font = new BitmapFont();

        // Get final message from strategy
        resultMessage = strategy.getFinalMessage(totalPoints);

        // Load button textures
        buttonTexture = new Texture("button_normal.png");
        buttonHoverTexture = new Texture("button_hover.png");

        // Create back button using ButtonManager
        backButton = buttonManager.createButton(
            "Back to House",
            Gdx.graphics.getWidth() / 2f - 100,
            100,
            200,
            60,
            buttonTexture,
            buttonHoverTexture
        );
    }

    @Override
    public void update(float delta) {
        backButton.update();

        if (backButton.isClicked()) {
            // Pop multiple states to go back to DatingHouseState
            gsm.pop(); // ResultState
            gsm.pop(); // ChallengeState
            gsm.pop(); // DatingConversationState
            gsm.pop(); // StoryState
            // Now back at DatingHouseState
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw result image
        batch.draw(resultImage,
            Gdx.graphics.getWidth() / 2f - 200,
            Gdx.graphics.getHeight() / 2f,
            400, 300);

        // Draw result text
        font.getData().setScale(2.5f);
        String title = passed ? "SUCCESS!" : "FAILED...";
        font.draw(batch, title,
            Gdx.graphics.getWidth() / 2f - 100,
            Gdx.graphics.getHeight() - 100);

        font.getData().setScale(1.5f);
        font.draw(batch, "Total Points: " + totalPoints,
            Gdx.graphics.getWidth() / 2f - 100,
            Gdx.graphics.getHeight() - 150);

        font.draw(batch, resultMessage,
            Gdx.graphics.getWidth() / 2f - 300, 300);

        // Draw back button
        backButton.render(batch, font);

        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        resultImage.dispose();
        font.dispose();

        // Dispose textures
        if (buttonTexture != null) {
            buttonTexture.dispose();
        }
        if (buttonHoverTexture != null) {
            buttonHoverTexture.dispose();
        }

        // Release button back to pool
        if (backButton != null) {
            buttonManager.releaseButton(backButton);
            backButton = null;
        }
    }
}
