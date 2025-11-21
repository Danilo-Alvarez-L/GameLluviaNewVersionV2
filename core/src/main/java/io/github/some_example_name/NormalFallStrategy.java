package io.github.some_example_name;

import com.badlogic.gdx.math.Rectangle;

/**
 * Estrategia de movimiento estándar: Caída vertical constante.
 */
public class NormalFallStrategy implements MovementStrategy {
    @Override
    public void move(Rectangle bounds, float speed, float delta) {
        // Desplaza el objeto hacia abajo en el eje Y según su velocidad
        bounds.y -= speed * delta;
    }
}