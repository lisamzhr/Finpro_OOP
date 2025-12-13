package com.finpro.frontend.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class ChallengeObject {
    private Rectangle bounds;
    private Texture texture;
    private float x, y;
    private float width, height;
    private boolean isClicked;
    private boolean isActive;
    private float velocityX;
    private float velocityY;
    private int pointValue;

    public ChallengeObject() {
        bounds = new Rectangle();
        isActive = false;
        isClicked = false;
    }

    public void set(float x, float y, float width, float height,
                    Texture texture, int pointValue) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.pointValue = pointValue;
        this.velocityX = 0;
        this.velocityY = 0;
        bounds.set(x, y, width, height);
        isActive = true;
        isClicked = false;
    }

    public void setWithVelocity(float x, float y, float width, float height,
                                Texture texture, int pointValue,
                                float velocityX, float velocityY) {
        set(x, y, width, height, texture, pointValue);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void update(float delta) {
        if (!isActive) return;

        // Update position with velocity
        x += velocityX * delta;
        y += velocityY * delta;
        bounds.setPosition(x, y);

        // Check mouse click
        if (Gdx.input.justTouched()) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            mousePos.y = Gdx.graphics.getHeight() - mousePos.y;

            if (bounds.contains(mousePos.x, mousePos.y)) {
                isClicked = true;
                isActive = false;
            }
        }

        // Deactivate if out of bounds
        if (x < -width || x > Gdx.graphics.getWidth() ||
            y < -height || y > Gdx.graphics.getHeight()) {
            isActive = false;
        }
    }

    public void render(SpriteBatch batch) {
        if (isActive && texture != null) {
            batch.draw(texture, x, y, width, height);
        }
    }

    public void reset() {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
        texture = null;
        velocityX = 0;
        velocityY = 0;
        pointValue = 0;
        isActive = false;
        isClicked = false;
        bounds.set(0, 0, 0, 0);
    }

    // Getters
    public boolean isClicked() {
        return isClicked;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getPointValue() {
        return pointValue;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
