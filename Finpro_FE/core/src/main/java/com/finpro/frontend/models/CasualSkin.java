package com.finpro.frontend.models;

import com.badlogic.gdx.graphics.Texture;

public class CasualSkin implements Skin {
    private Texture texture;

    public CasualSkin() {
        this.texture = new Texture("skins/casual.png");
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return "Casual";
    }

    @Override
    public int getSkinId() {
        return 0;
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
