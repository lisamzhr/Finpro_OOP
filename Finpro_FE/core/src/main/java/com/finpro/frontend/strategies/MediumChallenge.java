package com.finpro.frontend.strategies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.frontend.ChallengeObjectManager;

import java.util.Random;

public class MediumChallenge implements ChallengeGame {
    private ChallengeObjectManager objectManager;
    private Texture ingredientTexture;
    private Texture badIngredientTexture;
    private float spawnTimer;
    private float gameTimer;
    private int score;
    private boolean completed;
    private Random random;

    private static final float SPAWN_INTERVAL = 0.8f;
    private static final float GAME_DURATION = 20.0f;
    private static final int INGREDIENT_SIZE = 300;

    // Constructor with Dependency Injection
    public MediumChallenge(ChallengeObjectManager objectManager) {
        this.objectManager = objectManager;
        ingredientTexture = new Texture("dating/brian_purple_flower.png");
        badIngredientTexture = new Texture("dating/brian_red_flower.png");
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
        boolean isGood = random.nextFloat() < 0.7f;
        Texture texture = isGood ? ingredientTexture : badIngredientTexture;
        int points = isGood ? 3 : -2;

        // Move horizontally
        float velocityX = -50 + random.nextInt(100);

        objectManager.spawnMovingObject(x, y, INGREDIENT_SIZE, INGREDIENT_SIZE,
            texture, points, velocityX, 0);
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        objectManager.render(batch);

        font.getData().setScale(1.5f);
        font.draw(batch, "Time: " + (int)(GAME_DURATION - gameTimer) + "s",
            50, Gdx.graphics.getHeight() - 50);
        font.draw(batch, "Score: " + score,
            Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 50);

        font.getData().setScale(1f);
        font.draw(batch, "Click PURPLE flowers! Avoid RED ones!",
            50, Gdx.graphics.getHeight() - 100);

        if (completed) {
            font.getData().setScale(2f);
            font.draw(batch, "Challenge Complete!",
                Gdx.graphics.getWidth() / 2f - 150, Gdx.graphics.getHeight() / 2f);
        }
    }

    public void handleClick(float x, float y) {
        if (completed) return;
        objectManager.handleClick(x, y);
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
    }
}
