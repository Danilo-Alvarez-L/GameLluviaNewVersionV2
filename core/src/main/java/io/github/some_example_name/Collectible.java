package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Clase Abstracta para todos los objetos coleccionables (Gotas).
 * Cubre: GM1.4 (Clase abstracta)
 * Cubre: GM1.6 (Encapsulamiento de campos)
 * Cubre: GM2.2 (Template Method)
 */
public abstract class Collectible {

    protected Rectangle bounds;
    protected Texture texture;
    private String typeId;
    private float speed = 200; // Velocidad de caída base

    public Collectible(Texture texture, float x, float y, String typeId) {
        this.texture = texture;
        this.typeId = typeId;
        // Definimos el tamaño de colisión y dibujo en 64x64
        this.bounds = new Rectangle(x, y, 48, 48); 
    }

    // --- PATRÓN TEMPLATE METHOD (GM2.2) ---
    
    /**
     * Método Plantilla (Template Method).
     * Define el esqueleto del algoritmo de recolección.
     * Es 'final' para que las subclases no puedan cambiar el orden de los pasos.
     */
    public final void collect(GameContext ctx) {
        // Paso 1: Aplicar el efecto (Score, Vida, Poder)
        applyEffect(ctx);
        
        // Paso 2: Reproducir el sonido característico
        playSound();
    }

    /**
     * Operación Primitiva 1: Efecto específico de la gota.
     * (Este reemplaza al antiguo onCollected)
     */
    protected abstract void applyEffect(GameContext ctx);

    /**
     * Operación Primitiva 2: Sonido específico de la gota.
     */
    protected abstract void playSound();

    // ---------------------------------------

    /**
     * Actualiza la posición de la gota (caída).
     */
    public void update(float delta) {
        bounds.y -= speed * delta;
    }

    /**
     * Dibuja la gota en pantalla.
     */
    public void draw(SpriteBatch batch) {
        // Dibujamos con el tamaño definido en bounds
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    // --- Getters y Setters (Encapsulamiento GM1.6) ---

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean overlaps(Rectangle other) {
        return bounds.overlaps(other);
    }

    public String getTypeId() {
        return typeId;
    }
    
    public float getY() {
        return bounds.y;
    }
}