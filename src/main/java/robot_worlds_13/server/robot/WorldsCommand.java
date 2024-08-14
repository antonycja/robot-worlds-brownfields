package robot_worlds_13.server.robot;

import database.SqlCommands;

public class WorldsCommand {
    public void showWorlds(){
        SqlCommands sqlCommands = new SqlCommands();

        System.out.println("Here is the list of your saved world names:");
        for(String worldName: sqlCommands.getWorlds("world")){
            System.out.println("\t-" + worldName);
        }
    }
}
