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
    private DropFactory dropFactory;
	private PowerManager powerManager;
	private GodModeToggle godModeToggle;
	
	// --- AUDIO ---
	private Music rainMusic;
	private Sound hurtSound; 

	// --- PUNTUACIÓN Y BOSS ---
	private int nextPowerUpScore = 500;
	private Boss boss;
	private boolean bossDefeated = false;

	public GameScreen(final GameLluviaNewVersionV2 game) {
		this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
		  
        // Cargar recursos desde el Singleton
        rainMusic = Assets.getInstance().manager.get("rain.mp3", Music.class);
        hurtSound = Assets.getInstance().manager.get("hurt.ogg", Sound.class);
        
        // Inicializar Fábrica y Managers
        dropFactory = new DropFactory();
        
        Texture bucketTexture = Assets.getInstance().manager.get("bucket.png", Texture.class);
        Texture shieldTexture = Assets.getInstance().manager.get("power_shield_effect.png", Texture.class);
        tarro = new Tarro(bucketTexture, hurtSound, shieldTexture);
        
        // Inicializar Boss con su textura
        Texture bossTexture = Assets.getInstance().manager.get("boss.png", Texture.class);
        boss = new Boss(bossTexture);
         
		collectibles = new Array<>();
		powerManager = new PowerManager(this);
		godModeToggle = new GodModeToggle(this);
	      
	    camera = new OrthographicCamera();
	    camera.setToOrtho(false, 800, 480);
	      
	    tarro.crear();
	    
	    rainMusic.setVolume(0.5f);
		rainMusic.setLooping(true);
	    rainMusic.play();
	      
	    spawnCollectible(); 
	}
	
	private void spawnCollectible() {
		if (spawnPaused) return;
		
		// Si el boss está activo, reducimos la lluvia normal para centrarse en el combate
		if (boss.isActive()) {
			if (MathUtils.random(1, 100) > 10) return; 
		}

		float x = MathUtils.random(0, 800-64);
		float y = 480;
		
		Collectible newDrop = null;
		int type = MathUtils.random(1, 100); 
		
		if (type <= 40) newDrop = dropFactory.createDrop("BLUE", x, y);
		else if (type <= 70) newDrop = dropFactory.createDrop("BLACK", x, y);
		else if (type <= 90) newDrop = dropFactory.createDrop("GRAY", x, y); 
		else newDrop = dropFactory.createDrop("RED", x, y);
		
		if (newDrop != null) collectibles.add(newDrop);
		lastDropTime = TimeUtils.nanoTime();
	}
	
	private void spawnPowerUp() {
		float x = MathUtils.random(0, 800-64);
		float y = 480;
		Collectible powerUp;

		if (MathUtils.randomBoolean()) powerUp = dropFactory.createDrop("POWER_SHIELD", x, y);
		else powerUp = dropFactory.createDrop("POWER_CLEAN", x, y);
		
		if (powerUp != null) collectibles.add(powerUp);
	}
	
	private void checkInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) godModeToggle.toggle();
		// Debug cheats
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) powerManager.addPower(new PowerShield());
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) powerManager.addPower(new PowerCleanScreen());
		// Cheat para invocar al boss inmediatamente
		if (Gdx.input.isKeyJustPressed(Input.Keys.B) && !boss.isActive()) boss.spawn();
	}
	
	private void checkGameEvents() {
		// Spawneo de PowerUps
		if (tarro.getPuntos() >= nextPowerUpScore) {
			spawnPowerUp();
			nextPowerUpScore += 500; 
		}
		
		// INVOCAR BOSS: A los 200 puntos
		if (tarro.getPuntos() >= 200 && !boss.isActive() && !bossDefeated) {
			boss.spawn();
		}
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		checkInput();
		checkGameEvents();
		powerManager.update(delta);
		
		// Lógica del Boss: Movimiento y Ataque
		if (boss.isActive()) {
			// 1. Actualizar movimiento del Boss
			boss.update(delta); 
			
			// 2. Intentar atacar (si ha pasado el tiempo)
			// Esto devuelve un objeto Collectible (gota negra) si ataca, o null si no.
			Collectible projectile = boss.tryToAttack(dropFactory);
			
			if (projectile != null) {
				// ¡Importante! Añadimos el proyectil a la lista del juego para que se dibuje y mueva
				collectibles.add(projectile);
			}
		}
		
		batch.begin();
		
		font.draw(batch, "Puntos: " + tarro.getPuntos(), 5, 475);
		font.draw(batch, "Vidas: " + tarro.getVidas(), 670, 475);
		font.draw(batch, "HighScore: " + game.getHigherScore(), camera.viewportWidth/2-50, 475);
		
		// Dibujar vida del Boss si está activo
		if (boss.isActive()) {
			font.setColor(1, 0, 0, 1); // Rojo para el boss
			font.draw(batch, "REY TORMENTA HP: " + boss.getHealth(), camera.viewportWidth/2 - 60, 450);
			font.setColor(1, 1, 1, 1); // Blanco normal
		}
		
		if (!tarro.estaHerido()) {
	        tarro.actualizarMovimiento();
		}
		
		if(TimeUtils.nanoTime() - lastDropTime > 800000000) spawnCollectible(); 
		
		// Bucle de colisiones y lógica de gotas
		Iterator<Collectible> iter = collectibles.iterator();
		while (iter.hasNext()) {
			Collectible drop = iter.next();
			drop.update(delta);
			
			if(drop.getY() + 64 < 0) { 
				iter.remove();
			}
			else if(drop.overlaps(tarro.getArea())) {
				// --- MECÁNICA ESPECIAL: REFLEJAR ATAQUE ---
				// Si es una gota negra (ataque) y tenemos ESCUDO activo...
				if (boss.isActive() && powerManager.isShieldActive() && drop.getTypeId().equals("BLACK")) {
					// Reflejamos el daño al Boss
					boss.takeDamage(1);
					
					// Usamos un sonido para el rebote (reutilizamos el de la gota buena)
					Assets.getInstance().manager.get("drop.wav", Sound.class).play();
					iter.remove();
					
					// Si el Boss muere
					if (!boss.isActive()) {
						bossDefeated = true;
						tarro.sumarPuntos(1000); // Bonus enorme
						cleanScreen(); // Limpiar proyectiles restantes
					}
				} else {
					// Comportamiento normal (Template Method)
					drop.collect(this); 
					iter.remove();
				}
			} else {
				drop.draw(batch);
			}
		}
		
		// Dibujar entidades
		tarro.dibujar(batch);
		if (boss.isActive()) {
			boss.draw(batch);
		}

		batch.end();
		
		if (tarro.getVidas() <= 0 && !godModeToggle.isActive()) {
			if (game.getHigherScore() < tarro.getPuntos())
	    		  game.setHigherScore(tarro.getPuntos());  
	    	  
	    	  game.setScreen(new GameOverScreen(game));
	    	  dispose();
		}
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {
	  rainMusic.play();
	}

	@Override
	public void hide() {}

	@Override
	public void pause() {
		rainMusic.stop();
		game.setScreen(new PausaScreen(game, this)); 
	}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
      tarro.destruir();
	}
	
	// --- Implementación GameContext ---
	@Override public void sumarPuntaje(int puntos) { tarro.sumarPuntos(puntos); }
	@Override public void restarPuntaje(int puntos) { tarro.restarPuntos(puntos); }
	@Override public void perderVida(int vidas) { tarro.dañar(); }
	@Override public void ganarVida(int vidas) { tarro.ganarVida(vidas); }
	@Override public void setShieldActive(boolean active) { tarro.setShieldActive(active); }
	
	@Override
	public void cleanScreen() {
		// Limpieza de pantalla (igual que antes)
		int puntosGanados = 0;
		int topePuntos = 200;
		Iterator<Collectible> iter = collectibles.iterator();
		while (iter.hasNext()) {
			Collectible drop = iter.next();
			String type = drop.getTypeId();
			if (type.equals("RED_LIFE")) continue;
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

	@Override public void setGodMode(boolean active) { tarro.setGodMode(active); }
	@Override public void pausarSpawn(boolean pausa) { this.spawnPaused = pausa; }
	@Override public int getVidasActuales() { return tarro.getVidas(); }
	@Override public int getPuntajeActual() { return tarro.getPuntos(); }
	@Override public int getVidasMaximas() { return tarro.getVidasMaximas(); }
	
	@Override
	public void activatePower(String powerId) {
		if (powerId.equals("SHIELD")) powerManager.addPower(new PowerShield());
		else if (powerId.equals("CLEAN_SCREEN")) powerManager.addPower(new PowerCleanScreen());
	}
}