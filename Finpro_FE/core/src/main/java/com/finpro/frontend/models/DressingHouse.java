package com.finpro.frontend.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Gdx;
import com.finpro.frontend.factories.SkinFactory;

public class DressingHouse {
    private Texture houseTexture;
    private Rectangle bounds;
    private float x, y;
    private float width, height;
    private boolean isHovered;

    // Untuk skin selection
    private int currentSkinIndex;
    private int totalSkins;

    public DressingHouse(float x, float y) { // Constructor butuh x, y
        this.x = x;
        this.y = y;
        this.currentSkinIndex = 0;
        this.totalSkins = SkinFactory.getTotalSkins();

        // Load house texture (untuk menu)
        houseTexture = new Texture("menu/DressingHouse.png");
        float scale = 0.9f;
        this.width = houseTexture.getWidth() * scale;
        this.height = houseTexture.getHeight() * scale;

        // Bounds untuk collision detection
        bounds = new Rectangle(x, y, width, height);
        isHovered = false;
    }

    public void render(SpriteBatch batch) {
        // Draw house texture (untuk menu screen)
        batch.draw(houseTexture, x, y, width, height);

        // Optional: highlight saat hover
        if (isHovered) {
            // Bisa tambahkan efek visual
        }
    }

    public void update() {
        // Cek mouse position untuk hover detection
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        mousePos.y = Gdx.graphics.getHeight() - mousePos.y;

        isHovered = bounds.contains(mousePos.x, mousePos.y);

        if (isHovered && Gdx.input.justTouched()) {
            onClicked();
        }

        // Update cursor saat hover
        if (isHovered) {
            Gdx.graphics.setSystemCursor(com.badlogic.gdx.graphics.Cursor.SystemCursor.Hand);
        }
    }

    private void onClicked() {
        System.out.println("Dressing House clicked!");
        // State change akan di-handle di MenuState
    }

    // Skin selection methods (untuk DressingHouseState)
    public Skin getCurrentSkin() {
        return SkinFactory.createSkin(currentSkinIndex);
    }

    public int getCurrentSkinIndex() {
        return currentSkinIndex;
    }

    public void nextSkin() {
        currentSkinIndex = (currentSkinIndex + 1) % totalSkins;
    }

    public void previousSkin() {
        currentSkinIndex = (currentSkinIndex - 1 + totalSkins) % totalSkins;
    }

    public void dispose() {
        if (houseTexture != null) {
            houseTexture.dispose();
        }
    }

    // Getters
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isHovered() {
        return isHovered;
    }
}
