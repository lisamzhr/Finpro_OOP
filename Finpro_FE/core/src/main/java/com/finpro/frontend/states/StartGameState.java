package com.finpro.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.finpro.frontend.MusicManager;
import com.finpro.frontend.models.Player;
import com.finpro.frontend.models.Button;
import com.finpro.frontend.ButtonManager;
import com.finpro.frontend.factory.ButtonFactory;
import com.finpro.frontend.services.BackendService;

public class StartGameState implements GameState {

    private GameStateManager gsm;
    private BackendService backend;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private BitmapFont titleFont;
    private ButtonManager buttonManager;

    private Button registerButton;
    private Button loginButton;
    private Button okButton;
    private Rectangle inputBox;

    private Texture buttonTexture;
    private Texture buttonHoverTexture;
    private Texture startGameBGTexture;

    private boolean loading = false;
    private String screenMode = "menu"; // "menu", "register_input", "register_success", "login_username", "login_playerid"
    private String inputText = "";
    private String tempUsername = "";
    private String createdPlayerId = "";

    private static final Color CREAM = new Color(1f, 0.98f, 0.9f, 1f);
    private static final Color PINK = new Color(1f, 0.75f, 0.8f, 1f);
    private static final Color DARK_PINK = new Color(0.9f, 0.6f, 0.65f, 1f);
    private static final Color INPUT_BG = new Color(1f, 1f, 1f, 1f);

    public StartGameState(GameStateManager gsm, ButtonManager buttonManager) {
        this.gsm = gsm;
        backend = new BackendService();
        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();
        font.getData().setScale(1.5f);

        titleFont = new BitmapFont();
        titleFont.getData().setScale(2.5f);

        MusicManager.getInstance().playMusic(MusicManager.DEFAULT_MUSIC);

        // Initialize ButtonManager
        this.buttonManager = buttonManager;

        // Load button textures
        buttonTexture = new Texture("button_normal.png");
        buttonHoverTexture = new Texture("button_hover.png");

        // Load backgorund
        startGameBGTexture = new Texture("menu/startGameBG.png");

        float buttonWidth = 200;
        float buttonHeight = 60;
        float centerX = Gdx.graphics.getWidth() / 2f - buttonWidth / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        // Create buttons using ButtonManager
        registerButton = buttonManager.createButton(
            "REGISTER",
            centerX,
            centerY + 50,
            buttonWidth,
            buttonHeight,
            buttonTexture,
            buttonHoverTexture
        );

        loginButton = buttonManager.createButton(
            "LOGIN",
            centerX,
            centerY - 50,
            buttonWidth,
            buttonHeight,
            buttonTexture,
            buttonHoverTexture
        );

        okButton = buttonManager.createButton(
            "NEXT",
            centerX,
            centerY - 150,
            180,
            60,
            buttonTexture,
            buttonHoverTexture
        );

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

        if (screenMode.equals("menu")) {
            registerButton.update();
            loginButton.update();

            if (registerButton.isClicked()) {
                screenMode = "register_input";
                inputText = "";
            } else if (loginButton.isClicked()) {
                screenMode = "login_username";
                inputText = "";
                tempUsername = "";
            }
        } else if (screenMode.equals("register_success")) {
            okButton.update();

            if (okButton.isClicked()) {
                Player p = new Player(createdPlayerId, tempUsername, 1);
                gsm.setPlayer(p);
                gsm.set(new MenuState(gsm, buttonManager));
            }
        }
    }

