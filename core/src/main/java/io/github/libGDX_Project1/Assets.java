package io.github.libGDX_Project1;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
    public static final AssetManager MANAGER = new AssetManager();
    // Components.java, Main.java
    public static final String BACKGROUND = "textures/texture_grass_background.png";
    public static final String STARTINGSOUND = "audio/pacman_beginning.mp3";
    public static final String GAMEOVERSOUND = "audio/pacman-die.mp3";
    public static final String WEAKNESS = "audio/weakness.mp3";
    public static final String SUCCESFULLYATE = "audio/success.wav";
    public static final String DAMAGESOUND = "audio/damage.mp3";
    public static final String DESTRUCTION = "audio/pacman_eatghost.wav";
    public static final String MUSIC = "audio/sound_Main_Music.mp3";
    public static final String INTERFACE = "textures/texture_interface.png";
    public static final String ITEMS = "atlas/items.atlas";
    // Snake.java
    public static final String IDLETEXTURE = "textures/texture_snake_idle.png";
    public static final String MOVING_ATLAS = "atlas/snake_moving.atlas";
    public static final String EATING_ATLAS_R = "atlas/eating.atlas";
    public static final String EATING_ATLAS_L = "atlas/eating_left.atlas";
    public static final String HISS = "audio/snake-hissing-6092.mp3";
    public static final String EATING = "audio/eating.ogg";
    public static final String LIFE_UP = "audio/life.mp3";
    public static final String DEAD_0 = "textures/texture_dead_standing.png";
    public static final String DEAD_1 = "textures/texture_dead_lying.png";
    // Bullet.java
    public static final String BULLET_ATLAS = "atlas/bulletSheet.atlas";
    public static final String BOUNCE = "audio/bounce.mp3";
    // JellyBeans.java
    public static final String HEALING = "audio/heal.mp3";
    // CharacterShadow.java
    public static final String SHADOW = "textures/texture_shadow.png";
    // Ghost.java
    public static final String GHOSTS = "atlas/ghosts.atlas";
    public static final String SOUND = "audio/sound_ghost.mp3";

    public static void LOAD_ALL(){
        MANAGER.load(BACKGROUND, Texture.class);
        MANAGER.load(STARTINGSOUND, Sound.class);
        MANAGER.load(GAMEOVERSOUND, Sound.class);
        MANAGER.load(WEAKNESS, Sound.class);
        MANAGER.load(SUCCESFULLYATE, Sound.class);
        MANAGER.load(DAMAGESOUND, Sound.class);
        MANAGER.load(DESTRUCTION, Sound.class);
        MANAGER.load(MUSIC, Music.class);
        MANAGER.load(INTERFACE, Texture.class);
        MANAGER.load(ITEMS, TextureAtlas.class);
        MANAGER.load(IDLETEXTURE, Texture.class);
        MANAGER.load(MOVING_ATLAS, TextureAtlas.class);
        MANAGER.load(EATING_ATLAS_R, TextureAtlas.class);
        MANAGER.load(EATING_ATLAS_L, TextureAtlas.class);
        MANAGER.load(HISS, Sound.class);
        MANAGER.load(EATING, Sound.class);
        MANAGER.load(LIFE_UP, Sound.class);
        MANAGER.load(DEAD_0, Texture.class);
        MANAGER.load(DEAD_1, Texture.class);
        MANAGER.load(BULLET_ATLAS, TextureAtlas.class);
        MANAGER.load(BOUNCE, Sound.class);
        MANAGER.load(HEALING, Sound.class);
        MANAGER.load(SHADOW, Texture.class);
        MANAGER.load(GHOSTS, TextureAtlas.class);
        MANAGER.load(SOUND, Sound.class);
    }

    public static void DISPOSE_ALL(){
        MANAGER.dispose();
    }

}
