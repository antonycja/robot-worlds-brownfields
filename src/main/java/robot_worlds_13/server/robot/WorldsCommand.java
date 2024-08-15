package robot_worlds_13.server.robot;

import database.SqlCommands;

public class WorldsCommand {
    private final SqlCommands sqlCommands;
    public WorldsCommand(){
        this.sqlCommands = new SqlCommands();

    }
    public void showWorlds(){
//        if(sqlCommands.getWorlds().isEmpty())
        try {
            System.out.println("Here is the list of your saved world names:");
            for(String worldName: sqlCommands.getWorlds("world")){
                System.out.println("\t-" + worldName);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\t-No worlds to display. Try saving a world first.");
        }
    }
}
