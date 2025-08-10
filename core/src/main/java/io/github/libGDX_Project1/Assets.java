package io.github.libGDX_Project1;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
    public static final AssetManager MANAGER = new AssetManager();
    // Components.java, Main.java
    public static final String STARTINGSOUND = "audio/audio_pacman_beginning.mp3";
    public static final String GAMEOVERSOUND = "audio/audio_pacman-die.mp3";
    public static final String WEAKNESS = "audio/audio_bullet_weak.mp3";
    public static final String SUCCESFULLYATE = "audio/audio_10.wav";
    public static final String DAMAGESOUND = "audio/audio_damage.mp3";
    public static final String DESTRUCTION = "audio/audio_bullet_destroyed.wav";
    public static final String MUSIC = "audio/music_Main.mp3";
    public static final String ITEMS_1 = "atlas/items_1.atlas";
    public static final String ITEMS_2 = "atlas/items_2.atlas";
    public static final String CANT_EAT= "audio/audio_cancel.ogg";
    // Snake.java
    public static final String HISS = "audio/audio_snake_hissing.mp3";
    public static final String EATING = "audio/audio_snake_eating.ogg";
    public static final String LIFE_UP = "audio/audio_healing_0.mp3";
    public static final String SNAKE_ANIMATON_KEYFRAMES = "atlas/snake_animations.atlas";
    // Bullet.java
    public static final String BULLET_ATLAS = "atlas/bulletSheet.atlas";
    public static final String BOUNCE = "audio/audio_bounce.mp3";
    public static final String FIRING = "audio/audio_cannon_fire.mp3";
    // JellyBeans.java
    public static final String HEALING = "audio/audio_healing_1.mp3";
    // Ghost.java
    public static final String GHOSTS = "atlas/ghosts.atlas";
    public static final String SOUND = "audio/audio_ghost.ogg";
    // Acaba Ayı Uyuyacak mı?
    public static final String SECRETSOUND = "audio/audio_arthur_rdr2.mp3";

    public static void LOAD_ALL(){
        MANAGER.load(STARTINGSOUND, Sound.class);
        MANAGER.load(GAMEOVERSOUND, Sound.class);
        MANAGER.load(WEAKNESS, Sound.class);
        MANAGER.load(SUCCESFULLYATE, Sound.class);
        MANAGER.load(DAMAGESOUND, Sound.class);
        MANAGER.load(DESTRUCTION, Sound.class);
        MANAGER.load(MUSIC, Music.class);
        MANAGER.load(ITEMS_1, TextureAtlas.class);
        MANAGER.load(ITEMS_2, TextureAtlas.class);
        MANAGER.load(CANT_EAT, Sound.class);
        MANAGER.load(HISS, Sound.class);
        MANAGER.load(EATING, Sound.class);
        MANAGER.load(LIFE_UP, Sound.class);
        MANAGER.load(SNAKE_ANIMATON_KEYFRAMES, TextureAtlas.class);
        MANAGER.load(BULLET_ATLAS, TextureAtlas.class);
        MANAGER.load(BOUNCE, Sound.class);
        MANAGER.load(FIRING, Sound.class);
        MANAGER.load(HEALING, Sound.class);
        MANAGER.load(GHOSTS, TextureAtlas.class);
        MANAGER.load(SOUND, Sound.class);
        MANAGER.load(SECRETSOUND, Sound.class);
    }

    public static void DISPOSE_ALL(){
        MANAGER.dispose();
    }

}
