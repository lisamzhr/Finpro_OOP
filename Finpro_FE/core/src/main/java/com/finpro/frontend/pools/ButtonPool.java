package com.finpro.frontend.pools;


import com.finpro.frontend.models.Button;

public class ButtonPool extends ObjectPool<Button> {

    @Override
    protected Button createObject() {
        return new Button();
    }

    @Override
    protected void resetObject(Button button) {
        button.reset();
    }
}
