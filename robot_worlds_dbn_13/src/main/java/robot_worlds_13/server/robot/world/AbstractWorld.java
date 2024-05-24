package robot_worlds_13.server.robot.world;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import robot_worlds_13.server.robot.maze.*;
import robot_worlds_13.server.robot.Position;
import robot_worlds_13.server.robot.Robot;
import robot_worlds_13.server.*;

public class AbstractWorld implements IWorld {
    private final Position TOP_LEFT = new Position(-100,200);
    private final Position BOTTOM_RIGHT = new Position(100,-200);
    public static final Position CENTRE = IWorld.CENTRE;
    

    public Server serverObject;
    public ArrayList<String> obstacleInStringFormat = new ArrayList<>();
    public int visibility = 10;


    private List<Obstacle> obstacles = new ArrayList<>();
    private final Maze maze;
    private  Direction currentDirection;
    private Position position;
    private String curentRobotName;

    public AbstractWorld (Maze mazeChosen, Server givenServerObject) {
        
        this.obstacles = mazeChosen.getObstacles();
        this.maze = mazeChosen;
        this.position = IWorld.CENTRE;
        this.currentDirection = Direction.UP;
        this.serverObject = givenServerObject;
    }

    public AbstractWorld (Maze mazeChosen) {
        
        this.obstacles = mazeChosen.getObstacles();
        this.maze = mazeChosen;
        this.position = IWorld.CENTRE;
        this.currentDirection = Direction.UP;
    }

    public Maze getMaze(){
        return this.maze;
    }

    @Override
    public UpdateResponse updatePosition(int nrSteps) {
        int newX = this.position.getX();
        int newY = this.position.getY();
        Position positionBeforeUpdate = new Position(newX, newY);


        if (Direction.UP.equals(this.currentDirection)) {
            newY = newY + nrSteps;
        }else if(Direction.RIGHT.equals(this.currentDirection)){
            newX = newX + nrSteps;
        }else  if(Direction.DOWN.equals(this.currentDirection)){
            newY = newY - nrSteps;
        }else if(Direction.LEFT.equals(this.currentDirection)){
            newX = newX - nrSteps;
        }

        
        Position newPosition = new Position(newX, newY);
        // is new position outside world
        if (!newPosition.isIn(TOP_LEFT, BOTTOM_RIGHT)){
            return UpdateResponse.FAILED_OUTSIDE_WORLD;
        }

        // check if new position inside of obstacle
        else if (!isNewPositionAllowed(newPosition)) {
            return UpdateResponse.FAILED_OBSTRUCTED;
        }

        // is position not take by a robot already?
        else if (!isPositionNotOccupiedByRobot(newPosition)){
            return UpdateResponse.FAILED_OBSTRUCTED;
        }

        // checking if path is not blocked by an obstacle in the world
        else if (!isPathClearFromObstacles(positionBeforeUpdate, newPosition)) {
            return UpdateResponse.FAILED_OBSTRUCTED;
        }

        // checking if path is not blocked by another robot in the world
        else if (!isPathNotBlockedByRobot(positionBeforeUpdate, newPosition)) {
            return UpdateResponse.FAILED_OBSTRUCTED;
        }

        // updating the robot position inside the 
        ArrayList<Object> currentState = serverObject.nameRobotMap.get(curentRobotName);
        currentState.set(0, newPosition);
        this.position = newPosition;
            return UpdateResponse.SUCCESS;
    }

