package com.finpro.frontend.strategies;

public interface DatingStrategy {
    public int datingConversationStage();
    public int challengeStage();
    public boolean isPass(int totalPoints);
    public void finalStage(int totalPoints);
}
