package database.orm;

import net.lemnik.eodsql.QueryTool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static database.orm.ConnectDB.worldDAO;

public class OrmDB {
    public static void main(String[] args) throws InterruptedException {
        ConnectDB connectDB = new ConnectDB();
        String worldName = "sinazo";
        String typeName = "dam";
        worldDAO.addType("lake");
        if (worldDAO.countWorldName(worldName) == 0 ){
            if(worldDAO.countTypeByName(typeName) > 0){
                worldDAO.addWorld(worldName, 40,500);
                worldDAO.addObstacle(worldName, 5,5,4,typeName);
            }
            else {System.out.println("'" + typeName + "' does not exist in types table. Failed to Save.");}
        } else{System.out.println("'" + worldName+"' already exists, try another name or 'worlds' to see all saved worlds. Failed to Save.");}

        final List<WorldDO> allWorlds = worldDAO.getAllWorlds();

        System.out.println(worldDAO.getNumberOfWorlds());
        System.out.println("Here is a list of all saved worlds:");
        System.out.println("\t\tName\t\tWidth\t\tHeight");
        allWorlds.forEach(world -> displayWorld(world));
    }

    public static void displayWorld(WorldDO world) {
        System.out.println("\t\t"+world.getPrimaryKey() +"\t\t"+world.getWidth()+"\t\t"+world.getHeight());
    }

}
