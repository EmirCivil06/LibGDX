package io.github.libGDX_Project1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends Game {
    protected SpriteBatch batch;
    protected FitViewport viewport;
    protected Sprite background;
    protected Texture backgroundTexture;
    protected TextureAtlas items;
    protected MainMenuScreen mainMenuScreen;

    public Preferences preferences;
    public String BooleanKey = "gamePlayedAtLeastOnce", IntegerKey = "highScore";

    @Override
    public void create(){
        Assets.LOAD_ALL();
        Assets.MANAGER.finishLoading();
        items = Assets.MANAGER.get(Assets.ITEMS, TextureAtlas.class);
        backgroundTexture = Assets.MANAGER.get(Assets.BACKGROUND, Texture.class);
        background = new Sprite(backgroundTexture);
        batch = new SpriteBatch();
        viewport = new FitViewport(500, 250);
        preferences = Gdx.app.getPreferences("PacSnakePrefs");
        if (!preferences.getBoolean(BooleanKey)) {
            preferences.putBoolean(BooleanKey, false);
            preferences.flush();
        }
        mainMenuScreen = new MainMenuScreen(this);
        setScreen(mainMenuScreen);
    }
    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
    }


    @Override
    public void render(){
        super.render();
        Gdx.graphics.setForegroundFPS(90);
    }

    @Override
    public void dispose(){
        if (batch != null) {
            batch.dispose();
        }
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
