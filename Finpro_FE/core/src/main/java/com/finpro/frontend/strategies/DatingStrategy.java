package com.finpro.frontend.strategies;

public interface DatingStrategy {
    int datingConversationStage();
    int challengeStage();
    boolean isPass(int totalPoints);
    String getFinalMessage(int totalPoints);

    // New methods for LibGDX
    String getStory();
    String getConversationQuestion(int stage);
    String[][] getConversationChoices(int stage); // [choice text, points]
    String getChallengeDescription();
}
