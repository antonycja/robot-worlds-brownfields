package robot_worlds_13.client;

import entity.Player;

import javax.swing.*;

import com.google.gson.Gson;

import java.awt.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

import robot_worlds_13.client.Main;
import robot_worlds_13.server.robot.Direction;
import robot_worlds_13.server.robot.Position;

public class GamePanel extends JPanel implements Runnable {

    // Screen settings
    final int originalTitleSize = 10; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTitleSize * scale;  // 48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    public ArrayList<Player> players = new ArrayList<>();

    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
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

    public int height = 800;
    public int width = 400;

    public GamePanel() {
        
        this.setPreferredSize(new Dimension(width, height));
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
                if (response == null || response.isEmpty() || response.size() == 0) {
                    continue;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            

            System.out.println(response);

            // if response is launch create robot
            //     add it to list of robots
            String name = (String) response.get("name");
            int startX = (int) Math.round(( (double) ((ArrayList<Double>) response.get("position")).get(0)));
            int startY = (int) Math.round(( (double) ((ArrayList<Double>) response.get("position")).get(1)));

            if (((String) response.get("message")).equalsIgnoreCase("LAUNCH")) {
                // Position startingPosition = new Position(, playerSpeed)
                Player player = new Player(this, keyH, new Position(startX, startY), name);
                players.add(player);
            }

            Player currentPlayer = new Player();
            for (Player player: players) {
                if (name.equals(player.characterName)){
                    currentPlayer = player;
                    System.out.println("Player found: " + name);
                }


            // if response move
                // using the name, get player object
                // from and too position, move by one unit
            if (((String) response.get("message")).equalsIgnoreCase("FRONT")) {
                
                        int previousX = (int) Math.round(( (double) ((ArrayList<Double>) response.get("previousPosition")).get(0)));
                        int previousY = (int) Math.round(( (double) ((ArrayList<Double>) response.get("previousPosition")).get(1)));
                        if (startX > previousX) {
                            for (int step=previousX; step!=startX; step++) {
                                currentPlayer.update(Direction.EAST);
                            }
                        }
                        if (startY > previousY) {
                            for (int step=previousY; step != startY; step++){
                                currentPlayer.update(Direction.NORTH);
                            }
                        }
                        if (startX < previousX) {
                            for (int step=previousX; step!=startX; step++) {
                                currentPlayer.update(Direction.WEST);
                            }
                        }
                        if (startY < previousY) {
                            for (int step=previousY; step!=previousY; step++) {
                                currentPlayer.update(Direction.SOUTH);
                            }
                        }
                    
                }
            }

            if (((String) response.get("message")).equalsIgnoreCase("BACK")) {
                
                int previousX = (int) Math.round(( (double) ((ArrayList<Double>) response.get("previousPosition")).get(0)));
                int previousY = (int) Math.round(( (double) ((ArrayList<Double>) response.get("previousPosition")).get(1)));
                if (startX < previousX) {
                    for (int step=previousX; step!=startX; step--) {
                        currentPlayer.updateBack(Direction.EAST);
                    }
                }
                if (startY < previousY) {
                    for (int step=previousY; step != startY; step--){
                        currentPlayer.updateBack(Direction.NORTH);
                    }
                }
                if (startX > previousX) {
                    for (int step=previousX; step!=startX; step--) {
                        currentPlayer.updateBack(Direction.WEST);
                    }
                }
                if (startY > previousY) {
                    for (int step=previousY; step!=previousY; step--) {
                        currentPlayer.updateBack(Direction.SOUTH);
                    }
                }
            
        }

            if (((String) response.get("message")).equalsIgnoreCase("RIGHT")) {
                switch ( currentPlayer.direction) {
                    case "up":
                        currentPlayer.direction = "right";
                        break;
                    case "down":
                        currentPlayer.direction = "left";
                        break;
                    case "left":
                        currentPlayer.direction = "up";
                        break;
                    case "right":
                        currentPlayer.direction = "down";
                        break;
                }
            }

            if (((String) response.get("message")).equalsIgnoreCase("LEFT")) {
                switch ( currentPlayer.direction) {
                    case "up":
                        currentPlayer.direction = "left";
                        break;
                    case "down":
                        currentPlayer.direction = "right";
                        break;
                    case "left":
                        currentPlayer.direction = "down";
                        break;
                    case "right":
                        currentPlayer.direction = "up";
                        break;
                }
            }


            


            // System.err.println(response);
            

            long currentTime = System.nanoTime();

            // 1 UPDATE: update information such as character positions
            // update();
            // 2 DRAW: draw screen with the updated information
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;  //converting time to milliseconds

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (players == null) {
            return;
        }

        for (Player player: players) {
            player.update();
        }
        
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
        
        if (players != null) {
            for (Player player: players) {
                player.draw(g2);
            }
        }
        

        g2.dispose();
    }

    
}