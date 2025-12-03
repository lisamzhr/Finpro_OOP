package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.services.BackendService;
import com.badlogic.gdx.graphics.GL20;

public class StartGameState implements GameState {

    private GameStateManager gsm;
    private BackendService backend;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private BitmapFont titleFont;

    private Rectangle registerButton;
    private Rectangle loginButton;
    private Rectangle okButton;
    private Rectangle inputBox;

    private boolean loading = false;
    private String screenMode = "menu"; // "menu", "register_input", "register_success", "login_username", "login_playerid"
    private String inputText = "";
    private String tempUsername = "";
    private String createdPlayerId = "";

    private static final Color CREAM = new Color(1f, 0.98f, 0.9f, 1f);
    private static final Color PINK = new Color(1f, 0.75f, 0.8f, 1f);
    private static final Color DARK_PINK = new Color(0.9f, 0.6f, 0.65f, 1f);
    private static final Color INPUT_BG = new Color(1f, 1f, 1f, 1f);

    public StartGameState(GameStateManager gsm) {
        this.gsm = gsm;
        backend = new BackendService();
        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();
        font.getData().setScale(1.5f);

        titleFont = new BitmapFont();
        titleFont.getData().setScale(2.5f);

        float buttonWidth = 200;
        float buttonHeight = 60;
        float centerX = Gdx.graphics.getWidth() / 2f - buttonWidth / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        registerButton = new Rectangle(centerX, centerY + 50, buttonWidth, buttonHeight);
        loginButton = new Rectangle(centerX, centerY - 50, buttonWidth, buttonHeight);
        okButton = new Rectangle(centerX, centerY - 150, buttonWidth, buttonHeight);
        inputBox = new Rectangle(centerX - 100, centerY, 400, 50);

        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
            @Override
            public boolean keyTyped(char character) {
                if (screenMode.equals("menu") || screenMode.equals("register_success") || loading) {
                    return false;
                }

                if (character == '\b' && inputText.length() > 0) {
                    inputText = inputText.substring(0, inputText.length() - 1);
                } else if (character == '\r' || character == '\n') {
                    handleInputSubmit();
                } else if (Character.isLetterOrDigit(character) || character == '-' || character == '_') {
                    if (inputText.length() < 20) {
                        inputText += character;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void update(float delta) {
        if (loading) return;

        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (screenMode.equals("menu")) {
                if (registerButton.contains(x, y)) {
                    screenMode = "register_input";
                    inputText = "";
                } else if (loginButton.contains(x, y)) {
                    screenMode = "login_username";
                    inputText = "";
                    tempUsername = "";
                }
            } else if (screenMode.equals("register_success")) {
                if (okButton.contains(x, y)) {
                    Player p = new Player(createdPlayerId, tempUsername);
                    gsm.set(new MenuState(gsm, p));
                }
            } else if (!screenMode.equals("menu")) {
                // Check if clicked outside input box to submit
                if (!inputBox.contains(x, y) && y < inputBox.y) {
                    handleInputSubmit();
                }
            }
        }
    }

    private void handleInputSubmit() {
        String trimmed = inputText.trim();
        if (trimmed.isEmpty()) return;

        if (screenMode.equals("register_input")) {
            loading = true;
            tempUsername = trimmed;

            backend.register(tempUsername, new BackendService.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    createdPlayerId = extractValue(response, "playerId");

                    System.out.println("REGISTER SUCCESS! PlayerId: " + createdPlayerId);

                    Gdx.app.postRunnable(() -> {
                        loading = false;
                        inputText = "";
                        screenMode = "register_success";
                    });
                }

                @Override
                public void onError(String error) {
                    System.err.println("REGISTER ERROR: " + error);
                    Gdx.app.postRunnable(() -> {
                        loading = false;
                        inputText = "";
                        screenMode = "menu";
                    });
                }
            });

        } else if (screenMode.equals("login_username")) {
            tempUsername = trimmed;
            inputText = "";
            screenMode = "login_playerid";

        } else if (screenMode.equals("login_playerid")) {
            loading = true;
            String playerId = trimmed;

            backend.login(tempUsername, playerId, new BackendService.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    String pid = extractValue(response, "playerId");
                    String uname = extractValue(response, "username");

                    System.out.println("LOGIN SUCCESS!");

                    Player p = new Player(pid, uname);
                    Gdx.app.postRunnable(() -> gsm.set(new MenuState(gsm, p)));
                }

                @Override
                public void onError(String error) {
                    System.err.println("LOGIN ERROR: " + error);
                    Gdx.app.postRunnable(() -> {
                        loading = false;
                        inputText = "";
                        tempUsername = "";
                        screenMode = "menu";
                    });
                }
            });
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // Clear screen
        Gdx.gl.glClearColor(CREAM.r, CREAM.g, CREAM.b, CREAM.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (screenMode.equals("menu")) {
            renderMenuScreen();
        } else if (screenMode.equals("register_input")) {
            renderRegisterInputScreen();
        } else if (screenMode.equals("register_success")) {
            renderRegisterSuccessScreen();
        } else if (screenMode.equals("login_username")) {
            renderLoginUsernameScreen();
        } else if (screenMode.equals("login_playerid")) {
            renderLoginPlayerIdScreen();
        }

        shapeRenderer.end();

        // Draw text
        batch.begin();

        if (screenMode.equals("menu")) {
            renderMenuText(batch);
        } else if (screenMode.equals("register_input")) {
            renderRegisterInputText(batch);
        } else if (screenMode.equals("register_success")) {
            renderRegisterSuccessText(batch);
        } else if (screenMode.equals("login_username")) {
            renderLoginUsernameText(batch);
        } else if (screenMode.equals("login_playerid")) {
            renderLoginPlayerIdText(batch);
        }

        if (loading) {
            titleFont.setColor(PINK);
            titleFont.draw(batch, "Loading...",
                Gdx.graphics.getWidth()/2 - 100,
                Gdx.graphics.getHeight()/2);
        }

        batch.end();
    }

    private void renderMenuScreen() {
        shapeRenderer.setColor(PINK);
        shapeRenderer.rect(registerButton.x, registerButton.y, registerButton.width, registerButton.height);
        shapeRenderer.rect(loginButton.x, loginButton.y, loginButton.width, loginButton.height);
    }

    private void renderMenuText(SpriteBatch batch) {
        titleFont.setColor(PINK);
        titleFont.draw(batch, "WELCOME!",
            Gdx.graphics.getWidth()/2 - 120,
            Gdx.graphics.getHeight() - 100);

        font.setColor(Color.WHITE);
        font.draw(batch, "REGISTER",
            registerButton.x + 50,
            registerButton.y + 40);
        font.draw(batch, "LOGIN",
            loginButton.x + 70,
            loginButton.y + 40);
    }

    private void renderRegisterInputScreen() {
        shapeRenderer.setColor(INPUT_BG);
        shapeRenderer.rect(inputBox.x, inputBox.y, inputBox.width, inputBox.height);

        shapeRenderer.setColor(PINK);
        shapeRenderer.rectLine(inputBox.x, inputBox.y, inputBox.x + inputBox.width, inputBox.y, 3);
        shapeRenderer.rectLine(inputBox.x, inputBox.y + inputBox.height, inputBox.x + inputBox.width, inputBox.y + inputBox.height, 3);
        shapeRenderer.rectLine(inputBox.x, inputBox.y, inputBox.x, inputBox.y + inputBox.height, 3);
        shapeRenderer.rectLine(inputBox.x + inputBox.width, inputBox.y, inputBox.x + inputBox.width, inputBox.y + inputBox.height, 3);
    }

    private void renderRegisterInputText(SpriteBatch batch) {
        titleFont.setColor(PINK);
        titleFont.draw(batch, "REGISTER",
            Gdx.graphics.getWidth()/2 - 120,
            Gdx.graphics.getHeight() - 100);

        font.setColor(DARK_PINK);
        font.draw(batch, "Enter your username:",
            inputBox.x,
            inputBox.y + inputBox.height + 40);

        font.setColor(Color.BLACK);
        font.draw(batch, inputText + "_",
            inputBox.x + 10,
            inputBox.y + 32);

        font.setColor(Color.GRAY);
        font.getData().setScale(1f);
        font.draw(batch, "Press ENTER to submit",
            inputBox.x,
            inputBox.y - 20);
        font.getData().setScale(1.5f);
    }

    private void renderRegisterSuccessScreen() {
        shapeRenderer.setColor(INPUT_BG);
        float boxWidth = 550;
        float boxHeight = 320;
        float boxX = Gdx.graphics.getWidth()/2 - boxWidth/2;
        float boxY = Gdx.graphics.getHeight()/2 - boxHeight/2;

        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);

        shapeRenderer.setColor(PINK);
        shapeRenderer.rectLine(boxX, boxY, boxX + boxWidth, boxY, 4);
        shapeRenderer.rectLine(boxX, boxY + boxHeight, boxX + boxWidth, boxY + boxHeight, 4);
        shapeRenderer.rectLine(boxX, boxY, boxX, boxY + boxHeight, 4);
        shapeRenderer.rectLine(boxX + boxWidth, boxY, boxX + boxWidth, boxY + boxHeight, 4);

        // Tombol NEXT
        shapeRenderer.setColor(PINK);
        shapeRenderer.rect(okButton.x, okButton.y, okButton.width, okButton.height);
    }

