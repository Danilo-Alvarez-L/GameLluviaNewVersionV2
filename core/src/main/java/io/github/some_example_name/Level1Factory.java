package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Level1Factory implements GameLevelFactory {
    private Texture dropBlue;
    private Texture dropBlack;
    private Texture dropShield;
    private Texture dropClean;
    private Texture bossProjectile;

    public Level1Factory() {
        Assets assets = Assets.getInstance();
        dropBlue = assets.manager.get("drop.png", Texture.class);
        dropBlack = assets.manager.get("dropBad.png", Texture.class);
        dropShield = assets.manager.get("drop_shield.png", Texture.class);
        dropClean = assets.manager.get("power_clean_screen.png", Texture.class);
        bossProjectile = assets.manager.get("boss_projectile.png", Texture.class);
    }

    @Override
    public Collectible createEnemy(float x, float y) {
        // Nivel 1: Mayoría gotas azules (buenas), algunas negras (malas)
        if (MathUtils.randomBoolean(0.7f)) {
            // 70% de probabilidad de gota azul
            return new DropBlue(dropBlue, x, y);
        } else {
            return new DropBlack(dropBlack, x, y);
        }
        // Nota: Aquí NO aplicamos ZigZag, dejamos que caigan normal (facilito)
    }

    @Override
    public Collectible createPowerUp(float x, float y) {
        if (MathUtils.randomBoolean()) {
            return new ColleciblePowerUp(dropShield, x, y, "SHIELD");
        } else {
            return new ColleciblePowerUp(dropClean, x, y, "CLEAN_SCREEN");
        }
    }

    @Override
    public Collectible createBossProjectile(float x, float y) {
        return new BossLightning(bossProjectile, x, y);
    }
}