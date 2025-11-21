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
        // --- Texturas Generales ---
        manager.load("drop.png", Texture.class);
        manager.load("dropBad.png", Texture.class);
        manager.load("drop_gray.png", Texture.class);
        manager.load("drop_red_life.png", Texture.class);
        manager.load("bucket.png", Texture.class);
        manager.load("power_shield_effect.png", Texture.class);
        manager.load("drop_shield.png", Texture.class);
        manager.load("power_clean_screen.png", Texture.class);
        
        // --- Recursos del Boss ---
        manager.load("boss.png", Texture.class); 
        manager.load("boss_projectile.png", Texture.class);

        // --- FONDOS DE PANTALLA ---
        manager.load("menu_bg.png", Texture.class); 
        manager.load("gameover_bg.png", Texture.class);
        manager.load("victory_bg.png", Texture.class);
        manager.load("pause_bg.png", Texture.class);
        
        // NUEVO: Fondo del juego principal
        manager.load("game_bg.png", Texture.class);

        // --- Audio ---
        manager.load("intro.mp3", Music.class);
        manager.load("drop.wav", Sound.class);
        manager.load("hurt.ogg", Sound.class);
        manager.load("soundgray.wav", Sound.class);
        manager.load("rain.mp3", Music.class);
        manager.load("boss_death.mp3", Sound.class);
    }

    public void dispose() {
        manager.dispose();
        instance = null;
    }
}