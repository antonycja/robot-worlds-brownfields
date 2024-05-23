package robot_worlds_13.server.robot;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;
    }

    @Override
    public String toString () {
        return "(" + String.valueOf(this.x) + ", " + String.valueOf(this.y) + ")";
    }

    public boolean isIn(Position topLeft, Position bottomRight) {
        boolean withinTop = this.y <= topLeft.getY();
        boolean withinBottom = this.y >= bottomRight.getY();
        boolean withinLeft = this.x >= topLeft.getX();
        boolean withinRight = this.x <= bottomRight.getX();
        return withinTop && withinBottom && withinLeft && withinRight;
    }

    public boolean isInObstacle (Position obstacleBottomLeft, Position topRightObstacle) {
        boolean withinTop = this.y <= topRightObstacle.getY();
        boolean withinBottom = this.y >= obstacleBottomLeft.getY();

        boolean withinLeft = this.x >= obstacleBottomLeft.getX();
        boolean withinRight = this.x <= topRightObstacle.getX();

        return withinTop && withinBottom && withinLeft && withinRight;
    }
}