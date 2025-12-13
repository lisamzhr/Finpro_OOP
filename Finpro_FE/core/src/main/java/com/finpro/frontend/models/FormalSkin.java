package com.finpro.frontend.models;

import com.badlogic.gdx.graphics.Texture;

public class FormalSkin implements Skin {
    private Texture texture;

    public FormalSkin() {
        this.texture = new Texture("skins/formal.png");
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return "Formal";
    }

    @Override
    public int getSkinId() {
        return 1;
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
