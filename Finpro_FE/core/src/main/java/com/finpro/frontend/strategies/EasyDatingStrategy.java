package com.finpro.frontend.strategies;

public class EasyDatingStrategy implements DatingStrategy {
    @Override
    public int datingConversationStage() {
        return 3;
    }

    @Override
    public int challengeStage() {
        return 1;
    }

    @Override
    public boolean isPass(int totalPoints) {
        return totalPoints >= 15;
    }

    @Override
    public String getFinalMessage(int totalPoints) {
        if (totalPoints >= 20) {
            return "Alex: I really like you! Let's meet again! 💖";
        } else if (totalPoints >= 15) {
            return "Alex: That was fun! See you around! 😊";
        } else {
            return "Alex: Maybe we should just be friends...";
        }
    }

    @Override
    public String getStory() {
        return "Alex adalah atlet basket di kampus. Dia energetic, " +
            "suka outdoor activities, dan punya personality yang fun. " +
            "Kalian akan date di gym hall!";
    }

    @Override
    public String getConversationQuestion(int stage) {
        switch(stage) {
            case 0: return "Alex: Hey! What do you like to do for fun?";
            case 1: return "Alex: Haha nice! So, what's your vibe?";
            case 2: return "Alex: Cool! Before we start, any last words?";
            default: return "";
        }
    }

    @Override
    public String[][] getConversationChoices(int stage) {
        switch(stage) {
            case 0: return new String[][] {
                {"Talk about sports", "5"},
                {"Talk about movies", "2"},
                {"Talk about food", "3"}
            };
            case 1: return new String[][] {
                {"Joke around", "4"},
                {"Be serious", "2"},
                {"Flirt", "5"}
            };
            case 2: return new String[][] {
                {"Compliment him", "5"},
                {"Ask about his hobby", "3"},
                {"Share your story", "4"}
            };
            default: return new String[][]{};
        }
    }

    @Override
    public String getChallengeDescription() {
        return "Alex challenges you to a 3-point shootout! " +
            "He wants to see if you can keep up with him!";
    }
}
