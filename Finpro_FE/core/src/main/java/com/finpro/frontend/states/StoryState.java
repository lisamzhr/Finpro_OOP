package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.finpro.frontend.models.SimpleButton;
import com.finpro.frontend.strategies.DatingStrategy;

public class StoryState implements GameState {
    protected GameStateManager gsm;
    private Texture background;
    private Texture boyImage;
    private BitmapFont font;
    private DatingStrategy strategy;
    private String boyId;
    private String storyText;
    private SimpleButton continueButton;

    public StoryState(GameStateManager gsm, DatingStrategy strategy, String boyId) {
        this.gsm = gsm;
        this.strategy = strategy;
        this.boyId = boyId;

        background = new Texture("dating/datingGarden.png");
        boyImage = new Texture("dating/" + boyId.toLowerCase() + "_full.png");
        font = new BitmapFont();

        // Get story from strategy
        storyText = strategy.getStory();

        // Create continue button
        continueButton = new SimpleButton("Continue",
            Gdx.graphics.getWidth() / 2f - 100,
            100, 200, 60);
    }

    @Override
    public void update(float delta) {
        continueButton.update();

        if (continueButton.isClicked()) {
            // Move to dating conversation state
            gsm.push(new DatingConversationState(gsm, strategy, boyId));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        // Draw background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw boy image
        batch.draw(boyImage, 50, 100, 400, 600);

        // Draw story text (wrap text)
        font.getData().setScale(1.5f);
        drawWrappedText(batch, font, storyText, 500, 600, 700);

        // Draw continue button
        continueButton.render(batch, font);

        batch.end();
    }

    private void drawWrappedText(SpriteBatch batch, BitmapFont font,
                                 String text, float x, float y, float maxWidth) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        float currentY = y;
        com.badlogic.gdx.graphics.g2d.GlyphLayout layout = new com.badlogic.gdx.graphics.g2d.GlyphLayout();

        for (String word : words) {
            String testLine = line + word + " ";
            layout.setText(font, testLine);

            if (layout.width > maxWidth) {
                font.draw(batch, line.toString(), x, currentY);
                currentY -= 30;
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
        continueButton.dispose();
    }
}
