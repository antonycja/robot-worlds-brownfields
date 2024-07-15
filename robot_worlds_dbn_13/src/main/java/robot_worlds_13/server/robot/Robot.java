package robot_worlds_13.server.robot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import robot_worlds_13.server.robot.maze.*;
import robot_worlds_13.server.robot.world.*;

/**
 * Represents a robot in the simulated world.
 * Robots can execute various commands and interact with the environment.
 */
public class Robot {
    private final Position TOP_LEFT;
    private final Position BOTTOM_RIGHT;

    /** The centre position of the world. */
    public static final Position CENTRE = new Position(0,0);

    /** The current position of the robot. */
    public Position position;

    /** The current direction the robot is facing. */
    private IWorld.Direction currentDirection;

    /** The current status of the robot. */
    private String status;

    /** The name of the robot. */
    private String name;

    /** The world in which the robot operates. */
    public AbstractWorld worldData;

    /** The maximum number of shields the robot can have. */
    public int maxShields;

    /** The maximum amount of ammunition the robot can carry. */
    public int maxAmmo;

    /** The current amount of ammunition the robot has. */
    public int ammo;

    /** The current number of shields the robot has. */
    public int shields;

    /** The distance at which the robot's bullets can reach. */
    public int bulletDistance;

    /** The response to be sent to the client. */
    private String responseToClient = "{}";

    /** Indicates if the robot is currently undergoing repairs. */
    private boolean repairing = false;
    private boolean reloading = false;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /** The reload time for the robot's ammunition. */
    private int reloadTime;
    private int repairingTime;
    public Position previouPosition;

    /** The response to be sent to the GUI client. */
    private String responseGUIToClient = "{}";

    /**
     * Constructs a robot with the specified name.
     * Initializes its position, direction, and status.
     * Uses a default text world with a simple maze.
     * @param name The name of the robot.
     */
    public Robot(String name) {
        // Initialize world boundaries
        TOP_LEFT = new Position(-100,200);
        BOTTOM_RIGHT = new Position(100, -200);
        // Initialize robot properties
        this.name = name;
        this.status = "NORMAL";
        this.position = CENTRE;
        this.currentDirection = IWorld.Direction.NORTH;
        this.worldData = new TextWorld(new SimpleMaze());
        this.reloadTime = 5;
        this.repairingTime = 5;
        this.maxAmmo = 5;
        this.ammo = 5;
        this.maxShields = 5;
        this.shields = 5;
        this.bulletDistance = 5;
    }

    public Robot(String name, Position startPosition) {
        // Initialize world boundaries
        TOP_LEFT = new Position(-100,200);
        BOTTOM_RIGHT = new Position(100, -200);
        // Initialize robot properties
        System.out.println(name + " is starting at " + startPosition);
        this.name = name;
        System.out.println(name + " is starting at " + startPosition);
        this.status = "NORMAL";
        System.out.println(this.status + " is starting at " + startPosition);
        this.position = startPosition;
        System.out.println(this.position + " is starting at " + startPosition);
        this.currentDirection = IWorld.Direction.NORTH;
        System.out.println(this.currentDirection + " is starting at " + startPosition);
        this.worldData = new TextWorld(new SimpleMaze());
        System.out.println(this.worldData + " is starting at " + startPosition);
        this.maxAmmo = 5;
        this.ammo = 5;
        this.reloadTime = worldData.ammoReloadTime;
        this.maxShields = 5;
        this.shields = 5;
        this.repairingTime = 5;
        this.bulletDistance = 5;

    }

    public Robot(String name, AbstractWorld worldObject) {
        // Initialize world boundaries
        this.TOP_LEFT = worldObject.TOP_LEFT;
        this.BOTTOM_RIGHT = worldObject.BOTTOM_RIGHT;
        // Initialize robot properties
        this.name = name;
        this.status = "NORMAL";
        this.position = CENTRE;
        this.previouPosition = CENTRE;
        this.currentDirection = IWorld.Direction.NORTH;
        this.worldData = worldObject;
        this.maxAmmo = 5;
        this.ammo = 5;
        this.reloadTime = worldData.ammoReloadTime;
        this.maxShields = 5;
        this.shields = 5;
        this.repairingTime = 5;
        this.bulletDistance = 5;

    }

    public Robot(String name, AbstractWorld worldObject, Position startingPosition) {
        // Initialize world boundaries
        this.TOP_LEFT = worldObject.TOP_LEFT;
        this.BOTTOM_RIGHT = worldObject.BOTTOM_RIGHT;
        // Initialize robot properties
        this.name = name;
        this.status = "NORMAL";
        this.position = startingPosition;
        this.currentDirection = IWorld.Direction.NORTH;
        this.worldData = worldObject;
        this.maxAmmo = 5;
        this.ammo = 5;
        this.reloadTime = worldData.ammoReloadTime;
        this.maxShields = 5;
        this.shields = 5;
        this.repairingTime = 5;
        this.bulletDistance = 5;
        this.previouPosition = startingPosition;
    }

