package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameLluviaNewVersionV2 extends Game {

    private SpriteBatch batch;
    private BitmapFont font;
    private int higherScore;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // usa la fuente Arial por defecto de libGDX

        // --- INICIO DE LA MODIFICACIÓN (Singleton GM2.1) ---
        // 1. Llama al Singleton para poner a cargar todos los assets
        Assets.getInstance().load();
        
        // 2. Espera de forma bloqueante a que todo termine de cargar
        // (En un juego más grande, aquí iría una pantalla de carga)
        Assets.getInstance().manager.finishLoading();
        
        // 3. Ahora que todo está cargado, muestra el menú principal
        this.setScreen(new MainMenuScreen(this));
        // --- FIN DE LA MODIFICACIÓN ---
    }

    public void render() {
        super.render(); // ¡Importante!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        
        // --- INICIO DE LA MODIFICACIÓN (Singleton GM2.1) ---
        // 4. Llama al Singleton para liberar todos los assets
        Assets.getInstance().dispose();
        // --- FIN DE LA MODIFICACIÓN ---
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public int getHigherScore() {
        return higherScore;
    }

    public void setHigherScore(int higherScore) {
        this.higherScore = higherScore;
    }
}