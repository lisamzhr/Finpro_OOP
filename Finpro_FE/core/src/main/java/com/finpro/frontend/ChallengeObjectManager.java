package com.finpro.frontend;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.frontend.factory.ChallengeObjectFactory;
import com.finpro.frontend.models.ChallengeObject;
import java.util.ArrayList;
import java.util.List;

public class ChallengeObjectManager {
    private ChallengeObjectFactory factory;
    private List<ChallengeObject> activeObjects;

    public ChallengeObjectManager() {
        this.factory = new ChallengeObjectFactory();
        this.activeObjects = new ArrayList<>();
    }

    public ChallengeObject spawnObject(float x, float y, float width, float height,
                                       Texture texture, int pointValue) {
        ChallengeObject obj = factory.createObject(x, y, width, height, texture, pointValue);
        activeObjects.add(obj);
        return obj;
    }

    public ChallengeObject spawnMovingObject(float x, float y, float width, float height,
                                             Texture texture, int pointValue,
                                             float velocityX, float velocityY) {
        ChallengeObject obj = factory.createMovingObject(x, y, width, height, texture,
            pointValue, velocityX, velocityY);
        activeObjects.add(obj);
        return obj;
    }

    public void update(float delta) {
        List<ChallengeObject> toRemove = new ArrayList<>();

        for (ChallengeObject obj : activeObjects) {
            obj.update(delta);

            if (!obj.isActive()) {
                toRemove.add(obj);
            }
        }

        // Remove inactive objects and return to pool
        for (ChallengeObject obj : toRemove) {
            activeObjects.remove(obj);
            factory.releaseObject(obj);
        }
    }

    public void render(SpriteBatch batch) {
        for (ChallengeObject obj : activeObjects) {
            obj.render(batch);
        }
    }

    public int checkClicked() {
        int pointsEarned = 0;
        List<ChallengeObject> toRemove = new ArrayList<>();

        for (ChallengeObject obj : activeObjects) {
            if (obj.isClicked()) {
                pointsEarned += obj.getPointValue();
                toRemove.add(obj);
            }
        }

        for (ChallengeObject obj : toRemove) {
            activeObjects.remove(obj);
            factory.releaseObject(obj);
        }

        return pointsEarned;
    }

    public void clearAll() {
        for (ChallengeObject obj : activeObjects) {
            factory.releaseObject(obj);
        }
        activeObjects.clear();
    }

    public int getActiveCount() {
        return activeObjects.size();
    }

    public void dispose() {
        clearAll();
    }
}
