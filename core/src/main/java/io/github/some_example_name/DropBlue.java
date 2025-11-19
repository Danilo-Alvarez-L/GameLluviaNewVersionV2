package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound; // Necesario para la variable Sound

/**
 * Gota Azul: +10 puntos.
 * Cubre: GM1.4 (Clase hija concreta)
 * Cubre: GM2.2 (Implementa pasos del Template Method)
 */
public class DropBlue extends Collectible {

    private static final int PUNTOS_GANADOS = 10;

    public DropBlue(Texture texture, float x, float y) {
        super(texture, x, y, "BLUE");
    }

    // --- CAMBIO PARA GM2.2 (TEMPLATE METHOD) ---
    // Ya no usamos onCollected(). Ahora implementamos los pasos específicos.

    @Override
    protected void applyEffect(GameContext ctx) {
        // Paso 1: El efecto específico de la gota azul
        ctx.sumarPuntaje(PUNTOS_GANADOS);
    }

    @Override
    protected void playSound() {
        // Paso 2: El sonido específico. Usamos el Singleton para obtenerlo.
        Assets.getInstance().manager.get("drop.wav", Sound.class).play();
    }
}