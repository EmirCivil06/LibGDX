package io.github.libGDX_Project1;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class CharacterShadow {
    protected Sprite sprite;

    CharacterShadow(){
        sprite = new Sprite(Assets.MANAGER.get(Assets.ITEMS_2, TextureAtlas.class).findRegion("texture_shadow"));
    }

    public void init(float characterWidth, float characterHeight, float characterAlpha){
        sprite.setAlpha(0.4f * characterAlpha);
        sprite.setSize(characterWidth * 1.08f, characterHeight * 0.15f);
    }

    public void changePos_Snake(Snake snake, float characterX, float characterY, float offsetX, float offsetY){
        if (snake.lastLookDirection == Snake.Direction.RIGHT) sprite.setPosition(characterX - offsetX * 1.5f, characterY - offsetY);
        if (snake.lastLookDirection == Snake.Direction.LEFT) sprite.setPosition(characterX + offsetX * 0.5f, characterY - offsetY);
    }

    public void changePos(float characterX, float characterY, float offsetX, float offsetY) {
        sprite.setPosition(characterX - offsetX, characterY - offsetY);
    }
}
