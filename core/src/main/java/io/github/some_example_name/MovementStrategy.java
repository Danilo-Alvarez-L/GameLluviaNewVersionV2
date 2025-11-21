package io.github.some_example_name;

import com.badlogic.gdx.math.Rectangle;

/**
 * Interfaz que define la estrategia de movimiento para los objetos coleccionables.
 * Patrón de diseño: Strategy (GM2.3)
 */
public interface MovementStrategy {
    /**
     * Aplica la lógica de movimiento a un objeto.
     * @param bounds El rectángulo que define la posición y tamaño del objeto.
     * @param speed La velocidad base del objeto.
     * @param delta El tiempo transcurrido desde el último frame.
     */
    void move(Rectangle bounds, float speed, float delta);
}