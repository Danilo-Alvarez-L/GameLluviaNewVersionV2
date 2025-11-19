package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

/**
 * Un Collectible especial que, al ser recogido, activa un Power en el juego.
 * Esto permite que los poderes (Escudo, Limpiar Pantalla) caigan como ítems.
 * Cumple con GM1.4 (es una hija de Collectible).
 * Cubre: GM2.2 (Implementa pasos del Template Method)
 */
public class ColleciblePowerUp extends Collectible {

    private String powerId; // "SHIELD" o "CLEAN_SCREEN"

    public ColleciblePowerUp(Texture texture, float x, float y, String powerId) {
        // Le damos un TypeId único para poder identificarlo
        super(texture, x, y, "POWER_UP_" + powerId); 
        this.powerId = powerId;
    }

    // --- CAMBIO PARA GM2.2 (TEMPLATE METHOD) ---

    @Override
    protected void applyEffect(GameContext ctx) {
        // Paso 1: Activa el poder correspondiente en el contexto del juego
        ctx.activatePower(this.powerId);
    }

    @Override
    protected void playSound() {
        // Paso 2: Sonido al recoger el power-up
        Assets.getInstance().manager.get("drop.wav", Sound.class).play();
    }
}