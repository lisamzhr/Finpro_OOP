package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.finpro.frontend.MusicManager;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.ChallengeObjectManager;
import com.finpro.frontend.factory.ChallengeObjectFactory;
import com.finpro.frontend.strategies.*;
import java.util.ArrayList;
import java.util.List;

public class ChallengeState implements GameState {
    protected GameStateManager gsm;
    private Player player;
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

    // ✅ Add GlyphLayout for text wrapping
    private GlyphLayout layout;

    // ✅ Add texture for text background box
    private Texture textBoxBackground;

    public ChallengeState(GameStateManager gsm, DatingStrategy datingStrategy,
                          String boyId, int conversationPoints, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.player = gsm.getPlayer();
        this.datingStrategy = datingStrategy;
        this.boyId = boyId;
        this.conversationPoints = conversationPoints;
        this.buttonManager = buttonManager;
        this.showContinueButton = false;

        background = new Texture("dating/" + boyId.toLowerCase() + "_Background_Chall.png");
        font = new BitmapFont();
        layout = new GlyphLayout(); // ✅ Initialize layout

        textBoxBackground = new Texture("dating/decisionBox.png");

        ChallengeObjectFactory factory = new ChallengeObjectFactory();
        challengeObjectManager = new ChallengeObjectManager(factory);

        challengeGame = createChallengeStrategy(boyId, challengeObjectManager);
        challengeGame.start();

        buttonTexture = new Texture("button_normal.png");
        buttonHoverTexture = new Texture("button_hover.png");
        MusicManager.getInstance().playMusic(MusicManager.DATING_MUSIC);

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

        if (player != null) {
            System.out.println("ChallengeState - Player: " + player.getUsername() + " | Skin ID: " + player.getSelectedSkinId());
        }
    }

    private void setupInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                float y = Gdx.graphics.getHeight() - screenY;
                challengeGame.handleClick(screenX, y);
                return true;
            }
        });
    }

    private ChallengeGame createChallengeStrategy(String boyId,
                                                  ChallengeObjectManager manager) {
        switch (boyId) {
            case "ALEX":
                return new EasyChallenge(manager);
            case "BRIAN":
                return new MediumChallenge(manager);
            case "CHRIS":
                return new HardChallenge(manager);
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

        // Draw background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // ✅ Draw title dengan text wrapping
        float titleBoxWidth = 300;
        float titleBoxHeight = 50;
        float titleBoxX = Gdx.graphics.getWidth() / 2f - titleBoxWidth / 2;
        float titleBoxY = Gdx.graphics.getHeight() - 100;

        drawWrappedText(batch, font, textBoxBackground,
            "CHALLENGE TIME!",
            titleBoxX, titleBoxY, titleBoxWidth, titleBoxHeight);

        // Draw challenge game
        challengeGame.render(batch, font);

        // Draw continue button if game completed
        if (showContinueButton) {
            continueButton.render(batch, font);
        }

        batch.end();
    }

    // ✅ Method untuk text wrapping
    private void drawWrappedText(SpriteBatch batch, BitmapFont font, Texture background,
                                 String text, float boxX, float boxY, float boxWidth, float boxHeight) {
        // Draw background box
        batch.draw(background, boxX, boxY, boxWidth, boxHeight);

        // Text wrapping
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        List<String> lines = new ArrayList<>();
        float maxWidth = boxWidth - 40; // Padding kiri-kanan

        for (String word : words) {
            String testLine = line + word + " ";
            layout.setText(font, testLine);

            if (layout.width > maxWidth) {
                lines.add(line.toString().trim());
                line = new StringBuilder(word + " ");
            } else {
                line.append(word).append(" ");
            }
        }
        lines.add(line.toString().trim());

        // Draw text (centered in box)
        font.getData().setScale(2f);
        font.setColor(0, 0, 0, 1); // Black text

        float lineHeight = 30;
        float totalTextHeight = lines.size() * lineHeight;
        float textY = boxY + boxHeight/2 + totalTextHeight/2;

        for (String textLine : lines) {
            layout.setText(font, textLine);
            float textX = boxX + (boxWidth - layout.width) / 2; // Center horizontally
            font.draw(batch, textLine, textX, textY);
            textY -= lineHeight;
        }

        // Reset color & scale
        font.setColor(1, 1, 1, 1);
        font.getData().setScale(1f);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);

        background.dispose();
        font.dispose();

        if (textBoxBackground != null) {
            textBoxBackground.dispose();
        }

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
