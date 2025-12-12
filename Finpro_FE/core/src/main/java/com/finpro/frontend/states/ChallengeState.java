package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.finpro.frontend.models.SimpleButton;
import com.finpro.frontend.strategies.DatingStrategy;

public class ChallengeState implements GameState {
    protected GameStateManager gsm;
    private Texture background;
    private BitmapFont font;
    private DatingStrategy strategy;
    private String boyId;
    private int totalPoints;

    private String challengeDescription;
    private SimpleButton acceptButton;
    private SimpleButton declineButton;

    public ChallengeState(GameStateManager gsm, DatingStrategy strategy,
                          String boyId, int totalPoints) {
        this.gsm = gsm;
        this.strategy = strategy;
        this.boyId = boyId;
        this.totalPoints = totalPoints;

        background = new Texture("dating/" + boyId.toLowerCase() + "_Background_Chall.png");
        font = new BitmapFont();

        challengeDescription = strategy.getChallengeDescription();

        float centerX = Gdx.graphics.getWidth() / 2f;
        acceptButton = new SimpleButton("Accept Challenge (+10)",
            centerX - 250, 200, 200, 60);
        declineButton = new SimpleButton("Decline (-5)",
            centerX + 50, 200, 200, 60);
    }

    @Override
    public void update(float delta) {
        acceptButton.update();
        declineButton.update();

        if (acceptButton.isClicked()) {
            totalPoints += 10;
            gsm.push(new ResultState(gsm, strategy, boyId, totalPoints));
        }

        if (declineButton.isClicked()) {
            totalPoints -= 5;
            gsm.push(new ResultState(gsm, strategy, boyId, totalPoints));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font.getData().setScale(2f);
        font.draw(batch, "CHALLENGE TIME!",
            Gdx.graphics.getWidth() / 2f - 150,
            Gdx.graphics.getHeight() - 100);

        font.getData().setScale(1.5f);
        font.draw(batch, challengeDescription,
            Gdx.graphics.getWidth() / 2f - 300, 500);

        acceptButton.render(batch, font);
        declineButton.render(batch, font);

        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
        acceptButton.dispose();
        declineButton.dispose();
    }
}