    public Robot(String name, AbstractWorld worldObject, Position startingPosition, Map<String, Integer> robotConfigurable) {
        // Initialize world boundaries
        this.TOP_LEFT = worldObject.TOP_LEFT;
        this.BOTTOM_RIGHT = worldObject.BOTTOM_RIGHT;
        // Initialize robot properties
        this.name = name;
        this.status = "NORMAL";
        this.position = startingPosition;
        this.currentDirection = IWorld.Direction.NORTH;
        this.worldData = worldObject;
        this.maxShields = (robotConfigurable.get("shields") != null) ? robotConfigurable.get("shields") : 5;
        this.maxAmmo = (robotConfigurable.get("shots") != null) ? robotConfigurable.get("shots") : 5;
        this.ammo = maxAmmo;
        this.reloadTime = worldData.ammoReloadTime;
        this.shields = maxShields;
        this.bulletDistance = (robotConfigurable.get("bulletDistance") != null) ? robotConfigurable.get("bulletDistance") : 5;
        this.previouPosition = startingPosition;
        this.repairingTime = (worldObject.hasShieldRepairTime()) ? worldObject.shieldRepairTime : 5;
    }

    /**
     * Retrieves the current status of the robot.
     * @return The current status of the robot.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Retrieves the current direction the robot is facing.
     * @return The current direction of the robot.
     */
    public IWorld.Direction getCurrentDirection() {
        return this.currentDirection;
    }

    /**
     * Retrieves the current position of the robot.
     * @return The current position of the robot.
     */
    public Position getCurrentPosition() {
        return this.position;
    }

    /**
     * Handles the execution of a command by the robot.
     * @param command The command to be executed.
     * @return True if the command was executed successfully, false otherwise.
     */
    public boolean handleCommand(Command command) {
        return command.execute(this);
    }

    /**
     * Updates the position of the robot based on the number of steps and direction.
     * @param nrSteps The number of steps to move.
     * @param towards The direction in which to move ("front" or "back").
     * @return True if the position was updated successfully, false otherwise.
     */
    public boolean updatePosition(int nrSteps, String towards){
        int newX = this.position.getX();
        int newY = this.position.getY();
        this.previouPosition = new Position(newX, newY);

        if (IWorld.Direction.NORTH.equals(this.currentDirection) && towards == "front") {
            newY = newY + nrSteps;
        }
        else if (IWorld.Direction.SOUTH.equals(this.currentDirection) && towards == "front") {
            newY = newY - nrSteps;
        }
        else if (IWorld.Direction.WEST.equals(this.currentDirection) && towards == "front") {
            newX = newX - nrSteps;
        }
        else if (IWorld.Direction.EAST.equals(this.currentDirection) && towards == "front") {
            newX = newX + nrSteps;
        }

        if (IWorld.Direction.NORTH.equals(this.currentDirection) && towards == "back") {
            newY = newY - nrSteps;
        }
        else if (IWorld.Direction.SOUTH.equals(this.currentDirection) && towards == "back") {
            newY = newY + nrSteps;
        }
        else if (IWorld.Direction.WEST.equals(this.currentDirection) && towards == "back") {
            newX = newX + nrSteps;
        }
        else if (IWorld.Direction.EAST.equals(this.currentDirection) && towards == "back") {
            newX = newX - nrSteps;
        }

        Position newPosition = new Position(newX, newY);
        if (newPosition.isIn(TOP_LEFT, BOTTOM_RIGHT)){
            this.position = newPosition;
            return true;
        }
        return false;
    }

    /**
     * Updates the direction of the robot (turns left or right).
     * @param turnTo The direction to turn ("left" or "right").
     * @return True if the direction was updated successfully, false otherwise.
     */
    public boolean updateDirection(String turnTo) {

        if (this.currentDirection == IWorld.Direction.NORTH && turnTo == "left") {
            this.currentDirection = IWorld.Direction.WEST;
        }
        else if (this.currentDirection == IWorld.Direction.WEST && turnTo == "left") {
            this.currentDirection = IWorld.Direction.SOUTH;
        }
        else if (this.currentDirection == IWorld.Direction.SOUTH && turnTo == "left") {
            this.currentDirection = IWorld.Direction.EAST;
        }
        else if (this.currentDirection == IWorld.Direction.EAST && turnTo == "left") {
            this.currentDirection = IWorld.Direction.NORTH;
        }
        else if (this.currentDirection == IWorld.Direction.NORTH && turnTo == "right") {
            this.currentDirection = IWorld.Direction.EAST;
        }
        else if (this.currentDirection == IWorld.Direction.EAST && turnTo == "right") {
            this.currentDirection = IWorld.Direction.SOUTH;
        }
        else if (this.currentDirection == IWorld.Direction.SOUTH && turnTo == "right") {
            this.currentDirection = IWorld.Direction.WEST;
        }
        else if (this.currentDirection == IWorld.Direction.WEST && turnTo == "right") {
            this.currentDirection = IWorld.Direction.NORTH;
        }

        this.previouPosition = position;
        return true;
    }

