package robot_worlds_13.client;

import entity.Player;

import javax.swing.*;

import com.google.gson.Gson;

import java.awt.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

import robot_worlds_13.client.Main;

public class GamePanel extends JPanel implements Runnable {

    // Screen settings
    final int originalTitleSize = 3; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTitleSize * scale;  // 48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);
    // Set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;
    
    String localAddress = "localhost";
    String serverIpAddress = "20.20.15.94";
    Socket sThisClient;
    DataOutputStream dout;
    DataInputStream din;
    Scanner line = new Scanner(System.in);
    String robotName = "";
    Gson gson = new Gson();

    public GamePanel() {
        

        this.setPreferredSize(new Dimension(400, 800));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS; // 1 billion nanoseconds divided by FPS
        double nextDrawTime = System.nanoTime() + drawInterval;

        try {
            sThisClient = new Socket(localAddress, 2201);
            dout = new DataOutputStream(sThisClient.getOutputStream());
            din = new DataInputStream(sThisClient.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (gameThread != null) {
            
            // listen for command
            Map<String, Object> response;
            try {
                response = GUIProtocol.jsonResponseUnpacker(din.readUTF());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            

            System.out.println(response);

            // if response is launch create robot
                // add it to list of robots

            // if response move
                // using the name, get player object
                // from and too position, move by one unit

            


            // System.err.println(response);
            

            // long currentTime = System.nanoTime();

            // // 1 UPDATE: update information such as character positions
            // update();
            // // 2 DRAW: draw screen with the updated information
            // repaint();

            // try {
            //     double remainingTime = nextDrawTime - System.nanoTime();
            //     remainingTime = remainingTime / 1000000;  //converting time to milliseconds

            //     if (remainingTime < 0) {
            //         remainingTime = 0;
            //     }

            //     Thread.sleep((long) remainingTime);

            //     nextDrawTime += drawInterval;
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw Cartesian axes
        g.drawLine(0, 400, 400, 400); // Horizontal line (x-axis)
        g.drawLine(200, 0, 200, 800); // Vertical line (y-axis)

        // Swing point (example: Swing coordinate (300, 300))
        int xSwing = 100;
        int ySwing = 100;

        // Convert to Cartesian coordinates
        int xCartesian = xSwing - 200;
        int yCartesian = 400 - ySwing;

        // Draw a point at the converted coordinates
        g.fillOval(xSwing - 4, ySwing - 4, 8, 8);
        g.drawString("Swing: (" + xSwing + ", " + ySwing + ")", xSwing + 10, ySwing);
        g.drawString("Cartesian: (" + xCartesian + ", " + yCartesian + ")", xSwing + 10, ySwing + 20);

        // Draw borders for the play area
        int leftX = 0;   // Corresponds to Cartesian -200
        int rightX = 400; // Corresponds to Cartesian +200
        int topY = 0;    // Corresponds to Cartesian +400
        int bottomY = 800; // Corresponds to Cartesian -400


        // Top border (y=400, the middle line, minus 400 units upward)
        g.drawLine(leftX, topY + 400, rightX, topY + 400);
        // Bottom border (y=400, the middle line, plus 400 units downward)
        g.drawLine(leftX, bottomY - 400, rightX, bottomY - 400);
        // Left border
        g.drawLine(leftX + 200, topY, leftX + 200, bottomY);
        // Right border
        g.drawLine(rightX - 200, topY, rightX - 200, bottomY);
        
        Graphics2D g2 = (Graphics2D) g;

        player.draw(g2);

        g2.dispose();
    }

    
}