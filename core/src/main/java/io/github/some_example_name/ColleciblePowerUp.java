package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;

/**
 * Un Collectible especial que, al ser recogido, activa un Power en el juego.
 * Esto permite que los poderes (Escudo, Limpiar Pantalla) caigan como ítems.
 * Cumple con GM1.4 (es una hija de Collectible).
 */
public class CollectiblePowerUp extends Collectible {

    private String powerId; // "SHIELD" o "CLEAN_SCREEN"

    public CollectiblePowerUp(Texture texture, float x, float y, String powerId) {
        // Le damos un TypeId único para poder identificarlo
        super(texture, x, y, "POWER_UP_" + powerId); 
        this.powerId = powerId;
    }

    /**
     * Al ser recolectado, le dice al GameContext que active el poder
     * que este ítem representa.
     */
    @Override
    public void onCollected(GameContext ctx) {
        // Llama al nuevo método que añadiremos a GameContext
        ctx.activatePower(this.powerId);
    }
}