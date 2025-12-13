package com.finpro.frontend.models;

import com.badlogic.gdx.graphics.Texture;

public class ModernSkin implements Skin {
    private Texture texture;

    public ModernSkin() {
        this.texture = new Texture("skins/modern.png");
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return "Modern";
    }

    @Override
    public int getSkinId() {
        return 4;
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
