package robot_worlds_13.server.configuration;

import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.Obstacle;
import robot_worlds_13.server.robot.world.SquareObstacle;

import java.util.ArrayList;
import java.util.Scanner;

public class ServerConfiguration {

    // ANSI escape codes for colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private int portNum;
    private int size;
    private int pit;
    private String obstacle;
    private String lake;

    // Default constructor with default values
    public ServerConfiguration() {
        this.portNum = 5050;
        this.size = 1; // Default size
        this.pit = 0;     // Default pit (0 means no pit, 1 means pit)
        this.obstacle = "0,0"; // Default obstacle coordinates
        this.lake = "0,0"; // Default lake coordinates
    }

    // Constructor with parsed arguments
    public ServerConfiguration(int port, int size, int pit, String obstacle, String lake) {
        this.portNum = port;
        this.size = size;
        this.pit = pit;
        this.obstacle = obstacle;
        this.lake = lake;
    }

    // Method to configure the world with obstacles
    public void configureWorld(AbstractWorld world) {
        Obstacle obstacleObj = new SquareObstacle(
                Integer.parseInt(obstacle.split(",")[0]),
                Integer.parseInt(obstacle.split(",")[1])
        );
        ArrayList<Obstacle> obstacles = (ArrayList<Obstacle>) world.getObstacles();
        obstacles.add(obstacleObj);
        world.setObstacles(obstacles);

        if (pit == 1) {
            Obstacle pitObj = new SquareObstacle(1, 1); // Example pit coordinates
            ArrayList<Obstacle> pits = (ArrayList<Obstacle>) world.getBottomLessPits();
            pits.add(pitObj);
            world.setObstacles(pits);
        }

        Obstacle lakeObj = new SquareObstacle(
                Integer.parseInt(lake.split(",")[0]),
                Integer.parseInt(lake.split(",")[1])
        );
        ArrayList<Obstacle> lakes = (ArrayList<Obstacle>) world.getLakes();
        lakes.add(lakeObj);
        world.setObstacles(lakes);
    }

    public int getPortNum() {
        return portNum;
    }

    public int getSize() {
        return size;
    }

    public int getPit() {
        return pit;
    }

    public String getObstacle() {
        return obstacle;
    }

    public String getLake() {
        return lake;
    }

    public static int getIntInput(Scanner scanner, String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    break;
                } else {
                    System.out.println(ANSI_RED + "Input must be between " + min + " and " + max + "." + ANSI_RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "Invalid input, enter a numeric value." + ANSI_RESET);
            }
        }
        return value;
    }
}
