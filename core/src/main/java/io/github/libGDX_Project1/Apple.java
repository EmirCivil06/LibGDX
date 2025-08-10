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
    protected float width = 18.5f;
    protected float sizeOffset = 147 / 126f;
    protected float starting_X = MathUtils.random(0, 500f - width);
    protected float starting_Y = MathUtils.random(0, 250 - width * sizeOffset);

    // Aslında yeni elma objesi oluşturmuyoruz, yerini değiştiriyoruz
    public void spawnNew(){
        float coordX = MathUtils.random(50, 500f - width);
        float coordY = MathUtils.random(50, 250f - width * sizeOffset);
        sprite.setPosition(coordX, coordY);
        hitbox.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    Apple(){
        sprite = new Sprite(Assets.MANAGER.get(Assets.ITEMS_1, TextureAtlas.class).findRegion("texture_apple"));
        sprite.setSize(width, width * sizeOffset);
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
