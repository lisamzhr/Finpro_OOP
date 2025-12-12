package com.finpro.frontend.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class ChoiceButton {
    private String text;
    private Rectangle bounds;
    private int points;
    private boolean isHovered;
    private boolean wasClicked;
    private Texture buttonTexture;
    private Texture buttonHoverTexture;

    public ChoiceButton(String text, float x, float y, float width, float height, int points) {
        this.text = text;
        this.bounds = new Rectangle(x, y, width, height);
        this.points = points;
        this.isHovered = false;
        this.wasClicked = false;

        // Create button textures
        this.buttonTexture = createButtonTexture((int)width, (int)height,
            new Color(0.2f, 0.2f, 0.2f, 0.8f), Color.WHITE);
        this.buttonHoverTexture = createButtonTexture((int)width, (int)height,
            new Color(0.3f, 0.6f, 0.9f, 1f), Color.WHITE);
    }

    private Texture createButtonTexture(int width, int height, Color fillColor, Color borderColor) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        // Fill background
        pixmap.setColor(fillColor);
        pixmap.fill();

        // Draw border
        pixmap.setColor(borderColor);
        pixmap.drawRectangle(0, 0, width, height);

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return texture;
    }

    public void update() {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        mousePos.y = Gdx.graphics.getHeight() - mousePos.y;

        isHovered = bounds.contains(mousePos.x, mousePos.y);

        if (isHovered && Gdx.input.justTouched()) {
            wasClicked = true;
            System.out.println("Button clicked: " + text + " (Points: " + points + ")");
        }
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        // Draw button background
        Texture currentTexture = isHovered ? buttonHoverTexture : buttonTexture;
        batch.draw(currentTexture, bounds.x, bounds.y, bounds.width, bounds.height);

        // Draw text
        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);

        // Word wrap untuk text panjang
        String wrappedText = wrapText(text, 55);
        String[] lines = wrappedText.split("\n");

        float textY = bounds.y + bounds.height / 2 + (lines.length * 10);
        for (String line : lines) {
            font.draw(batch, line, bounds.x + 20, textY);
            textY -= 20;
        }
    }

    private String wrapText(String text, int maxLength) {
        if (text.length() <= maxLength) return text;

        StringBuilder wrapped = new StringBuilder();
        String[] words = text.split(" ");
        int currentLength = 0;

        for (String word : words) {
            if (currentLength + word.length() > maxLength) {
                wrapped.append("\n");
                currentLength = 0;
            }
            wrapped.append(word).append(" ");
            currentLength += word.length() + 1;
        }

        return wrapped.toString().trim();
    }

    public boolean isClicked() {
        boolean clicked = wasClicked;
        wasClicked = false;
        return clicked;
    }

    public int getPoints() {
        return points;
    }

    public String getText() {
        return text;
    }

    public void dispose() {
        if (buttonTexture != null) {
            buttonTexture.dispose();
        }
        if (buttonHoverTexture != null) {
            buttonHoverTexture.dispose();
        }
    }
}
