package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class DropFactory {
    private Texture dropBlueImage;
    private Texture dropBlackImage;
    private Texture dropGrayImage;
    private Texture dropRedImage;
    private Texture dropShieldImage;
    private Texture dropCleanImage;
    private Texture bossProjectileTexture; 

    public DropFactory() {
        Assets assets = Assets.getInstance();
        dropBlueImage = assets.manager.get("drop.png", Texture.class);
        dropBlackImage = assets.manager.get("dropBad.png", Texture.class);
        dropGrayImage = assets.manager.get("drop_gray.png", Texture.class);
        dropRedImage = assets.manager.get("drop_red_life.png", Texture.class);
        dropShieldImage = assets.manager.get("drop_shield.png", Texture.class);
        dropCleanImage = assets.manager.get("power_clean_screen.png", Texture.class);
        
        // Cargamos la textura del rayo
        bossProjectileTexture = assets.manager.get("boss_projectile.png", Texture.class);
    }

    public Collectible createDrop(String type, float x, float y) {
        switch (type) {
            case "BLUE": 
                return new DropBlue(dropBlueImage, x, y);
            
            case "BLACK": 
                // CORREGIDO: Se eliminó 'MathUtils.random(200, 350)' porque DropBlack no lo acepta
                return new DropBlack(dropBlackImage, x, y);
                
            case "GRAY": 
                // CORREGIDO: Se eliminó 'MathUtils.random(200, 350)' porque DropGray no lo acepta
                return new DropGray(dropGrayImage, x, y);
                
            case "RED": 
                return new LifeDropRed(dropRedImage, x, y);
                
            case "POWER_SHIELD": 
                return new ColleciblePowerUp(dropShieldImage, x, y, "SHIELD");
                
            case "POWER_CLEAN": 
                return new ColleciblePowerUp(dropCleanImage, x, y, "CLEAN_SCREEN");
            
            // El ataque del Boss
            case "BOSS_PROJECTILE":
                return new BossLightning(bossProjectileTexture, x - 15, y);
            
            default: 
                return null;
        }
    }
}