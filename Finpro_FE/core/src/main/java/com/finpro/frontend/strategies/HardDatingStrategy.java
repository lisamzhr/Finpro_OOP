package com.finpro.frontend.strategies;

public class HardDatingStrategy implements DatingStrategy{
    @Override
    public int datingConversationStage() { return 3; }

    @Override
    public int challengeStage() { return 1; }

    @Override
    public boolean isPass(int totalPoints) { return totalPoints >= 22; }

    @Override
    public String getFinalMessage(int totalPoints) {
        if (totalPoints >= 30) {
            return "Chris: You're my perfect muse... Let me write poetry about you! ";
        } else if (totalPoints >= 22) {
            return "Chris: You have a beautiful soul. I'd love to see you again ";
        } else {
            return "Chris: We see the world differently... But that's okay.";
        }
    }

    @Override
    public String getStory() {
        return "Chris adalah mahasiswa Fakultas Hukum yang unik. Di balik pemikiran " +
            "logisnya tentang justice dan law, dia punya jiwa seni yang mendalam. " +
            "Dia melukis untuk mengekspresikan perasaan, menulis puisi di waktu senggang, " +
            "dan percaya bahwa art is the highest form of hope. " +
            "Kalian akan date di art gallery favoritnya!";
    }

    @Override
    public String getConversationQuestion(int stage) {
        switch(stage) {
            case 0: return "Chris: Welcome! This painting is my favorite. What do you see when you look at abstract art?";
            case 1: return "Chris: Interesting...Law school is very logical, but I need art to balance it. How do you balance your life?";
            case 2: return "Chris: Deep. Last question - if I paint your portrait, what emotion should I capture?";
            default: return "";
        }
    }

    @Override
    public String[][] getConversationChoices(int stage) {
        switch(stage) {
            case 0: return new String[][] {
                {"I see emotions, stories, and the artist's soul expressed freely", "2"},
                {"It's about interpretation, everyone sees something different", "8"},
                {"Honestly? Just random colors and shapes to me", "5"}
            };
            case 1: return new String[][] {
                {"I balance logic with creativity too, both are important", "8"},
                {"I focus on one thing at a time, easier that way", "5"},
                {"I just go with the flow, no specific balance", "2"}
            };
            case 2: return new String[][] {
                {"Capture the spark in my eyes when I'm passionate about something", "4"},
                {"Something peaceful and content, I guess", "2"},
                {"I don't know... whatever you want?", "8"}
            };
            default: return new String[][]{};
        }
    }

    @Override
    public String getChallengeDescription() {
        return "Chris wants to catch his favorite paint";
    }
}