    /**
     * Returns a string representation of the robot's current state.
     * @return A string representing the robot's current state.
     */
    @Override
    public String toString() {
       return "[" + this.position.getX() + "," + this.position.getY() + "] "
                + this.name + "> " + this.status;
    }

    /**
     * Retrieves the current position of the robot.
     * @return The current position of the robot.
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Sets the status of the robot.
     * @param status The new status to set for the robot.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieves the name of the robot.
     * @return The name of the robot.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the state of the robot as a formatted string.
     * @return A string representing the current state of the robot.
     */
    public String getRobotStateString () {
        String stateMessage = "";
        stateMessage = stateMessage + getPosition().toString() + "\n" +
            currentDirection.toString() + "\n" +
            shields + "\n" +
            ammo + "\n" +
            status + "\n";

        return stateMessage;
    }

    /**
     * Decreases the ammunition count of the robot.
     */
    public void decreaseAmmo () {
        this.ammo -= 1;
    }

    /**
     * Retrieves the current ammunition count of the robot.
     * @return The current ammunition count.
     */
    public int ammoAvailable () {
        return this.ammo;
    }

    /**
     * Decreases the shields of the robot by 1.
     */
    public void decreaseShields () {
        this.shields -= 1;
    }

    /**
     * Sets the response message to be sent to the robot client.
     * @param stateGiven The response message to be sent.
     */
    public void setResponseToRobot (String stateGiven) {
        this.responseToClient = stateGiven;
    }

    /**
     * Retrieves the response message to be sent to the robot client.
     * @return The response message to be sent.
     */
    public String getResponseToRobot () {
        return this.responseToClient;
    }

    /**
     * Retrieves the current state of the robot.
     * @return A map containing the robot's position, direction, shields, shots (ammo), and status.
     */
    public Map<String, Object> getRobotState () {
        Map<String, Object> state = new HashMap<>();
        state.put("position", new int[] {position.getX(), position.getY()});
        state.put("direction", getCurrentDirection());
        state.put("shields", shields);
        state.put("shots", ammo);
        state.put("status", getStatus());

        return state;
    }

    /**
     * Sets the GUI response to be sent to the robot.
     * @param stateGiven The GUI response to be set.
     */
    public void setGUIResponseToRobot (String stateGiven) {
        this.responseGUIToClient = stateGiven;
    }

    /**
     * Retrieves the GUI response to be sent to the robot.
     * @return The GUI response to be sent.
     */
    public String getGUIResponseToRobot () {
        return this.responseGUIToClient;
    }

    /**
     * Retrieves the GUI state of the robot, including its name, previous position, current position, direction, shields, shots, and status.
     * @return A map representing the GUI state of the robot.
     */
    public Map<String, Object> getGUIRobotState () {
        Map<String, Object> state = new HashMap<>();
        state.put("name", this.name);
        state.put("previousPosition", new int[] {previouPosition.getX(), previouPosition.getY()});
        state.put("position", new int[] {position.getX(), position.getY()});
        state.put("direction", getCurrentDirection());
        state.put("shields", shields);
        state.put("shots", ammo);
        state.put("status", getStatus());
        return state;
    }

    /**
     * Retrieves the available shields of the robot.
     * @return The available shields of the robot.
     */
    public int shieldsAvailable () {
        return this.shields;
    }

    /**
     * Retrieves the bullet distance of the robot.
     * @return The bullet distance of the robot.
     */
    public int getBulletDistance() {
        return this.bulletDistance;
    }

    /**
     * Initiates the repair process for the shields of the robot.
     * @param repairTime The time it takes to repair the shields.
     */
    public void repairShields(int repairTime){
        if (!repairing){
            repairing = true;
            status = "REPAIR";
            scheduler.schedule(() -> {
                this.shields = maxShields;
                //Repair shields to max value
                status = "NORMAL";
                repairing = false;
            },repairTime, TimeUnit.SECONDS);
        }
    }

    /**
     * Initiates the reload process for the ammunition of the robot.
     * @param reloadTime The time it takes to reload the ammunition.
     */
    public void reload(int reloadTime) {
        if (!reloading){
            reloading = true;
            status = "RELOAD";
            scheduler.schedule(() -> {
                this.ammo = maxAmmo;
                //Repair shields to max value
                status = "NORMAL";
                reloading = false;
            },reloadTime, TimeUnit.SECONDS);
        }
    }

    /**
     * Retrieves the reload time for the ammunition.
     * @return The reload time in seconds.
     */
    public int getReloadTime() {
        return this.reloadTime;
    }

    /**
     * Sets the reload time for the ammunition.
     * @param reloadTime The reload time to be set in seconds.
     */
    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

    public void setDeadStatus() {
        this.status = "DEAD";
    }

    public int getRepairTime() {
        return this.repairingTime;
    }

    

}