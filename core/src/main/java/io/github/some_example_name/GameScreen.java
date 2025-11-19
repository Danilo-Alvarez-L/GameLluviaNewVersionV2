package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input; 
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator; 

public class GameScreen implements Screen, GameContext {
	
	final GameLluviaNewVersionV2 game;
    private OrthographicCamera camera;
	private SpriteBatch batch;	   
	private BitmapFont font;
	private Tarro tarro;
	
	// --- LÓGICA DE JUEGO ---
	private Array<Collectible> collectibles;
    private long lastDropTime;
    private boolean spawnPaused = false;
	
    // --- PATRONES DE DISEÑO ---
    // GM2.4 Abstract Factory: Delegamos la creación de objetos a la fábrica
    private DropFactory dropFactory;
    
    // GM2.3 Strategy: Delegamos la gestión de poderes al manager
	private PowerManager powerManager;
	
    // GM1.6: Módulo de GodMode
	private GodModeToggle godModeToggle;
	
	// Música de fondo
	private Music rainMusic;
	// Sonido de daño para el tarro (los demás sonidos van en las gotas)
	private Sound hurtSound; 

	// Puntuación para el próximo PowerUp
	private int nextPowerUpScore = 500;

	public GameScreen(final GameLluviaNewVersionV2 game) {
		this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
		  
        // Obtener música desde el Singleton Assets (GM2.1)
        rainMusic = Assets.getInstance().manager.get("rain.mp3", Music.class);
        hurtSound = Assets.getInstance().manager.get("hurt.ogg", Sound.class);
        
        // Inicializar la Fábrica (GM2.4)
        dropFactory = new DropFactory();
        
        // Crear el Tarro (obteniendo texturas del Singleton)
        Texture bucketTexture = Assets.getInstance().manager.get("bucket.png", Texture.class);
        Texture shieldTexture = Assets.getInstance().manager.get("power_shield_effect.png", Texture.class);
        
        tarro = new Tarro(bucketTexture, hurtSound, shieldTexture);
         
		// Inicializar listas y gestores
		collectibles = new Array<>();
		powerManager = new PowerManager(this);
		godModeToggle = new GodModeToggle(this);
	      
	    // Cámara
	    camera = new OrthographicCamera();
	    camera.setToOrtho(false, 800, 480);
	      
	    tarro.crear();
	    
	    // Iniciar música
	    rainMusic.setVolume(0.5f);
		rainMusic.setLooping(true);
	    rainMusic.play();
	      
	    // Crear la primera gota
	    spawnCollectible(); 
	}
	
	/**
	 * Crea un nuevo collectible usando la Fábrica (GM2.4).
	 * Ya no hay 'new DropBlue()' aquí, todo pasa por dropFactory.
	 */
	private void spawnCollectible() {
		if (spawnPaused) return;

		float x = MathUtils.random(0, 800-64);
		float y = 480;
		
		Collectible newDrop = null;
		int type = MathUtils.random(1, 100); // Probabilidad 1-100
		
		if (type <= 40) { // 40% Gota Azul
			newDrop = dropFactory.createDrop("BLUE", x, y);
		} else if (type <= 70) { // 30% Gota Negra
			newDrop = dropFactory.createDrop("BLACK", x, y);
		} else if (type <= 90) { // 20% Gota Gris
			newDrop = dropFactory.createDrop("GRAY", x, y); 
		} else { // 10% Gota Roja
			newDrop = dropFactory.createDrop("RED", x, y);
		}
		
		if (newDrop != null) {
			collectibles.add(newDrop);
		}
		lastDropTime = TimeUtils.nanoTime();
	}
	
	/**
	 * Spawnea un Power-Up usando la Fábrica (GM2.4).
	 */
	private void spawnPowerUp() {
		float x = MathUtils.random(0, 800-64);
		float y = 480;
		Collectible powerUp;

		if (MathUtils.randomBoolean()) {
			powerUp = dropFactory.createDrop("POWER_SHIELD", x, y);
		} else {
			powerUp = dropFactory.createDrop("POWER_CLEAN", x, y);
		}
		
		if (powerUp != null) {
		    collectibles.add(powerUp);
		}
	}
	
