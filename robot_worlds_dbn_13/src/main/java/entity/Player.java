package entity;

import robot_worlds_13.client.GamePanel;
import robot_worlds_13.client.KeyHandler;
import robot_worlds_13.server.robot.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Represents the player character in the game.
 * The Player class extends the Entity class and handles the player's movement,
 * image, and drawing.
 */
public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    public String characterName;

    private Position position;
    private Position destination;

    public Player() {

    }

    public Player(GamePanel gp, KeyHandler keyH, Position startPosition, String givenName) {
        this.gp = gp;
        this.keyH = keyH;
        this.characterName = givenName;
        // x = 200;
        // y = 400;
        this.position = startPosition;
        x = (gp.width / 2) + startPosition.getX() - (gp.tileSize / 2);
        y = (gp.height / 2) - startPosition.getY() - (gp.tileSize / 2);
        setDefaultValues();
        getPlayerImage();
        direction = "up";
    }

    public Player(GamePanel gp, KeyHandler keyH, Position startPosition, String directionGiven, String givenName) {
        this.gp = gp;
        this.keyH = keyH;
        this.characterName = givenName;
        // x = 200;
        // y = 400;
        this.position = startPosition;
        x = (gp.width / 2) + startPosition.getX() - (gp.tileSize / 2);
        y = (gp.height / 2) - startPosition.getY() - (gp.tileSize / 2);
        setDefaultValues();
        getPlayerImage();
        direction = setDirection(directionGiven);
    }

    public void setDefaultValues() {
        // Set player's default position
        speed = 1;
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_up_1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_up_2.png")));
            down1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_down_1.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_down_2.png")));
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_left_1.png")));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_left_2.png")));
            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_right_1.png")));
            right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_right_2.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (keyH.upPressed) {
            direction = "up";
            y -= speed;
        } else if (keyH.downPressed) {
            direction = "down";
            y += speed;
        } else if (keyH.leftPressed) {
            direction = "left";
            x -= speed;
        } else if (keyH.rightPressed) {
            direction = "right";
            x += speed;
        }

        spriteCounter++;
        if (spriteCounter > 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
            }
            spriteCounter = 0;
        }

        // Implement smooth movement towards destination
        int dx = Integer.compare(destination.getX(), position.getX());
        int dy = Integer.compare(destination.getY(), position.getY());
        position.setX(position.getX() + dx);
        position.setY(position.getY() + dy);
    }

    public void update(Direction headingDirection) {

        if (headingDirection == Direction.NORTH) {
            direction = "up";
            y -= speed;
        } else if (headingDirection == Direction.SOUTH) {
            direction = "down";
            y += speed;
        } else if (headingDirection == Direction.WEST) {
            direction = "left";
            x -= speed;
        } else if (headingDirection == Direction.EAST) {
            direction = "right";
            x += speed;
        }

        spriteCounter++;
        if (spriteCounter > 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
            }
            spriteCounter = 0;
        }
    }

    public void updateBack(Direction headingDirection) {

        if (headingDirection == Direction.NORTH) {
            direction = "up";
            y += speed;
        } else if (headingDirection == Direction.SOUTH) {
            direction = "down";
            y -= speed;
        } else if (headingDirection == Direction.WEST) {
            direction = "left";
            x += speed;
        } else if (headingDirection == Direction.EAST) {
            direction = "right";
            x -= speed;
        }

        spriteCounter++;
        if (spriteCounter > 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        // g2.setColor(Color.white);
        // g2.fillRect(x, y, gp.tileSize, gp.tileSize);
        BufferedImage image = null;

        switch (direction) {
            case "up":
                image = up1;
                break;
            case "down":
                image = down1;
                break;
            case "left":
                image = left1;
                break;
            case "right":
                image = right1;
                break;
        }
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 12));

        // Calculate the width of the name to center it above the player
        int nameWidth = g2.getFontMetrics().stringWidth(characterName);
        int nameX = x + (gp.tileSize - nameWidth) / 2;
        int nameY = y - 5;

        g2.drawString(characterName, nameX, nameY);
    }

    public String setDirection(String directionGiven) {
        switch (directionGiven) {
            case "NORTH":
                return "up";
            case "WEST":
                return "left";
            case "EAST":
                return "right";
            case "SOUTH":
                return "down";
            default:
                return "up";
        }
    }

    public void setDestination(Position destination) {
        this.destination = destination;
    }

    public boolean hasReachedDestination() {
        return position.equals(destination);
    }

}




