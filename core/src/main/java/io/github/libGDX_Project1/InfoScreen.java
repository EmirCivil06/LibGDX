package io.github.libGDX_Project1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class InfoScreen extends InputAdapter implements Screen {

    private final Main MainGame;
    protected Sprite infoTable;
    protected Stage stage;
    protected Image blackSlide_1, blackSlide_2;
    protected Skin skin;
    protected BitmapFont font1;
    private final GlyphLayout layout_1 = new GlyphLayout();
    private final SpriteBatch batch;
    protected Table root;

    public InfoScreen(Main game){
        this.MainGame = game;
        // Font ayarları
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pixelon.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter param2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param2.size = 15;
        param2.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ÇŞİĞÜÖçşığüö";
        param1.size = 25;
        param1.characters = FreeTypeFontGenerator.DEFAULT_CHARS +
            "ÇŞİĞÜÖçşığüö";
        font1 = generator.generateFont(param1);
        generator.dispose();

        infoTable = new Sprite(Assets.MANAGER.get(Assets.INTERFACE, Texture.class));
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        stage = new Stage(MainGame.viewport);
        skin = new Skin(Gdx.files.internal("craftacular/skin/craftacular-ui.json"));

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Texture blackTexture = new Texture(pixmap);
        pixmap.dispose();

        root = new Table();
        root.setFillParent(true);

        TextButton startBtn = new TextButton("OK", skin);
        startBtn.getLabel().setFontScale(0.35f);
        root.add(startBtn).width(50).height(25f).grow().bottom().pad(5);

        blackSlide_1 = new Image(new TextureRegionDrawable(new TextureRegion(blackTexture)));
        blackSlide_1.setSize(Gdx.graphics.getWidth() * 1.5f, Gdx.graphics.getHeight());
        blackSlide_1.setPosition(0, 0);
        blackSlide_1.addAction(Actions.moveTo(-Gdx.graphics.getWidth() * 1.5f, 0, 1f));// sağdan başlasın

        blackSlide_2 = new Image(new TextureRegionDrawable(new TextureRegion(blackTexture)));
        blackSlide_2.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        blackSlide_2.setPosition(Gdx.graphics.getWidth(), 0);

        startBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                blackSlide_2.addAction(Actions.sequence(
                    Actions.moveTo(0, 0, 0.8f), // sağdan içeri 0.4 sn'de
                    Actions.delay(0.2f),
                    Actions.run(() -> MainGame.setScreen(new GameScreen(MainGame)))
                ));
            }
        });
        stage.addActor(root);
        stage.addActor(blackSlide_1);
        stage.addActor(blackSlide_2);

        Gdx.input.setInputProcessor(stage); // inputlar sahneye yönlendirilir
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.getViewport().apply();
        // Arka plan ve infoTable çizimi için batch kullanımı:
        float vw = MainGame.viewport.getWorldWidth();
        float vh = MainGame.viewport.getWorldHeight();

        batch.setProjectionMatrix(stage.getViewport().getCamera().combined); // artık stage viewport kullanılıyor
        batch.begin();

        MainGame.background.setSize(vw, vh);
        MainGame.background.setPosition(0, 0);
        MainGame.background.draw(batch);

        infoTable.setSize(vw * 0.5f, vh * 0.45f); // %50 genişlik, %40 yükseklik gibi
        infoTable.setPosition((vw - infoTable.getWidth()) / 2f, (vh - infoTable.getHeight()) / 2f);
        infoTable.draw(batch);

        layout_1.setText(font1, "KONTROLLER");
        float x_1 = (vw - layout_1.width) / 2;
        font1.draw(batch, "KONTROLLER", x_1, vh * 0.9f); // örneğin %90 yükseklikte

        batch.end();
        stage.act(delta);
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);

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

    }

}
