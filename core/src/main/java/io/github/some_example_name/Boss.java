package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class Boss {
    private Rectangle bounds;
    private Texture texture;
    private int health;
    private int maxHealth;
    private boolean active;
    private float speed = 150f;
    private int direction = 1; // 1 derecha, -1 izquierda

    // Temporizador para ataques
    private long lastAttackTime;
    private long attackInterval = 1500000000L; // 1.5 segundos

    public Boss(Texture texture) {
        this.texture = texture;
        this.maxHealth = 1; // Puedes subirle la vida si quieres que dure más
        this.health = maxHealth;
        this.active = false;
        
        // Tamaño del Boss (128x128)
        this.bounds = new Rectangle(800 / 2 - 64, 480 + 100, 128, 128); 
    }

    public void spawn() {
        this.active = true;
        this.health = maxHealth;
        this.bounds.y = 350; // Altura a la que aparece en pantalla
        this.bounds.x = 800 / 2 - 64;
        this.lastAttackTime = TimeUtils.nanoTime();
    }

    public void update(float delta) {
        if (!active) return;

        // Movimiento lateral
        bounds.x += speed * direction * delta;
        
        // Rebotar en los bordes
        if (bounds.x < 0) {
            bounds.x = 0;
            direction = 1;
        }
        if (bounds.x > 800 - bounds.width) {
            bounds.x = 800 - bounds.width;
            direction = -1;
        }
    }
    
    public Collectible tryToAttack(DropFactory factory) {
        if (!active) return null;
        
        // Verificamos el tiempo de ataque
        if (TimeUtils.nanoTime() - lastAttackTime > attackInterval) {
            lastAttackTime = TimeUtils.nanoTime();
            
            // El disparo sale del centro del Boss
            float dropX = bounds.x + bounds.width / 2; 
            float dropY = bounds.y;
            
            // Crea el rayo (BOSS_PROJECTILE)
            return factory.createDrop("BOSS_PROJECTILE", dropX, dropY);
        }
        return null;
    }

    // --- AQUÍ ESTÁ EL CAMBIO ---
    public void draw(SpriteBatch batch) {
        if (active) {
            // Usamos la versión avanzada de draw para rotar la imagen
            batch.draw(texture, 
                bounds.x, bounds.y,             // Posición X, Y
                bounds.width / 2, bounds.height / 2, // Punto de rotación (Centro de la imagen)
                bounds.width, bounds.height,    // Ancho y Alto
                1, 1,                           // Escala (1 = tamaño normal)
                180,                            // ROTACIÓN: 180 grados (Boca abajo)
                0, 0,                           // Coordenadas de textura (inicio)
                texture.getWidth(), texture.getHeight(), // Coordenadas de textura (fin)
                false, false);                  // No invertir horizontal/verticalmente
        }
    }
    // ---------------------------

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            active = false; // Boss derrotado
        }
    }

    public boolean isActive() {
        return active;
    }

    public Rectangle getBounds() {
        return bounds;
    }
    
    public int getHealth() {
        return health;
    }
}