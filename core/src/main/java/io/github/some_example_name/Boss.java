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
    private long attackInterval = 1500000000L; // 1.5 segundos en nanosegundos

    public Boss(Texture texture) {
        this.texture = texture;
        this.maxHealth = 10; // Vida del Boss
        this.health = maxHealth;
        this.active = false;
        
        // Tamaño del Boss (ajustar según tu sprite, ej. 128x128)
        this.bounds = new Rectangle(800 / 2 - 64, 480 + 100, 128, 128); 
    }

    public void spawn() {
        this.active = true;
        this.health = maxHealth;
        this.bounds.y = 350; // Baja a la pantalla
        this.bounds.x = 800 / 2 - 64;
        // Inicializamos el tiempo para que ataque pronto
        this.lastAttackTime = TimeUtils.nanoTime();
    }

    public void update(float delta) {
        if (!active) return;

        // 1. Movimiento lateral (tipo Space Invaders)
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
        
        // Nota: Eliminamos la llamada interna a attack() que tenías antes
        // porque ahora GameScreen llama a tryToAttack() explícitamente.
    }
    
    /**
     * Intenta realizar un ataque si ha pasado el tiempo suficiente.
     * Devuelve el proyectil (Collectible) si ataca, o null si no.
     */
    public Collectible tryToAttack(DropFactory factory) {
        if (!active) return null;
        
        // Verificamos si pasó el tiempo de intervalo (1.5 segundos)
        if (TimeUtils.nanoTime() - lastAttackTime > attackInterval) {
            lastAttackTime = TimeUtils.nanoTime(); // Reiniciamos el contador
            
            // Dispara gota negra desde el centro del boss
            float dropX = bounds.x + bounds.width / 2 - 24; 
            float dropY = bounds.y - 48;
            
            return factory.createDrop("BLACK", dropX, dropY);
        }
        return null;
    }

    public void draw(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

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