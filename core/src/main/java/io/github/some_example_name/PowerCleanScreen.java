package io.github.some_example_name;

/**
 * Poder Instantáneo: Limpia la pantalla de gotas (ver lógica en GameScreen).
 * Cubre: GM1.5 (Clase de implementación)
 */
public class PowerCleanScreen implements Power {

    private boolean applied = false;

    @Override
    public String getId() {
        return "CLEAN_SCREEN";
    }

    @Override
    public float getDurationSeconds() {
        return 0; // Es instantáneo
    }

    @Override
    public void apply(GameContext ctx) {
        ctx.cleanScreen();
        applied = true;
    }

    @Override
    public void revert(GameContext ctx) {
        // No hace nada, es instantáneo
    }

    @Override
    public void update(float delta) {
        // No hace nada
    }

    @Override
    public boolean isFinished() {
        return applied; // Termina inmediatamente después de apply()
    }
}