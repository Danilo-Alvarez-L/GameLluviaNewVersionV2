package io.github.some_example_name;

/**
 * Clase simple para gestionar el GodMode (según plan GM1.6).
 */
public class GodModeToggle {

    private boolean active = false;
    private GameContext context;

    public GodModeToggle(GameContext context) {
        this.context = context;
    }

    public void toggle() {
        this.active = !this.active;
        context.setGodMode(this.active);
    }

    public boolean isActive() {
        return active;
    }
}