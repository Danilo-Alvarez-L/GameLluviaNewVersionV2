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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator; 

// GM1.6: GameScreen implementa la interfaz de contexto para bajo acoplamiento
public class GameScreen implements Screen, GameContext {
	
	final GameLluviaNewVersionV2 game;
    private OrthographicCamera camera;
	private SpriteBatch batch;	   
	private BitmapFont font;
	private Tarro tarro;
	
	// --- LÓGICA DE COLLECTIBLE (reemplaza a Lluvia.java) ---
	private Array<Collectible> collectibles;
    private long lastDropTime;
	
    // Texturas existentes
    private Texture dropTexture; // Gota azul
    private Texture dropBadTexture; // Gota negra
	
	// Texturas NUEVAS
	private Texture dropGrayTexture;
	private Texture dropRedTexture;
	private Texture powerCleanPickupTexture;
	private Texture powerShieldPickupTexture;
	private Texture effectShieldTexture;
	
	private Sound dropSound;
	private Sound hurtSound; // <-- MODIFICADO (Movido aquí para usarlo en toda la clase)
	private Sound grayDropSound; // <-- MODIFICADO (Declarado el nuevo sonido)
	private Music rainMusic;
	private boolean spawnPaused = false;
	// -----------------------------------------------------------------
	
	// --- SISTEMAS AÑADIDOS PARA GM1.5 y GM1.6 ---
	private PowerManager powerManager;
	private GodModeToggle godModeToggle;
	
	// --- LÓGICA DE PUNTUACIÓN (Petición 3) ---
	private int nextPowerUpScore = 500;
	// -------------------------------------------------

	public GameScreen(final GameLluviaNewVersionV2 game) {
		this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
		  
	      // Cargar texturas existentes
         dropTexture = new Texture(Gdx.files.internal("drop.png"));
         dropBadTexture = new Texture(Gdx.files.internal("dropBad.png"));
		 
		 // Cargar las nuevas texturas
         dropGrayTexture = new Texture(Gdx.files.internal("drop_gray.png"));
		 dropRedTexture = new Texture(Gdx.files.internal("drop_red_life.png")); 
		 powerCleanPickupTexture = new Texture(Gdx.files.internal("power_clean_screen.png"));
		 effectShieldTexture = new Texture(Gdx.files.internal("power_shield_effect.png"));
		 powerShieldPickupTexture = new Texture(Gdx.files.internal("drop_shield.png"));
		 
         dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
	     rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		  hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")); // <-- MODIFICADO (Se asigna a la variable de la clase)
		  
		  // --- ¡AQUÍ ESTÁ TU NUEVO SONIDO! ---
		  grayDropSound = Gdx.audio.newSound(Gdx.files.internal("soundgray.wav")); // <-- MODIFICADO
		  // ------------------------------------
		  
		  // Le pasamos la textura del efecto de escudo al Tarro
		  tarro = new Tarro(new Texture(Gdx.files.internal("bucket.png")), hurtSound, effectShieldTexture);
         
		  // Inicializar sistemas de GM1.4, GM1.5, GM1.6
		  collectibles = new Array<>();
		  powerManager = new PowerManager(this); // GM1.6 (Composición)
		  godModeToggle = new GodModeToggle(this); // GM1.6
	      
	      // camera
	      camera = new OrthographicCamera();
	      camera.setToOrtho(false, 800, 480);
	      
	      tarro.crear();
	      spawnCollectible(); // Crear la primera gota
	      rainMusic.setVolume(0.5f);
		  rainMusic.setLooping(true);
	      rainMusic.play();
	}
	
	/**
	 * Crea un nuevo collectible (gota) y lo añade a la lista.
	 */
	private void spawnCollectible() {
		if (spawnPaused) return;

		float x = MathUtils.random(0, 800-64);
		float y = 480;
		
		Collectible newDrop = null;
		int type = MathUtils.random(1, 100); // Rango 1-100 para probabilidad
		
		if (type <= 40) { // 40% Gota Azul
			newDrop = new DropBlue(dropTexture, x, y);
		} else if (type <= 70) { // 30% Gota Negra
			newDrop = new DropBlack(dropBadTexture, x, y);
		} else if (type <= 90) { // 20% Gota Gris
			newDrop = new DropGray(dropGrayTexture, x, y); 
		} else { // 10% Gota Roja (Vida)
			newDrop = new LifeDropRed(dropRedTexture, x, y);
		}
		
		if (newDrop != null) {
			collectibles.add(newDrop);
		}
		lastDropTime = TimeUtils.nanoTime();
	}
	
	/**
	 * Spawnea un Power-Up aleatorio
	 */
	private void spawnPowerUp() {
		float x = MathUtils.random(0, 800-64);
		float y = 480;
		Collectible powerUp;

		// 50/50 chance para cualquiera de los dos poderes
		if (MathUtils.randomBoolean()) {
			powerUp = new ColleciblePowerUp(powerShieldPickupTexture, x, y, "SHIELD");
		} else {
			powerUp = new ColleciblePowerUp(powerCleanPickupTexture, x, y, "CLEAN_SCREEN");
		}
		collectibles.add(powerUp);
	}
	
	/**
	 * Revisa input del teclado para testing (GodMode, Poderes)
	 */
	private void checkInput() {
		// Control de GodMode (GM1.6) - Tecla "G"
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
			godModeToggle.toggle();
		}
		
