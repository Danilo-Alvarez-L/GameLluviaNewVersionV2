package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class VictoryScreen implements Screen {

    final GameLluviaNewVersionV2 game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private GameLevelFactory levelFactory;

    private Texture background;

    public VictoryScreen(final GameLluviaNewVersionV2 game, GameLevelFactory factory) {
        this.game = game;
        this.levelFactory = factory;
        
        this.batch = game.getBatch();
        this.font = game.getFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        
        Assets assets = Assets.getInstance();
        if (assets.manager.isLoaded("victory_bg.png")) {
            background = assets.manager.get("victory_bg.png", Texture.class);
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
        
        font.draw(batch, "¡VICTORIA! ¡Has derrotado al Rey Tormenta!", 20, 460);
        font.draw(batch, "Toca para jugar de nuevo", 20, 400);
        
        batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game, levelFactory));
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