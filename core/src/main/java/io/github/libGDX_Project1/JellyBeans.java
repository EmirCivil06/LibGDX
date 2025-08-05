package io.github.libGDX_Project1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class JellyBeans {
    protected Sprite sprite;
    protected Rectangle hitbox = new Rectangle();
    protected float Starting_X = 560;
    protected float Starting_y = 285;
    protected final float WIDTH = 25;
    protected final float HEIGHT = 25;
    protected Sound healing;

    JellyBeans() {
        sprite = new Sprite(Assets.MANAGER.get(Assets.ITEMS, TextureAtlas.class).findRegion("texture_jellybeans"));
        sprite.setSize(WIDTH, HEIGHT);
        sprite.setPosition(Starting_X, Starting_y);
        healing = Assets.MANAGER.get(Assets.HEALING, Sound.class);
        hitbox.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public boolean check(){
        return MathUtils.random(0, 100) % 13 == 0;
    }

    public void try_to_spawn(){
        if(check()){
            float rand_X = MathUtils.random(0, 500 - WIDTH);
            float rand_Y = MathUtils.random(0, 250 - HEIGHT);
            sprite.setPosition(rand_X, rand_Y);
        } else{
            sprite.setPosition(Starting_X, Starting_y);
        }
        hitbox.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public boolean EatingConditionsCreated(Snake snake, float delta){
        if ((snake.rightFacedEatingAnim.getKeyFrameIndex(delta) == 4 || snake.leftFacedEatingAnim.getKeyFrameIndex(delta) == 4) && Gdx.input.isKeyPressed(Input.Keys.ENTER) && !snake.ATE_BEANS) {
            return snake.hitbox.overlaps(hitbox);
        }
        return false;
    }

}
