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

        // 1. Carga de Assets (Singleton)
        Assets.getInstance().load();
        Assets.getInstance().manager.finishLoading();
        
        // --- CORRECCIÓN IMPORTANTE ---
        // No llamamos a GameScreen directo porque ahora requiere una fábrica.
        // Llamamos al MainMenuScreen, que ya tiene la lógica para elegir nivel.
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render(); 
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        Assets.getInstance().dispose();
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