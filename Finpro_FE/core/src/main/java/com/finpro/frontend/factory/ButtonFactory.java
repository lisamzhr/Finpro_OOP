package com.finpro.frontend.factory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.pools.ButtonPool;

public class ButtonFactory {
    private ButtonPool buttonPool;
    private BitmapFont defaultFont;

    public ButtonFactory(BitmapFont defaultFont) {
        this.defaultFont = defaultFont;
        this.buttonPool = new ButtonPool();
    }

    // ========== Regular Button Creation ==========

    // Create button dengan text
    public Button createButton(String text, float x, float y, float width, float height,
                               Texture texture) {
        Button button = buttonPool.obtain();
        button.set(text, x, y, width, height, texture);
        return button;
    }

    // Create button dengan text + hover texture
    public Button createButton(String text, float x, float y, float width, float height,
                               Texture texture, Texture hoverTexture) {
        Button button = buttonPool.obtain();
        button.set(text, x, y, width, height, texture, hoverTexture);
        return button;
    }

    // Create button tanpa text
    public Button createButtonNoText(float x, float y, float width, float height,
                                     Texture texture) {
        Button button = buttonPool.obtain();
        button.setNoText(x, y, width, height, texture);
        return button;
    }

    // Create button tanpa text + hover texture
    public Button createButtonNoText(float x, float y, float width, float height,
                                     Texture texture, Texture hoverTexture) {
        Button button = buttonPool.obtain();
        button.setNoText(x, y, width, height, texture, hoverTexture);
        return button;
    }

    // ========== Choice Button Creation (tanpa texture) ==========

    public Button createChoiceButton(String text, float x, float y,
                                     float width, float height, int points) {
        Button button = buttonPool.obtain();
        button.setWithPoints(text, x, y, width, height, points);
        return button;
    }

    // ========== Release Methods ==========

    public void releaseButton(Button button) {
        buttonPool.release(button);
    }

    public void releaseAll() {
        buttonPool.releaseAll();
    }

    // ========== Info Methods ==========

    public int getActiveCount() {
        return buttonPool.getActiveCount();
    }

    public ButtonPool getButtonPool() {
        return buttonPool;
    }

    public BitmapFont getDefaultFont() {
        return defaultFont;
    }

    // ========== Dispose ==========

    public void dispose() {
        releaseAll();
    }
}
