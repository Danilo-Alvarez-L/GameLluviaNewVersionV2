package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input; 
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final GameLluviaNewVersionV2 game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    
    private Texture background;
    private Music menuMusic;

    public MainMenuScreen(final GameLluviaNewVersionV2 game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        
        Assets assets = Assets.getInstance();
        
        // Carga de recursos decorativos
        if (assets.manager.isLoaded("menu_bg.png")) {
            background = assets.manager.get("menu_bg.png", Texture.class);
        }
        
        if (assets.manager.isLoaded("intro.mp3")) {
            menuMusic = assets.manager.get("intro.mp3", Music.class);
            if (menuMusic != null) {
                menuMusic.setLooping(true);
                menuMusic.setVolume(0.5f);
            }
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        
        // Dibujar Fondo
        if (background != null) {
            batch.draw(background, 0, 0, 800, 480);
        }
        
        // Escalar la fuente un poco (1.5x)
        font.getData().setScale(1.5f, 1.5f);
        
        // --- CAMBIO DE COORDENADAS (Arriba a la Izquierda) ---
        // X = 20 (Margen izquierdo pequeño)
        // Y = Se calcula desde arriba (480) hacia abajo
        
        // Título: Casi pegado arriba
        font.draw(batch, "Bienvenido a Recolecta Gotas!!!", 20, 460);
        
        // Opciones: Un poco más abajo, alineadas a la izquierda
        font.draw(batch, "Presiona 1 para Nivel FACIL", 20, 400);
        font.draw(batch, "Presiona 2 para Nivel DIFICIL (ZigZag)", 20, 360);
        font.draw(batch, "O toca la pantalla para comenzar (Facil)", 20, 320);

        batch.end();

        // Lógica de selección
        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            game.setScreen(new GameScreen(game, new Level1Factory()));
            dispose();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            game.setScreen(new GameScreen(game, new Level2Factory()));
            dispose();
        }
    }

    @Override
    public void show() {
        if (menuMusic != null) menuMusic.play();
    }

    @Override
    public void hide() {
        if (menuMusic != null) menuMusic.stop();
    }

    @Override public void dispose() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
}