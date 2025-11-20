package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {
	private final GameLluviaNewVersionV2 game;
	private SpriteBatch batch;	   
	private BitmapFont font;
	private OrthographicCamera camera;

	public GameOverScreen(final GameLluviaNewVersionV2 game) {
		this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}

	@Override
	public void render(float delta) {
		// Limpia la pantalla con un color azul oscuro
		ScreenUtils.clear(0, 0, 0.2f, 1);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		// Dibuja el texto de "Game Over" centrado
		font.draw(batch, "GAME OVER ", 360, 300);
		
		// Muestra el puntaje más alto almacenado en la clase principal
		font.draw(batch, "Récord actual: " + game.getHigherScore(), 340, 250);
		
		// Dibuja las instrucciones para reiniciar
		font.draw(batch, "Toca en cualquier lado para reiniciar.", 260, 150);
		batch.end();

		// Detecta si el usuario toca la pantalla para volver al juego
		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
		// Actualiza el viewport de la cámara para mantener la consistencia al redimensionar la ventana
		camera.viewportWidth = 800;
		camera.viewportHeight = 480;
		camera.update();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		// No se liberan batch ni font aquí porque son gestionados por la clase Game principal
	}
}