    private void handleInputSubmit() {
        String trimmed = inputText.trim();
        if (trimmed.isEmpty()) return;

        if (screenMode.equals("register_input")) {
            loading = true;
            screenMode = "loading";
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
                        screenMode = "menu"; // Kembali ke menu jika error
                    });
                }
            });

        } else if (screenMode.equals("login_username")) {
            tempUsername = trimmed;
            inputText = "";
            screenMode = "login_playerid";

        } else if (screenMode.equals("login_playerid")) {
            loading = true;
            screenMode = "loading";
            String playerId = trimmed;

            backend.login(tempUsername, playerId, new BackendService.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    String pid = extractValue(response, "playerId");
                    String uname = extractValue(response, "username");
                    String lvl = extractValue(response, "level");
                    String fcoin = extractValue(response, "fashionCoin");
                    String skinId = extractValue(response, "selectedSkinId");


                    System.out.println("LOGIN SUCCESS!");

                    Player p = new Player(pid, uname, Integer.valueOf(lvl));
                    if (!fcoin.isEmpty()) {
                        p.setFashionCoin(Float.parseFloat(fcoin));
                    }
                    if (!skinId.isEmpty()) {
                        p.setSelectedSkinId(Integer.parseInt(skinId));
                    }
                    gsm.setPlayer(p);
                    Gdx.app.postRunnable(() -> gsm.set(new MenuState(gsm, buttonManager)));
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(startGameBGTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Draw shapes (backgrounds, input boxes, borders)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (screenMode.equals("register_input") || screenMode.equals("login_username") || screenMode.equals("login_playerid")) {
            renderInputBox();
        } else if (screenMode.equals("register_success")) {
            renderSuccessBox();
        } else if (screenMode.equals("loading")) {
            renderLoadingBox();
        }

        shapeRenderer.end();

        batch.begin();
        if (screenMode.equals("menu")) {
            renderMenuText(batch);
            registerButton.render(batch, font);
            loginButton.render(batch, font);
        } else if (screenMode.equals("register_input")) {
            renderRegisterInputText(batch);
        } else if (screenMode.equals("register_success")) {
            renderRegisterSuccessText(batch);
            okButton.render(batch, font);
        } else if (screenMode.equals("login_username")) {
            renderLoginUsernameText(batch);
        } else if (screenMode.equals("login_playerid")) {
            renderLoginPlayerIdText(batch);
        } else if (screenMode.equals("loading")) {
            renderLoadingText(batch);
        }
        batch.end();
    }

    private void renderMenuText(SpriteBatch batch) {
        titleFont.setColor(PINK);
        titleFont.draw(batch, "WELCOME!",
            Gdx.graphics.getWidth()/2 - 120,
            Gdx.graphics.getHeight() - 100);
    }

    private void renderInputBox() {
        // White background
        shapeRenderer.setColor(INPUT_BG);
        shapeRenderer.rect(inputBox.x, inputBox.y, inputBox.width, inputBox.height);

        // Pink border
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

    private void renderSuccessBox() {
        float boxWidth = 500;
        float boxHeight = 380;
        float boxX = Gdx.graphics.getWidth()/2f - boxWidth/2f;
        float boxY = Gdx.graphics.getHeight()/2f - boxHeight/2f;

        // Background box
        shapeRenderer.setColor(INPUT_BG);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);

        // Border
        shapeRenderer.setColor(PINK);
        shapeRenderer.rectLine(boxX, boxY, boxX + boxWidth, boxY, 6);
        shapeRenderer.rectLine(boxX, boxY + boxHeight, boxX + boxWidth, boxY + boxHeight, 4);
        shapeRenderer.rectLine(boxX, boxY, boxX, boxY + boxHeight, 6);
        shapeRenderer.rectLine(boxX + boxWidth, boxY, boxX + boxWidth, boxY + boxHeight, 4);
    }

    private void renderRegisterSuccessText(SpriteBatch batch) {
        float boxWidth = 500;
        float boxHeight = 380;
        float boxX = Gdx.graphics.getWidth()/2f - boxWidth/2f;
        float boxY = Gdx.graphics.getHeight()/2f - boxHeight/2f;

        float paddingTop = 40;
        float yStart = boxY + boxHeight - paddingTop;

        // SUCCESS title
        titleFont.setColor(PINK);
        titleFont.draw(batch, "SUCCESS!", boxX + 40, yStart);

        // Subtitle
        font.setColor(DARK_PINK);
        font.getData().setScale(1.3f);
        font.draw(batch, "Your account has been created!", boxX + 40, yStart - 50);

        // Username line
        font.getData().setScale(1.5f);
        font.setColor(Color.BLACK);
        font.draw(batch, "Username: " + tempUsername, boxX + 40, yStart - 110);

        // Player ID
        font.setColor(PINK);
        font.getData().setScale(2f);
        font.draw(batch, "ID: " + createdPlayerId, boxX + 40, yStart - 170);
        font.getData().setScale(1.5f);

        // Warning text
        font.setColor(Color.RED);
        font.getData().setScale(1.1f);
        font.draw(batch, "SAVE THIS ID! You need it to login.", boxX + 40, yStart - 220);
        font.getData().setScale(1.5f);
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

    private void renderLoadingBox() {
        float w = 600;
        float h = 200;
        float x = Gdx.graphics.getWidth()/2f - w/2f;
        float y = Gdx.graphics.getHeight()/2f - h/2f;

        shapeRenderer.setColor(INPUT_BG);
        shapeRenderer.rect(x, y, w, h);

        shapeRenderer.setColor(PINK);
        shapeRenderer.rectLine(x, y, x+w, y, 4);
        shapeRenderer.rectLine(x, y+h, x+w, y+h, 4);
        shapeRenderer.rectLine(x, y, x, y+h, 4);
        shapeRenderer.rectLine(x+w, y, x+w, y+h, 4);
    }

    private void renderLoadingText(SpriteBatch batch) {
        titleFont.setColor(PINK);
        titleFont.draw(batch, "Loading...",
            Gdx.graphics.getWidth()/2 - 120,
            Gdx.graphics.getHeight()/2 + 20);

        font.setColor(Color.GRAY);
        font.draw(batch, "Please wait",
            Gdx.graphics.getWidth()/2 - 60,
            Gdx.graphics.getHeight()/2 - 20);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        titleFont.dispose();

        if (buttonTexture != null) {
            buttonTexture.dispose();
        }
        if (buttonHoverTexture != null) {
            buttonHoverTexture.dispose();
        }

        if (startGameBGTexture != null) {
            startGameBGTexture.dispose();
        }

        // Release buttons back to pool
        if (registerButton != null) {
            buttonManager.releaseButton(registerButton);
        }
        if (loginButton != null) {
            buttonManager.releaseButton(loginButton);
        }
        if (okButton != null) {
            buttonManager.releaseButton(okButton);
        }
    }

    private String extractValue(String json, String key) {
        try {
            String search = "\"" + key + "\":";
            int start = json.indexOf(search);
            if (start == -1) return "";
            start += search.length();

            // Skip whitespace
            while (start < json.length() && json.charAt(start) == ' ') {
                start++;
            }

            // String value
            if (json.charAt(start) == '\"') {
                start++;
                int end = json.indexOf("\"", start);
                return json.substring(start, end);
            }

            // Number value
            int end = start;
            while (end < json.length() &&
                (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) {
                end++;
            }
            return json.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }
}
