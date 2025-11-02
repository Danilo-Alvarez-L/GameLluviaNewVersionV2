package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;

/**
 * Gota Roja: +1 vida (Opcional según plan, pero recomendada).
 * Cubre: GM1.4 (Clase hija concreta)
 */
public class LifeDropRed extends Collectible {

    private static final int VIDAS_GANADAS = 1;

    public LifeDropRed(Texture texture, float x, float y) {
        super(texture, x, y, "RED_LIFE");
    }

    @Override
    public void onCollected(GameContext ctx) {
        // La lógica para no superar el máximo de vidas
        // la manejaremos dentro del método ganarVida() en GameContext/Tarro.
        ctx.ganarVida(VIDAS_GANADAS);
    }
}