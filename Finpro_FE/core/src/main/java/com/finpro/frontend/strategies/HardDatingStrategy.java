package com.finpro.frontend.strategies;

public class HardDatingStrategy implements DatingStrategy{
    @Override
    public int datingConversationStage() { return 3; }

    @Override
    public int challengeStage() { return 1; }

    @Override
    public boolean isPass(int totalPoints) { return totalPoints >= 12; }

    @Override
    public String getFinalMessage(int totalPoints) {
        if (totalPoints >= 20) {
            return "Chris: You inspire me! Let me paint you sometime! 🎨❤️";
        } else if (totalPoints >= 12) {
            return "Chris: Thanks for the lovely time! 😊";
        } else {
            return "Chris: Maybe we're not on the same wavelength...";
        }
    }

    @Override
    public String getStory() {
        return "Chris adalah art student. Dia romantic, creative, dan " +
            "passionate tentang seni. Dia suka music, painting, dan poetry. " +
            "Kalian akan date di art gallery!";
    }

    @Override
    public String getConversationQuestion(int stage) {
        switch(stage) {
            case 0: return "Chris: Hey! What kind of art speaks to your soul?";
            case 1: return "Chris: Beautiful answer. How do you express yourself?";
            case 2: return "Chris: I love that. What moves you emotionally?";
            default: return "";
        }
    }

    @Override
    public String[][] getConversationChoices(int stage) {
        switch(stage) {
            case 0: return new String[][] {
                {"Visual arts & paintings", "5"},
                {"Music & poetry", "5"},
                {"Not really into art", "1"}
            };
            case 1: return new String[][] {
                {"Through creativity", "5"},
                {"Through words", "4"},
                {"I don't really", "2"}
            };
            case 2: return new String[][] {
                {"Beauty in simple things", "5"},
                {"Deep connections", "5"},
                {"Success & achievements", "2"}
            };
            default: return new String[][]{};
        }
    }

    @Override
    public String getChallengeDescription() {
        return "Chris wants to paint a portrait of you! " +
            "Will you be his muse for the evening?";
    }
    @Override
    public ChallengeGame createChallenge() {
        // Return a new instance of the challenge game for this character
        return new MediumChallenge();
    }
}
