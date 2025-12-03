package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.states.StartGameState;

public class MenuState implements GameState {

    private GameStateManager gsm;
    private Player player;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private Rectangle startGameButton;

    private static final Color PASTEL_BLUE = new Color(0.68f, 0.85f, 0.9f, 1f);
    private static final Color PINK = new Color(1f, 0.75f, 0.8f, 1f);

    public MenuState(GameStateManager gsm, Player player) {
        this.gsm = gsm;
        this.player = player;

        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(Color.WHITE);

        shapeRenderer = new ShapeRenderer();

        // Button "Start Game" di tengah
        float buttonWidth = 250;
        float buttonHeight = 80;
        float centerX = Gdx.graphics.getWidth() / 2f - buttonWidth / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f - buttonHeight / 2f;

        startGameButton = new Rectangle(centerX, centerY, buttonWidth, buttonHeight);
    }

    @Override
    public void update(float delta) {
        // Kalau player udah ada (sudah login), skip button logic
        if (player != null) {
            // Nanti tambahin logic game
            return;
        }

        // Kalau belum login, tunggu klik "Start Game"
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (startGameButton.contains(x, y)) {
                gsm.set(new StartGameState(gsm));
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // Set background biru muda pastel
        Gdx.gl.glClearColor(PASTEL_BLUE.r, PASTEL_BLUE.g, PASTEL_BLUE.b, PASTEL_BLUE.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (player == null) {
            // Tampilan awal: button "Start Game"
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(PINK);
            shapeRenderer.rect(startGameButton.x, startGameButton.y,
                startGameButton.width, startGameButton.height);
            shapeRenderer.end();

            batch.begin();
            font.draw(batch, "START GAME",
                startGameButton.x + 30,
                startGameButton.y + startGameButton.height/2 + 15);
            batch.end();
        } else {
            // Tampilan setelah login
            batch.begin();
            font.getData().setScale(1.5f);
            font.draw(batch, "Welcome: " + player.getUsername(), 100, 400);
            font.draw(batch, "ID: " + player.getId(), 100, 370);
            batch.end();
        }
    }

    @Override
    public void dispose() {
        if (font != null) {
            font.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
