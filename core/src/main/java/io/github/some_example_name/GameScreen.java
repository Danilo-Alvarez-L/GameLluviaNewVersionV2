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
    private GameLevelFactory levelFactory; 
	private PowerManager powerManager;
	private GodModeToggle godModeToggle;
	
	// --- AUDIO E IMAGENES ---
	private Music rainMusic;
	private Sound hurtSound; 
    
    // NUEVO: Fondo del juego
    private Texture background;

	// --- PUNTUACIÓN Y BOSS ---
	private int nextPowerUpScore = 100;
	private Boss boss;
	private boolean bossDefeated = false;
	private long timeBossDefeated = 0;

	public GameScreen(final GameLluviaNewVersionV2 game, GameLevelFactory factory) {
		this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
		  
        // Cargar recursos
        rainMusic = Assets.getInstance().manager.get("rain.mp3", Music.class);
        hurtSound = Assets.getInstance().manager.get("hurt.ogg", Sound.class);
        
        // NUEVO: Cargar fondo del juego
        // Usamos isLoaded por seguridad
        if (Assets.getInstance().manager.isLoaded("game_bg.png")) {
            background = Assets.getInstance().manager.get("game_bg.png", Texture.class);
        }
        
        this.levelFactory = factory;
        
        Texture bucketTexture = Assets.getInstance().manager.get("bucket.png", Texture.class);
        Texture shieldTexture = Assets.getInstance().manager.get("power_shield_effect.png", Texture.class);
        tarro = new Tarro(bucketTexture, hurtSound, shieldTexture);
        
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
	
	// ... (spawnCollectible, spawnPowerUp, checkInput, checkGameEvents siguen igual) ...
	private void spawnCollectible() {
		if (spawnPaused) return;
		if (boss.isActive()) {
			if (MathUtils.random(1, 100) > 10) return; 
		}
		float x = MathUtils.random(0, 800-64);
		float y = 480;
		Collectible newDrop = levelFactory.createEnemy(x, y);
		if (newDrop != null) collectibles.add(newDrop);
		lastDropTime = TimeUtils.nanoTime();
	}
	
	private void spawnPowerUp() {
		float x = MathUtils.random(0, 800-64);
		float y = 480;
		Collectible powerUp = levelFactory.createPowerUp(x, y);
		if (powerUp != null) collectibles.add(powerUp);
	}
	
	private void checkInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) godModeToggle.toggle();
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) powerManager.addPower(new PowerShield());
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) powerManager.addPower(new PowerCleanScreen());
		if (Gdx.input.isKeyJustPressed(Input.Keys.B) && !boss.isActive()) boss.spawn();
	}
	
	private void checkGameEvents() {
		if (tarro.getPuntos() >= nextPowerUpScore) {
			spawnPowerUp();
			nextPowerUpScore += 500; 
		}
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
		
		if (boss.isActive()) {
			boss.update(delta); 
			Collectible projectile = boss.tryToAttack(levelFactory);
			if (projectile != null) collectibles.add(projectile);
		}
		
		batch.begin();
		
		// --- NUEVO: DIBUJAR FONDO PRIMERO ---
		// Se dibuja antes que el texto y los objetos
		if (background != null) {
            batch.draw(background, 0, 0, 800, 480);
        }
		
		font.draw(batch, "Puntos: " + tarro.getPuntos(), 5, 475);
		font.draw(batch, "Vidas: " + tarro.getVidas(), 670, 475);
		font.draw(batch, "HighScore: " + game.getHigherScore(), camera.viewportWidth/2-50, 475);
		
		if (boss.isActive()) {
			font.setColor(1, 0, 0, 1); 
			font.draw(batch, "REY TORMENTA HP: " + boss.getHealth(), camera.viewportWidth/2 - 60, 450);
			font.setColor(1, 1, 1, 1); 
		}
		
		if (!tarro.estaHerido()) {
	        tarro.actualizarMovimiento();
		}
		
		if(TimeUtils.nanoTime() - lastDropTime > 800000000) spawnCollectible(); 
		
		Iterator<Collectible> iter = collectibles.iterator();
		while (iter.hasNext()) {
			Collectible drop = iter.next();
			drop.update(delta);
			
			if(drop.getY() + 64 < 0) { 
				iter.remove();
			}
			else if(drop.overlaps(tarro.getArea())) {
				if (boss.isActive() && powerManager.isShieldActive() && drop.getTypeId().equals("BOSS_PROJECTILE")) {
					boss.takeDamage(1);
					Assets.getInstance().manager.get("drop.wav", Sound.class).play();
					iter.remove();
					
					if (!boss.isActive()) {
						bossDefeated = true;
						tarro.sumarPuntos(1000); 
						cleanScreen(); 
                        rainMusic.stop(); 
                        Assets.getInstance().manager.get("boss_death.mp3", Sound.class).play();
						timeBossDefeated = TimeUtils.nanoTime();
						break; 
					}
				} else {
					drop.collect(this); 
					iter.remove();
				}
			} else {
				drop.draw(batch);
			}
		}
		
		tarro.dibujar(batch);
		if (boss.isActive()) {
			boss.draw(batch);
		}

		batch.end();
		
		if (bossDefeated) {
			if (TimeUtils.nanoTime() - timeBossDefeated > 3000000000L) {
				rainMusic.stop(); 
				game.setScreen(new VictoryScreen(game, levelFactory));
				dispose(); 
			}
		}
		
		if (tarro.getVidas() <= 0 && !godModeToggle.isActive()) {
			if (game.getHigherScore() < tarro.getPuntos())
	    		  game.setHigherScore(tarro.getPuntos());  
	    	  game.setScreen(new GameOverScreen(game, levelFactory));
	    	  dispose();
		}
	}

	// ... (resize, show, hide, pause, resume, dispose e implementaciones de GameContext igual que antes) ...
	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = 800;
		camera.viewportHeight = 480;
		camera.update();
	}

	@Override
	public void show() {
	  rainMusic.play();
	}

	@Override
	public void hide() {}

	@Override
	public void pause() {
		rainMusic.stop();
		game.setScreen(new PausaScreen(game, this, levelFactory)); 
	}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
      tarro.destruir();
	}
	
	@Override public void sumarPuntaje(int puntos) { tarro.sumarPuntos(puntos); }
	@Override public void restarPuntaje(int puntos) { tarro.restarPuntos(puntos); }
	@Override public void perderVida(int vidas) { tarro.dañar(); }
	@Override public void ganarVida(int vidas) { tarro.ganarVida(vidas); }
	@Override public void setShieldActive(boolean active) { tarro.setShieldActive(active); }
	
	@Override
	public void cleanScreen() {
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
			if (type.equals("BLACK") || type.equals("GRAY") || type.startsWith("POWER_UP") || type.equals("BOSS_PROJECTILE")) {
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