package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.ButtonManager;
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
    private ButtonManager buttonManager;

    private int currentStage;
    private int totalPoints;
    private int maxConversationStages;

    private String currentQuestion;
    private List<Button> choiceButtons;
    private ShapeRenderer shapeRenderer;

    public DatingConversationState(GameStateManager gsm, DatingStrategy strategy,
                                   String boyId, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.strategy = strategy;
        this.boyId = boyId;
        this.buttonManager = buttonManager;
        this.currentStage = 0;
        this.totalPoints = 0;
        this.maxConversationStages = strategy.datingConversationStage();

        background = new Texture("dating/" + boyId.toLowerCase() + "_Background_Conv.png");
        boyImage = new Texture("dating/" + boyId.toLowerCase() + "_full.png");
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();

        choiceButtons = new ArrayList<>();
        loadConversationStage();
    }

    private void loadConversationStage() {
        // Release previous buttons back to pool
        for (Button btn : choiceButtons) {
            buttonManager.releaseButton(btn);
        }
        choiceButtons.clear();

        // Get question and choices from strategy
        currentQuestion = strategy.getConversationQuestion(currentStage);
        String[][] choices = strategy.getConversationChoices(currentStage);

        float startY = 300;
        float spacing = 80;

        for (int i = 0; i < choices.length; i++) {
            String text = choices[i][0];
            int points = Integer.parseInt(choices[i][1]);

            // Create choice button using ButtonManager
            Button btn = buttonManager.createChoiceButton(
                text,
                Gdx.graphics.getWidth() / 2f - 300,
                startY - (i * spacing),
                600,
                60,
                points
            );
            choiceButtons.add(btn);
        }
    }

    @Override
    public void update(float delta) {
        boolean buttonClicked = false;
        Button clickedButton = null;

        // First pass: detect clicks without modifying the list
        for (Button btn : choiceButtons) {
            btn.update();

            if (btn.isClicked()) {
                buttonClicked = true;
                clickedButton = btn;
                break;
            }
        }

        // Second pass: handle the clicked button AFTER iteration is done
        if (buttonClicked && clickedButton != null) {
            totalPoints += clickedButton.getPoints();
            System.out.println("Points gained: " + clickedButton.getPoints() + " | Total: " + totalPoints);

            currentStage++;

            if (currentStage < maxConversationStages) {
                loadConversationStage();
            } else {
                // Move to challenge state
                gsm.push(new ChallengeState(gsm, strategy, boyId, totalPoints));
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        // Draw background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        int boyPos = 300;
        if (boyId.equals("ALEX")) {
            boyPos = 1100;
        }
        batch.draw(boyImage, boyPos, 0, boyImage.getWidth()/2, boyImage.getHeight()/2);

        batch.end();

        // Draw semi-transparent boxes
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Question box background
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(480, 650, 750, 80);
        shapeRenderer.end();

        // Stage counter background
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(30, Gdx.graphics.getHeight() - 50, 300, 40);
        shapeRenderer.end();

        // Points counter background
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(Gdx.graphics.getWidth() - 180, Gdx.graphics.getHeight() - 50, 160, 40);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();

        // Draw question text
        font.getData().setScale(2f);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, currentQuestion, 500, 710);

        // Draw stage counter
        font.getData().setScale(1.2f);
        font.draw(batch, "Stage: " + (currentStage + 1) + "/" + maxConversationStages,
            50, Gdx.graphics.getHeight() - 20);

        // Draw points
        font.draw(batch, "Points: " + totalPoints,
            Gdx.graphics.getWidth() - 160, Gdx.graphics.getHeight() - 20);

        // Draw choice buttons
        for (Button btn : choiceButtons) {
            btn.render(batch, font);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        boyImage.dispose();
        font.dispose();
        shapeRenderer.dispose();

        // Release all buttons back to pool
        for (Button btn : choiceButtons) {
            buttonManager.releaseButton(btn);
        }
        choiceButtons.clear();
    }
}
