package robot_worlds_13.server.robot;

import database.SqlCommands;
import database.orm.WorldDO;
import robot_worlds_13.server.ServerProtocol;
import robot_worlds_13.server.robot.world.AbstractWorld;
import robot_worlds_13.server.robot.world.Obstacle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static database.orm.ConnectDB.worldDAO;
import static database.orm.OrmDB.displayWorld;

public class DeleteCommand {
    private String worldName;

    public DeleteCommand(String worldName) {
        if (worldName.isEmpty()){
            worldName = promptForWorldName().toLowerCase();
        }
        this.worldName = worldName.toLowerCase();
    }

    public DeleteCommand() {
        this.worldName = promptForWorldName().toLowerCase();
    }

    private String promptForWorldName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a name for the world: ");
        return scanner.nextLine().trim();
    }

    private boolean checkWorldExists(){
        return worldDAO.countWorldName(this.worldName) > 0;
    }

    public void deleteWorld(AbstractWorld world) {
        Map<String, Object> data = new HashMap<>();
        try{
            if (!checkWorldExists()){
                throw new IllegalCallerException("'"+this.worldName + "' does not exist, Enter a valid name.");
            }
            if(world.getName().equals(this.worldName)){world.setName("default");}
            worldDAO.deleteWorld(this.worldName);
            worldDAO.deleteWorldObjects(this.worldName);
            System.out.println("Deleted world with the name: " + this.worldName);
            data.put("message", "World deleted successfully");
            ServerProtocol.buildResponse("OK", data);
            System.out.println(ServerProtocol.buildResponse("OK", data));

        } catch (IllegalCallerException e) {System.out.println(e.getMessage());}

    }
}
