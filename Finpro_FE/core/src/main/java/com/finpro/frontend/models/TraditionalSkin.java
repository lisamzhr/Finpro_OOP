package com.finpro.frontend.models;

import com.badlogic.gdx.graphics.Texture;

public class TraditionalSkin implements Skin{
    private Texture texture;

    public TraditionalSkin() {
        this.texture = new Texture("skins/traditional.png");
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return "Traditional";
    }

    @Override
    public int getSkinId() {
        return 3;
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
