package com.finpro.frontend;

import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    private Map<String, Texture> textures;

    public TextureManager() {
        textures = new HashMap<>();
    }

    public void loadTexture(String key, String path) {
        if (!textures.containsKey(key)) {
            textures.put(key, new Texture(path));
        }
    }

    public Texture getTexture(String key) {
        return textures.get(key);
    }

    public boolean hasTexture(String key) {
        return textures.containsKey(key);
    }

    public void disposeTexture(String key) {
        Texture texture = textures.remove(key);
        if (texture != null) {
            texture.dispose();
        }
    }

    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();
    }
}
