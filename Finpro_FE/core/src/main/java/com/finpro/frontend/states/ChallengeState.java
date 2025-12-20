package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter; // TAMBAHKAN
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.ChallengeObjectManager;
import com.finpro.frontend.factory.ChallengeObjectFactory;
import com.finpro.frontend.strategies.DatingStrategy;
import com.finpro.frontend.strategies.ChallengeGame;
import com.finpro.frontend.strategies.MediumChallenge;

public class ChallengeState implements GameState {
    protected GameStateManager gsm;
    private Texture background;
    private BitmapFont font;
    private DatingStrategy datingStrategy;
    private String boyId;
    private int conversationPoints;
    private ButtonManager buttonManager;

    private ChallengeObjectManager challengeObjectManager;
    private ChallengeGame challengeGame;

    private Button continueButton;
    private Texture buttonTexture;
    private Texture buttonHoverTexture;
    private boolean showContinueButton;

    public ChallengeState(GameStateManager gsm, DatingStrategy datingStrategy,
                          String boyId, int conversationPoints, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.datingStrategy = datingStrategy;
        this.boyId = boyId;
        this.conversationPoints = conversationPoints;
        this.buttonManager = buttonManager;
        this.showContinueButton = false;

        background = new Texture("dating/" + boyId.toLowerCase() + "_Background_Chall.png");
        font = new BitmapFont();

        ChallengeObjectFactory factory = new ChallengeObjectFactory();
        challengeObjectManager = new ChallengeObjectManager(factory);

        challengeGame = createChallengeStrategy(boyId, challengeObjectManager);
        challengeGame.start();

        buttonTexture = new Texture("button_normal.png");
        buttonHoverTexture = new Texture("button_hover.png");

        float centerX = Gdx.graphics.getWidth() / 2f;

        continueButton = buttonManager.createButton(
            "Continue",
            centerX - 100,
            100,
            200,
            60,
            buttonTexture,
            buttonHoverTexture
        );

        setupInputProcessor();
    }

    private void setupInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // Flip Y coordinate karena LibGDX coordinate system
                float y = Gdx.graphics.getHeight() - screenY;

                // Pass click ke challenge game
                challengeGame.handleClick(screenX, y);

                return true;
            }
        });
    }

    private ChallengeGame createChallengeStrategy(String boyId,
                                                  ChallengeObjectManager manager) {
        switch (boyId) {
            case "ALEX":
                return new MediumChallenge(manager);
            case "BRIAN":
                return new MediumChallenge(manager);
            case "CHRIS":
                return new MediumChallenge(manager);
            default:
                return new MediumChallenge(manager);
        }
    }

    @Override
    public void update(float delta) {
        if (!challengeGame.isCompleted()) {
            challengeGame.update(delta);
        } else {
            showContinueButton = true;
        }

        if (showContinueButton) {
            continueButton.update();

            if (continueButton.isClicked()) {
                int challengeScore = challengeGame.getFinalScore();
                int totalPoints = conversationPoints + challengeScore;

                System.out.println("Conversation Points: " + conversationPoints);
                System.out.println("Challenge Score: " + challengeScore);
                System.out.println("Total Points: " + totalPoints);

                gsm.push(new ResultState(gsm, datingStrategy, boyId, totalPoints, buttonManager));
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font.getData().setScale(2f);
        font.draw(batch, "CHALLENGE TIME!",
            Gdx.graphics.getWidth() / 2f - 150,
            Gdx.graphics.getHeight() - 50);

        challengeGame.render(batch, font);

        if (showContinueButton) {
            continueButton.render(batch, font);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        // ========== TAMBAHKAN INI ==========
        // Clear input processor saat state di-dispose
        Gdx.input.setInputProcessor(null);

        background.dispose();
        font.dispose();

        if (challengeGame != null) {
            challengeGame.dispose();
        }

        if (challengeObjectManager != null) {
            challengeObjectManager.dispose();
        }

        if (buttonTexture != null) {
            buttonTexture.dispose();
        }
        if (buttonHoverTexture != null) {
            buttonHoverTexture.dispose();
        }

        if (continueButton != null) {
            buttonManager.releaseButton(continueButton);
            continueButton = null;
        }
    }
}
