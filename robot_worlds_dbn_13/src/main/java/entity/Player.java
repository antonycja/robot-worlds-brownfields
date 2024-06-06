package entity;

import robot_worlds_13.client.GamePanel;
import robot_worlds_13.client.KeyHandler;
import robot_worlds_13.server.robot.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    public String characterName;

    public Player() {

    }

    public Player(GamePanel gp, KeyHandler keyH, Position startPosition, String givenName) {
        this.gp = gp;
        this.keyH = keyH;
        this.characterName = givenName;
        // x = 200;
        // y = 400;
        x = (gp.width / 2) + startPosition.getX() - 4;
        y = (gp.height / 2) - startPosition.getY() - 4;
        setDefaultValues();
        getPlayerImage();
        direction = "up";
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
        if(spriteCounter >10) {
            if(spriteNum == 1) {
                spriteNum = 2;
            }
            spriteCounter = 0;
        }
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
        if(spriteCounter >10) {
            if(spriteNum == 1) {
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
        if(spriteCounter >10) {
            if(spriteNum == 1) {
                spriteNum = 2;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
//        g2.setColor(Color.white);
//        g2.fillRect(x, y, gp.tileSize, gp.tileSize);
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
        }

        
    }




