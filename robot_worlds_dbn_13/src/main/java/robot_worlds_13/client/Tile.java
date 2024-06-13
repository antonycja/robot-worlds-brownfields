package robot_worlds_13.client;

import java.awt.image.BufferedImage;

/**
 * Represents a single tile in the game world.
 * 
 * The `Tile` class encapsulates the properties of a single tile, including a
 * `BufferedImage` representing the tile's visual appearance, and a `collision`
 * flag indicating whether the tile is collidable.
 */
public class Tile {

    public BufferedImage image;
    public boolean collision = false;

}
