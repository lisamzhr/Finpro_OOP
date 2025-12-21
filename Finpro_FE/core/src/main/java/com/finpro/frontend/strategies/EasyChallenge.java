package com.finpro.frontend.strategies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.finpro.frontend.ChallengeObjectManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EasyChallenge implements ChallengeGame {
    private ChallengeObjectManager objectManager;
    private Texture ingredientTexture;
    private Texture badIngredientTexture;
    private float spawnTimer;
    private float gameTimer;
    private int score;
    private boolean completed;
    private Random random;

    // ✅ Add for text wrapping
    private GlyphLayout layout;
    private Texture textBoxBackground;

    private static final float SPAWN_INTERVAL = 1.5f;
    private static final float GAME_DURATION = 15.0f;
    private static final int INGREDIENT_SIZE = 300;

    // Constructor with Dependency Injection
    public EasyChallenge(ChallengeObjectManager objectManager) {
        this.objectManager = objectManager;
        ingredientTexture = new Texture("dating/alex_cup_cake.png");
        badIngredientTexture = new Texture("dating/alex_stroberi_cake.png");

        // ✅ Initialize text wrapping components
        layout = new GlyphLayout();
        textBoxBackground = new Texture("dating/decisionBox.png");

        random = new Random();
        score = 0;
        spawnTimer = 0;
        gameTimer = 0;
        completed = false;
    }

    @Override
    public void start() {
        score = 0;
        spawnTimer = 0;
        gameTimer = 0;
        completed = false;
        objectManager.clearAll();
    }

    @Override
    public void update(float delta) {
        if (completed) return;

        gameTimer += delta;
        spawnTimer += delta;

        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnIngredient();
            spawnTimer = 0;
        }

        objectManager.update(delta);

        score += objectManager.checkClicked();

        if (gameTimer >= GAME_DURATION) {
            completed = true;
        }
    }

    private void spawnIngredient() {
        float x = random.nextInt(Gdx.graphics.getWidth() - INGREDIENT_SIZE);
        float y = random.nextInt(Gdx.graphics.getHeight() - 200) + 100;

        // 70% good, 30% bad
        boolean isGood = random.nextFloat() < 0.6f;
        Texture texture = isGood ? ingredientTexture : badIngredientTexture;
        int points = isGood ? 3 : -2;

        // Move horizontally
        float velocityX = -50 + random.nextInt(100);

        objectManager.spawnMovingObject(x, y, ingredientTexture.getWidth()/2, ingredientTexture.getHeight()/2,
            texture, points, velocityX, 0);
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        objectManager.render(batch);

        // ✅ Timer box (kiri atas)
        float timerBoxWidth = 150;
        float timerBoxHeight = 50;
        float timerBoxX = 30;
        float timerBoxY = Gdx.graphics.getHeight() - 70;

        String timerText = "Time: " + (int)(GAME_DURATION - gameTimer) + "s";
        drawWrappedText(batch, font, textBoxBackground, timerText,
            timerBoxX, timerBoxY, timerBoxWidth, timerBoxHeight, 1.5f);

        // ✅ Score box (kanan atas)
        float scoreBoxWidth = 150;
        float scoreBoxHeight = 50;
        float scoreBoxX = Gdx.graphics.getWidth() - scoreBoxWidth - 30;
        float scoreBoxY = Gdx.graphics.getHeight() - 70;

        String scoreText = "Score: " + score;
        drawWrappedText(batch, font, textBoxBackground, scoreText,
            scoreBoxX, scoreBoxY, scoreBoxWidth, scoreBoxHeight, 1.5f);

        // ✅ Instruction box (tengah atas)
        float instructionBoxWidth = 400;
        float instructionBoxHeight = 60;
        float instructionBoxX = Gdx.graphics.getWidth() / 2f - instructionBoxWidth / 2;
        float instructionBoxY = Gdx.graphics.getHeight() - 150;

        String instructionText = "Click Cup cakes! Avoid Strawberry cake ones!";
        drawWrappedText(batch, font, textBoxBackground, instructionText,
            instructionBoxX, instructionBoxY, instructionBoxWidth, instructionBoxHeight, 1f);

        // ✅ Completion message box (tengah layar)
        if (completed) {
            float completeBoxWidth = 400;
            float completeBoxHeight = 100;
            float completeBoxX = Gdx.graphics.getWidth() / 2f - completeBoxWidth / 2;
            float completeBoxY = Gdx.graphics.getHeight() / 2f - completeBoxHeight / 2;

            String completeText = "Challenge Complete!";
            drawWrappedText(batch, font, textBoxBackground, completeText,
                completeBoxX, completeBoxY, completeBoxWidth, completeBoxHeight, 2f);
        }
    }

    public void handleClick(float x, float y) {
        if (completed) return;
        objectManager.handleClick(x, y);
    }

    // ✅ Enhanced drawWrappedText dengan parameter scale
    private void drawWrappedText(SpriteBatch batch, BitmapFont font, Texture background,
                                 String text, float boxX, float boxY, float boxWidth, float boxHeight, float scale) {
        // Draw background box
        batch.draw(background, boxX, boxY, boxWidth, boxHeight);

        // Set font scale first
        font.getData().setScale(scale);

        // Text wrapping
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        List<String> lines = new ArrayList<>();
        float maxWidth = boxWidth - 40; // Padding kiri-kanan

        for (String word : words) {
            String testLine = line + word + " ";
            layout.setText(font, testLine);

            if (layout.width > maxWidth) {
                if (line.length() > 0) {
                    lines.add(line.toString().trim());
                    line = new StringBuilder(word + " ");
                } else {
                    // Word too long, add it anyway
                    lines.add(word);
                    line = new StringBuilder();
                }
            } else {
                line.append(word).append(" ");
            }
        }
        if (line.length() > 0) {
            lines.add(line.toString().trim());
        }

        // Draw text (centered in box)
        font.setColor(0, 0, 0, 1); // Black text

        float lineHeight = 25 * scale; // Scale line height
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
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public int getFinalScore() {
        return score;
    }

    @Override
    public void dispose() {
        // Don't dispose manager - it's shared!
        // Only dispose textures that this class owns
        objectManager.clearAll();
        ingredientTexture.dispose();
        badIngredientTexture.dispose();

        // ✅ Dispose text box background
        if (textBoxBackground != null) {
            textBoxBackground.dispose();
        }
    }
}
