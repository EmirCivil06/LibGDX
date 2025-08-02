package io.github.libGDX_Project1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class GameOverScreen extends InputAdapter implements Screen {

    private final Main MainGame;
    protected BitmapFont font_1, font_2;
    protected GlyphLayout layout_1 = new GlyphLayout();
    protected GlyphLayout layout_2 = new GlyphLayout();

    public GameOverScreen(Main game){
        this.MainGame = game;
        MainGame.background.setPosition(0, 0);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pixelon.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter_one = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter_two = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter_three = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter_two.size = 15;
        parameter_one.size = 25;
        parameter_three.size = 12;
        font_1 = new BitmapFont();
        font_1.getData().setScale(1.5f);
        font_1 = generator.generateFont(parameter_one);
        font_2 = new BitmapFont();
        font_2.getData().setScale(1f);
        font_2 = generator.generateFont(parameter_two);
        generator.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Components.MainMusic.stop();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        MainGame.viewport.apply();
        MainGame.batch.setProjectionMatrix(MainGame.viewport.getCamera().combined);
        MainGame.batch.begin();
        MainGame.background.setSize(MainGame.viewport.getWorldWidth(), MainGame.viewport.getWorldHeight());
        MainGame.background.setAlpha(0.5f);
        MainGame.background.draw(MainGame.batch);
        layout_1.setText(font_1, "OYUN BITTI");
        layout_2.setText(font_2, "Gelecek sefere bol sans!");
        float x_1 = (MainGame.viewport.getWorldWidth() - layout_1.width) / 2;
        float x_2 = (MainGame.viewport.getWorldWidth() - layout_2.width) / 2;

        font_1.draw(MainGame.batch, "OYUN BITTI", x_1, 220);
        font_2.draw(MainGame.batch, "Gelecek sefere bol sans!", x_2, 110);
        MainGame.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        MainGame.viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font_1.dispose();
        font_2.dispose();
        MainGame.backgroundTexture.dispose();
        Assets.DISPOSE_ALL();
    }

}
