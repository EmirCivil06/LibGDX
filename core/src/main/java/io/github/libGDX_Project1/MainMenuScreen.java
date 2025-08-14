package io.github.libGDX_Project1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class MainMenuScreen extends InputAdapter implements Screen {

    private final Main MainGame;
    private final TextureRegion goldApple;
    private final BitmapFont font_1;
    private final BitmapFont font_2, font_3;
    private final GlyphLayout layout_1 = new GlyphLayout();
    private final GlyphLayout layout_2 = new GlyphLayout();
    private final Sound Beggining;
    protected float width = 25f;
    protected float sizeOffset = 147 / 126f;

    private Stage stage;
    protected Image blackSlide;
    private boolean transitioning = false;
    boolean gamePlayedBefore;
    public MainMenuScreen(Main game){
        this.MainGame = game;

        goldApple = MainGame.items.findRegion("texture_golden_apple");
        Beggining = Assets.MANAGER.get(Assets.STARTINGSOUND, Sound.class);
        MainGame.background.setPosition(0, 0);

        // Font ayarları
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pixelon.ttf"));
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Chirp-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter param2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter param3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param1.size = 25;
        param2.size = 15;
        param3.size = 17;
        param3.shadowOffsetX = 2;
        param3.shadowOffsetY = 2;
        font_1 = generator.generateFont(param1);
        font_2 = fontGenerator.generateFont(param3);
        font_2.getData().setScale(0.75f);
        font_3 = fontGenerator.generateFont(param3);
        font_3.getData().setScale(0.75f);
        generator.dispose();
        fontGenerator.dispose();

        Beggining.play(0.55f);

        gamePlayedBefore = MainGame.preferences.getBoolean(MainGame.BooleanKey);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        // 1x1 siyah texture oluştur
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Texture blackTexture = new Texture(pixmap);
        pixmap.dispose();

        blackSlide = new Image(new TextureRegionDrawable(new TextureRegion(blackTexture)));
        blackSlide.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        blackSlide.setPosition(Gdx.graphics.getWidth(), 0);// sağdan başlasın
        Image BlackSlide = new Image(blackTexture);
        BlackSlide.setPosition(0, 0);
        BlackSlide.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        BlackSlide.addAction(Actions.fadeOut(0.5f));
        stage.addActor(BlackSlide);
        stage.addActor(blackSlide);

        Gdx.input.setInputProcessor(stage); // inputlar sahneye yönlendirilir
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        MainGame.viewport.apply();
        MainGame.batch.setProjectionMatrix(MainGame.viewport.getCamera().combined);

        MainGame.batch.begin();
        MainGame.background.setSize(MainGame.viewport.getWorldWidth(), MainGame.viewport.getWorldHeight());
        MainGame.background.setAlpha(0.5f);
        MainGame.background.draw(MainGame.batch);
        if (gamePlayedBefore) {
            font_3.draw(MainGame.batch, "Kontroller", 5, 15);
        }

        layout_1.setText(font_1, "PAC-SNAKE");
        layout_2.setText(MainGame.pixeloid, "Başlamak için SPACE'e basın!");
        float x_1 = (MainGame.viewport.getWorldWidth() - layout_1.width) / 2;
        float x_2 = (MainGame.viewport.getWorldWidth() - layout_2.width) / 2;

        font_1.draw(MainGame.batch, "PAC-SNAKE", x_1, 220);
        MainGame.batch.draw(goldApple, x_1 - 30, 200, width, width * sizeOffset);
        MainGame.batch.draw(goldApple, x_1 + layout_1.width + 5, 200, width, width * sizeOffset);
        MainGame.pixeloid.draw(MainGame.batch, "Başlamak için SPACE'e basın!", x_2, 110);
        font_2.draw(MainGame.batch, "Credits", 455, 15);
        MainGame.batch.end();

        // Giriş yapma kontrolü ve sahne geçişi

        if (!transitioning && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Beggining.stop();
            transitioning = true;
            blackSlide.addAction(Actions.sequence(
                Actions.moveTo(0, 0, 0.4f), // sağdan içeri 0.4 sn'de
                Actions.delay(0.2f),
                Actions.run(this::changeScreen)
            ));
        }

        if (Gdx.input.getX() > Gdx.graphics.getWidth() / 1.103f && Gdx.input.getY() > Gdx.graphics.getHeight() / 1.1 ) {
            if (Gdx.input.isTouched()) {
                Beggining.stop();
                MainGame.setScreen(new Credits(MainGame));
            }
                font_2.setColor(Color.YELLOW);
        } else font_2.setColor(Color.WHITE);
        if ((Gdx.input.getX() < Gdx.graphics.getWidth() / 10f && Gdx.input.getY() > Gdx.graphics.getHeight() / 1.1f) && gamePlayedBefore) {
            if (Gdx.input.isTouched()) {
                blackSlide.addAction(Actions.sequence(
                    Actions.moveTo(0, 0, 0.4f),
                    Actions.delay(0.2f),
                    Actions.run(() ->MainGame.setScreen(new InfoScreen(MainGame)))
                ));
            }
            font_3.setColor(Color.YELLOW);
        } else font_3.setColor(Color.WHITE);

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        MainGame.viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    public void changeScreen(){
        if (gamePlayedBefore) {
            MainGame.setScreen(new GameScreen(MainGame));
        } else MainGame.setScreen(new InfoScreen(MainGame));
    }

    @Override
    public void dispose() {
        font_1.dispose();
        font_2.dispose();
        Beggining.dispose();
        stage.dispose();
        font_3.dispose();
    }
}
