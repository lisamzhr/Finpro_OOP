package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.finpro.frontend.models.ChoiceButton;
import com.finpro.frontend.strategies.DatingStrategy;
import java.util.ArrayList;
import java.util.List;

public class DatingConversationState implements GameState {
    protected GameStateManager gsm;
    private Texture background;
    private Texture boyImage;
    private BitmapFont font;
    private DatingStrategy strategy;
    private String boyId;

    private int currentStage;
    private int totalPoints;
    private int maxConversationStages;

    private String currentQuestion;
    private List<ChoiceButton> choiceButtons;

    public DatingConversationState(GameStateManager gsm, DatingStrategy strategy, String boyId) {
        this.gsm = gsm;
        this.strategy = strategy;
        this.boyId = boyId;
        this.currentStage = 0;
        this.totalPoints = 0;
        this.maxConversationStages = strategy.datingConversationStage();

        background = new Texture("dating/datingGarden.png");
        boyImage = new Texture("dating/" + boyId.toLowerCase() + "_full.png");
        font = new BitmapFont();

        choiceButtons = new ArrayList<>();
        loadConversationStage();
    }

    private void loadConversationStage() {
        choiceButtons.clear();

        // Get question and choices from strategy
        currentQuestion = strategy.getConversationQuestion(currentStage);
        String[][] choices = strategy.getConversationChoices(currentStage);

        float startY = 300;
        float spacing = 80;

        for (int i = 0; i < choices.length; i++) {
            String text = choices[i][0];
            int points = Integer.parseInt(choices[i][1]);

            ChoiceButton btn = new ChoiceButton(
                text,
                Gdx.graphics.getWidth() / 2f - 300,
                startY - (i * spacing),
                600, 60,
                points
            );
            choiceButtons.add(btn);
        }
    }

    @Override
    public void update(float delta) {
        for (ChoiceButton btn : choiceButtons) {
            btn.update();

            if (btn.isClicked()) {
                totalPoints += btn.getPoints();
                System.out.println("Points gained: " + btn.getPoints() + " | Total: " + totalPoints);

                currentStage++;

                if (currentStage < maxConversationStages) {
                    loadConversationStage();
                } else {
                    // Move to challenge state
                    gsm.push(new ChallengeState(gsm, strategy, boyId, totalPoints));
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(boyImage, 50, 100, 400, 600);

        // Draw question
        font.getData().setScale(1.5f);
        font.draw(batch, currentQuestion, 500, 700);

        // Draw stage counter
        font.getData().setScale(1f);
        font.draw(batch, "Stage: " + (currentStage + 1) + "/" + maxConversationStages,
            50, Gdx.graphics.getHeight() - 20);
        font.draw(batch, "Points: " + totalPoints,
            Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 20);

        // Draw choice buttons
        for (ChoiceButton btn : choiceButtons) {
            btn.render(batch, font);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        boyImage.dispose();
        font.dispose();
        for (ChoiceButton btn : choiceButtons) {
            btn.dispose();
        }
    }
}
