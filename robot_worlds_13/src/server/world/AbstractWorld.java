package server.world;

import java.util.List;
import java.util.ArrayList;

import server.Position;
import server.Robot;
import server.maze.*;

public class AbstractWorld implements IWorld {
    private final Position TOP_LEFT = new Position(-100,200);
    private final Position BOTTOM_RIGHT = new Position(100,-200);
    public static final Position CENTRE = IWorld.CENTRE;


    private List<Obstacle> obstacles = new ArrayList<>();
    private final Maze maze;
    private  Direction currentDirection;
    private Position position;

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
        if (!newPosition.isIn(TOP_LEFT, BOTTOM_RIGHT)){
            return UpdateResponse.FAILED_OUTSIDE_WORLD;
        }

        else if (!isNewPositionAllowed(newPosition)) {
            return UpdateResponse.FAILED_OBSTRUCTED;
        }

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
        if(!position.isIn(TOP_LEFT,BOTTOM_RIGHT)) {
            return false;
        }
        
        for (Obstacle obstacle: obstacles){
            if(obstacle.blocksPosition(position)){
                return false;
            }
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
            System.out.println("There are some obstacles:");
            for(Obstacle obstacle: obstacles ){
                int xBottomLeft = obstacle.getBottomLeftX();
                int yBottomLeft = obstacle.getBottomLeftY();

                int xTopRight = obstacle.getBottomLeftX()+4;
                int yTopRight = obstacle.getBottomLeftY()+4;

                System.out.printf("- At position %d,%d (to %d,%d)%n", xBottomLeft, yBottomLeft, xTopRight, yTopRight);
            }
        }

    }

    public boolean isPathBlocked(Position currentPosition, Position destPosition){
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
    
    
}
