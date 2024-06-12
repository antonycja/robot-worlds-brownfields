package robot_worlds_13.server.robot.world;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import robot_worlds_13.server.robot.maze.*;
import robot_worlds_13.server.robot.Position;
import robot_worlds_13.server.robot.Robot;
import robot_worlds_13.server.*;

public class AbstractWorld implements IWorld {
    public final Position TOP_LEFT;
    public final Position BOTTOM_RIGHT;
    public static final Position CENTRE = IWorld.CENTRE;
    

    private static Gson gson = new Gson();
    public Server serverObject;
    public ArrayList<String> obstacleInStringFormat = new ArrayList<>();

    Random rand = new Random();


    private List<Obstacle> obstacles = new ArrayList<>();
    private List<Obstacle> bottomLessPits = new ArrayList<>();
    private List<Obstacle> lakes = new ArrayList<>();

    private final Maze maze;
    private  Direction currentDirection;
    private Position position;
    private String curentRobotName;
    private int robotHitBoxFromCenter;
    
    // configure
    public int visibility;
    public int ammoReloadTime;
    public int shieldRepairTime;
    public int maximumShieldStrength;
    public int maximumShots;
    public int width;
    public int height;

    public AbstractWorld (Maze mazeChosen, Server givenServerObject, Map<String, Integer> mapConfigurables) {
        this.obstacles = mazeChosen.getObstacles();
        this.bottomLessPits = mazeChosen.getBottomLessPits();
        this.lakes = mazeChosen.getLakes();


        this.robotHitBoxFromCenter = obstacles.get(0).getSize() / 2;
        this.maze = mazeChosen;
        this.position = IWorld.CENTRE;
        this.currentDirection = Direction.NORTH;
        this.serverObject = givenServerObject;

        // configured from server
        this.width = (mapConfigurables.get("width") != null) ? mapConfigurables.get("width") : 400;
        this.height = (mapConfigurables.get("height") != null) ? mapConfigurables.get("height") : 800;
        
        TOP_LEFT = new Position(-(width / 2),(height / 2));
        BOTTOM_RIGHT = new Position((width / 2),-(height / 2));
        
        this.visibility = (mapConfigurables.get("visibility") != null) ? mapConfigurables.get("visibility") : 100;
        this.ammoReloadTime = (mapConfigurables.get("reload") != null) ? mapConfigurables.get("reload") : 5;
        this.shieldRepairTime = (mapConfigurables.get("repair") != null) ? mapConfigurables.get("repair") : 5 ;
        this.maximumShieldStrength = (mapConfigurables.get("shields") != null) ? mapConfigurables.get("shields") : 5;
        this.maximumShots = (mapConfigurables.get("shots") != null) ? mapConfigurables.get("shots") : 5;
    }

    public AbstractWorld (Maze mazeChosen) {

        TOP_LEFT = new Position(-100,200);
        BOTTOM_RIGHT = new Position(100, -200);
        
        this.obstacles = mazeChosen.getObstacles();
        this.bottomLessPits = mazeChosen.getBottomLessPits();
        this.lakes = mazeChosen.getLakes();
        
        this.robotHitBoxFromCenter = obstacles.get(0).getSize() / 2;
        this.maze = mazeChosen;
        this.position = IWorld.CENTRE;
        this.currentDirection = Direction.NORTH;

        this.visibility = 10;
        this.ammoReloadTime = 5;
        this.shieldRepairTime = 5;
        this.maximumShieldStrength = 5;
        this.maximumShots = 5;
    }

    public Maze getMaze() {
        return this.maze;
    }

