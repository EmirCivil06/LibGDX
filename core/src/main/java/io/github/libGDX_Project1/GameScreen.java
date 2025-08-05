package io.github.libGDX_Project1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen extends InputAdapter implements Screen {

    private final Main MainGame;
    private final Snake snake;
    private final Apple Apple;
    Array<Bullet> bullets = new Array<>();
    private final JellyBeans jellyBeans;
    protected TextureRegion heart;
    protected Sprite shieldIcon;

    private float Delta_E;
    private float Delta_M;
    private float Delta_P;
    private float Timer_Inv;
    private float TIMER;
    private float Timer_Healing;
    private boolean bulletFired = false, wasEating = false, highScored = false;

    private final Components components;
    private Stage stage;
    protected Image blackSlide;
    private final Ghost yellow, red, blue, pink;
    private final Container<Label> container;

    public GameScreen(Main mainGame){
        this.MainGame = mainGame;
        Apple = new Apple();
        snake = new Snake();
        jellyBeans = new JellyBeans();
        heart = Assets.MANAGER.get(Assets.ITEMS, TextureAtlas.class).findRegion("texture_heart");
        shieldIcon = new Sprite(Assets.MANAGER.get(Assets.ITEMS, TextureAtlas.class).findRegion("texture_shield"));

        components = new Components();
        yellow = new Ghost(GhostType.YELLOW);
        red = new Ghost(GhostType.RED);
        blue = new Ghost(GhostType.BLUE);
        pink = new Ghost(GhostType.PINK);

        MainGame.preferences.putBoolean(MainGame.BooleanKey, true);
        MainGame.preferences.flush();

        container = new Container<>(components.newHighScore);
        container.size(components.newHighScore.getPrefWidth(), components.newHighScore.getPrefHeight());
        container.setPosition(components.newHighScore.getX(), components.newHighScore.getY(), Align.center);
        container.setOrigin(container.getWidth() / 2, container.getHeight() / 2);
        container.setTransform(true);
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
        blackSlide.setSize(Gdx.graphics.getWidth() * 1.5f, Gdx.graphics.getHeight());
        blackSlide.setPosition(0, 0);
        blackSlide.addAction(Actions.sequence(
            Actions.moveTo(-Gdx.graphics.getWidth() * 1.5f, 0, 0.6f),
            Actions.delay(0.4f),
            Actions.fadeOut(0.5f)
        ));// sağdan başlasın
        stage.addActor(container);
        stage.addActor(components.highScore);
        stage.addActor(components.scoreDisplay);
        stage.addActor(blackSlide);
    }

    @Override
    public void render(float delta) {
        if (!snake.isDying) snake.SNAKE_EVENT_SETTER_INPUT(187.5f, delta, components);
        draw(delta);
        logic(delta);
        components.highScore.setText("Yüksek Skor: " + MainGame.preferences.getInteger(MainGame.IntegerKey));
        stage.act(delta);
        stage.draw();

    }

    private void draw(float delta){

        float pitch = MathUtils.random(0.85f, 1.15f);
        boolean goingRight = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean goingLeft = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean goingUp = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean goingDown = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean isEating = Gdx.input.isKeyPressed(Input.Keys.ENTER);
        String SCORE = String.format("%09d", Components.score);
        String TIME = String.format("%.1f", Components.timer);
        components.scoreDisplay.setText("Skor: " + SCORE);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        MainGame.viewport.apply();
        MainGame.batch.setProjectionMatrix(MainGame.viewport.getCamera().combined);
        components.healing.start();
        components.healing.update(delta);

        MainGame.batch.begin();
        MainGame.batch.draw(MainGame.backgroundTexture,0,0, MainGame.viewport.getWorldWidth(), MainGame.viewport.getWorldHeight());
        snake.shadow.sprite.draw(MainGame.batch);
        Apple.sprite.draw(MainGame.batch);
        jellyBeans.sprite.draw(MainGame.batch);

        // -------------------------------------------------------------------------------------
        // -------------------BULLET AKTİVASYONU, ÇİZİMİ VE MANTIĞI-----------------------------
        for (Bullet bullet : bullets) {
            bullet.ActivateLogic(MainGame.viewport.getWorldWidth(), MainGame.viewport.getWorldHeight());
            if (bullet.hitbox.overlaps(snake.hitbox)) {
                // Çarpışma işlemleri
                damageCharacter(pitch);

                if (snake.invincible){
                    bullet.sprite.setAlpha(0);
                    if (bullet.Timer_Self > bullet.INTERVAL) {
                        components.destruction.play(0.4f);
                        bullets.removeIndex(bullets.indexOf(bullet, true));
                        Components.score += 200;
                    }
                }
            }
        }
        components.BULLET_ARRAY_EVENT_SETTER_DRAW(MainGame.batch, bullets, snake);
        // ----------------------------------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------------------------------

        if ((yellow.Overlaps(snake) || red.Overlaps(snake) || blue.Overlaps(snake) || pink.Overlaps(snake))) {
            damageCharacter(pitch);
        }

        // ----------------------------------------------------------------------------------------------------------------------
        // -------------------------EATING VE DYING KONTROLÜ---------------------------------------------------------------------
        if (goingRight || goingLeft || goingUp || goingDown) isEating = false;
        if (isEating && !wasEating) {
            Delta_E = 0;
            snake.ATE_APPLE = false;
        } wasEating = isEating;

        if (!snake.isDying){
            if (isEating) {
                Delta_E += delta;
                snake.SNAKE_EVENT_SETTER_EATING(Delta_E, MainGame.batch);
            } else if (goingRight || goingLeft || goingUp || goingDown) {
                // Hareket varsa animasyon zamanı ilerletilsin
                Delta_E = 0;
                Delta_M += delta;

                TextureRegion moveRegion = snake.movingAnim.getKeyFrame(Delta_M, true);
                snake.Moving.setRegion(moveRegion);

                if (snake.lastLookDirection == Snake.Direction.RIGHT) {
                    if (snake.Moving.isFlipX()) snake.Moving.flip(true, false);
                } else {
                    if (!snake.Moving.isFlipX()) snake.Moving.flip(true, false);
                }
                snake.Moving.setPosition(snake.Idle.getX(), snake.Idle.getY());
                snake.Moving.draw(MainGame.batch);

            } else {
                // Sabit yılan sprite'ı çiz
                snake.Idle.setPosition(snake.Moving.getX(), snake.Moving.getY());
                snake.Idle.draw(MainGame.batch);
                Delta_E = 0;
            }
        } else snake.dead_0.draw(MainGame.batch);
        // ---------------------------------------------------------------------------------------------------------------
        // ---------------------------------------------------------------------------------------------------------------

        components.setInfoTable(MainGame.batch, snake, delta, heart);

    // ------------------------------PARÇACIK EFEKTİ ÇİZİMİ-----------------------------------
        if (snake.ATE_BEANS || snake.ATE_5) {
            Delta_P += delta;
            if (Delta_P < 0.5f) {
                components.healing.draw(MainGame.batch);
            } else snake.ATE_5 = false;
        }
    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------

    // --------------------------------------HAYALET DÜŞMANLARIN ÇİZİMİ VE MANTIĞI-----------------------------------------
    //  Bütün bu hayalet düşmanlar için yeni bir Array tanımlamak istemedim. Zaten 8 adet maksimum olacak şekilde ayarladım
        if (!snake.isDying) {
            if (snake.applesEaten >= 40) {
                yellow.draw(delta, MainGame.batch);
                yellow.logic(delta, MainGame.viewport.getWorldWidth(), MainGame.viewport.getWorldHeight());
                yellow.update(delta);
            }
            if (snake.applesEaten >= 70) {
                red.draw(delta, MainGame.batch);
                red.logic(delta, MainGame.viewport.getWorldWidth(), MainGame.viewport.getWorldHeight());
                red.update(delta);
            }
            if (snake.applesEaten >= 130) {
                blue.draw(delta, MainGame.batch);
                blue.logic(delta, MainGame.viewport.getWorldWidth(), MainGame.viewport.getWorldHeight());
                blue.update(delta);
            }
            if (snake.applesEaten >= 240) {
                pink.draw(delta, MainGame.batch);
                pink.logic(delta, MainGame.viewport.getWorldWidth(), MainGame.viewport.getWorldHeight());
                pink.update(delta);
            }
        }

        snake.SNAKE_EVENT_SETTER_DEATH(MainGame.batch);
        if (snake.invincible) {
            MainGame.batch.draw(shieldIcon, snake.Idle.getX() - 0.75f, snake.Idle.getY() + snake.Idle.getHeight() + 1.55f, 12f, 12f);
            Components.timer -= Gdx.graphics.getDeltaTime();
            components.font.draw(MainGame.batch, TIME, snake.Idle.getX() + 12.5f, snake.Idle.getY() + snake.Idle.getHeight() * 1.5f);
        } else Components.timer = 7.0f;
        MainGame.batch.end();
    }

    private void logic(float delta){
        float worldWidth = MainGame.viewport.getWorldWidth();
        float worldHeight = MainGame.viewport.getWorldHeight();
        snake.SNAKE_EVENT_SETTER_CLAMP(worldWidth, worldHeight);

        if (Components.score > MainGame.preferences.getInteger(MainGame.IntegerKey)) {
            if (!highScored) {
                container.addAction(Actions.sequence(
                    Actions.moveTo(container.getX(), 650, 0.6f),
                    Actions.delay(0.3f),
                    Actions.scaleTo(1.25f, 1.25f, 0.15f),
                    Actions.scaleTo(1f, 1f, 0.35f),
                    Actions.delay(1.2f),
                    Actions.moveTo(container.getX(), 1000, 0.6f)
                    ));
                highScored = true;
            }
            components.scoreDisplay.setColor(Color.YELLOW);
            components.highScore.addAction(Actions.moveTo(-350, components.highScore.getY(), 1f));
            MainGame.preferences.putInteger(MainGame.IntegerKey, Components.score);
            MainGame.preferences.flush();
        }

        if (!snake.invincible && !snake.ATE_5) {
            Delta_P = 0;
            components.weakness.stop();
            components.healing.reset();
        }

        snake.HissDelay += delta;
        float randomPitch = MathUtils.random(0.75f, 1.5f);
        // --------------------------------------------------------------------------------------------------
        // ------------------------------------YEME OLAYLARININ MANTIĞI--------------------------------------
        boolean isEating = Gdx.input.isKeyPressed(Input.Keys.ENTER);
        if (!isEating) {
            snake.ATE_APPLE = false;
        }
        if (Apple.EatingConditionsCreated(snake, Delta_E)) {
            jellyBeans.try_to_spawn();
            Apple.APPLE_EVENT_SETTER_DESPAWN(jellyBeans);
            Components.score += 200;
            snake.applesEaten++;
            snake.ATE_APPLE = true;
            if (snake.applesEaten % 10 == 0) {
                components.successfullyAte.play(0.75f);
            }

            if (snake.applesEaten % 5 == 0 && snake.health < 8) {
                snake.health++;
                components.lifeUp.play(0.2f);
                components.TableTimer = 0;
                snake.ATE_5 = true;
            }
        }

        if (jellyBeans.EatingConditionsCreated(snake, Delta_E)) {
                Components.score += 400;
                Apple.spawnNew();
                jellyBeans.healing.play(0.55f);
                components.weakness.play(0.08f);
                jellyBeans.sprite.setPosition(jellyBeans.Starting_X, jellyBeans.Starting_y);
                snake.ATE_BEANS = true;
        }
        // -------------------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------------------

        // ----------------------------------------------------------------------------------------------------------
        // ---------------------------------JELİBON'UN YILANA SONSUZLUK SAĞLAMASI------------------------------------
        if (snake.ATE_BEANS) {
            final float INTERVAL = 7f;
            TIMER += delta;

            if (!snake.healed_by_BEAN && snake.health < 8) {
                snake.health++;
                snake.healed_by_BEAN = true;
            }
            if (TIMER < INTERVAL) {
                snake.invincible = true;
            } else {
                snake.invincible = false;
                snake.ATE_BEANS = false;
                snake.healed_by_BEAN = false;
                TIMER = 0;   // sadece burada sıfırla
            }
        }

        else {
            TIMER = 0;
            snake.healed_by_BEAN = false;
        }
        components.CHANGE_BULLET_COLOUR(bullets, snake);
        // ----------------------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------------------

        // ----------------------------------------------------------------------------------------------------
        // ----------------------------------MERMİNİN DİĞER LOGIC İŞLEMLERİ------------------------------------
        if (bullets.size == 0) components.weakness.stop();
        if (snake.damaged){
            Timer_Inv += delta;
            snake.damaged = snake.SNAKE_EVENT_SETTER_DAMAGED(Timer_Inv);
        }

        boolean shouldFire = (snake.applesEaten == 1) || (snake.applesEaten > 0 && snake.applesEaten % 10 == 0);
        if (shouldFire && !bulletFired) {
            if (snake.invincible) components.weakness.play(0.08f);
            bullets.add(new Bullet());
            bulletFired = true;
        }
        if (!shouldFire) bulletFired = false;
        if (snake.health == 0) {
            Components.MainMusic.stop();
            bullets.clear();
            if (!snake.isDying) {
                components.gameOverSound.play(0.5f);
                snake.isDying = true;      // <-- Yılan ölmeye başlasın
            }
            components.changeScenery(delta, MainGame, snake, this, blackSlide);
        } else {
            snake.SNAKE_EVENT_SETTER_SOUND(randomPitch, Apple, jellyBeans, Delta_E, isEating);
        }
        if (!isEating) {
            Delta_E = 0;
            snake.ATE_APPLE = false;
        }
        if (snake.invincible) {
            for (Bullet bullet:bullets){
                bullet.CHANGE_ALPHA(TIMER);
            }
            Timer_Healing += delta;
            if (Timer_Healing < 0.7f) snake.SNAKE_EVENT_SETTER_START_HEALING();
        } else Timer_Healing = 0;
        snake.SNAKE_EVENT_SETTER_HEALING();
        // ----------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------

        if (snake.applesEaten == 1) Components.MainMusic.play();
    }

    public void damageCharacter(float soundPitch) {
        if (!snake.damaged && !snake.invincible) {
            components.damageSound.play(0.5f, soundPitch, 0);
            snake.damaged = true;
            Timer_Inv = 0;
            snake.health--;
            if (Components.score > 0) Components.score -= 100;
        }
    }

    @Override
    public void resize(int width, int height) {
        MainGame.viewport.update(width, height, true);
    }
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        for (Bullet bullet: bullets){
            bullet.bounce.dispose();
        }
        Components.MainMusic.dispose();
        components.disposeElements();
        snake.dispose();
    }
}
