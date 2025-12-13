package com.finpro.frontend.models;

import com.badlogic.gdx.graphics.Texture;

public interface Skin {
    Texture getTexture();
    String getName();
    int getSkinId();
    void dispose();
}
