package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;

public class BossLightning extends Collectible {

    private boolean active = true; // Para evitar que golpee más de una vez

    public BossLightning(Texture texture, float x, float y) {
        // 1. Llamamos al constructor de la clase padre (Collectible)
        // Pasamos la textura, posición y el ID "BOSS_PROJECTILE"
        super(texture, x, y, "BOSS_PROJECTILE");
        
        // 2. Redefinimos el tamaño. Tu clase padre pone 48x48, 
        // pero el rayo necesita ser estrecho y alto (30x90).
        this.bounds.width = 30;
        this.bounds.height = 90;
    }

    @Override
    public void update(float delta) {
        // Sobrescribimos el movimiento para que sea más rápido que las gotas normales
        // La clase padre usa 'speed' privada, así que movemos 'bounds' manualmente aquí
        // 500 de velocidad para que sea difícil de esquivar
        bounds.y -= 500 * delta;
    }

    // --- IMPLEMENTACIÓN DEL TEMPLATE METHOD ---
    // Tu clase Collectible usa un patrón Template Method.
    // Aquí solo definimos QUE HACE (efecto) y QUE SUENA.

    @Override
    protected void applyEffect(GameContext ctx) {
        // Lógica del golpe: Si está activo, quita vida
        if (active) {
            ctx.perderVida(1);
            active = false; // Desactivar para no golpear dos veces seguidas
        }
    }

    @Override
    protected void playSound() {
        // Reproducir sonido de herida
        Assets.getInstance().manager.get("hurt.ogg", Sound.class).play();
    }
    
    // NO hace falta implementar draw(), getBounds() ni getTypeId()
    // porque la clase padre Collectible ya lo hace por nosotros.
}