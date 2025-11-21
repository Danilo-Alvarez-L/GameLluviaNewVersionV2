package io.github.some_example_name;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;

/**
 * Estrategia de movimiento oscilatorio: Caída en patrón de Zig-Zag.
 */
public class ZigZagStrategy implements MovementStrategy {
    private float time = 0; // Acumulador de tiempo para calcular la oscilación

    @Override
    public void move(Rectangle bounds, float speed, float delta) {
        time += delta;
        
        // Aplica gravedad (movimiento vertical)
        bounds.y -= speed * delta;
        
        // Aplica movimiento lateral usando función seno para crear oscilación
        // Frecuencia: 5, Amplitud: 150
        bounds.x += MathUtils.sin(time * 5) * 150 * delta; 
        
        // Mantiene el objeto dentro de los límites laterales de la pantalla (0 - 800)
        if (bounds.x < 0) bounds.x = 0;
        if (bounds.x > 800 - 64) bounds.x = 800 - 64;
    }
}