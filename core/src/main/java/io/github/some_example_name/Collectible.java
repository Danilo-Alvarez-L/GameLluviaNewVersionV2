package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Clase Abstracta para todos los objetos coleccionables (Gotas).
 * Cubre: GM1.4 (Clase abstracta)
 * Cubre: GM1.6 (Encapsulamiento de campos)
 * Cubre: GM2.2 (Template Method)
 * Cubre: GM2.3 (Strategy - Composición de estrategia de movimiento)
 */
public abstract class Collectible {

    protected Rectangle bounds;
    protected Texture texture;
    private String typeId;
    private float speed = 200; // Velocidad de caída base
    
    // Referencia a la estrategia de movimiento actual
    private MovementStrategy movementStrategy;

    public Collectible(Texture texture, float x, float y, String typeId) {
        this.texture = texture;
        this.typeId = typeId;
        // Definimos el tamaño de colisión y dibujo en 48x48
        this.bounds = new Rectangle(x, y, 48, 48); 
        
        // Asignamos la estrategia de movimiento por defecto (Caída normal)
        this.movementStrategy = new NormalFallStrategy();
    }

    /**
     * Permite cambiar la estrategia de movimiento en tiempo de ejecución.
     * @param strategy La nueva estrategia de movimiento a aplicar.
     */
    public void setMovementStrategy(MovementStrategy strategy) {
        this.movementStrategy = strategy;
    }

    // --- PATRÓN TEMPLATE METHOD (GM2.2) ---
    
    /**
     * Método Plantilla (Template Method).
     * Define el esqueleto del algoritmo de recolección.
     */
    public final void collect(GameContext ctx) {
        // Paso 1: Aplicar el efecto (Score, Vida, Poder)
        applyEffect(ctx);
        
        // Paso 2: Reproducir el sonido característico
        playSound();
    }

    /**
     * Operación Primitiva 1: Efecto específico de la gota.
     */
    protected abstract void applyEffect(GameContext ctx);

    /**
     * Operación Primitiva 2: Sonido específico de la gota.
     */
    protected abstract void playSound();

    // ---------------------------------------

    /**
     * Actualiza la posición de la gota delegando el cálculo a la estrategia de movimiento.
     */
    public void update(float delta) {
        if (movementStrategy != null) {
            movementStrategy.move(bounds, speed, delta);
        }
    }

    /**
     * Dibuja la gota en pantalla.
     */
    public void draw(SpriteBatch batch) {
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