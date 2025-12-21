// models/Player.java
package com.finpro.frontend.models;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.frontend.observers.PlayerListener;
import com.finpro.frontend.factory.SkinFactory;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private String id;
    private String username;
    private int level;
    private float fashionCoin;
    private int selectedSkinId = 0;

    private List<PlayerListener> listeners = new ArrayList<>();

    public Player(String id, String username, int level) {
        this.id = id;
        this.username = username;
        this.level = level;
        fashionCoin = 5;
    }

    //Observer Pattern
    public void addListener(PlayerListener listener) {
        listeners.add(listener);
    }
    public void removeListener(PlayerListener listener) {
        listeners.remove(listener);
    }
    private void notifyListeners(String eventType) {
        for (PlayerListener listener : listeners) {
            listener.onPlayerUpdated(this, eventType);
        }
    }

    //GETTERS
    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public int getLevel() {
        return level;
    }
    public float getFashionCoin() {
        return fashionCoin;
    }

    //Setter + notify
    public void setUsername(String username) {
        this.username = username;
        notifyListeners("USERNAME_CHANGED");
    }
    public void setLevel(int level) {
        this.level = level;
        notifyListeners("LEVEL_CHANGED");
    }
    public void setFashionCoin(float fashionCoin) {
        this.fashionCoin = fashionCoin;
        notifyListeners("COIN_CHANGED");
    }


    // Skin management
    public void setSelectedSkinId(int skinId) {
        this.selectedSkinId = skinId;
        notifyListeners("SKIN_CHANGED");
    }

    public int getSelectedSkinId() {
        return selectedSkinId;
    }

    public Skin getCurrentSkin() {
        return SkinFactory.createSkin(selectedSkinId);
    }

    // ✅ Check if player has enough coins for a skin
    public boolean canAffordSkin(int skinId) {
        Skin skin = SkinFactory.createSkin(skinId);
        boolean canAfford = fashionCoin >= skin.getPrice();
        skin.dispose();
        return canAfford;
    }

    // Render method
    public void render(SpriteBatch batch, float x, float y, float width, float height) {
        Skin currentSkin = getCurrentSkin();
        batch.draw(currentSkin.getTexture(), x, y, width, height);
    }

    public void render(SpriteBatch batch, float x, float y) {
        render(batch, x, y, getCurrentSkin().getTexture().getWidth()*0.65f, getCurrentSkin().getTexture().getHeight()*0.65f);
    }
}
