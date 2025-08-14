package io.github.libGDX_Project1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Snake {
    protected Sprite Idle, Moving, rightFacedEating, leftFacedEating, dead_0, dead_1;
    protected TextureAtlas entireAtlas;
    protected Animation<TextureRegion> movingAnim, rightFacedEatingAnim, leftFacedEatingAnim;
    protected Rectangle hitbox = new Rectangle();
    protected int health = 5;
    protected int applesEaten = 0;
    protected float HissDelay, Alpha_Healing = 0f;
    protected boolean fadingIn = true;
    protected final float D_INTERVAL = 1.75f;
    protected enum Direction {LEFT, RIGHT}
    protected Direction lastLookDirection = Direction.RIGHT;
    protected boolean goingRight = Gdx.input.isKeyPressed(Input.Keys.D);
    protected boolean goingLeft = Gdx.input.isKeyPressed(Input.Keys.A);
    protected boolean damaged, ATE_APPLE, invincible, ATE_BEANS, healed_by_BEAN, ATE_5, isDying = false, NO = false;
    protected Color originalColor;
    protected CharacterShadow shadow;
    private boolean healingEffectActive= false;
    private float DeathTimer, COOLDOWN = 0;
    private static final float COUNTDOWN = 0.5f;
    protected Sound eating, hiss, secret, no;

    public Snake(){
        shadow = new CharacterShadow();

        Idle = new Sprite(Assets.MANAGER.get(Assets.ITEMS_2, TextureAtlas.class).findRegion("texture_snake_idle"));
        Idle.setSize(25, 25);
        Idle.setPosition(25, 25);

        entireAtlas = Assets.MANAGER.get(Assets.SNAKE_ANIMATON_KEYFRAMES, TextureAtlas.class);

        movingAnim = new Animation<>(0.085f, entireAtlas.findRegions("mov"));
        movingAnim.setPlayMode(Animation.PlayMode.LOOP);

        rightFacedEatingAnim = new Animation<>(0.081f, entireAtlas.findRegions("eating"));
        rightFacedEatingAnim.setPlayMode(Animation.PlayMode.LOOP);

        leftFacedEatingAnim = new Animation<>(0.081f, entireAtlas.findRegions("eatingl"));
        leftFacedEatingAnim.setPlayMode(Animation.PlayMode.LOOP);

        Moving = new Sprite(movingAnim.getKeyFrame(0));
        Moving.setSize(25, 25);
        Moving.setPosition(25, 25);

        rightFacedEating = new Sprite(rightFacedEatingAnim.getKeyFrame(0));
        rightFacedEating.setSize(25, 25);
        rightFacedEating.setPosition(25, 25);

        leftFacedEating = new Sprite(leftFacedEatingAnim.getKeyFrame(0));
        leftFacedEating.setSize(25, 25);
        leftFacedEating.setPosition(25, 25);

        dead_0 = new Sprite(Assets.MANAGER.get(Assets.ITEMS_2, TextureAtlas.class).findRegion("texture_dead_standing"));
        dead_0.setSize(25, 25);
        dead_1 = new Sprite(Assets.MANAGER.get(Assets.ITEMS_2, TextureAtlas.class).findRegion("texture_dead_lying"));
        dead_1.setSize(38.001f, 18.22f);
        originalColor = new Color(Idle.getColor());

        eating = Assets.MANAGER.get(Assets.EATING, Sound.class);
        hiss = Assets.MANAGER.get(Assets.HISS, Sound.class);
        secret = Assets.MANAGER.get(Assets.SECRETSOUND, Sound.class);
        no = Assets.MANAGER.get(Assets.CANT_EAT, Sound.class);
    }

    public boolean SNAKE_EVENT_SETTER_DAMAGED(float timer){
        float INV_INTERVAL = 4f;
        float sin = 30;
        if (health == 0){
            sin = 20;
            INV_INTERVAL = D_INTERVAL;
        }
        if (applesEaten >= 50) {
            INV_INTERVAL = 3.35f;
        } if (applesEaten >= 90) {
            INV_INTERVAL = 3f;
        } if (applesEaten >= 200) {
            INV_INTERVAL = 2.5f;
        } if (applesEaten >= 500) {
            INV_INTERVAL = 2f;
        } if (applesEaten >= 800) {
            INV_INTERVAL = 1.5f;
        } if (applesEaten >= 1200) {
            INV_INTERVAL = 1.1f;
        }
        if (timer < INV_INTERVAL) {
            float alpha = 0.5f + 0.5f * (float)Math.sin(sin * timer); // 30: yanıp sönme hızı
            Idle.setAlpha(alpha);
            Moving.setAlpha(alpha);
            rightFacedEating.setAlpha(alpha);
            leftFacedEating.setAlpha(alpha);
            if (health == 0){
                dead_0.setPosition(Idle.getX(), Idle.getY());
                dead_1.setPosition(Idle.getX() - 3, Idle.getY() - 2.5f);
                dead_0.setAlpha(alpha);
            }
            return true;
        } else {
            Idle.setAlpha(1f);
            Moving.setAlpha(1f);
            rightFacedEating.setAlpha(1f);
            leftFacedEating.setAlpha(1f);
            return false;
        }
    }

    public void SNAKE_EVENT_SETTER_INPUT(float speed, float delta, Components comp){
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            Idle.translateX(speed * -1 * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            Idle.translateY(speed * -1 * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            Idle.translateX(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            Idle.translateY(speed * delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            lastLookDirection = Direction.LEFT;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)){
            lastLookDirection = Direction.RIGHT;
        }

        if (delta >= 0) {
            rightFacedEating.setPosition(Idle.getX(), Idle.getY());
            leftFacedEating.setPosition(Idle.getX(), Idle.getY());
        }
        float hitboxWidth = Idle.getWidth() * 0.35f;
        float hitboxHeight = Idle.getHeight() * 0.7f;
        float hitbox_X;
        float hitbox_Y;

        if (lastLookDirection == Direction.RIGHT) {
             hitbox_X = Idle.getX() + 11f;
        } else {
            hitbox_X = Idle.getX() + 5f;
        }
        hitbox_Y = Idle.getY() + 3f;

        hitbox.set(hitbox_X, hitbox_Y, hitboxWidth, hitboxHeight);
        comp.healing.setPosition(Idle.getX() + Idle.getWidth() / 2, Idle.getY());

        if (goingRight && !goingLeft) lastLookDirection = Direction.RIGHT;
        else if (goingLeft && !goingRight) lastLookDirection = Direction.LEFT;

        if (lastLookDirection == Direction.LEFT && !Idle.isFlipX()) {
            Idle.flip(true, false);
            dead_0.flip(true, false);
            dead_1.flip(true, false);
        }
        if (lastLookDirection == Direction.RIGHT && Idle.isFlipX()) {
            Idle.flip(true, false);
            dead_0.flip(true, false);
            dead_1.flip(true, false);
        }
        shadow.init(Idle.getWidth(), Idle.getHeight(), Idle.getColor().a);
        shadow.changePos_Snake(this, Idle.getX(), Idle.getY(), 1.5f, 1.5f);
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) shadow.sprite.setSize(shadow.sprite.getWidth() * 1.0875f, shadow.sprite.getHeight());
    }

    public void SNAKE_EVENT_SETTER_EATING(float DELTA, SpriteBatch batch){
        if (lastLookDirection == Direction.RIGHT) {
            TextureRegion RegionR = rightFacedEatingAnim.getKeyFrame(DELTA, true);
            rightFacedEating.setRegion(RegionR);
            rightFacedEating.setPosition(Idle.getX(), Idle.getY());
            rightFacedEating.setFlip(false, false);
            rightFacedEating.setSize(Idle.getWidth() * 1.08f, Idle.getHeight());
            rightFacedEating.draw(batch);
        } else {
            TextureRegion RegionL = leftFacedEatingAnim.getKeyFrame(DELTA, true);
            leftFacedEating.setRegion(RegionL);
            leftFacedEating.setPosition(Idle.getX(), Idle.getY());
            leftFacedEating.setFlip(false, false);
            leftFacedEating.setSize(Idle.getWidth() * 1.08f, Idle.getHeight());
            leftFacedEating.draw(batch);
        }
    }

    public void SNAKE_EVENT_SETTER_START_HEALING() {
        if (!healingEffectActive) {
            healingEffectActive = true;
            fadingIn = true;
            Alpha_Healing = 0f;
        }
    }

    public void SNAKE_EVENT_SETTER_HEALING(){
            if (!healingEffectActive) return;
            Color targetColor = Color.GREEN;
            float delta = Gdx.graphics.getDeltaTime();

            if (fadingIn) {
                Alpha_Healing += delta / 0.35f;
                if (Alpha_Healing >= 1f) {
                    Alpha_Healing = 1f;
                    fadingIn = false;
                }
            } else {
                Alpha_Healing -= delta / 0.35f;
                if (Alpha_Healing <= 0f) {
                    Alpha_Healing = 0f;
                    healingEffectActive = false;
                    // Done fading
                }
            }
            Idle.setColor(originalColor.cpy().lerp(targetColor, Alpha_Healing));
            Moving.setColor(originalColor.cpy().lerp(targetColor, Alpha_Healing));
            rightFacedEating.setColor(originalColor.cpy().lerp(targetColor, Alpha_Healing));
            leftFacedEating.setColor(originalColor.cpy().lerp(targetColor, Alpha_Healing));
    }

    public void SNAKE_EVENT_SETTER_SOUND(float randomPitch, Apple Apple, JellyBeans jellyBeans, float Delta, boolean isEating){
        COOLDOWN -= Gdx.graphics.getDeltaTime();
        final float INTERVAL = 9f;
        if (HissDelay >= INTERVAL) {
            hiss.play(1.0f, randomPitch, 0);
            HissDelay = 0;
        }

        NO = false;
        boolean eaten = false;
        if ((rightFacedEatingAnim.getKeyFrameIndex(Delta) == 3 || leftFacedEatingAnim.getKeyFrameIndex(Delta) == 3) && isEating) {
            if (hitbox.overlaps(Apple.hitbox) || (hitbox.overlaps(jellyBeans.hitbox) && !invincible)){
                eaten = true;
            }
            if (hitbox.overlaps(jellyBeans.hitbox) && MathUtils.random(1, 200) == 1) secret.play(0.8f);
        }

        if ((rightFacedEatingAnim.getKeyFrameIndex(Delta) == 3 || leftFacedEatingAnim.getKeyFrameIndex(Delta) == 3) && isEating) {
            if (hitbox.overlaps(jellyBeans.hitbox) && invincible) {
                NO = true;
            }
        }

        if (eaten && COOLDOWN <= 0f) {
            eating.play(0.25f, randomPitch, 0);
            COOLDOWN = COUNTDOWN;
        } else if (NO && COOLDOWN <= 0f) {
            no.play(1, 0.9f, 0);
            COOLDOWN = COUNTDOWN;
        }
    }
    public void SNAKE_EVENT_SETTER_CLAMP(float worldWidth, float worldHeight){
        Idle.setX(MathUtils.clamp(Idle.getX(), 0, worldWidth - Idle.getWidth()));
        Idle.setY(MathUtils.clamp(Idle.getY(), 0, worldHeight - Idle.getHeight()));
        Moving.setX(MathUtils.clamp(Moving.getX(), 0, worldWidth - Moving.getWidth()));
        Moving.setY(MathUtils.clamp(Moving.getY(), 0, worldHeight - Moving.getHeight()));
    }

    public void SNAKE_EVENT_SETTER_DEATH(SpriteBatch batch){
        if (health == 0){
            DeathTimer += Gdx.graphics.getDeltaTime();
            if (DeathTimer > D_INTERVAL) {
                shadow.sprite.setSize(dead_1.getWidth(), shadow.sprite.getHeight());
                shadow.sprite.setAlpha(0.4f);
                Idle.setAlpha(0);
                Moving.setAlpha(0);
                rightFacedEating.setAlpha(0);
                leftFacedEating.setAlpha(0);
                dead_0.setAlpha(0);
                dead_1.draw(batch);
            } else shadow.sprite.setAlpha(Idle.getColor().a * 0.4f);
        }
    }

    public void dispose() {
        hiss.dispose();
        eating.dispose();
    }
}
