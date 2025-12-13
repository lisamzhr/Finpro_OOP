package com.finpro.frontend;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.factory.ButtonFactory;

import java.util.List;

public class ButtonManager {
    private ButtonFactory factory;

    public ButtonManager(ButtonFactory factory) {
        this.factory = factory;
    }

    // ========== Regular Buttons ==========

    public Button createButton(String text, float x, float y, float width, float height,
                               Texture texture) {
        return factory.createButton(text, x, y, width, height, texture);
    }

    public Button createButton(String text, float x, float y, float width, float height,
                               Texture texture, Texture hoverTexture) {
        return factory.createButton(text, x, y, width, height, texture, hoverTexture);
    }

    public Button createButtonNoText(float x, float y, float width, float height,
                                     Texture texture) {
        return factory.createButtonNoText(x, y, width, height, texture);
    }

    public Button createButtonNoText(float x, float y, float width, float height,
                                     Texture texture, Texture hoverTexture) {
        return factory.createButtonNoText(x, y, width, height, texture, hoverTexture);
    }

    // ========== Choice Buttons ==========

    public Button createChoiceButton(String text, float x, float y,
                                     float width, float height, int points) {
        return factory.createChoiceButton(text, x, y, width, height, points);
    }

    // ========== Management ==========

    public void releaseButton(Button button) {
        factory.releaseButton(button);
    }

    public void releaseAll() {
        factory.releaseAll();
    }

    public void updateAll() {
        List<Button> activeButtons = factory.getButtonPool().getInUse();
        for (Button button : activeButtons) {
            button.update();
        }
    }

    public void renderAll(SpriteBatch batch) {
        List<Button> activeButtons = factory.getButtonPool().getInUse();
        BitmapFont font = factory.getDefaultFont();
        for (Button button : activeButtons) {
            button.render(batch, font);
        }
    }

    public int getActiveCount() {
        return factory.getActiveCount();
    }

    public List<Button> getActiveButtons() {
        return factory.getButtonPool().getInUse();
    }

    public void dispose() {
        releaseAll();
    }
}
