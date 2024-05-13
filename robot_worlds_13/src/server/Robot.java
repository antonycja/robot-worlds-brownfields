package server;

import java.util.Arrays;
import java.util.List;

import server.maze.*;
import server.world.*;

public class Robot {
    private final Position TOP_LEFT = new Position(-100,200);
    private final Position BOTTOM_RIGHT = new Position(100,-200);

    public static final Position CENTRE = new Position(0,0);

    private Position position;
    private IWorld.Direction currentDirection;
    private String status;
    private String name;
    public AbstractWorld worldData;

    public Robot(String name) {
        this.name = name;
        this.status = "Ready";
        this.position = CENTRE;
        this.currentDirection = IWorld.Direction.UP;
        this.worldData = new TextWorld(new SimpleMaze());
    }

    public Robot(String name, AbstractWorld worldObject) {
        this.name = name;
        this.status = "Ready";
        this.position = CENTRE;
        this.currentDirection = IWorld.Direction.UP;
        this.worldData = worldObject;
    }

    public String getStatus() {
        return this.status;
    }

    public IWorld.Direction getCurrentDirection() {
        return this.currentDirection;
    }

    public boolean handleCommand(Command command) {
        return command.execute(this);
    }

    public boolean updatePosition(int nrSteps, String towards){
        int newX = this.position.getX();
        int newY = this.position.getY();

        // where towards is either "front" or "back"
        // symbolising whether this function was called by forward / back command

        if (IWorld.Direction.UP.equals(this.currentDirection) && towards == "front") {
            newY = newY + nrSteps;
        }
        else if (IWorld.Direction.DOWN.equals(this.currentDirection) && towards == "front") {
            newY = newY - nrSteps;
        }
        else if (IWorld.Direction.LEFT.equals(this.currentDirection) && towards == "front") {
            newX = newX - nrSteps;
        }
        else if (IWorld.Direction.RIGHT.equals(this.currentDirection) && towards == "front") {
            newX = newX + nrSteps;
        }

        if (IWorld.Direction.UP.equals(this.currentDirection) && towards == "back") {
            newY = newY - nrSteps;
        }
        else if (IWorld.Direction.DOWN.equals(this.currentDirection) && towards == "back") {
            newY = newY + nrSteps;
        }
        else if (IWorld.Direction.LEFT.equals(this.currentDirection) && towards == "back") {
            newX = newX + nrSteps;
        }
        else if (IWorld.Direction.RIGHT.equals(this.currentDirection) && towards == "back") {
            newX = newX - nrSteps;
        }

        Position newPosition = new Position(newX, newY);
        if (newPosition.isIn(TOP_LEFT, BOTTOM_RIGHT)){
            this.position = newPosition;
            return true;
        }
        return false;
    }
    
    // added
    public boolean updateDirection(String turnTo) {
        // System.out.println(turnTo);

        if (this.currentDirection == IWorld.Direction.UP && turnTo == "left") {
            this.currentDirection = IWorld.Direction.LEFT;
        }
        else if (this.currentDirection == IWorld.Direction.LEFT && turnTo == "left") {
            this.currentDirection = IWorld.Direction.DOWN;
        }
        else if (this.currentDirection == IWorld.Direction.DOWN && turnTo == "left") {
            this.currentDirection = IWorld.Direction.RIGHT;
        }
        else if (this.currentDirection == IWorld.Direction.RIGHT && turnTo == "left") {
            this.currentDirection = IWorld.Direction.UP;
        }
        else if (this.currentDirection == IWorld.Direction.UP && turnTo == "right") {
            this.currentDirection = IWorld.Direction.RIGHT;
        }
        else if (this.currentDirection == IWorld.Direction.RIGHT && turnTo == "right") {
            this.currentDirection = IWorld.Direction.DOWN;
        }
        else if (this.currentDirection == IWorld.Direction.DOWN && turnTo == "right") {
            this.currentDirection = IWorld.Direction.LEFT;
        }
        else if (this.currentDirection == IWorld.Direction.LEFT && turnTo == "right") {
            this.currentDirection = IWorld.Direction.UP;
        }
        return true;
    }

    @Override
    public String toString() {
       return "[" + this.position.getX() + "," + this.position.getY() + "] "
               + this.name + "> " + this.status;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
}