    private void renderRegisterSuccessText(SpriteBatch batch) {
        titleFont.setColor(PINK);
        titleFont.draw(batch, "SUCCESS!",
            Gdx.graphics.getWidth()/2 - 120,
            Gdx.graphics.getHeight()/2 + 120);

        font.setColor(DARK_PINK);
        font.getData().setScale(1.3f);
        font.draw(batch, "Your account has been created!",
            Gdx.graphics.getWidth()/2 - 170,
            Gdx.graphics.getHeight()/2 + 50);
        font.getData().setScale(1.5f);

        font.setColor(Color.BLACK);
        font.draw(batch, "Username: " + tempUsername,
            Gdx.graphics.getWidth()/2 - 150,
            Gdx.graphics.getHeight()/2);

        font.setColor(PINK);
        font.getData().setScale(2f);
        font.draw(batch, "ID: " + createdPlayerId,
            Gdx.graphics.getWidth()/2 - 100,
            Gdx.graphics.getHeight()/2 - 50);
        font.getData().setScale(1.5f);

        font.setColor(Color.RED);
        font.getData().setScale(1.1f);
        font.draw(batch, "SAVE THIS ID! You need it to login.",
            Gdx.graphics.getWidth()/2 - 190,
            Gdx.graphics.getHeight()/2 - 100);
        font.getData().setScale(1.5f);

        // Tombol NEXT
        font.setColor(Color.WHITE);
        font.getData().setScale(1.8f);
        font.draw(batch, "NEXT",
            okButton.x + 65,
            okButton.y + 42);
        font.getData().setScale(1.5f);
    }

