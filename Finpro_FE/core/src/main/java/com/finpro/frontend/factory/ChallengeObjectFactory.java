package com.finpro.frontend.factory;

import com.badlogic.gdx.graphics.Texture;
import com.finpro.frontend.models.ChallengeObject;
import com.finpro.frontend.pools.ChallengeObjectPool;

public class ChallengeObjectFactory {
    private ChallengeObjectPool pool;

    public ChallengeObjectFactory() {
        this.pool = new ChallengeObjectPool();
    }

    public ChallengeObject createObject(float x, float y, float width, float height,
                                        Texture texture, int pointValue) {
        ChallengeObject obj = pool.obtain();
        obj.set(x, y, width, height, texture, pointValue);
        return obj;
    }

    public ChallengeObject createMovingObject(float x, float y, float width, float height,
                                              Texture texture, int pointValue,
                                              float velocityX, float velocityY) {
        ChallengeObject obj = pool.obtain();
        obj.setWithVelocity(x, y, width, height, texture, pointValue, velocityX, velocityY);
        return obj;
    }

    public void releaseObject(ChallengeObject obj) {
        pool.release(obj);
    }

    public void releaseAll() {
        pool.releaseAll();
    }

    public ChallengeObjectPool getPool() {
        return pool;
    }

    public int getActiveCount() {
        return pool.getActiveCount();
    }
}

