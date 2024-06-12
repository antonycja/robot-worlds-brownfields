package robot_worlds_13.server.robot.maze;

import java.util.ArrayList;
import java.util.List;

import robot_worlds_13.server.*;
import robot_worlds_13.server.robot.Position;
import robot_worlds_13.server.robot.world.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleMaze extends AbstractMaze {

    private List<Obstacle> obstacles;
    private List<Obstacle> bottomLessPits;
    private List<Obstacle> lakes;
    private Random random;
    private int minCoordinate = -200; // Minimum coordinate value
    private int maxCoordinate = 200;  // Maximum coordinate value
    private int step = 40;            // Step for multiples of 40

    public SimpleMaze() {
        this.obstacles = new ArrayList<>();
        this.bottomLessPits = new ArrayList<>();
        this.lakes = new ArrayList<>();
        this.random = new Random();

        generateRandomObstacles();
    }

    private Position generateUniquePosition() {
        while (true) {
            int x = minCoordinate + random.nextInt((maxCoordinate - minCoordinate) / step + 1) * step;
            int y = minCoordinate + random.nextInt((maxCoordinate - minCoordinate) / step + 1) * step;
            Position newPos = new Position(x, y);
            if (isPositionUnique(newPos)) {
                return newPos;
            }
        }
    }

    private boolean isPositionUnique(Position pos) {
        return !(isPositionOccupied(pos, obstacles) || isPositionOccupied(pos, bottomLessPits) || isPositionOccupied(pos, lakes));
    }

    private boolean isPositionOccupied(Position pos, List<Obstacle> obstaclesList) {
        for (Obstacle ob : obstaclesList) {
            if (((SquareObstacle) ob).getPosition().equals(pos)) {
                return true;
            }
        }
        return false;
    }

    private void generateRandomObstacles() {
        // Generate obstacles
        for (int i = 0; i < 5; i++) {
            obstacles.add(new SquareObstacle(generateUniquePosition().getX(), generateUniquePosition().getY()));
        }
        
        // Generate bottomless pits
        for (int i = 0; i < 3; i++) {
            bottomLessPits.add(new SquareObstacle(generateUniquePosition().getX(), generateUniquePosition().getY()));
        }

        // Generate lakes
        for (int i = 0; i < 2; i++) {
            lakes.add(new SquareObstacle(generateUniquePosition().getX(), generateUniquePosition().getY()));
        }
    }

    @Override
    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }

    public List<Obstacle> getBottomLessPits() {
        return this.bottomLessPits;
    }

    public List<Obstacle> getLakes() {
        return this.lakes;
    }

    @Override
    public boolean blocksPath(Position a, Position b) {
        // Implement logic to determine if any obstacle blocks the path from Position a to Position b
        return false;
    }
}