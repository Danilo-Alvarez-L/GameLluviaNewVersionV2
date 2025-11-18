package io.github.some_example_name;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

/**
 * Aplica el patrón Singleton para gestionar de forma centralizada
 * todos los assets (texturas, sonidos, música) del juego.
 * Esto asegura que los assets se carguen una sola vez.
 */
public class Assets {

    // 1. La única instancia (privada y estática)
    private static Assets instance;

    // El gestor de assets de libGDX
    public final AssetManager manager = new AssetManager();

    // 2. El constructor (privado)
    private Assets() {
        // Constructor privado para evitar instanciación externa
    }

    // 3. El punto de acceso global (público y estático)
    public static synchronized Assets getInstance() {
        if (instance == null) {
            instance = new Assets();
        }
        return instance;
    }

    // Método para cargar todos los assets en la cola del manager
    public void load() {
        // Texturas
        manager.load("drop.png", Texture.class);
        manager.load("dropBad.png", Texture.class);
        manager.load("drop_gray.png", Texture.class);
        manager.load("drop_red_life.png", Texture.class);
        manager.load("bucket.png", Texture.class);
        manager.load("power_shield_effect.png", Texture.class);
        manager.load("drop_shield.png", Texture.class);
        manager.load("power_clean_screen.png", Texture.class);

        // Sonidos
        manager.load("drop.wav", Sound.class);
        manager.load("hurt.ogg", Sound.class);
        manager.load("soundgray.wav", Sound.class);
        
        // Música
        manager.load("rain.mp3", Music.class);
    }

    // Método para liberar la memoria
    public void dispose() {
        manager.dispose();
        instance = null;
    }
}