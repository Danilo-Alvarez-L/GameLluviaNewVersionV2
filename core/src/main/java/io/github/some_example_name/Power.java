package io.github.some_example_name;

/**
 * Interfaz para todos los Poderes (temporales o instantáneos).
 * Cubre: GM1.5 (Interfaz)
 */
public interface Power {

    /**
     * Identificador único del poder (ej. "SHIELD", "CLEAN_SCREEN").
     */
    String getId();

    /**
     * Duración en segundos. 0 si es instantáneo.
     */
    float getDurationSeconds();

    /**
     * Aplica el efecto del poder usando el contexto.
     */
    void apply(GameContext ctx);

    /**
     * Revierte el efecto del poder (si es temporal).
     * Se llama cuando expira el tiempo.
     */
    void revert(GameContext ctx);

    /**
     * Actualiza el timer interno del poder.
     */
    void update(float delta);

    /**
     * Verifica si el poder ha expirado :p.
     */
    boolean isFinished(); //:p
}