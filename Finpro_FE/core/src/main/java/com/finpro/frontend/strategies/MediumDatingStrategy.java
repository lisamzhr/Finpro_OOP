package com.finpro.frontend.strategies;

public class MediumDatingStrategy implements DatingStrategy {
    @Override
    public int datingConversationStage() { return 4; }

    @Override
    public int challengeStage() { return 1; }

    @Override
    public boolean isPass(int totalPoints) { return totalPoints >= 18; }

    @Override
    public String getFinalMessage(int totalPoints) {
        if (totalPoints >= 25) {
            return "Brian: You're really smart! I'd love to see you again! 🤓💕";
        } else if (totalPoints >= 18) {
            return "Brian: Interesting conversation, let's stay in touch!";
        } else {
            return "Brian: That was... okay. Maybe next time.";
        }
    }

    @Override
    public String getStory() {
        return "Brian adalah computer science major. Dia cerdas, suka coding, " +
            "dan passionate tentang technology. Date di library!";
    }

    @Override
    public String getConversationQuestion(int stage) {
        switch(stage) {
            case 0: return "Brian: Hi! What interests you intellectually?";
            case 1: return "Brian: Fascinating. What's your take on technology?";
            case 2: return "Brian: I see. How do you approach problem-solving?";
            case 3: return "Brian: Interesting perspective. Last question?";
            default: return "";
        }
    }

    @Override
    public String[][] getConversationChoices(int stage) {
        switch(stage) {
            case 0: return new String[][] {
                {"Science & Tech", "5"},
                {"Arts & Music", "2"},
                {"Gaming", "4"}
            };
            case 1: return new String[][] {
                {"AI is the future", "5"},
                {"Tech is overrated", "1"},
                {"Balanced view", "3"}
            };
            case 2: return new String[][] {
                {"Analytical approach", "5"},
                {"Creative approach", "3"},
                {"Ask for help", "2"}
            };
            case 3: return new String[][] {
                {"Compliment intelligence", "5"},
                {"Ask about projects", "4"},
                {"Make tech joke", "3"}
            };
            default: return new String[][]{};
        }
    }

    @Override
    public String getChallengeDescription() {
        return "Brian challenges you to solve a coding puzzle together! " +
            "Can you debug this algorithm?";
    }
    @Override
    public ChallengeGame createChallenge() {
        // Return a new instance of the challenge game for this character
        return new MediumChallenge();
    }
}
