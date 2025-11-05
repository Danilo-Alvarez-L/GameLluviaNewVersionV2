package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Gdx;

/**
 * Clase Abstracta para todos los objetos coleccionables (Gotas).
 * Cubre: GM1.4 (Clase abstracta)
 * Cubre: GM1.6 (Encapsulamiento de campos)
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

    /**
     * GM1.4: Método abstracto que las clases hijas DEBEN implementar.
     */
    public abstract void onCollected(GameContext ctx);

    /**
     * Actualiza la posición de la gota (caída).
     */
    public void update(float delta) {
        bounds.y -= speed * delta;
    }

    /**
     * Dibuja la gota en pantalla.
     * (MODIFICADO para forzar el tamaño 64x64)
     */
    public void draw(SpriteBatch batch) {
        // Le decimos a batch.draw que dibuje la textura (sin importar su tamaño)
        // en la posición (bounds.x, bounds.y) y con el tamaño (bounds.width, bounds.height)
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
    // :p
}