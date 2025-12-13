package com.finpro.frontend.models;

import com.badlogic.gdx.graphics.Texture;

public class SportSkin implements Skin{
    private Texture texture;

    public SportSkin() {
        this.texture = new Texture("skins/sport.png");
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return "Sport";
    }

    @Override
    public int getSkinId() {
        return 2;
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
