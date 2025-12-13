package com.finpro.frontend.strategies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public interface ChallengeGame {
    void start();
    void update(float delta);
    void render(SpriteBatch batch, BitmapFont font);
    boolean isCompleted();
    int getFinalScore();
    void dispose();
}
