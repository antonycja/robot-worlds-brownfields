package robot_worlds_13.server.configuration;

import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.Obstacle;
import robot_worlds_13.server.robot.world.SquareObstacle;

import java.util.ArrayList;

public class ServerConfiguration {

    private int portNum;
    private int size;
    private int pit;
    private String obstacle;
    private String lake;

    // Default constructor with default values
    public ServerConfiguration() {
        this.portNum = 5050;
        this.size = 1000; // Default size
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
}
