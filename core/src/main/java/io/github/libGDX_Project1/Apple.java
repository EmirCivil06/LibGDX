package io.github.libGDX_Project1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Apple {
    protected Sprite sprite;
    protected Rectangle hitbox = new Rectangle();
    protected float starting_X = MathUtils.random(0, 478.125f);
    protected float starting_Y = MathUtils.random(0, 228.125f);

    // Aslında yeni elma objesi oluşturmuyoruz, yerini değiştiriyoruz
    public void spawnNew(){
        float coordX = MathUtils.random(50, 478.125f);
        float coordY = MathUtils.random(50, 223.125f);
        sprite.setPosition(coordX, coordY);
        hitbox.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    Apple(){
        sprite = new Sprite(Assets.MANAGER.get(Assets.ITEMS, TextureAtlas.class).findRegion("texture_apple"));
        sprite.setSize(21.875f, 21.875f);
        sprite.setPosition(starting_X, starting_Y);
        hitbox.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public boolean EatingConditionsCreated(Snake s, float delta) {
        if ((s.rightFacedEatingAnim.getKeyFrameIndex(delta) == 4 || s.leftFacedEatingAnim.getKeyFrameIndex(delta) == 4) && Gdx.input.isKeyPressed(Input.Keys.ENTER) && !s.ATE_APPLE) {
            return s.hitbox.overlaps(hitbox);
        }
        return false;
    }

    public void APPLE_EVENT_SETTER_DESPAWN(JellyBeans jellyBeans){
        if (jellyBeans.sprite.getX() == jellyBeans.Starting_X) {
            spawnNew();
        } else {
            sprite.setPosition(550, 200);
        }
    }
}
