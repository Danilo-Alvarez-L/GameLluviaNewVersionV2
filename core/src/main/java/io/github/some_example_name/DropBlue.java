package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;

/**
 * Gota Azul: +10 puntos.
 * Cubre: GM1.4 (Clase hija concreta)
 */
public class DropBlue extends Collectible {

    private static final int PUNTOS_GANADOS = 10;

    public DropBlue(Texture texture, float x, float y) {
        super(texture, x, y, "BLUE");
    }

    @Override
    public void onCollected(GameContext ctx) {
        ctx.sumarPuntaje(PUNTOS_GANADOS);
    }
}