package robot_worlds_13.server.robot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import robot_worlds_13.server.robot.maze.*;
import robot_worlds_13.server.robot.world.*;

public class Robot {
    private final Position TOP_LEFT;
    private final Position BOTTOM_RIGHT;

    public static final Position CENTRE = new Position(0,0);

    public Position position;
    private IWorld.Direction currentDirection;
    private String status;
    private String name;
    public AbstractWorld worldData;
    public int maxShields;
    public int maxAmmo;
    public int ammo;
    public int shields;
    public int bulletDistance;
    private String responseToClient = "{}";
    private boolean repairing = false;
    private boolean reloading = false;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private int reloadTime;
    private int repairingTime;
    public Position previouPosition;
    private String responseGUIToClient = "{}";

    public Robot(String name) {
        TOP_LEFT = new Position(-100,200);
        BOTTOM_RIGHT = new Position(100, -200);
        this.name = name;
        this.status = "NORMAL";
        this.position = CENTRE;
        this.currentDirection = IWorld.Direction.NORTH;
        this.worldData = new TextWorld(new SimpleMaze());
        this.reloadTime = 5;
        this.repairingTime = 5;
    }

    public Robot(String name, Position startPosition) {
        TOP_LEFT = new Position(-100,200);
        BOTTOM_RIGHT = new Position(100, -200);
        this.name = name;
        this.status = "NORMAL";
        this.position = startPosition;
        this.currentDirection = IWorld.Direction.NORTH;
        this.worldData = new TextWorld(new SimpleMaze());
        this.reloadTime = 5;
        this.repairingTime = 5;
    }

    public Robot(String name, AbstractWorld worldObject) {
        this.TOP_LEFT = worldObject.TOP_LEFT;
        this.BOTTOM_RIGHT = worldObject.BOTTOM_RIGHT;
        this.name = name;
        this.status = "NORMAL";
        this.position = CENTRE;
        this.previouPosition = CENTRE;
        this.currentDirection = IWorld.Direction.NORTH;
        this.worldData = worldObject;
        this.reloadTime = 5;
        this.repairingTime = 5;

    }

    public Robot(String name, AbstractWorld worldObject, Position startingPosition) {
        this.TOP_LEFT = worldObject.TOP_LEFT;
        this.BOTTOM_RIGHT = worldObject.BOTTOM_RIGHT;
        this.name = name;
        this.status = "NORMAL";
        this.position = startingPosition;
        this.currentDirection = IWorld.Direction.NORTH;
        this.worldData = worldObject;
        this.maxAmmo = 5;
        this.ammo = 5;
        this.maxShields = 5;
        this.shields = 5;
        this.bulletDistance = 5;
        this.previouPosition = startingPosition;
        this.repairingTime = 5;
    }

    public Robot(String name, AbstractWorld worldObject, Position startingPosition, Map<String, Integer> robotConfigurable) {
        this.TOP_LEFT = worldObject.TOP_LEFT;
        this.BOTTOM_RIGHT = worldObject.BOTTOM_RIGHT;
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
        this.bulletDistance = (robotConfigurable.get("bulletDistance") != null) ? robotConfigurable.get("bulletDistance") : 5;;
        this.previouPosition = startingPosition;
        this.repairingTime = (worldObject.hasShieldRepairTime()) ? worldObject.shieldRepairTime : 5;
        
        

    }

    public String getStatus() {
        return this.status;
    }

    public IWorld.Direction getCurrentDirection() {
        return this.currentDirection;
    }

    public Position getCurrentPosition() {
        return this.position;
    }

    public boolean handleCommand(Command command) {
        return command.execute(this);
    }

    public boolean updatePosition(int nrSteps, String towards){
        int newX = this.position.getX();
        int newY = this.position.getY();
        this.previouPosition = new Position(newX, newY);

        // where towards is either "front" or "back"
        // symbolising whether this function was called by forward / back command

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
    
    // added
    public boolean updateDirection(String turnTo) {
        // System.out.println(turnTo);

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

    public String getRobotStateString () {
        String stateMessage = "";
        stateMessage = stateMessage + getPosition().toString() + "\n" +
            currentDirection.toString() + "\n" +
            shields + "\n" +
            ammo + "\n" +
            status + "\n";

        return stateMessage;
    }

    // in relation to fire command
    public void decreaseAmmo () {
        this.ammo -= 1;
    }

    public int ammoAvailable () {
        return this.ammo;
    }

    public void decreaseShields () {
        this.shields -= 1;
    }

    public void setResponseToRobot (String stateGiven) {
        this.responseToClient = stateGiven;
    }

    public String getResponseToRobot () {
        return this.responseToClient;
    }

    public Map<String, Object> getRobotState () {
        Map<String, Object> state = new HashMap<>();
        state.put("position", new int[] {position.getX(), position.getY()});
        state.put("direction", getCurrentDirection());
        state.put("shields", shields);
        state.put("shots", ammo);
        state.put("status", getStatus());

        return state;
    }

    public void setGUIResponseToRobot (String stateGiven) {
        this.responseGUIToClient = stateGiven;
    }

    public String getGUIResponseToRobot () {
        return this.responseGUIToClient;
    }

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

    public int shieldsAvailable () {
        return this.shields;
    }

    public int getBulletDistance() {
        return this.bulletDistance;
    }

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

    public int getReloadTime() {
        return this.reloadTime;
    }

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