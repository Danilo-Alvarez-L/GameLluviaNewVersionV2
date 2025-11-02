package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;

/**
 * Gota Negra: -1 vida.
 * Cubre: GM1.4 (Clase hija concreta)
 */
public class DropBlack extends Collectible {

    private static final int VIDAS_PERDIDAS = 1;

    public DropBlack(Texture texture, float x, float y) {
        super(texture, x, y, "BLACK");
    }

    @Override
    public void onCollected(GameContext ctx) {
        ctx.perderVida(VIDAS_PERDIDAS);
    }
}