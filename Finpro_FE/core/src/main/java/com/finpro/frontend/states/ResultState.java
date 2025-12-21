package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.finpro.frontend.MusicManager;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.services.BackendService;
import com.finpro.frontend.strategies.DatingStrategy;

import java.util.ArrayList;
import java.util.List;

public class ResultState implements GameState {
    protected GameStateManager gsm;
    private Player player;
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
    private BackendService backendService;
    private GlyphLayout layout;
    private Texture textBox;

    public ResultState(GameStateManager gsm, DatingStrategy strategy,
                       String boyId, int totalPoints, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.player = gsm.getPlayer();
        this.strategy = strategy;
        this.boyId = boyId;
        this.totalPoints = totalPoints;
        this.buttonManager = buttonManager;
        this.passed = strategy.isPass(totalPoints);
        textBox = new Texture("dating/decisionBox.png");
        layout = new GlyphLayout();

        backendService = new BackendService();
        MusicManager.getInstance().playMusic(MusicManager.DATING_MUSIC);

        background = new Texture("dating/" + this.boyId + "_Background_Conv.png");
        resultImage = new Texture(passed ?
            "dating/" + this.boyId + "_success.png"
            : "dating/" + this.boyId + "_failed.png"
        );
        font = new BitmapFont();

        resultMessage = strategy.getFinalMessage(totalPoints);

        buttonTexture = new Texture("button_normal.png");
        buttonHoverTexture = new Texture("button_hover.png");

        backButton = buttonManager.createButton(
            "Back to House",
            Gdx.graphics.getWidth() / 2f - 100,
            100,
            200,
            60,
            buttonTexture,
            buttonHoverTexture
        );

        if (passed) {
            Player player = gsm.getPlayer();
            if(player != null){
                player.setLevel(player.getLevel() + 1);
            }
        }
    }

    @Override
    public void update(float delta) {
        backButton.update();

        if (backButton.isClicked()) {
            gsm.pop(); // ResultState
            gsm.pop(); // ChallengeState
            gsm.pop(); // DatingConversationState
            gsm.pop(); // StoryState
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float maxWidth = 500f;
        float maxHeight = 350f;

        float imgW = resultImage.getWidth();
        float imgH = resultImage.getHeight();

        float scale = Math.min(maxWidth / imgW, maxHeight / imgH);

        float drawW = imgW * scale;
        float drawH = imgH * scale;

        float x = (Gdx.graphics.getWidth() - drawW) / 2f;
        float y = (Gdx.graphics.getHeight() - drawH) / 2f + 50;

        batch.draw(resultImage, x, y, drawW, drawH);

        font.getData().setScale(2.5f);
        font.setColor(Color.WHITE);
        String title = passed ? "SUCCESS!" : "FAILED...";
        layout.setText(font, title);

        float titlePadding = 25;
        float titleBoxWidth = layout.width + (titlePadding * 2);
        float titleBoxHeight = layout.height + (titlePadding * 1.2f);
        float titleY = Gdx.graphics.getHeight() - 100;
        float titleBoxX = (Gdx.graphics.getWidth() - titleBoxWidth) / 2f;
        float titleBoxY = titleY - (layout.height / 2f) - (titleBoxHeight / 2f);

        batch.draw(textBox, titleBoxX, titleBoxY, titleBoxWidth, titleBoxHeight);
        font.draw(batch, title,
            (Gdx.graphics.getWidth() - layout.width) / 2f,
            titleY);
        font.getData().setScale(1.5f);
        String pointsText = "Total Points: " + totalPoints;
        layout.setText(font, pointsText);

        float pointsPadding = 20;
        float pointsBoxWidth = layout.width + (pointsPadding * 2);
        float pointsBoxHeight = layout.height + (pointsPadding * 1.2f);
        float pointsY = Gdx.graphics.getHeight() - 160;
        float pointsBoxX = (Gdx.graphics.getWidth() - pointsBoxWidth) / 2f;
        float pointsBoxY = pointsY - (layout.height / 2f) - (pointsBoxHeight / 2f);

        batch.draw(textBox, pointsBoxX, pointsBoxY, pointsBoxWidth, pointsBoxHeight);
        font.draw(batch, pointsText,
            (Gdx.graphics.getWidth() - layout.width) / 2f,
            pointsY);

        float textMaxWidth = 850;
        float padding = 30;
        float lineHeight = 35;
        font.getData().setScale(1.2f);
        font.setColor(Color.WHITE);

        String[] words = resultMessage.split(" ");
        StringBuilder line = new StringBuilder();
        List<String> lines = new ArrayList<>();

        for (String word : words) {
            String testLine = line + word + " ";
            layout.setText(font, testLine);

            if (layout.width > textMaxWidth) {
                lines.add(line.toString().trim());
                line = new StringBuilder(word + " ");
            } else {
                line.append(word).append(" ");
            }
        }
        lines.add(line.toString().trim());

        float maxLineWidth = 0;
        for (String textLine : lines) {
            layout.setText(font, textLine);
            if (layout.width > maxLineWidth) {
                maxLineWidth = layout.width;
            }
        }

        float boxHeight = (lines.size() * lineHeight) + (padding * 2) + 10;
        float boxWidth = maxLineWidth + (padding * 2);

        float boxX = (Gdx.graphics.getWidth() - boxWidth) / 2f;
        float boxY = 200;

        batch.draw(textBox, boxX, boxY, boxWidth, boxHeight);
        float textY = boxY + boxHeight - padding - 15;
        for (String textLine : lines) {
            layout.setText(font, textLine);
            float textX = boxX + padding;
            font.draw(batch, textLine, textX, textY);
            textY -= lineHeight;
        }

        font.getData().setScale(1f);
        font.setColor(Color.WHITE);

        backButton.render(batch, font);

        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        resultImage.dispose();
        font.dispose();
        textBox.dispose();
        if (buttonTexture != null) {
            buttonTexture.dispose();
        }
        if (buttonHoverTexture != null) {
            buttonHoverTexture.dispose();
        }
        if (backButton != null) {
            buttonManager.releaseButton(backButton);
            backButton = null;
        }
    }
}
