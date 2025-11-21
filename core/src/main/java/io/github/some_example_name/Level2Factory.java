package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Level2Factory implements GameLevelFactory {
    // Agregamos la textura de la gota azul
    private Texture dropBlue;
    private Texture dropBlack;
    private Texture dropGray;
    private Texture dropRed;
    private Texture dropShield;
    private Texture bossProjectile;

    public Level2Factory() {
        Assets assets = Assets.getInstance();
        // Cargar la textura azul
        dropBlue = assets.manager.get("drop.png", Texture.class);
        
        dropBlack = assets.manager.get("dropBad.png", Texture.class);
        dropGray = assets.manager.get("drop_gray.png", Texture.class);
        dropRed = assets.manager.get("drop_red_life.png", Texture.class);
        dropShield = assets.manager.get("drop_shield.png", Texture.class);
        bossProjectile = assets.manager.get("boss_projectile.png", Texture.class);
    }

    @Override
    public Collectible createEnemy(float x, float y) {
        Collectible enemy;
        
        float chance = MathUtils.random();
        
        // AJUSTE DE PROBABILIDADES PARA INCLUIR AZULES
        if (chance < 0.3f) {
            // 30% de probabilidad de gota Azul (Puntos)
            enemy = new DropBlue(dropBlue, x, y);
        } else if (chance < 0.7f) {
            // 40% de probabilidad de gota Negra
            enemy = new DropBlack(dropBlack, x, y);
        } else if (chance < 0.9f) {
            // 20% de probabilidad de gota Gris (Rápida)
            enemy = new DropGray(dropGray, x, y);
        } else {
            // 10% de probabilidad de gota Roja (Vida)
            enemy = new LifeDropRed(dropRed, x, y);
        }

        // --- ESTRATEGIA ZIGZAG ---
        // A todas (incluida la azul) les ponemos el movimiento loco
        enemy.setMovementStrategy(new ZigZagStrategy());
        
        return enemy;
    }

    @Override
    public Collectible createPowerUp(float x, float y) {
        Collectible power = new ColleciblePowerUp(dropShield, x, y, "SHIELD");
        power.setMovementStrategy(new ZigZagStrategy()); 
        return power;
    }

    @Override
    public Collectible createBossProjectile(float x, float y) {
        return new BossLightning(bossProjectile, x, y);
    }
}