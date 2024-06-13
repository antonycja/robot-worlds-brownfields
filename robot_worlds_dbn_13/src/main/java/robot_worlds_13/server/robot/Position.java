package robot_worlds_13.server.robot;

/**
 * Represents a position in a two-dimensional space, defined by its x and y coordinates.
 */
public class Position {
    private int x;
    private int y;

    /**
     * Constructs a Position object with the specified x and y coordinates.
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retrieves the x-coordinate of the position.
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the position.
     * @param x The new x-coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Retrieves the y-coordinate of the position.
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the position.
     * @param y The new y-coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Checks if this position is equal to another object.
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;
    }

    /**
     * Retrieves a string representation of the position.
     * @return A string in the format "(x, y)" representing the position.
     */
    @Override
    public String toString () {
        return "(" + String.valueOf(this.x) + ", " + String.valueOf(this.y) + ")";
    }

    /**
     * Checks if this position is within a specified rectangular area defined by its top-left and bottom-right positions.
     * @param topLeft The top-left position of the rectangular area.
     * @param bottomRight The bottom-right position of the rectangular area.
     * @return true if this position is within the specified area, false otherwise.
     */
    public boolean isIn(Position topLeft, Position bottomRight) {
        boolean withinTop = this.y <= topLeft.getY();
        boolean withinBottom = this.y >= bottomRight.getY();
        boolean withinLeft = this.x >= topLeft.getX();
        boolean withinRight = this.x <= bottomRight.getX();
        return withinTop && withinBottom && withinLeft && withinRight;
    }

    /**
     * Checks if this position is within a specified rectangular area defined by its bottom-left and top-right positions.
     * @param obstacleBottomLeft The bottom-left position of the rectangular area defining the obstacle.
     * @param topRightObstacle The top-right position of the rectangular area defining the obstacle.
     * @return true if this position is within the obstacle area, false otherwise.
     */
    public boolean isInObstacle (Position obstacleBottomLeft, Position topRightObstacle) {
        boolean withinTop = this.y <= topRightObstacle.getY();
        boolean withinBottom = this.y >= obstacleBottomLeft.getY();

        boolean withinLeft = this.x >= obstacleBottomLeft.getX();
        boolean withinRight = this.x <= topRightObstacle.getX();

        return withinTop && withinBottom && withinLeft && withinRight;
    }
}