		// Control de Power-ups (Ejemplo para probar)
		// Tecla "S" para Shield
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			powerManager.addPower(new PowerShield());
		}
		// Tecla "C" para CleanScreen
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			powerManager.addPower(new PowerCleanScreen());
		}
	}
	
	/**
	 * Revisa si el puntaje alcanzó el hito para un power-up
	 */
	private void checkPowerUpSpawn() {
		if (tarro.getPuntos() >= nextPowerUpScore) {
			spawnPowerUp();
			nextPowerUpScore += 500; // Define el siguiente hito (1000, 1500, ...)
		}
	}

	@Override
	public void render(float delta) {
		//limpia la pantalla con color azul obscuro.
		ScreenUtils.clear(0, 0, 0.2f, 1);
		//actualizar matrices de la cámara
		camera.update();
		//actualizar 
		batch.setProjectionMatrix(camera.combined);
		
		// Revisar input de teclado
		checkInput();
		
		// Revisar si debemos spawnear un power-up
		checkPowerUpSpawn();
		
		// Actualizar PowerManager (GM1.5 / GM1.6)
		// ¡IMPORTANTE! Esto AHORA aplica los poderes pendientes
		powerManager.update(delta);
		
		batch.begin();
		
		//dibujar textos
		font.draw(batch, "Gotas totales: " + tarro.getPuntos(), 5, 475);
		font.draw(batch, "Vidas : " + tarro.getVidas(), 670, 475);
		font.draw(batch, "HighScore : " + game.getHigherScore(), camera.viewportWidth/2-50, 475);
		
		if (!tarro.estaHerido()) {
			// movimiento del tarro desde teclado
	        tarro.actualizarMovimiento();
		}
		
		// Spawneo de collectibles (gotas)
		// Frecuencia aumentada a 0.8s
		if(TimeUtils.nanoTime() - lastDropTime > 800000000) spawnCollectible(); 
		
		// Actualizar y dibujar collectibles (GM1.4)
		Iterator<Collectible> iter = collectibles.iterator();
		while (iter.hasNext()) {
			Collectible drop = iter.next();
			drop.update(delta); // Mover la gota
			
			// Si la gota cae al suelo (se sale de la pantalla por abajo)
			if(drop.getY() + 64 < 0) { 
				iter.remove();
			}
			
			// Si la gota colisiona con el tarro
			if(drop.overlaps(tarro.getArea())) {
				drop.onCollected(this); // GM1.4 y GM1.6 (Polimorfismo y Contexto)
				
				// --- ¡BLOQUE DE SONIDO MODIFICADO! ---
				if (drop.getTypeId().equals("BLUE") || drop.getTypeId().equals("RED_LIFE") || drop.getTypeId().startsWith("POWER_UP")) {
					dropSound.play();
				}
				else if (drop.getTypeId().equals("GRAY")) { // <-- MODIFICADO
					grayDropSound.play(); // ¡Tu nuevo sonido!
				}
				else if (drop.getTypeId().equals("BLACK")) { // <-- MODIFICADO
					hurtSound.play(); // Sonido de daño
				}
				// --- FIN DEL BLOQUE ---
				
				iter.remove();
			} else {
				// Solo dibujamos la gota si no fue recolectada
				drop.draw(batch);
			}
		}
		
		// Dibujar tarro (Ahora Tarro.java se encarga de dibujar el escudo si está activo)
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
      dropSound.dispose();
	  hurtSound.dispose(); // <-- MODIFICADO (Añadido)
	  grayDropSound.dispose(); // <-- MODIFICADO (Añadido)
      rainMusic.dispose();
	  dropTexture.dispose();
	  dropBadTexture.dispose();
	  
	  // Disponer los nuevos assets
	  dropGrayTexture.dispose();
	  dropRedTexture.dispose();
	  powerCleanPickupTexture.dispose();
	  powerShieldPickupTexture.dispose();
	  effectShieldTexture.dispose();
	}
	
	// --- IMPLEMENTACIÓN DE INTERFAZ GAMECONTEXT (GM1.6) ---

	@Override
	public void sumarPuntaje(int puntos) {
		// La interfaz usa 'sumarPuntaje' (con J)
		// La clase Tarro usa 'sumarPuntos' (con S)
		// Este método sirve como "traductor"
		tarro.sumarPuntos(puntos); // <-- CORREGIDO A 'S'
	}

	@Override
	public void restarPuntaje(int puntos) {
		tarro.restarPuntos(puntos); // (Asegúrate que 'restarPuntos' exista en Tarro)
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

	/**
	 * Lógica de CleanScreen (GM1.5)
	 */
	@Override
	public void cleanScreen() {
		int puntosGanados = 0;
		int topePuntos = 200;
		
		Iterator<Collectible> iter = collectibles.iterator();
		while (iter.hasNext()) {
			Collectible drop = iter.next();
			String type = drop.getTypeId();
			
			// Gota Roja es inmune
			if (type.equals("RED_LIFE")) {
				continue;
			}
			
			// Sumar puntos de gotas azules (con tope)
			if (type.equals("BLUE") && puntosGanados < topePuntos) {
				puntosGanados += 10; 
				iter.remove();
			}
			
			// Eliminar gotas malas (negras y grises)
			if (type.equals("BLACK") || type.equals("GRAY")) {
				iter.remove();
			}
			
			// Eliminar ítems de Power-Up de la pantalla
			if (type.startsWith("POWER_UP")) {
				iter.remove();
			}
		}
		
		// --- ¡AQUÍ ESTÁ LA LÍNEA CORREGIDA! ---
		// Debe ser 'sumarPuntos' (con S) para coincidir con Tarro.java
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
		// GameScreen recibe la señal del CollectiblePowerUp
		// y le dice al PowerManager qué poder activar.
		if (powerId.equals("SHIELD")) {
			powerManager.addPower(new PowerShield());
		} else if (powerId.equals("CLEAN_SCREEN")) {
			powerManager.addPower(new PowerCleanScreen());
		}
	}
}