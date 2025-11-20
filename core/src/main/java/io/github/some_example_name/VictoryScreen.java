package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class VictoryScreen implements Screen {

    final GameLluviaNewVersionV2 game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    public VictoryScreen(final GameLluviaNewVersionV2 game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        // Fondo de color verde oscuro para la victoria
        ScreenUtils.clear(0, 0.2f, 0, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Texto de Victoria
        font.draw(batch, "¡ENHORABUENA!", 300, 300);
        font.draw(batch, "¡Has derrotado al Rey de la Tormenta!", 250, 250);
        font.draw(batch, "Toca en cualquier lugar para reiniciar.", 250, 200);
        batch.end();

        // Si toca la pantalla, vuelve al menú
        if (Gdx.input.isTouched()) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void show() {}
    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        // No hacemos dispose del font ni del batch porque pertenecen al objeto 'game'
    }
}