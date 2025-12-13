package com.finpro.frontend.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Button {
    private Rectangle bounds;
    private String text;
    private boolean isHovered;
    private boolean wasClicked;
    private Texture texture;
    private Texture hoverTexture;
    private GlyphLayout layout;
    private int points; // Untuk choice button

    public Button() {
        bounds = new Rectangle();
        layout = new GlyphLayout();
    }

    // Set dengan text
    public void set(String text, float x, float y, float width, float height, Texture texture) {
        this.text = text;
        this.texture = texture;
        this.hoverTexture = null;
        this.points = 0;
        bounds.set(x, y, width, height);
        isHovered = false;
        wasClicked = false;
    }

    // Set dengan text + hover texture
    public void set(String text, float x, float y, float width, float height,
                    Texture texture, Texture hoverTexture) {
        set(text, x, y, width, height, texture);
        this.hoverTexture = hoverTexture;
    }

    // Set dengan text + points (untuk choice button)
    public void setWithPoints(String text, float x, float y, float width, float height, int points) {
        this.text = text;
        this.texture = null;
        this.hoverTexture = null;
        this.points = points;
        bounds.set(x, y, width, height);
        isHovered = false;
        wasClicked = false;
    }

    // Set tanpa text (null)
    public void setNoText(float x, float y, float width, float height, Texture texture) {
        this.text = null;
        this.texture = texture;
        this.hoverTexture = null;
        this.points = 0;
        bounds.set(x, y, width, height);
        isHovered = false;
        wasClicked = false;
    }

    // Set tanpa text + hover texture
    public void setNoText(float x, float y, float width, float height,
                          Texture texture, Texture hoverTexture) {
        setNoText(x, y, width, height, texture);
        this.hoverTexture = hoverTexture;
    }

    // Reset untuk pool
    public void reset() {
        text = null;
        texture = null;
        hoverTexture = null;
        points = 0;
        isHovered = false;
        wasClicked = false;
        bounds.set(0, 0, 0, 0);
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
        // Kalau ada texture, draw texture
        if (texture != null) {
            Texture currentTexture = (isHovered && hoverTexture != null) ? hoverTexture : texture;
            batch.draw(currentTexture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
        // Kalau gak ada texture (choice button), draw colored box
        else {
            batch.end();

            Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA,
                com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);

            com.badlogic.gdx.graphics.glutils.ShapeRenderer shapeRenderer =
                new com.badlogic.gdx.graphics.glutils.ShapeRenderer();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);

            if (isHovered) {
                shapeRenderer.setColor(0.3f, 0.3f, 0.5f, 0.9f);
            } else {
                shapeRenderer.setColor(0.2f, 0.2f, 0.3f, 0.8f);
            }

            shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            shapeRenderer.end();
            shapeRenderer.dispose();

            Gdx.gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_BLEND);

            batch.begin();
        }

        // Draw text
        if (text != null && font != null) {
            font.getData().setScale(1.2f);
            layout.setText(font, text);

            // Center text kalau ada texture, left align kalau choice button
            float textX = (texture != null)
                ? bounds.x + (bounds.width - layout.width) / 2
                : bounds.x + 20;
            float textY = bounds.y + (bounds.height + layout.height) / 2;

            font.setColor(1, 1, 1, 1);
            font.draw(batch, text, textX, textY);
            font.getData().setScale(1f);
        }
    }

    public boolean isClicked() {
        boolean clicked = wasClicked;
        wasClicked = false;
        return clicked;
    }

    public int getPoints() {
        return points;
    }
}
