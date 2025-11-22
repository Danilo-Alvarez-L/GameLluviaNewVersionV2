package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;


public class Tarro {
	   private Rectangle bucket;
	   private Texture bucketImage;
	   private Sound sonidoHerido;
	   private int vidas = 3;
	   private int vidasMaximas = 5; 
	   private int puntos = 0;
	   private int velx = 400;
	   private boolean herido = false;
	   private int tiempoHeridoMax=50;
	   private int tiempoHerido;
	   
	   private Texture shieldTexture; 
	   private boolean shieldActive = false;
	   private boolean godModeActive = false;
	   
	   public Tarro(Texture bucketTex, Sound ss, Texture shieldTex) {
		   this.bucketImage = bucketTex;
		   this.sonidoHerido = ss;
		   this.shieldTexture = shieldTex; 
	   }
	   
		public int getVidas() {
			return vidas;
		}
		
		public int getVidasMaximas() {
			return vidasMaximas;
		}
	
		public int getPuntos() {
			return puntos;
		}
		public Rectangle getArea() {
			return bucket;
		}
		
		public void sumarPuntos(int pp) {
			puntos+=pp;
		}
		
		public void restarPuntos(int pp) {
			puntos -= pp;
			if (puntos < 0) puntos = 0; 
		}
		
		public void ganarVida(int v) {
			if (vidas < vidasMaximas) {
				vidas += v;
			}
		}

	   public void crear() {
		      bucket = new Rectangle();
		      bucket.x = 800 / 2 - 64 / 2;
		      bucket.y = 20;
		      // El tamaño del tarro es 64x64
		      bucket.width = 96;
		      bucket.height = 96;
	   }
	   
	   public void dañar() {
		  if (shieldActive || godModeActive || herido) {
			  return;
		  }
		  
		  vidas--;
		  herido = true;
		  tiempoHerido=tiempoHeridoMax;
		  sonidoHerido.play();
	   }
	   
	   public void setShieldActive(boolean active) {
		   this.shieldActive = active;
	   }
	   
	   public void setGodMode(boolean active) {
		   this.godModeActive = active;
	   }
	   
	   // --- MÉTODO DIBUJAR CORREGIDO ---
	   public void dibujar(SpriteBatch batch) { 
		 if (!herido) {
           // Forzamos el dibujo a 64x64 (bounds.width y bounds.height)
		   batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
		 } else {
           // Forzamos el dibujo a 64x64
		   batch.draw(bucketImage, bucket.x, bucket.y + MathUtils.random(-5,5), bucket.width, bucket.height);
		   tiempoHerido--;
		   if (tiempoHerido<=0) herido = false;
		 }
		 
		 // Dibuja el escudo si está activo
		 if (shieldActive) {
			float scale = 1.6f;
			float shieldWidth  = bucket.width  * scale;
			float shieldHeight = bucket.height * scale;
			float shieldX = bucket.x + bucket.width  / 2f - shieldWidth  / 2f;
			float shieldY = bucket.y + bucket.height / 2f - shieldHeight / 2f;
			batch.draw(shieldTexture, shieldX, shieldY, shieldWidth, shieldHeight);
		 }
	   } 
	   
	   
	   public void actualizarMovimiento() { 
		   if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= velx * Gdx.graphics.getDeltaTime();
		   if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += velx * Gdx.graphics.getDeltaTime();
		   if(bucket.x < 0) bucket.x = 0;
		   if(bucket.x > 800 - 64) bucket.x = 800 - 64;
	   }
	    

	public void destruir() {
        // --- INICIO DE LA MODIFICACIÓN (Singleton GM2.1) ---
		// La textura del 'bucketImage' ya no es propiedad de esta clase,
		// sino del Singleton 'Assets'. Por lo tanto, Tarro NO debe liberarla.
	    // bucketImage.dispose(); // <-- LÍNEA ELIMINADA
        // --- FIN DE LA MODIFICACIÓN ---
	}
	
   public boolean estaHerido() {
	   return herido;
   }
	   
}
