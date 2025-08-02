package io.github.libGDX_Project1;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Credits extends InputAdapter implements Screen {

    private Stage stage;
    private final Main MainGame;
    private final Texture snake;
    private final GlyphLayout layout_1 = new GlyphLayout();
    private final BitmapFont font_1, font_2, font_3;
    private Image Snake;
    private final Label label_0, label_1;

    public Credits(Main game){
        this.MainGame = game;
        snake = Assets.MANAGER.get(Assets.IDLETEXTURE, Texture.class);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pixelon.ttf"));
        FreeTypeFontGenerator generator1 = new FreeTypeFontGenerator(Gdx.files.internal("Chirp-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter param2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter param3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param1.size = 30;
        param2.size = 15;
        param2.shadowOffsetX = 2;
        param2.shadowOffsetY = 2;
        font_1 = generator.generateFont(param1);
        font_1.getData().setScale(0.8f);
        font_2 = generator1.generateFont(param2);
        font_2.getData().setScale(0.9f);
        param3.size = 16;
        param3.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "şŞüÜıİÖöÜüÇçğĞ";
        font_3 = generator1.generateFont(param3);
        param1.size = 16;
        generator.dispose();
        Label.LabelStyle style_0 = new Label.LabelStyle(font_3, Color.WHITE);
        Label.LabelStyle style_1 = new Label.LabelStyle(font_1, Color.RED);
        label_0 = new Label("A Game By Emir Civil \n \n Müzik \n \"SWORD\" Originally from game Deltarune \n Remixed by Melih Karal \n \n SFX ve Grafikler \n Includes assets inspired from (Games/Websites): \n Undertale \n Pac-Man \n Minecraft \n Super Mario Bros. \n Pixabay \n The Spriters Resource \n Myinstants \n Squarespace \n \n Playtesters \n Oyunumuzu test edenlere tesekkürlerimizi sunuyoruz! \n Talha Şenol \n Yiğit Doğan \n \n \n Bu oyundaki assetler eğitim amaçlı diğer oyunlardan ve web sitelerinden alınmıştır ve düzenlenmiştir. \n Orjinal assetlerin bütün hakları yukarıda belirtilen web sitelerine, yayıncılarına ve ek olarak Nintendo gibi kuruluşlara aittir. \n BU OYUN SADECE VE SADECE EĞİTİM AMAÇLI EMIR CIVIL TARAFINDAN YAPILMIŞTIR.", style_0);
        label_1 = new Label("DISCLAIMER", style_1);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        Pixmap pixmap =  new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Texture blackTexture = new Texture(pixmap);
        pixmap.dispose();

        Image blackSlide =  new Image(blackTexture);
        blackSlide.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        blackSlide.setPosition(0,0);
        blackSlide.addAction(Actions.fadeOut(0.4f));
        Snake = new Image(snake);
        Snake.setSize(Gdx.graphics.getWidth() / 22.5f, Gdx.graphics.getHeight() / 11.25f);
        Snake.setPosition(Gdx.graphics.getWidth() / 1.65f, Gdx.graphics.getHeight() - Snake.getHeight() * 1.35f);
        label_0.setAlignment(Align.center);
        label_0.setSize(150, 150);
        label_0.setPosition(MainGame.viewport.getWorldWidth() - label_0.getMaxWidth() + 100f / 2, 190);
        label_0.setFontScale(1f);
        label_1.setAlignment(Align.center);
        label_1.setSize(15, 15);
        label_1.setPosition(MainGame.viewport.getWorldWidth() - label_1.getMaxWidth() + 240f / 2, 80);
        stage.addActor(label_1);
        stage.addActor(label_0);
        stage.addActor(Snake);
        stage.addActor(blackSlide);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        MainGame.viewport.apply();
        MainGame.batch.setProjectionMatrix(MainGame.viewport.getCamera().combined);

        MainGame.batch.begin();
        MainGame.background.draw(MainGame.batch);
        layout_1.setText(font_1, "PAC-SNAKE");
        float x_1 = ((MainGame.viewport.getWorldWidth() - layout_1.width ) / 2) - Snake.getWidth() / 3f;
        font_1.draw(MainGame.batch, "PAC-SNAKE", x_1, 240);
        font_2.draw(MainGame.batch, "OK", 5, 15);
        MainGame.batch.end();

        if (Gdx.input.getX() > Gdx.graphics.getWidth() / 100f && Gdx.input.getY() > Gdx.graphics.getHeight() / 1.1  && Gdx.input.getX() < Gdx.graphics.getWidth() / 20f) {
            if (Gdx.input.isTouched()) {
                MainGame.setScreen(new MainMenuScreen(MainGame));
            }
            font_2.setColor(Color.YELLOW);
        } else font_2.setColor(Color.WHITE);

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
        font_2.dispose();
        font_3.dispose();
        snake.dispose();
        stage.dispose();
    }

}
