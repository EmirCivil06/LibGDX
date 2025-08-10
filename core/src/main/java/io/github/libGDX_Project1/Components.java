package io.github.libGDX_Project1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class Components {
    protected SpriteBatch batch;
    protected BitmapFont font_1, font_2;
    protected BitmapFont font, headerFont, infoFont;
    protected Sound startingSound, gameOverSound, weakness, destruction;
    public static Music MainMusic;
    protected ParticleEffect healing;
    protected static int score = 0;
    protected static float timer = 7;
    protected float sceneryChangingControl = 0;
    protected float TableTimer;
    protected Sound damageSound, successfullyAte, lifeUp;
    protected Label highScore, scoreDisplay, newHighScore;

    public Components(){
        batch =  new SpriteBatch();
        healing = new ParticleEffect();
        healing.load(Gdx.files.internal("particles/healing_particles.p"), Gdx.files.internal("particles/"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pixelon.ttf"));
        FreeTypeFontGenerator generator1 = new FreeTypeFontGenerator(Gdx.files.internal("Chirp-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter_one = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter_two = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter_three = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter_two.size = 15;
        parameter_one.size = 25;
        font_1 = new BitmapFont();
        font_1.getData().setScale(1.5f);
        font_1 = generator.generateFont(parameter_one);
        font_2 = new BitmapFont();
        font_2.getData().setScale(1f);
        font_2 = generator.generateFont(parameter_two);
        headerFont = new BitmapFont();
        headerFont.getData().setScale(1.5f);
        headerFont = generator.generateFont(parameter_one);
        infoFont = new BitmapFont();
        infoFont.getData().setScale(1f);
        infoFont = generator.generateFont(parameter_two);
        generator.dispose();

        parameter_three.size = 15;
        parameter_three.shadowOffsetY = 1;
        parameter_three.shadowOffsetX = 1;
        parameter_three.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "Üü";

        font = generator1.generateFont(parameter_three);
        font.getData().setScale(0.9f);
        generator1.dispose();

        startingSound = Assets.MANAGER.get(Assets.STARTINGSOUND, Sound.class);
        gameOverSound = Assets.MANAGER.get(Assets.GAMEOVERSOUND, Sound.class);
        weakness = Assets.MANAGER.get(Assets.WEAKNESS, Sound.class);
        destruction = Assets.MANAGER.get(Assets.DESTRUCTION, Sound.class);
        MainMusic = Assets.MANAGER.get(Assets.MUSIC, Music.class);
        MainMusic.setLooping(true);
        MainMusic.setVolume(0.80f);
        lifeUp = Assets.MANAGER.get(Assets.LIFE_UP, Sound.class);
        successfullyAte = Assets.MANAGER.get(Assets.SUCCESFULLYATE, Sound.class);
        damageSound = Assets.MANAGER.get(Assets.DAMAGESOUND, Sound.class);

        Label.LabelStyle style = new Label.LabelStyle(font, Color.YELLOW);
        Label.LabelStyle style1 = new Label.LabelStyle(font, Color.WHITE);

        highScore = new Label("Yüksek Skor: ",style);
        highScore.setSize(300, 300);
        highScore.setFontScale(2f);
        highScore.setPosition(30, 415);

        scoreDisplay = new Label("Skor: ",style1);
        scoreDisplay.setSize(300, 300);
        scoreDisplay.setFontScale(2f);
        scoreDisplay.setPosition(30, 455);

        newHighScore = new Label("Yeni Yüksek Skor!", style);
        newHighScore.setSize(300, 300);
        newHighScore.setPosition((float) (Gdx.graphics.getWidth()) / 2, 900);
        newHighScore.setFontScale(2f);

    }


    public void CHANGE_BULLET_COLOUR(Array<Bullet> bullets, Snake snake){
        // --- HER FRAME RENK KONTROLÜ ---
        Color bulletColor = snake.invincible ? Color.BLUE : Color.WHITE;
        for (int i = 0; i < bullets.size; i++) {
            bullets.get(i).sprite.setColor(bulletColor);
        }
    }

    public void BULLET_ARRAY_EVENT_SETTER_DRAW(SpriteBatch batch, Array<Bullet> bullets, Snake snake){
        for (Bullet bullet: bullets){
            if (bullet.sprite.getColor().a > 0) {
                bullet.shadow.changePos(bullet.sprite.getX(), bullet.sprite.getY(), 1, 5);
                bullet.shadow.sprite.draw(batch);
                bullet.SET_PARTICLES_LIFE(batch, snake);
                bullet.stateTime += Gdx.graphics.getDeltaTime();
                TextureRegion textureRegion = bullet.animation.getKeyFrame(bullet.stateTime, true);
                bullet.sprite.setRegion(textureRegion);
                bullet.sprite.draw(batch);
            }
            if (bullet.hitbox.overlaps(snake.hitbox)){
                if (snake.invincible){
                    bullet.particlesActive = true;
                    bullet.latestDir = Bullet.DOUBLE_DIRECTION.NONE;
                    bullet.SET_PARTICLES_DEATH(batch);
                }
            }
        }
    }

    public void setInfoTable(SpriteBatch batch, Snake snake, float delta, TextureRegion texture){
        font.draw(batch, "Yenen Elmalar: " + snake.applesEaten, 10, 240);
        font.draw(batch, "Kalan Can: " + snake.health + " x", 382.5f, 240);
        batch.draw(texture, 472.5f, 227, 15, 15);
        if (snake.health == 6 || snake.health == 7 || snake.health == 8) {
            TableTimer += delta;
            if (TableTimer < 1.35f) {
                font.draw(batch, "EKSTRA CAN!", 382.5f, 225);
            }
        }
    }

    public void changeScenery(float delta, Main MainGame, Snake snake, GameScreen screen, Image blackSlide){
        sceneryChangingControl += delta;
        if (sceneryChangingControl > snake.D_INTERVAL + 1.25f) {
            blackSlide.addAction(Actions.sequence(
                Actions.moveTo(0, 0),
                Actions.fadeIn(0.3f),
                Actions.run(() -> Components.MainMusic.stop()),
                Actions.run(() -> MainGame.setScreen(new GameOverScreen(MainGame))),
                Actions.run(screen::dispose)
            ));
        }
    }

    public void disposeElements() {
        font_2.dispose();
        font_1.dispose();
        headerFont.dispose();
        infoFont.dispose();
        font.dispose();
        startingSound.dispose();
        damageSound.dispose();
        successfullyAte.dispose();
        lifeUp.dispose();
    }
}
