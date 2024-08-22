package robot_worlds_13.server.robot.world;

import java.util.ArrayList;
import java.util.List;
import robot_worlds_13.server.robot.Position;

public class SquareObstacle implements Obstacle {
    private int x;
    private int y;
    private int size; // Dynamic size
    private String type; // Dynamic size
    private List<Position> obstacles = new ArrayList<>();
    private Position bottomLeftCorner;

    public SquareObstacle(int x, int y, int size, String type) {
        this.x = x;
        this.y = y;
        this.size = 5;
        this.type = type;
        this.generateObstacle();
    }

    @Override
    public int getBottomLeftX() {
        return this.x;
    }

    @Override
    public int getBottomLeftY() {
        return this.y;
    }

    @Override
    public int getSize() {
        return this.size; // Return dynamic size
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "SquareObstacle{" +
                "x_position=" + this.x +
                ", y_position=" + this.y +
                ", size=" + this.size + // Include size in the string representation
                ", type="+ this.type+
                '}';
    }

    private void generateObstacle() {
        this.bottomLeftCorner = new Position(getBottomLeftX(), getBottomLeftY());
        this.obstacles.add(bottomLeftCorner);
    }

    public List<Position> getObstacles() {
        return this.obstacles;
    }

    @Override
    public boolean blocksPosition(Position position) {
        List<Position> validObstacles = getObstacles();

        if (validObstacles.isEmpty()) {
            return false;
        }

        for (Position obstacle : validObstacles) {
            Position topRightObstacle = new Position(obstacle.getX() + getSize(), obstacle.getY() + getSize());

            if (position.isInObstacle(obstacle, topRightObstacle)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean blocksPath(Position firstPosition, Position secondPosition) {
        for (Position position : getRoutes(firstPosition, secondPosition)) {
            if (blocksPosition(position)) {
                return true;
            }
        }
        return false;
    }

    private List<Position> getRoutes(Position a, Position b) {
        int startX = a.getX();
        int startY = a.getY();
        int endX = b.getX();
        int endY = b.getY();
        List<Position> coordinates = new ArrayList<>();

        if (startX == endX && startY < endY) {
            for (int i = startY; i <= endY; i++) {
                coordinates.add(new Position(startX, i));
            }
        } else if (startX == endX && startY > endY) {
            for (int i = startY; i >= endY; i--) {
                coordinates.add(new Position(startX, i));
            }
        } else if (startX < endX && startY == endY) {
            for (int i = startX; i <= endX; i++) {
                coordinates.add(new Position(i, startY));
            }
        } else if (startX > endX && startY == endY) {
            for (int i = startX; i >= endX; i--) {
                coordinates.add(new Position(i, startY));
            }
        }

        return coordinates;
    }

    public Position getPosition() {
        return new Position(x, y);
    }
}
