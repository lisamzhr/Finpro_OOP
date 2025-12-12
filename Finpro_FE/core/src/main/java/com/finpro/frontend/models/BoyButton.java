package com.finpro.frontend.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class BoyButton {
    private Texture profileImg;
    private Rectangle bounds;
    private String name;
    private String boyId;
    private boolean isHovered;
    private boolean wasClicked;

    public BoyButton(String name, String texturePath, float x, float y, float width, float height, String boyId) {
        this.name = name;
        this.boyId = boyId;

        try {
            profileImg = new Texture(texturePath);
        } catch (Exception e) {
            // Fallback jika texture tidak ada
            profileImg = new Texture("default_profile.png");
        }

        bounds = new Rectangle(x, y, width, height);
        isHovered = false;
        wasClicked = false;
    }

    // Constructor overload untuk backward compatibility
    public BoyButton(String name, String texturePath, float x, float y, String boyId) {
        this(name, texturePath, x, y, 400, 700, boyId);
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
        float scale = isHovered ? 1.1f : 1.0f;
        float offsetX = (scale - 1) * bounds.width / 2;
        float offsetY = (scale - 1) * bounds.height / 2;

        batch.draw(profileImg,
            bounds.x - offsetX, bounds.y - offsetY,
            bounds.width * scale, bounds.height * scale);
    }

    public boolean isClicked() {
        boolean clicked = wasClicked;
        wasClicked = false;
        return clicked;
    }

    public String getBoyId() {
        return boyId;
    }

    public String getName() {
        return name;
    }

    public void dispose() {
        profileImg.dispose();
    }
}