    @Override
    public void updateDirection(boolean turnRight) {
        if(Direction.UP.equals(this.currentDirection)){
            if(turnRight) {
                this.currentDirection = Direction.RIGHT;
            }else{
                this.currentDirection = Direction.LEFT;
            }
        }else if(Direction.RIGHT.equals(this.currentDirection)){
            if(turnRight) {
                this.currentDirection = Direction.DOWN;
            }else{
                this.currentDirection = Direction.UP;
            }
        }else  if(Direction.DOWN.equals(this.currentDirection)){
            if(turnRight) {
                this.currentDirection = Direction.LEFT;
            }else{
                this.currentDirection = Direction.RIGHT;
            }
        }else if(Direction.LEFT.equals(this.currentDirection)){
            if(turnRight) {
                this.currentDirection = Direction.UP;
            }else{
                this.currentDirection = Direction.DOWN;
            }
        }

        // updating the direction of the robot inside the hashmap
        ArrayList<Object> currentState = serverObject.nameRobotMap.get(curentRobotName);
        currentState.set(1, this.currentDirection);
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public Direction getCurrentDirection() {
        return this.currentDirection;
    }

    @Override
    public boolean isNewPositionAllowed(Position position) {
        for (Obstacle obstacle: obstacles){
            if(obstacle.blocksPosition(position)){
                return false;
            }
        }
        return true;
    }

    public boolean isPositionNotOccupiedByRobot(Position position) {
        for (String name: serverObject.nameRobotMap.keySet()) {
            if (name.equals(this.curentRobotName)){
                continue;
            }

            ArrayList<Object> currentState = serverObject.nameRobotMap.get(name);
            Position thatRobotsPosition = (Position) currentState.get(0);
            if (thatRobotsPosition.equals(position)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPathNotBlockedByRobot(Position start, Position end) {
        List<Position> path = getRobotPath(start, end);
        for (Position currePosition: path) {
            if (!isPositionNotOccupiedByRobot(currePosition)) {
                return false;
            };
        }
        return true;
    }
    @Override
    public boolean isAtEdge() {

        if(getPosition().getY() == 200 && getPosition().getX() ==0) {
            return true;
        }else if(getPosition().getY() == 0 && getPosition().getX() ==100) {
            return true;
        }else if(getPosition().getY() == -200 && getPosition().getX() ==0) {
            return true;
        }else return getPosition().getY() == 0 && getPosition().getX() == -100;
    }

    @Override
    public void reset() {
        this.currentDirection = Direction.UP;
        this.position = CENTRE;
    }

    @Override
    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    @Override
    public void showObstacles() {
        if(this.obstacles.size() > 0) {
//            System.out.println("There are some obstacles:");
            for(Obstacle obstacle: obstacles ){
                int xBottomLeft = obstacle.getBottomLeftX();
                int yBottomLeft = obstacle.getBottomLeftY();

                int xTopRight = obstacle.getBottomLeftX()+4;
                int yTopRight = obstacle.getBottomLeftY()+4;

                //System.out.printf("- At position %d,%d (to %d,%d)%n", xBottomLeft, yBottomLeft, xTopRight, yTopRight);
                obstacleInStringFormat.add(String.format("- At position %d,%d (to %d,%d)%n", xBottomLeft, yBottomLeft, xTopRight, yTopRight));
            }
        }

    }

    // checks if the path is blocked by obstacles or not
    public boolean isPathClearFromObstacles(Position currentPosition, Position destPosition){
        if(position.isIn(TOP_LEFT,BOTTOM_RIGHT)){
            for(Obstacle obstacle: obstacles){
                if(obstacle.blocksPath(currentPosition, destPosition)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void giveCurrentRobotInfo(Robot target){
        this.position = target.getPosition();
        this.currentDirection = target.getCurrentDirection();
        this.curentRobotName = target.getName();
    }
    
    private List<Position> getRobotPath (Position a, Position b) {
        int startX = a.getX();
        int endX = b.getX();
        
        int startY = a.getY();
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

    public ArrayList<Position> lookAround () {
        int newX = this.position.getX();
        int newY = this.position.getY();
        Position positionBeforeUpdate = new Position(newX, newY);
        
        // obstructions
        ArrayList<Object> obstructions = new ArrayList<>();
        HashMap<String, ArrayList<Object>> mapOfObstructions = new HashMap<>();

        // look up
        List<Position> pathGoingUp = getRobotPath(positionBeforeUpdate, new Position(newX, newY + visibility));
        for (Position currePosition: pathGoingUp) {
            if (!isPositionNotOccupiedByRobot(currePosition)) {
                obstructions.add(currePosition); // add the position
                obstructions.add("Robot");// add the type of obstruction
                if (currentDirection == Direction.UP) { // add the direction
                    mapOfObstructions.put("North", obstructions);
                }
                
                if (currentDirection == Direction.DOWN) { // add the direction
                    mapOfObstructions.put("SOUTH", obstructions);
                }

                if (currentDirection == Direction.RIGHT) { // add the direction
                    mapOfObstructions.put("WEST", obstructions);
                }

                if (currentDirection == Direction.LEFT) { // add the direction
                    mapOfObstructions.put("EAST", obstructions);
                }
                
                break;
            }

            if (!isNewPositionAllowed(currePosition)) {
                obstructions.add(currePosition); // add the position
                obstructions.add("Obstacle");// add the type of obstruction
                if (currentDirection == Direction.UP) { // add the direction
                    mapOfObstructions.put("North", obstructions);
                }

                if (currentDirection == Direction.DOWN) { // add the direction
                    mapOfObstructions.put("SOUTH", obstructions);
                }

                if (currentDirection == Direction.RIGHT) { // add the direction
                    mapOfObstructions.put("WEST", obstructions);
                }

                if (currentDirection == Direction.LEFT) { // add the direction
                    mapOfObstructions.put("EAST", obstructions);
                }
                
                break;
            }
        
        }
        return new ArrayList<>();
    }
}
