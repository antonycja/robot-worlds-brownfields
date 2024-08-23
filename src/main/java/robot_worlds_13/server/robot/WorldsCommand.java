package robot_worlds_13.server.robot;

import database.orm.WorldDO;

import java.util.List;

import static database.orm.ConnectDB.worldDAO;

public class WorldsCommand {

public void showWorlds(){

    try {
        final List<WorldDO> allWorlds = worldDAO.getAllWorlds();
        int numberOfWorlds = worldDAO.getNumberOfWorlds();
        if(numberOfWorlds < 1) {
            System.out.println("\t-No worlds to display. Try saving a world first.");
            return;
        }
        System.out.println("You have " + numberOfWorlds +" saved, Here they are:");
        System.out.printf("%-20s %-10s %-10s%n", "Name", "Width", "Height");
        System.out.println("------------------------------------------------------");
        allWorlds.forEach(world ->
                System.out.printf("%-20s %-10d %-10d%n", world.getPrimaryKey(), world.getWidth(), world.getHeight())
        );
    } catch (Exception e) {
        System.out.println("\t-No worlds to display. Try saving a world first.");
    }


    }
}
