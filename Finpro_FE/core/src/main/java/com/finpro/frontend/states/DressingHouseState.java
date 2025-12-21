package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.MusicManager;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.models.DressingHouse;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.models.Skin;

public class DressingHouseState implements GameState {

    private GameStateManager gsm;
    private DressingHouse dressingHouse;
    private Player player;
    private ButtonManager buttonManager;

    private Texture background;
    private Texture nextButtonTexture;
    private Texture previousButtonTexture;
    private Texture saveButtonTexture;
    private Texture homeButtonTexture;
    private Texture textBoxTexture;

    private Button saveBtn;
    private Button nextBtn;
    private Button previousBtn;
    private Button homeBtn;

    private BitmapFont font;
    private BitmapFont smallFont;
    private GlyphLayout layout;

    private String statusMessage = "";
    private float statusTimer = 0;

    public DressingHouseState(GameStateManager gsm, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.player = gsm.getPlayer();
        this.buttonManager = buttonManager;

        background = new Texture("bg/dressingroom.png");
        nextButtonTexture = new Texture("dressing/nextButton.png");
        previousButtonTexture = new Texture("dressing/previousButton.png");
        saveButtonTexture = new Texture("dressing/saveButton.png");
        homeButtonTexture = new Texture("dressing/homeButton.png");
        textBoxTexture = new Texture("dating/decisionBox.png"); // boleh polos

        dressingHouse = new DressingHouse(0, 0);
        MusicManager.getInstance().playMusic(MusicManager.DRESSING_MUSIC);

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        previousBtn = buttonManager.createButtonNoText(
            80, sh / 2f - 60, 120, 120, previousButtonTexture
        );

        nextBtn = buttonManager.createButtonNoText(
            sw - 200, sh / 2f - 60, 120, 120, nextButtonTexture
        );

        saveBtn = buttonManager.createButtonNoText(
            100, 50, 120, 120, saveButtonTexture
        );

        homeBtn = buttonManager.createButtonNoText(
            240, 50, 120, 120, homeButtonTexture
        );

        font = new BitmapFont();
        smallFont = new BitmapFont();
        smallFont.getData().setScale(1.2f);

        layout = new GlyphLayout();
    }

    private void drawWrappedText(SpriteBatch batch,
                                 BitmapFont font,
                                 Texture background,
                                 String text,
                                 float centerX,
                                 float y,
                                 float maxWidth) {

        float padding = 20f;

        layout.setText(font, text, Color.BLACK, maxWidth, Align.left, true);

        float contentWidth = Math.min(layout.width, maxWidth);

        float boxWidth = contentWidth + padding * 2;
        float boxHeight = layout.height + padding * 2;

        // 🔑 CENTERING
        float boxX = centerX - boxWidth / 2f;

        batch.draw(background, boxX, y, boxWidth, boxHeight);

        font.setColor(Color.BLACK);
        font.draw(batch, layout, boxX + padding, y + boxHeight - padding);
    }



    @Override
    public void update(float dt) {
        buttonManager.updateAll();

        if (nextBtn.isClicked()) dressingHouse.nextSkin();
        if (previousBtn.isClicked()) dressingHouse.previousSkin();

        if (saveBtn.isClicked()) selectSkin();
        if (homeBtn.isClicked()) gsm.setState(new MenuState(gsm, buttonManager));

        if (statusTimer > 0) {
            statusTimer -= dt;
            if (statusTimer <= 0) statusMessage = "";
        }
    }

    private void selectSkin() {
        int id = dressingHouse.getCurrentSkinIndex();

        if (player.canAffordSkin(id)) {
            player.setSelectedSkinId(id);
            gsm.setState(new MenuState(gsm, buttonManager));
        } else {
            statusMessage = "Not enough coins!";
            statusTimer = 3f;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Skin currentSkin = dressingHouse.getCurrentSkin();

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        sb.begin();

        sb.draw(background, 0, 0, sw, sh);

        sb.draw(
            currentSkin.getTexture(),
            (sw - 600) / 2f,
            (sh - 900) / 2f,
            600,
            900
        );

        smallFont.setColor(Color.WHITE);
        smallFont.draw(sb, "Coins: " + (int) player.getFashionCoin(), 50, sh - 50);

        drawWrappedText(
            sb,
            smallFont,
            textBoxTexture,
            currentSkin.getName(),
            Gdx.graphics.getWidth() / 2f, // 👈 CENTER X
            100,
            500
        );


        buttonManager.renderAll(sb);

        if (!statusMessage.isEmpty()) {
            font.setColor(Color.RED);
            font.draw(sb, statusMessage, 150, 180);
        }

        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        nextButtonTexture.dispose();
        previousButtonTexture.dispose();
        saveButtonTexture.dispose();
        homeButtonTexture.dispose();
        textBoxTexture.dispose();
        font.dispose();
        smallFont.dispose();
    }
}
