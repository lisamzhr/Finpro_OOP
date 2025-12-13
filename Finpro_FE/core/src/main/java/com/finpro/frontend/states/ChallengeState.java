package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.strategies.ChallengeGame;
import com.finpro.frontend.strategies.DatingStrategy;

public class ChallengeState implements GameState {
    protected GameStateManager gsm;
    private Texture background;
    private BitmapFont font;
    private DatingStrategy strategy;
    private ChallengeGame challenge;
    private String boyId;
    private int totalPoints;
    private ButtonManager buttonManager;

    private String challengeDescription;
    private Button acceptButton;
    private Button declineButton;
    private Texture buttonTexture;
    private Texture buttonHoverTexture;

    // State tracking
    private enum ChallengePhase {
        INTRO,      // Showing challenge description with accept/decline
        PLAYING,    // Actually playing the mini-game
        COMPLETED   // Challenge finished, ready to transition
    }
    private ChallengePhase currentPhase;

    public ChallengeState(GameStateManager gsm, DatingStrategy strategy,
                          String boyId, int totalPoints, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.strategy = strategy;
        this.boyId = boyId;
        this.totalPoints = totalPoints;
        this.buttonManager = buttonManager;
        this.currentPhase = ChallengePhase.INTRO;

        background = new Texture("dating/" + boyId.toLowerCase() + "_Background_Chall.png");
        font = new BitmapFont();

        challengeDescription = strategy.getChallengeDescription();

        // Create the actual challenge game instance
        challenge = strategy.createChallenge();

        // Load button textures
        buttonTexture = new Texture("button_normal.png");
        buttonHoverTexture = new Texture("button_hover.png");

        float centerX = Gdx.graphics.getWidth() / 2f;

        // Create buttons using ButtonManager
        acceptButton = buttonManager.createButton(
            "Accept Challenge",
            centerX - 250,
            200,
            200,
            60,
            buttonTexture,
            buttonHoverTexture
        );

        declineButton = buttonManager.createButton(
            "Decline (-5)",
            centerX + 50,
            200,
            200,
            60,
            buttonTexture,
            buttonHoverTexture
        );
    }

    @Override
    public void update(float delta) {
        switch (currentPhase) {
            case INTRO:
                updateIntroPhase();
                break;
            case PLAYING:
                updatePlayingPhase(delta);
                break;
            case COMPLETED:
                // Transition to result state
                int challengeScore = challenge.getFinalScore();
                totalPoints += challengeScore;
                gsm.push(new ResultState(gsm, strategy, boyId, totalPoints, buttonManager));
                break;
        }
    }

    private void updateIntroPhase() {
        acceptButton.update();
        declineButton.update();

        if (acceptButton.isClicked()) {
            // Player accepted - start the actual challenge
            currentPhase = ChallengePhase.PLAYING;
            challenge.start();

            // Clean up intro buttons
            buttonManager.releaseButton(acceptButton);
            buttonManager.releaseButton(declineButton);
            acceptButton = null;
            declineButton = null;
        }

        if (declineButton.isClicked()) {
            // Player declined - lose points and go directly to results
            totalPoints -= 5;
            gsm.push(new ResultState(gsm, strategy, boyId, totalPoints, buttonManager));
        }
    }

    private void updatePlayingPhase(float delta) {
        challenge.update(delta);

        if (challenge.isCompleted()) {
            currentPhase = ChallengePhase.COMPLETED;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        // Always draw background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        switch (currentPhase) {
            case INTRO:
                renderIntroPhase(batch);
                break;
            case PLAYING:
            case COMPLETED:
                renderPlayingPhase(batch);
                break;
        }

        batch.end();
    }

    private void renderIntroPhase(SpriteBatch batch) {
        font.getData().setScale(2f);
        font.draw(batch, "CHALLENGE TIME!",
            Gdx.graphics.getWidth() / 2f - 150,
            Gdx.graphics.getHeight() - 100);

        font.getData().setScale(1.5f);
        font.draw(batch, challengeDescription,
            Gdx.graphics.getWidth() / 2f - 300, 500);

        // Draw buttons
        acceptButton.render(batch, font);
        declineButton.render(batch, font);
    }

    private void renderPlayingPhase(SpriteBatch batch) {
        // Let the challenge game render itself
        challenge.render(batch, font);
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();

        // Dispose challenge
        if (challenge != null) {
            challenge.dispose();
        }

        // Dispose textures
        if (buttonTexture != null) {
            buttonTexture.dispose();
        }
        if (buttonHoverTexture != null) {
            buttonHoverTexture.dispose();
        }

        // Release buttons back to pool (if they still exist)
        if (acceptButton != null) {
            buttonManager.releaseButton(acceptButton);
            acceptButton = null;
        }
        if (declineButton != null) {
            buttonManager.releaseButton(declineButton);
            declineButton = null;
        }
    }
}