    private void renderLoginUsernameScreen() {
        shapeRenderer.setColor(INPUT_BG);
        shapeRenderer.rect(inputBox.x, inputBox.y, inputBox.width, inputBox.height);

        shapeRenderer.setColor(PINK);
        shapeRenderer.rectLine(inputBox.x, inputBox.y, inputBox.x + inputBox.width, inputBox.y, 3);
        shapeRenderer.rectLine(inputBox.x, inputBox.y + inputBox.height, inputBox.x + inputBox.width, inputBox.y + inputBox.height, 3);
        shapeRenderer.rectLine(inputBox.x, inputBox.y, inputBox.x, inputBox.y + inputBox.height, 3);
        shapeRenderer.rectLine(inputBox.x + inputBox.width, inputBox.y, inputBox.x + inputBox.width, inputBox.y + inputBox.height, 3);
    }

    private void renderLoginUsernameText(SpriteBatch batch) {
        titleFont.setColor(PINK);
        titleFont.draw(batch, "LOGIN",
            Gdx.graphics.getWidth()/2 - 80,
            Gdx.graphics.getHeight() - 100);

        font.setColor(DARK_PINK);
        font.draw(batch, "Enter your username:",
            inputBox.x,
            inputBox.y + inputBox.height + 40);

        font.setColor(Color.BLACK);
        font.draw(batch, inputText + "_",
            inputBox.x + 10,
            inputBox.y + 32);

        font.setColor(Color.GRAY);
        font.getData().setScale(1f);
        font.draw(batch, "Press ENTER to continue",
            inputBox.x,
            inputBox.y - 20);
        font.getData().setScale(1.5f);
    }

    private void renderLoginPlayerIdScreen() {
        shapeRenderer.setColor(INPUT_BG);
        shapeRenderer.rect(inputBox.x, inputBox.y, inputBox.width, inputBox.height);

        shapeRenderer.setColor(PINK);
        shapeRenderer.rectLine(inputBox.x, inputBox.y, inputBox.x + inputBox.width, inputBox.y, 3);
        shapeRenderer.rectLine(inputBox.x, inputBox.y + inputBox.height, inputBox.x + inputBox.width, inputBox.y + inputBox.height, 3);
        shapeRenderer.rectLine(inputBox.x, inputBox.y, inputBox.x, inputBox.y + inputBox.height, 3);
        shapeRenderer.rectLine(inputBox.x + inputBox.width, inputBox.y, inputBox.x + inputBox.width, inputBox.y + inputBox.height, 3);
    }

    private void renderLoginPlayerIdText(SpriteBatch batch) {
        titleFont.setColor(PINK);
        titleFont.draw(batch, "LOGIN",
            Gdx.graphics.getWidth()/2 - 80,
            Gdx.graphics.getHeight() - 100);

        font.setColor(DARK_PINK);
        font.draw(batch, "Username: " + tempUsername,
            inputBox.x,
            inputBox.y + inputBox.height + 80);

        font.draw(batch, "Enter your Player ID:",
            inputBox.x,
            inputBox.y + inputBox.height + 40);

        font.setColor(Color.BLACK);
        font.draw(batch, inputText + "_",
            inputBox.x + 10,
            inputBox.y + 32);

        font.setColor(Color.GRAY);
        font.getData().setScale(1f);
        font.draw(batch, "Press ENTER to login",
            inputBox.x,
            inputBox.y - 20);
        font.getData().setScale(1.5f);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        titleFont.dispose();
    }

    private String extractValue(String json, String key) {
        try {
            String search = "\"" + key + "\":\"";
            int start = json.indexOf(search) + search.length();
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }
}
