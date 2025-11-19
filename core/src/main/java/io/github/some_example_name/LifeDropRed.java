package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

/**
 * Gota Roja: +1 vida (Opcional según plan, pero recomendada).
 * Cubre: GM1.4 (Clase hija concreta)
 * Cubre: GM2.2 (Implementa pasos del Template Method)
 */
public class LifeDropRed extends Collectible {

    private static final int VIDAS_GANADAS = 1;

    public LifeDropRed(Texture texture, float x, float y) {
        super(texture, x, y, "RED_LIFE");
    }

    // --- CAMBIO PARA GM2.2 (TEMPLATE METHOD) ---

    @Override
    protected void applyEffect(GameContext ctx) {
        // Paso 1: Efecto de ganar vida
        ctx.ganarVida(VIDAS_GANADAS);
    }

    @Override
    protected void playSound() {
        // Paso 2: Sonido normal de recolección (drop.wav)
        Assets.getInstance().manager.get("drop.wav", Sound.class).play();
    }
}