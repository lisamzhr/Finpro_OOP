package com.finpro.frontend.models;

public class ChoiceButton extends SimpleButton {
    private int points;

    public ChoiceButton(String text, float x, float y, float width, float height, int points) {
        super(text, x, y, width, height);
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
