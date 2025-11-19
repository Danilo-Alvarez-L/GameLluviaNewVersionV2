package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;

/**
 * Patrón Abstract Factory (o Factory Method centralizado).
 * Encapsula la creación de objetos Collectible para que GameScreen
 * no tenga que hacer 'new' directamente ni gestionar texturas individuales.
 * Cubre: GM2.4
 */
public class DropFactory {
    
    // Referencias a las texturas (se obtienen del Singleton Assets)
    private Texture dropTexture;
    private Texture dropBadTexture;
    private Texture dropGrayTexture;
    private Texture dropRedTexture;
    private Texture powerCleanTexture;
    private Texture powerShieldTexture;

    public DropFactory() {
        // Obtener texturas UNA SOLA VEZ desde el Singleton
        Assets assets = Assets.getInstance();
        this.dropTexture = assets.manager.get("drop.png", Texture.class);
        this.dropBadTexture = assets.manager.get("dropBad.png", Texture.class);
        this.dropGrayTexture = assets.manager.get("drop_gray.png", Texture.class);
        this.dropRedTexture = assets.manager.get("drop_red_life.png", Texture.class);
        this.powerCleanTexture = assets.manager.get("power_clean_screen.png", Texture.class);
        this.powerShieldTexture = assets.manager.get("drop_shield.png", Texture.class);
    }

    /**
     * Crea una gota basada en un tipo enumerado o string.
     */
    public Collectible createDrop(String type, float x, float y) {
        switch (type) {
            case "BLUE":
                return new DropBlue(dropTexture, x, y);
            case "BLACK":
                return new DropBlack(dropBadTexture, x, y);
            case "GRAY":
                return new DropGray(dropGrayTexture, x, y);
            case "RED":
                return new LifeDropRed(dropRedTexture, x, y);
            case "POWER_SHIELD":
                return new ColleciblePowerUp(powerShieldTexture, x, y, "SHIELD");
            case "POWER_CLEAN":
                return new ColleciblePowerUp(powerCleanTexture, x, y, "CLEAN_SCREEN");
            default:
                return null;
        }
    }
}