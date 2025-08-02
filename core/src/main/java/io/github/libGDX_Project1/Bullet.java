package io.github.libGDX_Project1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Bullet  {
    protected TextureAtlas atlas;
    protected Animation<TextureRegion> animation;
    protected Sprite sprite;
    protected Sound bounce;
    protected Rectangle hitbox;
    protected float starting_X;
    protected float starting_Y = 237;
    protected float stateTime, Timer_Self = 0;
    protected final float INTERVAL = 0.08f;
    protected ParticleEffect effect_self, effect_trail_0, effect_trail_1;
    protected CharacterShadow shadow;

    protected enum DOUBLE_DIRECTION {down_right, down_left, up_right, up_left, NONE}
    protected DOUBLE_DIRECTION latestDir;

    protected boolean particlesActive = false;
    private float bounceCD = 0f;            // geri sayım
    private static final float BOUNCE_COOLDOWN = 0.08f; // saniye

    public Bullet(){
        if (MathUtils.random(0, 100) % 2 == 0){
            starting_X = 0.2f;
            if (MathUtils.random(0, 1) == 1){
                starting_Y = 0.2f;
                latestDir = DOUBLE_DIRECTION.up_right;
            } else latestDir = DOUBLE_DIRECTION.down_right;

        } else {
            starting_X = 482.3f;
            if (MathUtils.random(0, 1) == 1){
                starting_Y = 0.2f;
                latestDir = DOUBLE_DIRECTION.up_left;
            }else latestDir = DOUBLE_DIRECTION.down_left;
        }
        atlas = Assets.MANAGER.get(Assets.BULLET_ATLAS, TextureAtlas.class);
        animation = new Animation<>(0.1f, atlas.findRegions("bullet"));
        animation.setPlayMode(Animation.PlayMode.LOOP);

        sprite = new Sprite(animation.getKeyFrame(0));
        sprite.setSize(17.5f, 17.5f);
        sprite.setPosition(starting_X, starting_Y);
        sprite.setAlpha(0);
        effect_self = new ParticleEffect();
        effect_self.load(Gdx.files.internal("particles/bullet-destroyed.p"), Gdx.files.internal("particles/"));
        effect_trail_0 = new ParticleEffect();
        effect_trail_0.load(Gdx.files.internal("particles/bullet_particle.p"), Gdx.files.internal("particles/"));
        effect_trail_1 = new ParticleEffect();
        effect_trail_1.load(Gdx.files.internal("particles/bullet_particle_weak.p"), Gdx.files.internal("particles/"));

        bounce = Assets.MANAGER.get(Assets.BOUNCE, Sound.class);
        shadow = new CharacterShadow();

        hitbox = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void ActivateLogic(float worldWidth, float worldHeight){
        float x = sprite.getX();
        float y = sprite.getY();
        float w = sprite.getWidth();
        float h = sprite.getHeight();
        float epsilon = 0.15f;
        float delta = Gdx.graphics.getDeltaTime();
        bounceCD -= delta;                  // geri say

        boolean bounced = false;            // bu karede çarptı mı?

        // Sağ kenar
        if (x + w >= worldWidth - epsilon) {
            if (latestDir == DOUBLE_DIRECTION.down_right) latestDir = DOUBLE_DIRECTION.down_left;
            else if (latestDir == DOUBLE_DIRECTION.up_right) latestDir = DOUBLE_DIRECTION.up_left;
            bounced = true;
        }
        // Sol kenar
        if (x <= epsilon) {
            if (latestDir == DOUBLE_DIRECTION.down_left) latestDir = DOUBLE_DIRECTION.down_right;
            else if (latestDir == DOUBLE_DIRECTION.up_left) latestDir = DOUBLE_DIRECTION.up_right;
            bounced = true;
        }
        // Alt kenar
        if (y <= epsilon) {
            if (latestDir == DOUBLE_DIRECTION.down_left) latestDir = DOUBLE_DIRECTION.up_left;
            else if (latestDir == DOUBLE_DIRECTION.down_right) latestDir = DOUBLE_DIRECTION.up_right;
            bounced = true;
        }
        // Üst kenar
        if (y + h >= worldHeight - epsilon) {
            if (latestDir == DOUBLE_DIRECTION.up_left) latestDir = DOUBLE_DIRECTION.down_left;
            else if (latestDir == DOUBLE_DIRECTION.up_right) latestDir = DOUBLE_DIRECTION.down_right;
            bounced = true;
        }

        if (bounced && bounceCD <= 0f) {    // soğuma bittiğinde çal
            bounce.play(0.15f);
            bounceCD = BOUNCE_COOLDOWN;     // yeniden başlat
        }
        changeSpeed();
    }

    public void changeSpeed(){
        hitbox.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        effect_self.setPosition(sprite.getX() + sprite.getWidth() / 2f, sprite.getY() + sprite.getHeight() / 2f);
        effect_trail_0.setPosition(sprite.getX() + sprite.getWidth() / 2f, sprite.getY() + sprite.getHeight() / 2f);
        effect_trail_1.setPosition(sprite.getX() + sprite.getWidth() / 2f, sprite.getY() + sprite.getHeight() / 2f);
        shadow.init(sprite.getWidth(), sprite.getHeight() * 1.1f, sprite.getColor().a);

        float speed = MathUtils.random(143.75f, 207.5f);
        float delta = Gdx.graphics.getDeltaTime();
        switch (latestDir){
            case down_right:
                sprite.translate(speed * delta, speed * delta * -1);
                break;
            case down_left:
                sprite.translate(speed * delta * -1, speed * delta * -1);
                break;
            case up_right:
                sprite.translate(speed * delta, speed * delta);
                break;
            case up_left:
                sprite.translate(speed * delta * -1, speed * delta);
                break;
            case NONE:
                sprite.translate(0, 0);
                break;
            default:
                sprite.translate(speed * delta, speed * delta);
        }
    }

    public void CHANGE_ALPHA(float timer){
        float alpha = 0.6f + (0.4f * (float) Math.sin(30 * timer));
        if (!particlesActive) sprite.setAlpha(alpha);
    }

    public void SET_PARTICLES_DEATH(SpriteBatch batch){
        if (particlesActive) {
            effect_self.start();
            effect_self.update(Gdx.graphics.getDeltaTime());
            Timer_Self += Gdx.graphics.getDeltaTime();
            if (Timer_Self < INTERVAL) effect_self.draw(batch);
        }
    }

    public void SET_PARTICLES_LIFE(SpriteBatch batch, Snake s){
        if (!s.invincible) {
            effect_trail_0.start();
            effect_trail_0.update(Gdx.graphics.getDeltaTime());
            effect_trail_0.draw(batch);
        } else {
            effect_trail_1.start();
            effect_trail_1.update(Gdx.graphics.getDeltaTime());
            effect_trail_1.draw(batch);
        }
    }
}
