package io.github.libGDX_Project1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends Game {
    protected SpriteBatch batch;
    protected FitViewport viewport;
    protected Sprite background;
    protected TextureRegion backgroundTexture;
    protected TextureAtlas items;
    protected MainMenuScreen mainMenuScreen;
    protected BitmapFont pixeloid;

    public Preferences preferences;
    public String BooleanKey = "gamePlayedAtLeastOnce", IntegerKey = "highScore";
    protected Ghost yellow_1, red_1, blue_1, pink_1;
    public float pixelsPerUnit = 2.8f;

    @Override
    public void create(){
        Assets.LOAD_ALL();
        Assets.MANAGER.finishLoading();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pixeloid.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param1.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "iİıŞşÜüĞğÇç";
        param1.size = 13;
        pixeloid = generator.generateFont(param1);
        pixeloid.getData().setScale(1.05f);

        items = Assets.MANAGER.get(Assets.ITEMS_1, TextureAtlas.class);
        backgroundTexture = Assets.MANAGER.get(Assets.ITEMS_2, TextureAtlas.class).findRegion("texture_grass_background");
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
        yellow_1 = new Ghost(GhostType.YELLOW);
        red_1 = new Ghost(GhostType.RED);
        blue_1 = new Ghost(GhostType.BLUE);
        pink_1 = new Ghost(GhostType.PINK);
    }
    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
    }

    public void setActiveGhostOnProgress(Ghost ghost, int setGameProgressLimit, int achievedGameProgress, float delta){
        if (achievedGameProgress >= setGameProgressLimit) {
            ghost.draw(delta, batch);
            ghost.logic(delta, viewport.getWorldWidth(), viewport.getWorldHeight());
            ghost.update(delta);
        }
    }

    public boolean OtherGhostOverlaps(Snake snake) {
        return yellow_1.Overlaps(snake) || red_1.Overlaps(snake) || blue_1.Overlaps(snake) || pink_1.Overlaps(snake);
    }

    @Override
    public void render(){
        super.render();
    }

    @Override
    public void dispose(){
        if (batch != null) {
            batch.dispose();
        }
        pixeloid.dispose();
        Assets.DISPOSE_ALL();
    }
}