    @Override
    public UpdateResponse updatePosition(int nrSteps) {
        int newX = this.position.getX();
        int newY = this.position.getY();
        Position positionBeforeUpdate = new Position(newX, newY);

        if (nrSteps >= 0) {
            if (Direction.NORTH.equals(this.currentDirection)) {
                newY = newY + nrSteps;
            }else if(Direction.EAST.equals(this.currentDirection)){
                newX = newX + nrSteps;
            }else  if(Direction.SOUTH.equals(this.currentDirection)){
                newY = newY - nrSteps;
            }else if(Direction.WEST.equals(this.currentDirection)){
                newX = newX - nrSteps;
            }
        } else {
            if (Direction.NORTH.equals(this.currentDirection)) {
                newY = newY + nrSteps;
            }else if(Direction.EAST.equals(this.currentDirection)){
                newX = newX + nrSteps;
            }else  if(Direction.SOUTH.equals(this.currentDirection)){
                newY = newY - nrSteps;
            }else if(Direction.WEST.equals(this.currentDirection)){
                newX = newX - nrSteps;
            }
        }
            

        
        Position newPosition = new Position(newX, newY);
        // is new position outside world
        if (!newPosition.isIn(TOP_LEFT, BOTTOM_RIGHT)){
            return UpdateResponse.FAILED_OUTSIDE_WORLD;
        }

        else if (isInBottomLessPit(newPosition)) {
            return UpdateResponse.FAILED_DEAD;
        }

        else if (!isPathClearFromPits(positionBeforeUpdate, newPosition)) {
            return UpdateResponse.FAILED_DEAD;
        }

        // check if new position inside of obstacle
        else if (!isNewPositionAllowed(newPosition)) {
            return UpdateResponse.FAILED_OBSTRUCTED;
        }

        // is position not take by a robot already?
        else if (!isPositionNotOccupiedByRobot(newPosition)){
            return UpdateResponse.FAILED_OBSTRUCTED_BY_ROBOT;
        }

        // checking if path is not blocked by an obstacle in the world
        else if (!isPathClearFromObstacles(positionBeforeUpdate, newPosition)) {
            return UpdateResponse.FAILED_OBSTRUCTED;
        }

        // checking if path is not blocked by another robot in the world
        else if (!isPathNotBlockedByRobot(positionBeforeUpdate, newPosition)) {
            return UpdateResponse.FAILED_OBSTRUCTED_BY_ROBOT;
        }

        // updating the robot position inside the 
        ArrayList<Object> currentState = serverObject.nameRobotMap.get(curentRobotName);
        currentState.set(0, newPosition);
        this.position = newPosition;
            return UpdateResponse.SUCCESS;
    }

