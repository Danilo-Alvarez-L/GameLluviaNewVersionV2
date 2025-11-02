package io.github.some_example_name;

/**
 * Interfaz de desacoplamiento (Puente) para cumplir GM1.6.
 * Permite que Collectible y Power interactúen con el juego
 * (sumar puntos, pausar spawn, etc.) sin conocer GameScreen.
 */
public interface GameContext {

    // Métodos para Collectibles (Gotas)
    void sumarPuntaje(int puntos);
    void restarPuntaje(int puntos);
    void perderVida(int vidas);
    void ganarVida(int vidas);

    // Métodos para Powers (Poderes)
    void setShieldActive(boolean active);
    void cleanScreen();
    void setGodMode(boolean active);
    void activatePower(String powerId); // <-- ESTA ES LA LÍNEA NUEVA

    // Métodos para el Spawner
    void pausarSpawn(boolean pausa);

    // Métodos para obtener estado (útil para la Gota Roja):d
    int getVidasActuales();
    int getPuntajeActual();
    int getVidasMaximas();
    
}