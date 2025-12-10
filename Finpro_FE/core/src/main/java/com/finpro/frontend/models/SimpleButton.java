package com.finpro.frontend.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class SimpleButton {
    private Rectangle bounds;
    private String text;
    private boolean isHovered;
    private boolean wasClicked;

    public SimpleButton(String text, float x, float y, float width, float height) {
        this.text = text;
        bounds = new Rectangle(x, y, width, height);
        isHovered = false;
        wasClicked = false;
    }

    public void update() {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        mousePos.y = Gdx.graphics.getHeight() - mousePos.y;

        isHovered = bounds.contains(mousePos.x, mousePos.y);

        if (isHovered && Gdx.input.justTouched()) {
            wasClicked = true;
        }

        if (isHovered) {
            Gdx.graphics.setSystemCursor(com.badlogic.gdx.graphics.Cursor.SystemCursor.Hand);
        }
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        // Draw button background (simple colored rectangle)
        // Note: Untuk production, pakai texture button

        font.getData().setScale(1.2f);
        font.draw(batch, text,
            bounds.x + 20, bounds.y + bounds.height / 2 + 10);
    }

    public boolean isClicked() {
        boolean clicked = wasClicked;
        wasClicked = false;
        return clicked;
    }

    public void dispose() {
        // Cleanup if needed
    }
}