    @Override
    public void updateDirection(boolean turnRight) {
        if(Direction.NORTH.equals(this.currentDirection)){
            if(turnRight) {
                this.currentDirection = Direction.EAST;
            }else{
                this.currentDirection = Direction.WEST;
            }
        }else if(Direction.EAST.equals(this.currentDirection)){
            if(turnRight) {
                this.currentDirection = Direction.SOUTH;
            }else{
                this.currentDirection = Direction.NORTH;
            }
        }else  if(Direction.SOUTH.equals(this.currentDirection)){
            if(turnRight) {
                this.currentDirection = Direction.WEST;
            }else{
                this.currentDirection = Direction.EAST;
            }
        }else if(Direction.WEST.equals(this.currentDirection)){
            if(turnRight) {
                this.currentDirection = Direction.NORTH;
            }else{
                this.currentDirection = Direction.SOUTH;
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

    //@Override
    public boolean isNewPositionAllowed1(Position position) {
        for (Obstacle obstacle: obstacles){
            if(obstacle.blocksPosition(position)){
                return false;
            }
        }

        for (Obstacle obstacle: bottomLessPits){
            if(obstacle.blocksPosition(position)){
                return false;
            }
        }

        for (Obstacle obstacle: lakes){
            if(obstacle.blocksPosition(position)){
                return false;
            }
        }
        return true;
    }

    public boolean isInBottomLessPit (Position position) {
        int robotSize = robotHitBoxFromCenter;

        for (Obstacle obstacle: bottomLessPits){
            if(obstacle.blocksPosition(position)){
                return true;
            }
        }
        // Calculate the corners of the robot's outer perimeter
        Position topLeft = new Position(position.getX() - robotSize / 2, position.getY() + robotSize / 2);
        Position bottomRight = new Position(position.getX() + robotSize / 2, position.getY() - robotSize / 2);

        for (Obstacle obstacle: bottomLessPits){
            if (obstacle.blocksPosition(topLeft) || 
                obstacle.blocksPosition(bottomRight) ||
                obstacle.blocksPosition(new Position(topLeft.getX(), bottomRight.getY())) ||
                obstacle.blocksPosition(new Position(bottomRight.getX(), topLeft.getY()))) {
                return true; // Collision detected
            }
        }
        return false;

    }

    @Override
    public boolean isNewPositionAllowed(Position position) {
        int robotSize = robotHitBoxFromCenter; // Size of the robot's outer perimeter

        for (Obstacle obstacle: obstacles){
            if(obstacle.blocksPosition(position)){
                return false;
            }
        }

        for (Obstacle obstacle: lakes){
            if(obstacle.blocksPosition(position)){
                return false;
            }
        }
    
        // Calculate the corners of the robot's outer perimeter
        Position topLeft = new Position(position.getX() - robotSize / 2, position.getY() + robotSize / 2);
        Position bottomRight = new Position(position.getX() + robotSize / 2, position.getY() - robotSize / 2);
        
        // Check each corner of the robot's perimeter against obstacles
        for (Obstacle obstacle : obstacles) {
            if (obstacle.blocksPosition(topLeft) || 
                obstacle.blocksPosition(bottomRight) ||
                obstacle.blocksPosition(new Position(topLeft.getX(), bottomRight.getY())) ||
                obstacle.blocksPosition(new Position(bottomRight.getX(), topLeft.getY()))) {
                return false; // Collision detected
            }
        }

        for (Obstacle obstacle: lakes){
            if (obstacle.blocksPosition(topLeft) || 
                obstacle.blocksPosition(bottomRight) ||
                obstacle.blocksPosition(new Position(topLeft.getX(), bottomRight.getY())) ||
                obstacle.blocksPosition(new Position(bottomRight.getX(), topLeft.getY()))) {
                return false; // Collision detected
            }
        }
    
        return true; // No collision detected
    }

    public boolean isPositionNotOccupiedByRobot1(Position position) {
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

    public boolean isPositionNotOccupiedByRobot(Position position) {
        int currentRobotX = position.getX();
        int currentRobotY = position.getY();
        int currentRobotSize = robotHitBoxFromCenter; // Size of the current robot's hit box
        
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

        // Calculate the corners of the current robot's hit box
        Position currentRobotTopLeft = new Position(currentRobotX - currentRobotSize / 2, currentRobotY + currentRobotSize / 2);
        Position currentRobotBottomRight = new Position(currentRobotX + currentRobotSize / 2, currentRobotY - currentRobotSize / 2);
    
        // Iterate through other robots to check for overlaps
        for (String name : serverObject.nameRobotMap.keySet()) {
            if (name.equals(this.curentRobotName)) {
                continue;
            }
    
            ArrayList<Object> currentState = serverObject.nameRobotMap.get(name);
            Position otherRobotPosition = (Position) currentState.get(0);
            int otherRobotSize = robotHitBoxFromCenter; // Size of other robot's hit box
    
            // Calculate the corners of the other robot's hit box
            Position otherRobotTopLeft = new Position(otherRobotPosition.getX() - otherRobotSize / 2, otherRobotPosition.getY() + otherRobotSize / 2);
            Position otherRobotBottomRight = new Position(otherRobotPosition.getX() + otherRobotSize / 2, otherRobotPosition.getY() - otherRobotSize / 2);
    
            // Check for overlap between hit boxes
            if (doHitBoxesOverlap(currentRobotTopLeft, currentRobotBottomRight, otherRobotTopLeft, otherRobotBottomRight)) {
                return false; // Position is occupied
            }
        }
        return true; // Position is not occupied
    }
    
    private boolean doHitBoxesOverlap(Position topLeft1, Position bottomRight1, Position topLeft2, Position bottomRight2) {
        // Check if the hit boxes overlap in the x-axis and y-axis
        return (topLeft1.getX() < bottomRight2.getX() &&
                bottomRight1.getX() > topLeft2.getX() &&
                topLeft1.getY() > bottomRight2.getY() &&
                bottomRight1.getY() < topLeft2.getY());
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
        this.currentDirection = Direction.NORTH;
        this.position = CENTRE;
    }

    @Override
    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Obstacle> getBottomLessPits() {
        return bottomLessPits;
    }

    public List<Obstacle> getLakes() {
        return lakes;
    }

    @Override
    public void showObstacles() {
        
    }

    public boolean hasShieldRepairTime(){
        //Assuming shieldRepairTime is initialized to 0 by default
        return shieldRepairTime != 0;
    }
    
    public List<String> getObstaclesAsString() {
        List<String> message = new ArrayList<String>();
        
        if(this.obstacles.size() > 0) {
            for(Obstacle obstacle: obstacles ){
                int xBottomLeft = obstacle.getBottomLeftX();
                int yBottomLeft = obstacle.getBottomLeftY();

                int xTopRight = obstacle.getBottomLeftX() + obstacle.getSize();
                int yTopRight = obstacle.getBottomLeftY() + obstacle.getSize();

                //System.out.printf("- At position %d,%d (to %d,%d)%n", xBottomLeft, yBottomLeft, xTopRight, yTopRight);
                message.add(String.format("- At position %d,%d (to %d,%d)%n", xBottomLeft, yBottomLeft, xTopRight, yTopRight));
            }
        }

        if(this.bottomLessPits.size() > 0) {
            for(Obstacle obstacle: bottomLessPits ){
                int xBottomLeft = obstacle.getBottomLeftX();
                int yBottomLeft = obstacle.getBottomLeftY();

                int xTopRight = obstacle.getBottomLeftX() + obstacle.getSize();
                int yTopRight = obstacle.getBottomLeftY() + obstacle.getSize();

                //System.out.printf("- At position %d,%d (to %d,%d)%n", xBottomLeft, yBottomLeft, xTopRight, yTopRight);
                message.add(String.format("- At position %d,%d (to %d,%d)%n", xBottomLeft, yBottomLeft, xTopRight, yTopRight));
            }
        }

        if(this.lakes.size() > 0) {
            for(Obstacle obstacle: lakes ){
                int xBottomLeft = obstacle.getBottomLeftX();
                int yBottomLeft = obstacle.getBottomLeftY();

                int xTopRight = obstacle.getBottomLeftX() + obstacle.getSize();
                int yTopRight = obstacle.getBottomLeftY() + obstacle.getSize();

                //System.out.printf("- At position %d,%d (to %d,%d)%n", xBottomLeft, yBottomLeft, xTopRight, yTopRight);
                message.add(String.format("- At position %d,%d (to %d,%d)%n", xBottomLeft, yBottomLeft, xTopRight, yTopRight));
            }
        }
        return message;

    }

    // checks if the path is blocked by obstacles or not
    public boolean isPathClearFromObstacles(Position currentPosition, Position destPosition){
        if(position.isIn(TOP_LEFT,BOTTOM_RIGHT)){
            for(Obstacle obstacle: obstacles){
                if(obstacle.blocksPath(currentPosition, destPosition)){
                    return false;
                }
            }

            for(Obstacle obstacle: bottomLessPits){
                if(obstacle.blocksPath(currentPosition, destPosition)){
                    return false;
                }
            }

            for(Obstacle obstacle: lakes){
                if(obstacle.blocksPath(currentPosition, destPosition)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isPathClearFromPits(Position currentPosition, Position destPosition) {
        for(Obstacle obstacle: bottomLessPits){
            if(obstacle.blocksPath(currentPosition, destPosition)){
                return false;
            }
        }
        return true;
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

    public ArrayList<Object> lookAround () {
        // for each firection up, down, left, right add the first obstacle / robot it sees to a list
        // 
        int newX = this.position.getX();
        int newY = this.position.getY();
        Position positionBeforeUpdate = new Position(newX, newY);

        // steps away
        int stepsAway = 0;
        
        // obstructions
        ArrayList<Object> obstructions = new ArrayList<>();

        // look NORTH OF THE CURRENT POSITION
        List<Position> pathGoingUp = getRobotPath(positionBeforeUpdate, new Position(newX, newY + visibility));
        for (Position currePosition: pathGoingUp) {
            stepsAway = getStepsAway(currePosition, positionBeforeUpdate);
            if (currePosition.equals(positionBeforeUpdate)) {
                continue;
            }
            if (!isPositionNotOccupiedByRobot(currePosition)) {
                if (currentDirection == Direction.NORTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "NORTH",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.SOUTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "SOUTH",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.EAST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "WEST",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.WEST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "EAST",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                break;
            }

            if (!isNewPositionAllowed(currePosition)) {
                if (currentDirection == Direction.NORTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "NORTH",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.SOUTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "SOUTH",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.EAST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "WEST",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.WEST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "EAST",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                
                break;
            }
        }

        // look down
        List<Position> pathGoingDown = getRobotPath(positionBeforeUpdate, new Position(newX, newY - visibility));
        for (Position currePosition: pathGoingDown) {
            if (currePosition.equals(positionBeforeUpdate)) {
                continue;
            }
            stepsAway = getStepsAway(currePosition, positionBeforeUpdate);
            if (!isPositionNotOccupiedByRobot(currePosition)) {
                if (currentDirection == Direction.NORTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "SOUTH",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.SOUTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "NORTH",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.EAST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "WEST",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.WEST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "EAST",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                
                break;
            }

            if (!isNewPositionAllowed(currePosition)) {
                if (currentDirection == Direction.NORTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "SOUTH",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.SOUTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "NORTH",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.EAST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "WEST",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }

                if (currentDirection == Direction.WEST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "EAST",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                break;
            }
        }

        // look east of current direction
        List<Position> pathGoingRight = getRobotPath(positionBeforeUpdate, new Position(newX + visibility, newY));
        for (Position currePosition: pathGoingRight) {
            if (currePosition.equals(positionBeforeUpdate)) {
                continue;
            }
            stepsAway = getStepsAway(currePosition, positionBeforeUpdate);
            if (!isPositionNotOccupiedByRobot(currePosition)) {
                obstructions.add("Robot");// add the type of obstruction
                if (currentDirection == Direction.NORTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "EAST",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.SOUTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "WEST",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.EAST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "NORTH",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.WEST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "SOUTH",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                
                break;
            }

            if (!isNewPositionAllowed(currePosition)) {
                if (currentDirection == Direction.NORTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "EAST",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.SOUTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "WEST",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.EAST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "NORTH",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.WEST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "SOUTH",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                break;
            }
        }

        // 
        // look WEST
        List<Position> pathGoingLeft = getRobotPath(positionBeforeUpdate, new Position(newX - visibility, newY));
        for (Position currePosition: pathGoingLeft) {
            if (currePosition.equals(positionBeforeUpdate)) {
                continue;
            }
            stepsAway = getStepsAway(currePosition, positionBeforeUpdate);
            if (!isPositionNotOccupiedByRobot(currePosition)) {
                if (currentDirection == Direction.NORTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "WEST",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.SOUTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "EAST",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.EAST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "SOUTH",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.WEST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "NORTH",
                        "type", "Robot",
                        "distance", stepsAway)));
                }
                
                break;
            }

            if (!isNewPositionAllowed(currePosition)) {
                if (currentDirection == Direction.NORTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "WEST",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.SOUTH) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "EAST",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.EAST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "SOUTH",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                if (currentDirection == Direction.WEST) { // add the direction
                    obstructions.add(gson.toJson(Map.of(
                        "direction", "NORTH",
                        "type", "Obstacle",
                        "distance", stepsAway)));
                }
                break;
            }
        } 

        return obstructions;
    }

    public Robot isHit (int bulletDistance) {
        Position startPosition = position;
        Position endPosition;
        if (currentDirection == Direction.NORTH) {
            endPosition = new Position(position.getX(), position.getY() + bulletDistance);
        } else if (currentDirection == Direction.SOUTH) {
            endPosition = new Position(position.getX(), position.getY() - bulletDistance);
        } else if (currentDirection == Direction.WEST) {
            endPosition = new Position(position.getX() - bulletDistance, position.getY());
        } else {
            endPosition = new Position(position.getX() + bulletDistance, position.getY());
        }

        List<Position> bulletPath = getRobotPath(startPosition, endPosition);
        for (Position currentBulletPosition: bulletPath) {
            for(Obstacle obstacle: obstacles){
                if(obstacle.blocksPosition(currentBulletPosition)){
                    // has hit an obstacle, its immedietly a miss
                    return new Robot("NonValid", currentBulletPosition);
                }
            }


            for (String name: serverObject.nameRobotMap.keySet()) {
                if (name.equals(this.curentRobotName)){
                    continue;
                }
                ArrayList<Object> currentState = serverObject.nameRobotMap.get(name);
                Position thatRobotsPosition = (Position) currentState.get(0);
                // has hit another robot, immedietly a hit
                // decrease the robots health;
                if (thatRobotsPosition.equals(currentBulletPosition)) {
                    Robot thatRobot = (Robot) currentState.get(2);
                    thatRobot.decreaseShields();
                    // set the robot as a global variable
                    return thatRobot;
                }
            }
        }
        return new Robot("NonValid", endPosition);
    }

    private Position getRandomPosition () {
        int maxY = (height / 2);
        int minY = -(height / 2);
        int coordinateY = roundToMultiple(rand.nextInt((maxY - minY) + 1) + minY, 10);

        int maxX = (width / 2);
        int minX = -(width / 2);
        int coordinateX = roundToMultiple(rand.nextInt((maxX - minX) + 1) + minX, 10);

        return new Position(coordinateX, coordinateY);
    }

    private int roundToMultiple(int number, int multiple) {
        return ((number + multiple / 2) / multiple) * multiple;
    }

    private boolean isPositionOccupied (Position destinationPosition) {
        if (!isNewPositionAllowed(destinationPosition)) {
            return true;
        } else if (!isPositionNotOccupiedByRobot(destinationPosition)) {
            return true;
        }
        else {
            return false;
        }
    }

    public Position generatePosition() {
        while (true) {
            Position generatedPosition = getRandomPosition();
            if (isPositionOccupied(generatedPosition)) {
                continue;
            }
            else {
                return generatedPosition;
            }
        }
    }

    public int getStepsAway (Position start, Position end) {
        int startX = start.getX();
        int startY = start.getY();

        int endX = end.getX();
        int endY = end.getY();

        if (startX != endX) {
            int difference = Math.abs(startX) - Math.abs(endX);
            return Math.abs(difference);
        } else {
            int difference = Math.abs(startY) - Math.abs(endY);
            return Math.abs(difference);
        }
    }



}
