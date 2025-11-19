package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

/**
 * Gota Negra: -1 vida.
 * Cubre: GM1.4 (Clase hija concreta)
 * Cubre: GM2.2 (Implementa pasos del Template Method)
 */
public class DropBlack extends Collectible {

    private static final int VIDAS_PERDIDAS = 1;

    public DropBlack(Texture texture, float x, float y) {
        super(texture, x, y, "BLACK");
    }

    // --- CAMBIO PARA GM2.2 (TEMPLATE METHOD) ---

    @Override
    protected void applyEffect(GameContext ctx) {
        // Paso 1: El efecto específico (perder vida)
        ctx.perderVida(VIDAS_PERDIDAS);
    }

    @Override
    protected void playSound() {
        // Paso 2: El sonido específico (hurt.ogg)
        Assets.getInstance().manager.get("hurt.ogg", Sound.class).play();
    }
}