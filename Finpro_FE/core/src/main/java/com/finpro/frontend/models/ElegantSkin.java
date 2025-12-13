package com.finpro.frontend.models;

import com.badlogic.gdx.graphics.Texture;

public class ElegantSkin implements Skin {
    private Texture texture;

    public ElegantSkin() {
        this.texture = new Texture("skins/elegant.png");
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return "Elegant";
    }

    @Override
    public int getSkinId() {
        return 5;
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
