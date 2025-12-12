package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.strategies.DatingStrategy;

public class StoryState implements GameState {
    protected GameStateManager gsm;
    private Texture background;
    private Texture boyImage;
    private BitmapFont font;
    private DatingStrategy strategy;
    private String boyId;
    private String storyText;
    private Button continueButton;
    private ButtonManager buttonManager;
    private Texture buttonTexture;
    private Texture buttonHoverTexture;
    private GlyphLayout layout;

    public StoryState(GameStateManager gsm, DatingStrategy strategy, String boyId, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.strategy = strategy;
        this.boyId = boyId;
        this.buttonManager = buttonManager;

        background = new Texture("dating/" + boyId.toLowerCase() + "_Background_Conv.png");
        boyImage = new Texture("dating/" + boyId.toLowerCase() + "_full.png");
        font = new BitmapFont();
        layout = new GlyphLayout();

        // Get story from strategy
        storyText = strategy.getStory();

        // Load button textures (adjust paths as needed)
        buttonTexture = new Texture("button_normal.png");
        buttonHoverTexture = new Texture("button_hover.png");

        // Create continue button using ButtonManager
        continueButton = buttonManager.createButton(
            "Continue",
            Gdx.graphics.getWidth() / 2f - 100,
            100,
            200,
            60,
            buttonTexture,
            buttonHoverTexture
        );
    }

    @Override
    public void update(float delta) {
        continueButton.update();

        if (continueButton.isClicked()) {
            // Move to dating conversation state
            gsm.push(new DatingConversationState(gsm, strategy, boyId, buttonManager));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        // Draw background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw boy image
        int boyPos = 300;
        if (boyId.equals("ALEX")) {
            boyPos = 1100;
        }
        batch.draw(boyImage, boyPos, 0, boyImage.getWidth() / 2, boyImage.getHeight() / 2);

        // Draw story text (wrap text)
        font.getData().setScale(3f);
        drawWrappedText(batch, font, storyText, 300, 900, 700);
        font.getData().setScale(1f);

        // Draw continue button
        continueButton.render(batch, font);

        batch.end();
    }

    private void drawWrappedText(SpriteBatch batch, BitmapFont font,
                                 String text, float x, float y, float maxWidth) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        float currentY = y;

        for (String word : words) {
            String testLine = line + word + " ";
            layout.setText(font, testLine);

            if (layout.width > maxWidth) {
                font.draw(batch, line.toString(), x, currentY);
                currentY -= 40;
                line = new StringBuilder(word + " ");
            } else {
                line.append(word).append(" ");
            }
        }
        font.draw(batch, line.toString(), x, currentY);
    }

    @Override
    public void dispose() {
        background.dispose();
        boyImage.dispose();
        font.dispose();
        buttonTexture.dispose();
        buttonHoverTexture.dispose();
        layout = null;

        // Release button back to pool
        if (continueButton != null) {
            buttonManager.releaseButton(continueButton);
            continueButton = null;
        }
    }
}
