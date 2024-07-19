package entity;

import java.awt.Color;
import java.awt.Graphics;

import robot_worlds_13.client.GamePanel;

/**
 * Represents a bullet entity in the game world.
 * Bullets can be fired by game entities and move in a straight line based on
 * their direction.
 * Bullets are responsible for their own movement and drawing on the game panel.
 */
public class Bullet {

    private int x, y;
    private final int speed = 10; // You can adjust the speed as necessary
    private String direction;
    private GamePanel panel;

    public Bullet(GamePanel panel, int startX, int startY, String direction) {
        this.x = startX;
        // this.x = (panel.width / 2) + startX - (panel.tileSize / 2);
        this.y = startY;
        // this.y = (panel.height / 2) - startY - (panel.tileSize / 2);
        this.direction = direction;
        this.panel = panel;
    }

    public void move() {
        switch (direction) {
            case "EAST":
                x += speed;
                break;
            case "WEST":
                x -= speed;
                break;
            case "NORTH":
                y -= speed;
                break;
            case "SOUTH":
                y += speed;
                break;
        }
        panel.repaint();
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x - 4, y, 10, 10); // Draw the bullet as a small red circle
    }

    public boolean isAtDestination(int destX, int destY) {
        return x == destX && y == destY;
    }
}

