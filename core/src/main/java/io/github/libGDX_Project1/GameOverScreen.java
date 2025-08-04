package io.github.libGDX_Project1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen extends InputAdapter implements Screen {

    private final Main MainGame;
    protected BitmapFont font_1;
    protected GlyphLayout layout_1 = new GlyphLayout();
    protected GlyphLayout layout_2 = new GlyphLayout();
    private Stage stage;

    public GameOverScreen(Main game){
        this.MainGame = game;
        MainGame.background.setPosition(0, 0);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pixelon.ttf"));
        FreeTypeFontGenerator generator1 = new FreeTypeFontGenerator(Gdx.files.internal("Pixeloid.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter_one = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter_two = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter_two.size = 12;
        parameter_one.size = 25;
        parameter_two.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "iİıŞşÜüĞğÇç";
        font_1 = generator.generateFont(parameter_one);
        font_1.getData().setScale(1.5f);
        generator.dispose();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Texture blackSlide  = new Texture(pixmap);
        pixmap.dispose();

        Image blackSLIDE = new Image(blackSlide);
        blackSLIDE.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        blackSLIDE.setPosition(0,0);
        blackSLIDE.addAction(Actions.fadeOut(0.6f));
        stage.addActor(blackSLIDE);
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
        layout_1.setText(font_1, "GAME OVER");
        layout_2.setText(MainGame.pixeloid, "Gelecek sefere bol şans!");
        float x_1 = (MainGame.viewport.getWorldWidth() - layout_1.width) / 2;
        float x_2 = (MainGame.viewport.getWorldWidth() - layout_2.width) / 2;

        font_1.draw(MainGame.batch, "GAME OVER", x_1, 220);
        MainGame.pixeloid.draw(MainGame.batch, "Gelecek sefere bol şans!", x_2, 110);
        MainGame.pixeloid.draw(MainGame.batch, "Çıkmak için ESC'e basın.", x_2,  90);
        MainGame.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        stage.act(delta);
        stage.draw();
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
        Assets.DISPOSE_ALL();
    }

}
