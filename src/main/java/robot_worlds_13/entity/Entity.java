package robot_worlds_13.entity;

import java.awt.image.BufferedImage;

/**
 * Represents an entity in the game world, such as a player, enemy, or object.
 * Entities have a position (x, y), a speed, and a set of sprite images to
 * represent their movement in different directions.
 * The `spriteCounter` and `spriteNum` fields are used to control the animation
 * of the entity's sprites.
 */
public class Entity {

    public int x, y;
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;
}
