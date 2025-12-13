package com.finpro.frontend.pools;

import com.finpro.frontend.models.ChallengeObject;

public class ChallengeObjectPool extends ObjectPool<ChallengeObject> {

    @Override
    protected ChallengeObject createObject() {
        return new ChallengeObject();
    }

    @Override
    protected void resetObject(ChallengeObject object) {
        object.reset();
    }
}
