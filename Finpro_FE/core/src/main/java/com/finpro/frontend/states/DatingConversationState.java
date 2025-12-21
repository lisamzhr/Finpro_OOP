package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.finpro.frontend.MusicManager;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.strategies.DatingStrategy;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class DatingConversationState implements GameState {
    protected GameStateManager gsm;
    private Player player;
    private Texture background;
    private Texture boyImage;
    private Texture textBox;
    private Texture questionBox;
    private BitmapFont font;
    private DatingStrategy strategy;
    private String boyId;
    private ButtonManager buttonManager;
    private GlyphLayout layout;

    private int currentStage;
    private int totalPoints;
    private int maxConversationStages;

    private String currentQuestion;
    private List<Button> choiceButtons;
    private ShapeRenderer shapeRenderer;

    public DatingConversationState(GameStateManager gsm, DatingStrategy strategy,
                                   String boyId, ButtonManager buttonManager) {
        this.gsm = gsm;
        this.player = gsm.getPlayer();
        this.strategy = strategy;
        this.boyId = boyId;
        this.buttonManager = buttonManager;
        this.currentStage = 0;
        this.totalPoints = 0;
        this.maxConversationStages = strategy.datingConversationStage();

        background = new Texture("dating/" + boyId.toLowerCase() + "_Background_Conv.png");
        boyImage = new Texture("dating/" + boyId.toLowerCase() + "_full.png");
        textBox = new Texture("dating/textBox_" + boyId.toLowerCase() +".png");
        questionBox = new Texture("dating/decisionBox.png");

        MusicManager.getInstance().playMusic(MusicManager.DATING_MUSIC);
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        choiceButtons = new ArrayList<>();

        System.out.println("Active buttons before load: " + buttonManager.getActiveCount());

        loadConversationStage();

        System.out.println("Active buttons after load: " + buttonManager.getActiveCount());
    }

    private void loadConversationStage() {
        System.out.println("Loading stage " + currentStage + " - Releasing " + choiceButtons.size() + " old buttons");
        for (Button btn : choiceButtons) {
            buttonManager.releaseButton(btn);
        }
        choiceButtons.clear();
        currentQuestion = strategy.getConversationQuestion(currentStage);
        String[][] choices = strategy.getConversationChoices(currentStage);

        float startY = 300;
        float spacing = 100;
        float buttonWidth = 600;
        float buttonHeight = 80;

        for (int i = 0; i < choices.length; i++) {
            String text = choices[i][0];
            int points = Integer.parseInt(choices[i][1]);

            Button btn = buttonManager.createChoiceButtonWithTexture(
                text,
                Gdx.graphics.getWidth() / 2f - buttonWidth/2,
                startY - (i * spacing),
                buttonWidth,
                buttonHeight,
                points,
                questionBox
            );

            if (btn == null) {
                System.err.println("ERROR: Failed to create choice button at stage " + currentStage);
            }

            choiceButtons.add(btn);
        }

        System.out.println("Created " + choiceButtons.size() + " buttons for stage " + currentStage);
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
                System.out.println("Conversation finished! Releasing buttons before challenge...");
                System.out.println("Active buttons before cleanup: " + buttonManager.getActiveCount());

                for (Button btn : choiceButtons) {
                    buttonManager.releaseButton(btn);
                }
                choiceButtons.clear();

                System.out.println("Active buttons after cleanup: " + buttonManager.getActiveCount());
                gsm.push(new ChallengeState(gsm, strategy, boyId, totalPoints, buttonManager));
            }
        }
    }

    private void drawWrappedText(SpriteBatch batch, BitmapFont font, Texture background,
                                 String text, float boxX, float boxY, float boxWidth, float boxHeight) {
        batch.draw(background, boxX, boxY, boxWidth, boxHeight);

        if (text == null || text.isEmpty()) {
            return;
        }

        float maxScale = 2f;
        float minScale = 0.8f;
        float currentScale = maxScale;
        float scaleStep = 0.2f;

        float horizontalPadding = 40;
        float verticalPadding = 35;

        List<String> bestFitLines = null;
        float bestScale = minScale;

        while (currentScale >= minScale) {
            font.getData().setScale(currentScale);

            List<String> lines = wrapText(font, text, boxWidth - horizontalPadding);
            layout.setText(font, "A");
            float lineHeight = layout.height;
            float totalTextHeight = lines.size() * lineHeight;
            if (totalTextHeight <= (boxHeight - verticalPadding)) {
                bestFitLines = lines;
                bestScale = currentScale;
                break;
            }

            currentScale -= scaleStep;
        }

        if (bestFitLines == null) {
            font.getData().setScale(minScale);
            bestFitLines = wrapText(font, text, boxWidth - horizontalPadding);
            bestScale = minScale;
        }
        font.getData().setScale(bestScale);
        font.setColor(0, 0, 0, 1);

        layout.setText(font, "A");
        float lineHeight = layout.height + 5;
        float totalTextHeight = bestFitLines.size() * lineHeight;
        float textY = boxY + (boxHeight + totalTextHeight) / 2;

        for (String textLine : bestFitLines) {
            layout.setText(font, textLine);
            float textX = boxX + (boxWidth - layout.width) / 2; // Center horizontally
            font.draw(batch, textLine, textX, textY);
            textY -= lineHeight;
        }

        font.setColor(1, 1, 1, 1);
        font.getData().setScale(1f);
    }

    // Helper method: wrap text berdasarkan width
    private List<String> wrapText(BitmapFont font, String text, float maxWidth) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        List<String> lines = new ArrayList<>();

        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            layout.setText(font, testLine);

            if (layout.width > maxWidth) {
                if (line.length() > 0) {
                    lines.add(line.toString().trim());
                    line = new StringBuilder(word);
                } else {
                    // Single word too long, force add
                    lines.add(word);
                    line = new StringBuilder();
                }
            } else {
                line = new StringBuilder(testLine);
            }
        }

        if (line.length() > 0) {
            lines.add(line.toString().trim());
        }

        return lines;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float playerX = 50;
        float playerY = 50;
        if(boyId.equals("ALEX")){
            playerX = 270;
        }
        player.render(batch, playerX, playerY);

        int boyPos = 300;
        if (boyId.equals("ALEX")) {
            boyPos = 1100;
        }
        batch.draw(boyImage, boyPos, 0, boyImage.getWidth()/2, boyImage.getHeight()/2);

        float boxWidth = 750;
        float boxHeight = 120;
        float boxX = (Gdx.graphics.getWidth() - boxWidth) / 2;
        float boxY = 550;

        drawWrappedText(batch, font, textBox, currentQuestion, boxX, boxY, boxWidth, boxHeight);

        for (Button btn : choiceButtons) {
            btn.render(batch, font);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        System.out.println("=== DatingConversationState Disposing ===");
        System.out.println("Active buttons before dispose: " + buttonManager.getActiveCount());

        background.dispose();
        boyImage.dispose();
        textBox.dispose();
        questionBox.dispose();
        font.dispose();
        shapeRenderer.dispose();

        for (Button btn : choiceButtons) {
            buttonManager.releaseButton(btn);
        }
        choiceButtons.clear();

        System.out.println("Active buttons after dispose: " + buttonManager.getActiveCount());
    }
}
