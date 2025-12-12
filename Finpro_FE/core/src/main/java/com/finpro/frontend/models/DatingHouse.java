package com.finpro.frontend.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Gdx;
import com.finpro.frontend.GameManager;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.states.DatingHouseState;
import com.finpro.frontend.states.GameState;

public class DatingHouse {
    private Texture houseTexture;
    private Rectangle bounds;
    private float x, y;
    private float width, height;
    private boolean isHovered;
    private GameManager gameManager;
    private ButtonManager buttonManager;

    public DatingHouse(float x, float y, ButtonManager buttonManager) {
        this.x = x;
        this.y = y;
        this.buttonManager = buttonManager;

        houseTexture = new Texture("menu/DatingHouse.png");
        float scale = 0.9f;
        this.width = houseTexture.getWidth() * scale;
        this.height = houseTexture.getHeight() * scale;
        bounds = new Rectangle(x, y, width, height);
        isHovered = false;

        // GET GAME MANAGER INSTANCE
        gameManager = GameManager.getInstance();
    }

    public void render(SpriteBatch batch) {
        batch.draw(houseTexture, x, y, width, height);
    }

    public void update() {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        mousePos.y = Gdx.graphics.getHeight() - mousePos.y;
        isHovered = bounds.contains(mousePos.x, mousePos.y);

        if (isHovered && Gdx.input.justTouched()) {
            onClicked();
        }

        if (isHovered) {
            Gdx.graphics.setSystemCursor(com.badlogic.gdx.graphics.Cursor.SystemCursor.Hand);
        }
    }

    private void onClicked() {
        System.out.println("Dating House clicked! Entering DatingHouseState...");

        // PUSH DATING HOUSE STATE WITH BUTTON MANAGER
        if (gameManager != null && gameManager.getGsm() != null) {
            gameManager.getGsm().push(new DatingHouseState(gameManager.getGsm(), buttonManager));
        }
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
