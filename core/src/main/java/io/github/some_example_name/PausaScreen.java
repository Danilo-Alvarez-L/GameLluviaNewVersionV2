package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class PausaScreen implements Screen {

    final GameLluviaNewVersionV2 game;
    private GameScreen currentGameScreen; 
    private GameLevelFactory levelFactory;
    
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    
    private Texture background;

    public PausaScreen(final GameLluviaNewVersionV2 game, GameScreen currentGameScreen, GameLevelFactory factory) {
        this.game = game;
        this.currentGameScreen = currentGameScreen;
        this.levelFactory = factory;
        
        this.batch = game.getBatch();
        this.font = game.getFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        
        Assets assets = Assets.getInstance();
        if (assets.manager.isLoaded("pause_bg.png")) {
            background = assets.manager.get("pause_bg.png", Texture.class);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        
        // 1. Fondo
        if (background != null) {
            batch.draw(background, 0, 0, 800, 480);
        }
        
        // 2. Texto (Alineado arriba a la izquierda)
        font.getData().setScale(1.5f, 1.5f);
        
        // Título pegado arriba
        font.draw(batch, "JUEGO PAUSADO", 20, 460);
        
        // Opciones en lista hacia abajo
        font.draw(batch, "Toca para CONTINUAR", 20, 400);
        font.draw(batch, "Presiona 'R' para REINICIAR Nivel", 20, 360);
        font.draw(batch, "Presiona 'M' para ir al MENU", 20, 320);
        
        batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(currentGameScreen);
            dispose();
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setScreen(new GameScreen(game, levelFactory));
            dispose();
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}