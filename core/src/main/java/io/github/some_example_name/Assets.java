package io.github.some_example_name;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

public class Assets {
    private static Assets instance;
    public final AssetManager manager = new AssetManager();

    private Assets() {}

    public static synchronized Assets getInstance() {
        if (instance == null) {
            instance = new Assets();
        }
        return instance;
    }

    public void load() {
        // Texturas existentes
        manager.load("drop.png", Texture.class);
        manager.load("dropBad.png", Texture.class);
        manager.load("drop_gray.png", Texture.class);
        manager.load("drop_red_life.png", Texture.class);
        manager.load("bucket.png", Texture.class);
        manager.load("power_shield_effect.png", Texture.class);
        manager.load("drop_shield.png", Texture.class);
        manager.load("power_clean_screen.png", Texture.class);
        
        // NUEVO: Textura del Boss
        manager.load("boss.png", Texture.class); // Asegúrate de tener este archivo

        // Sonidos y Música
        manager.load("drop.wav", Sound.class);
        manager.load("hurt.ogg", Sound.class);
        manager.load("soundgray.wav", Sound.class);
        manager.load("rain.mp3", Music.class);
    }

    public void dispose() {
        manager.dispose();
        instance = null;
    }
}