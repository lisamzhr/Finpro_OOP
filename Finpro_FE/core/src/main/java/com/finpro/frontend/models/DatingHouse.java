package com.finpro.frontend.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Gdx;
import com.finpro.frontend.GameManager;

public class DatingHouse {
    private Texture houseTexture;
    private Rectangle bounds;
    private float x, y;
    private float width, height;
    private boolean isHovered;
    private GameManager Game;

    public DatingHouse(float x, float y) {
        this.x = x;
        this.y = y;

        // Load texture
        houseTexture = new Texture("menu/DatingHouse.png");
        float scale = 0.9f;
        this.width = houseTexture.getWidth() * scale;
        this.height = houseTexture.getHeight() * scale;

        //buat bounds untuk collision detection
        bounds = new Rectangle(x, y, width, height);

        isHovered = false;
    }

    public void render(SpriteBatch batch) {
        // Draw house texture
        batch.draw(houseTexture, x, y, width, height);

        // Optional: tambahkan efek visual saat hover
        if (isHovered) {
            // Bisa tambahkan highlight atau border effect
            // Contoh: draw overlay dengan alpha
        }
    }

    public void update() {
        // Cek mouse position
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        // Untuk koordinat screen langsung (tanpa camera):
        mousePos.y = Gdx.graphics.getHeight() - mousePos.y;
        isHovered = bounds.contains(mousePos.x, mousePos.y);
        if (isHovered && Gdx.input.justTouched()) {
            onClicked();
        }

        // Update cursor jika hover
        if (isHovered) {
            Gdx.graphics.setSystemCursor(com.badlogic.gdx.graphics.Cursor.SystemCursor.Hand);
        }
    }

    private void onClicked() {
        System.out.println("Dating House clicked! Entering DatingHouseState...");
        // Trigger state change
        //Game.getInstance().setScreen(new DatingHouseState());
    }

    public void dispose() {
        houseTexture.dispose();
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
