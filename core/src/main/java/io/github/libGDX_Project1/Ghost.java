package io.github.libGDX_Project1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Ghost  {
    protected Sprite ghost;
    protected Animation<TextureRegion> animation;
    protected TextureAtlas atlas;
    protected float DELTA = 0;
    protected ParticleEffect particle, warn;
    protected boolean isDown = true, needsToBeDrawn = true;
    protected Rectangle hitbox = new Rectangle();
    private float soundCountdown = 0;
    public final float soundCooldown = 2.8f;
    private final Sound sound;
    protected GhostType type;

    public Ghost(GhostType type){
        this.type = type;
        String regionName = "";
        String particleFileName = "";

        switch (type) {
            case YELLOW:
                regionName = "yellowghost";
                particleFileName = "yellow1";
                break;
            case RED:
                regionName = "redghost";
                particleFileName = "red0";
                break;
            case BLUE:
                regionName = "blueghost";
                particleFileName = "blue0";
                break;
            case PINK:
                regionName = "pinkghost";
                particleFileName = "pink0";
                break;
        }

        atlas = Assets.MANAGER.get(Assets.GHOSTS, TextureAtlas.class);
        animation = new Animation<>(0.15f, atlas.findRegions(regionName));
        animation.setPlayMode(Animation.PlayMode.LOOP);
        particle = new ParticleEffect();
        particle.load(Gdx.files.internal("particles/" + particleFileName + ".p"), Gdx.files.internal("particles"));
        warn = new ParticleEffect();
        warn.load(Gdx.files.internal("particles/warning.p"), Gdx.files.internal("particles"));

        ghost = new Sprite(animation.getKeyFrame(0f, true));
        ghost.setSize(17.5f, 17.5f);
        float randomX = MathUtils.random(0, 500 - ghost.getWidth());
        ghost.setPosition(randomX, -200);
        warn.setPosition(ghost.getX() + ghost.getWidth() / 2, 10);
        sound = Assets.MANAGER.get(Assets.SOUND, Sound.class);
    }

    public void update(float delta){
        particle.start();
        particle.update(delta);
        particle.setPosition(ghost.getX() + ghost.getWidth() / 2, ghost.getY() + ghost.getHeight() / 2);
        warn.start();
        warn.update(delta);
        hitbox.set(ghost.getX() - 1.75f, ghost.getY() - 1.75f, ghost.getWidth() * 4.5f/5f, ghost.getHeight() * 4.5f/5f);
    }

    public boolean Overlaps(Snake snake){
        if (snake.invincible) ghost.setAlpha(0.75f);
        else ghost.setAlpha(1f);
        return hitbox.overlaps(snake.hitbox);
    }

    public void logic(float delta, float worldWidth, float worldHeight){
        soundCountdown -= delta;
        if (isDown){
            ghost.translateY(delta * 275f);
            if (soundCountdown <= 0){
                sound.play(0.3f);
                soundCountdown = soundCooldown;
            }
        }

        isDown = !(ghost.getY() >= worldHeight + ghost.getHeight() + 300);
        if (!isDown) {
            float randomX = MathUtils.random(0, worldWidth - ghost.getWidth());
            ghost.setPosition(randomX, -200);
        }
        needsToBeDrawn = ghost.getY() + ghost.getHeight() <= worldHeight - 200 && isDown;
    }

    public void draw(float delta, SpriteBatch batch){
        DELTA += delta;
        TextureRegion region = animation.getKeyFrame(DELTA, true);
        ghost.setRegion(region);
        particle.draw(batch);
        warn.setPosition(ghost.getX() + ghost.getWidth() / 2, 12);
        if (needsToBeDrawn) warn.draw(batch);
        else warn.reset();
        ghost.draw(batch);
    }


    public void dispose() {
        warn.dispose();
        particle.dispose();
        sound.dispose();
    }
}
