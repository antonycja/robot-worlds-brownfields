package robot_worlds_13.server.robot.maze;

import java.util.ArrayList;
import java.util.List;
import robot_worlds_13.server.robot.Position;
import robot_worlds_13.server.robot.world.*;
import java.util.Random;

/**
 * Represents a simple maze with randomly generated obstacles, bottomless pits, and lakes.
 * The maze is defined by a set of coordinates within a specified range, with obstacles placed at
 * multiples of a fixed step size.
 */
public class SimpleMaze extends AbstractMaze {

    private List<Obstacle> obstacles;
    private List<Obstacle> bottomLessPits;
    private List<Obstacle> lakes;
    private Random random;
    private int minCoordinate; // Minimum coordinate value
    private int maxCoordinate; // Maximum coordinate value
    private int step; // Step for multiples
    private int dynamicStep;
    private static final int MIN_STEP = 2   ; // Minimum step size
    private static final int MAX_STEP = 10; // Maximum step size

    /**
     * Constructs a new SimpleMaze instance, initializing the obstacles, bottomless pits, lakes, and a random number generator.
     * The generateRandomObstacles() method is then called to generate random obstacles for the maze.
     */

    public SimpleMaze() {
        this(200);
    }

    public SimpleMaze(int size) {
        this.obstacles = new ArrayList<>();
        this.bottomLessPits = new ArrayList<>();
        this.lakes = new ArrayList<>();
        this.random = new Random();
        this.minCoordinate = -size;
        this.maxCoordinate = size;
        this.step = calculateDynamicStep(size);
    }

    /**
     * Calculates a dynamic step size based on the maze size.
     *
     * @param size the size of the maze
     * @return a dynamically calculated step size
     */
    private int calculateDynamicStep(int size) {
        dynamicStep = size / 10; // Example: Step size is 10% of the maze size
        return Math.max(MIN_STEP, Math.min(dynamicStep, MAX_STEP));
    }

    /**
     * Generates a unique position within the maze bounds.
     *
     * @return a unique position within the maze bounds
     */
    private Position generateUniquePosition() {
        int attempts = 0;
        int maxAttempts = 100; // Max attempts before giving up

        while (attempts < maxAttempts) {
            int x = minCoordinate + random.nextInt((maxCoordinate - minCoordinate) / step + 1) * step;
            int y = minCoordinate + random.nextInt((maxCoordinate - minCoordinate) / step + 1) * step;
            Position newPos = new Position(x, y);
            if (isPositionUnique(newPos)) {
                return newPos;
            }
            attempts++;
        }

        // If no unique position is found, return null or handle the case differently
        return null;
    }

    private boolean isPositionUnique(Position pos) {
        return !(isPositionOccupied(pos, obstacles) || isPositionOccupied(pos, bottomLessPits)
                || isPositionOccupied(pos, lakes));
    }

    private boolean isPositionOccupied(Position pos, List<Obstacle> obstaclesList) {
        for (Obstacle ob : obstaclesList) {
            if (((SquareObstacle) ob).getPosition().equals(pos)) {
                return true;
            }
        }
        return false;
    }

    public void generateRandomObstacles() {
        // Generate obstacles
        for (int i = 0; i < 5; i++) {
            Position uniquePos = generateUniquePosition();
            if (uniquePos != null) {
                obstacles.add(new SquareObstacle(uniquePos.getX(), uniquePos.getY(), dynamicStep, "obstacle"));
            } else {
                System.out.println("Failed to place an obstacle: no unique position available.");
            }
        }

        // Generate bottomless pits
        for (int i = 0; i < 3; i++) {
            Position uniquePos = generateUniquePosition();
            if (uniquePos != null) {
                bottomLessPits.add(new SquareObstacle(uniquePos.getX(), uniquePos.getY(), dynamicStep, "pit"));
            } else {
                System.out.println("Failed to place a bottomless pit: no unique position available.");
            }
        }

        // Generate lakes
        for (int i = 0; i < 2; i++) {
            Position uniquePos = generateUniquePosition();
            if (uniquePos != null) {
                lakes.add(new SquareObstacle(uniquePos.getX(), uniquePos.getY(), dynamicStep,"lake"));
            } else {
                System.out.println("Failed to place a lake: no unique position available.");
            }
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
        // Implement logic to determine if any obstacle blocks the path from Position a
        // to Position b
        return false;
    }

    public void setMinCoordinate(int minCoordinate) {
        this.minCoordinate = - minCoordinate;
    }

    public int getDynamicStep() {
        return dynamicStep;
    }

    public void setMaxCoordinate(int maxCoordinate) {
        this.maxCoordinate = maxCoordinate;
    }

}