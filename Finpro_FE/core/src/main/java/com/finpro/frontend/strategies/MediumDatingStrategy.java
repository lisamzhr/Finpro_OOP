package com.finpro.frontend.strategies;

public class MediumDatingStrategy implements DatingStrategy {
    @Override
    public int datingConversationStage() { return 4; }

    @Override
    public int challengeStage() { return 1; }

    @Override
    public boolean isPass(int totalPoints) { return totalPoints >= 25; } // ✅ Lebih challenging

    @Override
    public String getFinalMessage(int totalPoints) {
        if (totalPoints >= 32) {
            return "Brian: You're amazing! Dinner date next? My treat! 🏀💕";
        } else if (totalPoints >= 25) {
            return "Brian: That was fun! Let's hang out again sometime! 😊";
        } else {
            return "Brian: It was nice meeting you, but I don't think we clicked...";
        }
    }

    @Override
    public String getStory() {
        return "Brian adalah mahasiswa Fakultas Ekonomi dan Bisnis yang aktif dan ambisius. " +
            "Paginya dia analisis saham, siangnya kuliah bisnis, " +
            "sorenya latihan basket buat DBL. Dia ngajak date di Blok M !";
    }

    @Override
    public String getConversationQuestion(int stage) {
        switch(stage) {
            case 0: return "Brian: Hi! What's your major?";
            case 1: return "Brian: Cool. What's your vibe for weekend plans?";
            case 2: return "Brian: Nice! By the way, I'm playing in DBL this season. Ever watched DBL games?";
            case 3: return "Brian: Awesome! Last one - where do you usually hang out on weekends?";
            default: return "";
        }
    }

    @Override
    public String[][] getConversationChoices(int stage) {
        switch(stage) {
            case 0: return new String[][] {
                {"Business or Economics - I'm into finance and entrepreneurship!", "5"},
                {"Tech or Engineering - innovation is the future", "3"},
                {"Arts or Social Sciences - creativity and humanity matter", "2"}
            };
            case 1: return new String[][] {
                {"Hit the gym, morning runs, or play sports with friends", "5"},
                {"Balance between active and chill, gym in the morning, hangout at night", "3"},
                {"Netflix marathon and sleep in. I need my rest!", "2"}
            };
            case 2: return new String[][] {
                {"Yes! I love watching DBL, the energy and skills are insane!", "5"},
                {"I've heard about it! Never watched live but sounds exciting", "3"},
                {"Not really into basketball... more of a badminton person", "2"}
            };
            case 3: return new String[][] {
                {"Sports venues, gyms, courts - anywhere I can stay active!", "5"},
                {"Coffee shops or malls - good for hanging out and people watching", "3"},
                {"Usually just at home, I'm a homebody honestly", "2"}
            };
            default: return new String[][]{};
        }
    }

    @Override
    public String getChallengeDescription() {
        return "Brian wants to test your reflexes! " +
            "Catch the purple flowers!";
    }
}
