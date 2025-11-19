package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

/**
 * Gota Gris: -10 puntos (Modificado).
 * Cubre: GM1.4 (Clase hija concreta)
 * Cubre: GM2.2 (Implementa pasos del Template Method)
 */
public class DropGray extends Collectible {

    // CAMBIO: Ahora quita 10 puntos en lugar de 70
    private static final int PUNTOS_PERDIDOS = 10;

    public DropGray(Texture texture, float x, float y) {
        super(texture, x, y, "GRAY");
    }

    // --- CAMBIO PARA GM2.2 (TEMPLATE METHOD) ---

    @Override
    protected void applyEffect(GameContext ctx) {
        // Paso 1: El efecto específico (restar puntos)
        ctx.restarPuntaje(PUNTOS_PERDIDOS);
    }

    @Override
    protected void playSound() {
        // Paso 2: El sonido específico (soundgray.wav)
        Assets.getInstance().manager.get("soundgray.wav", Sound.class).play();
    }
}