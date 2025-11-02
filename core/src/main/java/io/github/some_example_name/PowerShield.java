package io.github.some_example_name;

/**
 * Poder de Escudo: Invulnerable a gotas negras/grises por N segundos.
 * Cubre: GM1.5 (Clase de implementación)
 */
public class PowerShield implements Power {

    // Duración según el plan (6-8s).
    private static final float DURATION = 7.0f;
    private float timer;
    private boolean active;

    public PowerShield() {
        this.timer = DURATION;
        this.active = false;
    }

    @Override
    public String getId() {
        return "SHIELD";
    }

    @Override
    public float getDurationSeconds() {
        return DURATION;
    }

    @Override
    public void apply(GameContext ctx) {
        ctx.setShieldActive(true);
        this.active = true;
    }

    @Override
    public void revert(GameContext ctx) {
        ctx.setShieldActive(false);
        this.active = false;
    }

    @Override
    public void update(float delta) {
        if (active) {
            timer -= delta;
        }
    }

    @Override
    public boolean isFinished() {
        return timer <= 0;
    }
}