package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;

/**
 * Gota Gris: -70 puntos.
 * Cubre: GM1.4 (Clase hija concreta)
 */
public class DropGray extends Collectible {

    private static final int PUNTOS_PERDIDOS = 70;

    public DropGray(Texture texture, float x, float y) {
        super(texture, x, y, "GRAY");
    }

    @Override
    public void onCollected(GameContext ctx) {
        ctx.restarPuntaje(PUNTOS_PERDIDOS);
    }
}