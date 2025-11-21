package io.github.some_example_name;

/**
 * Interfaz ABSTRACT FACTORY (GM2.4)
 * Define la familia de objetos que cada nivel debe poder crear.
 */
public interface GameLevelFactory {
    Collectible createEnemy(float x, float y);
    Collectible createPowerUp(float x, float y);
    Collectible createBossProjectile(float x, float y);
}