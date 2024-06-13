package robot_worlds_13.client;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * The `TileManager` class is responsible for managing the tiles used in the
 * game world.
 * It loads the tile images from the resources and provides methods to draw the
 * tiles on the game panel.
 */
public class TileManager {

    GamePanel gp;
    Tile[] tile;

    public TileManager(GamePanel go) {
        this.gp = gp;
        tile = new Tile[10];
        getTileImage();
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(tile[0].image, 0, 0, gp.tileSize, gp.tileSize, null);
    }
}