	private void checkInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
			godModeToggle.toggle();
		}
		// Debug: Spawneamos poderes manualmente para probar
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			powerManager.addPower(new PowerShield());
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			powerManager.addPower(new PowerCleanScreen());
		}
	}
	
	private void checkPowerUpSpawn() {
		if (tarro.getPuntos() >= nextPowerUpScore) {
			spawnPowerUp();
			nextPowerUpScore += 500; 
		}
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		checkInput();
		checkPowerUpSpawn();
		
		// Actualizar PowerManager (GM2.3 Strategy)
		powerManager.update(delta);
		
		batch.begin();
		
		font.draw(batch, "Gotas totales: " + tarro.getPuntos(), 5, 475);
		font.draw(batch, "Vidas : " + tarro.getVidas(), 670, 475);
		font.draw(batch, "HighScore : " + game.getHigherScore(), camera.viewportWidth/2-50, 475);
		
		if (!tarro.estaHerido()) {
	        tarro.actualizarMovimiento();
		}
		
		// Spawneo de gotas
		if(TimeUtils.nanoTime() - lastDropTime > 800000000) spawnCollectible(); 
		
		// --- BUCLE PRINCIPAL DE JUEGO ---
		Iterator<Collectible> iter = collectibles.iterator();
		while (iter.hasNext()) {
			Collectible drop = iter.next();
			drop.update(delta);
			
			// Si cae al suelo
			if(drop.getY() + 64 < 0) { 
				iter.remove();
			}
			// Si colisiona con el tarro
			else if(drop.overlaps(tarro.getArea())) {
                // --- GM2.2: TEMPLATE METHOD ---
                // Llamamos a collect(). La gota misma decide qué sonido hacer 
                // y qué efecto aplicar (vida, puntos, poder).
				drop.collect(this); 
				iter.remove();
			} else {
				drop.draw(batch);
			}
		}
		
		tarro.dibujar(batch);

		batch.end();
		
		// Verificar Game Over
		if (tarro.getVidas() <= 0 && !godModeToggle.isActive()) {
			if (game.getHigherScore() < tarro.getPuntos())
	    		  game.setHigherScore(tarro.getPuntos());  
	    	  
	    	  game.setScreen(new GameOverScreen(game));
	    	  dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	  rainMusic.play();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		rainMusic.stop();
		game.setScreen(new PausaScreen(game, this)); 
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
      tarro.destruir();
      // Los assets se liberan en el Singleton Assets.dispose(), no aquí.
	}
	
	// --- IMPLEMENTACIÓN DE INTERFAZ GAMECONTEXT (GM1.6) ---

	@Override
	public void sumarPuntaje(int puntos) {
		tarro.sumarPuntos(puntos); 
	}

	@Override
	public void restarPuntaje(int puntos) {
		tarro.restarPuntos(puntos);
	}

	@Override
	public void perderVida(int vidas) {
		tarro.dañar();
	}

	@Override
	public void ganarVida(int vidas) {
		tarro.ganarVida(vidas);
	}

	@Override
	public void setShieldActive(boolean active) {
		tarro.setShieldActive(active);
	}

	@Override
	public void cleanScreen() {
		int puntosGanados = 0;
		int topePuntos = 200;
		
		Iterator<Collectible> iter = collectibles.iterator();
		while (iter.hasNext()) {
			Collectible drop = iter.next();
			String type = drop.getTypeId();
			
			// Gota Roja es inmune al clean screen
			if (type.equals("RED_LIFE")) {
				continue;
			}
			
			if (type.equals("BLUE") && puntosGanados < topePuntos) {
				puntosGanados += 10; 
				iter.remove();
			}
			
			if (type.equals("BLACK") || type.equals("GRAY") || type.startsWith("POWER_UP")) {
				iter.remove();
			}
		}
		tarro.sumarPuntos(Math.min(puntosGanados, topePuntos));
	}

	@Override
	public void setGodMode(boolean active) {
		tarro.setGodMode(active);
	}

	@Override
	public void pausarSpawn(boolean pausa) {
		this.spawnPaused = pausa;
	}

	@Override
	public int getVidasActuales() {
		return tarro.getVidas();
	}

	@Override
	public int getPuntajeActual() {
		return tarro.getPuntos();
	}

	@Override
	public int getVidasMaximas() {
		return tarro.getVidasMaximas();
	}
	
	@Override
	public void activatePower(String powerId) {
		if (powerId.equals("SHIELD")) {
			powerManager.addPower(new PowerShield());
		} else if (powerId.equals("CLEAN_SCREEN")) {
			powerManager.addPower(new PowerCleanScreen());
		}
	}
}