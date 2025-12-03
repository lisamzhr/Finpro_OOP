package com.finpro.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.frontend.states.GameStateManager;
import com.finpro.frontend.states.MenuState;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private GameStateManager gsm;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gsm = new GameStateManager();

        //Push MenuState sebagai state awal (player = null, akan tampil button Start Game)
        gsm.push(new MenuState(gsm, null));

        //Simpan gsm di GameManager supaya state lain bisa akses (kalau pakai GameManager)
        if (GameManager.getInstance() != null) {
            GameManager.getInstance().setGsm(gsm);
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        gsm.update(delta);
        gsm.render(batch);
    }

    @Override
    public void resize(int width, int height) {
        // Handle resize if needed
    }

    @Override
    public void dispose() {
        batch.dispose();
        gsm.dispose();
    }
}
