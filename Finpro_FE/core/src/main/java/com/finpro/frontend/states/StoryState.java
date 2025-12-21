package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.finpro.frontend.MusicManager;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.strategies.DatingStrategy;

public class StoryState implements GameState {
    protected GameStateManager gsm;
    private Player player;
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
    private Texture textBox;

    public StoryState(GameStateManager gsm, DatingStrategy strategy, String boyId, ButtonManager buttonManager) { // ✅ Hapus player parameter
        this.gsm = gsm;
        this.player = gsm.getPlayer();
        this.strategy = strategy;
        this.boyId = boyId;
        this.buttonManager = buttonManager;
        textBox = new Texture("dating/textbox_"+ boyId.toLowerCase() + ".png");
        MusicManager.getInstance().playMusic(MusicManager.DATING_MUSIC);

        background = new Texture("dating/" + boyId.toLowerCase() + "_Background_Conv.png");
        boyImage = new Texture("dating/" + boyId.toLowerCase() + "_full.png");
        font = new BitmapFont();
        layout = new GlyphLayout();

        storyText = strategy.getStory();

        buttonTexture = new Texture("button_normal.png");
        buttonHoverTexture = new Texture("button_hover.png");

        continueButton = buttonManager.createButton(
            "Continue",
            Gdx.graphics.getWidth() / 2f - 100,
            100,
            200,
            60,
            buttonTexture,
            buttonHoverTexture
        );

        if (player != null) {
            System.out.println("StoryState - Player: " + player.getUsername() + " | Skin ID: " + player.getSelectedSkinId());
        }
    }

    @Override
    public void update(float delta) {
        continueButton.update();

        if (continueButton.isClicked()) {
            gsm.push(new DatingConversationState(gsm, strategy, boyId, buttonManager));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float playerX = 50;
        float playerY = 50;
        int boyPos = 300;
        if (boyId.equals("ALEX")) {
            boyPos = 1100;
            playerX = 200;
        }
        player.render(batch, playerX, playerY);
        batch.draw(boyImage, boyPos, playerY, 500, 920);

        font.getData().setScale(2f);
        drawWrappedText(batch, font, textBox, storyText, 150, 0, 1000);
        font.getData().setScale(1f);
        continueButton.render(batch, font);

        batch.end();
    }

    private void drawWrappedText(SpriteBatch batch, BitmapFont font, Texture background,
                                 String text, float x, float y, float maxWidth) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        java.util.List<String> lines = new java.util.ArrayList<>();

        for (String word : words) {
            String testLine = line + word + " ";
            layout.setText(font, testLine);

            if (layout.width > maxWidth) {
                lines.add(line.toString().trim());
                line = new StringBuilder(word + " ");
            } else {
                line.append(word).append(" ");
            }
        }
        lines.add(line.toString().trim());

        float lineHeight = 35;
        float padding = 35;
        float boxHeight = (lines.size() * lineHeight) + (padding * 2);
        float boxWidth = maxWidth + (padding * 2);

        float boxX = (Gdx.graphics.getWidth() - boxWidth) / 2f;
        float boxY = 200;

        batch.draw(background, boxX, boxY, boxWidth, boxHeight);

        font.setColor(Color.BLACK);
        float textY = boxY + boxHeight - padding-20;

        for (String textLine : lines) {
            layout.setText(font, textLine);
            float textX = boxX + padding;
            font.draw(batch, textLine, textX, textY);
            textY -= lineHeight;
        }
    }

    @Override
    public void dispose() {
        background.dispose();
        boyImage.dispose();
        font.dispose();
        buttonTexture.dispose();
        buttonHoverTexture.dispose();
        layout = null;

        if (continueButton != null) {
            buttonManager.releaseButton(continueButton);
            continueButton = null;
        }
    }
}
