package io.github.libGDX_Project1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class Components {
    protected SpriteBatch batch;
    protected BitmapFont font_1, font_2;
    protected BitmapFont font, headerFont, infoFont;
    protected Sound startingSound, gameOverSound, weakness, destruction;
    protected static Music MainMusic;
    protected ParticleEffect healing;
    protected static int score = 0;
    protected static float timer = 7;
    protected float sceneryChangingControl = 0;
    protected float TableTimer;
    protected static Sound hiss, eating, lifeUp, successfullyAte, damageSound;
    protected Label highScore;

    public Components(){
        batch =  new SpriteBatch();
        healing = new ParticleEffect();
        healing.load(Gdx.files.internal("particles/healing_particles.p"), Gdx.files.internal("particles/"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pixelon.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter_one = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter_two = new FreeTypeFontGenerator.FreeTypeFontParameter();
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

        font = new BitmapFont();
        font.getData().setScale(0.9f);

        startingSound = Assets.MANAGER.get(Assets.STARTINGSOUND, Sound.class);
        gameOverSound = Assets.MANAGER.get(Assets.GAMEOVERSOUND, Sound.class);
        weakness = Assets.MANAGER.get(Assets.WEAKNESS, Sound.class);
        destruction = Assets.MANAGER.get(Assets.DESTRUCTION, Sound.class);
        MainMusic = Assets.MANAGER.get(Assets.MUSIC, Music.class);
        MainMusic.setLooping(true);
        MainMusic.setVolume(0.80f);
        hiss = Assets.MANAGER.get(Assets.HISS, Sound.class);
        eating = Assets.MANAGER.get(Assets.EATING, Sound.class);
        lifeUp = Assets.MANAGER.get(Assets.LIFE_UP, Sound.class);
        successfullyAte = Assets.MANAGER.get(Assets.SUCCESFULLYATE, Sound.class);
        damageSound = Assets.MANAGER.get(Assets.DAMAGESOUND, Sound.class);

        Label.LabelStyle style = new Label.LabelStyle(font, Color.YELLOW);
        highScore = new Label("Yuksek Skor: ",style);
        highScore.setSize(200, 200);
        highScore.setFontScale(2f);
        highScore.setPosition(30, 375);
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

    public void setInfoTable(SpriteBatch batch, Snake snake, float delta, TextureRegion texture, String SCORE){
        font.draw(batch, "Yenen Elmalar: " + snake.applesEaten, 10, 240);
        font.draw(batch, "Kalan Can: " + snake.health + " x", 382.5f, 240);
        font.draw(batch, "Skor : " + SCORE, 10, 220);
        batch.draw(texture, 472.5f, 227, 15, 15);
        if (snake.health == 4 || snake.health == 5 || snake.health == 6) {
            TableTimer += delta;
            if (TableTimer < 1.35f) {
                font.draw(batch, "EKSTRA CAN!", 382.5f, 225);
            }
        }
    }

    public void changeScenery(float delta, Main MainGame, Snake snake, GameScreen screen){
        sceneryChangingControl += delta;
        if (sceneryChangingControl > snake.D_INTERVAL + 1.25f) {
            MainGame.setScreen(new GameOverScreen(MainGame));
            screen.dispose();
        }
    }
}
