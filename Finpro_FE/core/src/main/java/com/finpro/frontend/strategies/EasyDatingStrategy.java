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
            return "Alex: I really like you! Let's meet again!";
        } else if (totalPoints >= 15) {
            return "Alex: That was fun! See you around!";
        } else {
            return "Alex: Maybe we should just be friends...";
        }
    }

    @Override
    public String getStory() {
        return "Alex adalah Mahasiswa Teknik Komputer. Dia sangat nonchalant, " +
            "Dia suka belajar dan hal-hal berbau tech and programming. " +
            "Kalian akan date di library!";
    }

    @Override
    public String getConversationQuestion(int stage) {
        switch(stage) {
            case 0: return "Alex: Hi! Do you like coding?";
            case 1: return "Alex: Haha nice! So, what's your hobby?";
            case 2: return "Alex: Cool! Before we start, any last words?";
            default: return "";
        }
    }

    @Override
    public String[][] getConversationChoices(int stage) {
        switch(stage) {
            case 0: return new String[][] {
                {"Yes", "5"},
                {"No", "2"},
                {"So so", "3"}
            };
            case 1: return new String[][] {
                {"Painting", "4"},
                {"I have no hobby", "2"},
                {"Study", "5"}
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
        return "Alex challenges you to pick cupcakes, pick carefully! ";
    }
}
