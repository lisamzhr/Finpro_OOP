package com.finpro.frontend.strategies;

public class MediumDatingStrategy implements DatingStrategy {
    @Override
    public int datingConversationStage() {
        return 0;
    }

    @Override
    public int challengeStage() {
        return 0;
    }

    @Override
    public boolean isPass(int totalPoints) {
        return false;
    }

    @Override
    public void finalStage(int totalPoints) {

    }
}
