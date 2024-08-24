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
        connectDB.connectDB();

        String worldName = "ben";
        String typeName = "lake";
//        System.out.println(worldDAO.getWorldData(worldName).getName());
        worldDAO.addType(typeName);
        if (worldDAO.countWorldName(worldName) == 0 ){
            if(worldDAO.countTypeByName(typeName) > 0){
                worldDAO.addWorld(worldName, 60,40);
                worldDAO.addObstacle(worldName, 3,9,4,typeName);
            }
            else {System.out.println("'" + typeName + "' does not exist in types table. Failed to Save.");}
        } else{System.out.println("'" + worldName+"' already exists, try another name or 'worlds' to see all saved worlds. Failed to Save.");}

        worldDAO.deleteWorld("tom");

        final List<WorldDO> allWorlds = worldDAO.getAllWorlds();
        final List<ObjectsDO> worldObjects = worldDAO.getObjectData(worldName);

        System.out.println(worldDAO.getNumberOfWorlds());
        System.out.println("Here is a list of all saved worlds:");
        System.out.printf("%-20s %-10s %-10s%n", "Name", "Width", "Height");
        System.out.println("------------------------------------------------------");
        allWorlds.forEach(world -> displayWorld(world));

        System.out.println("\n");
        System.out.println("Here is a list of all objects in "+worldName+":");
        System.out.printf("%-20s %-10s %-10s%n", "Type", "Size", "X,Y Position");
        System.out.println("------------------------------------------------------");
        worldObjects.forEach(object -> displayObjects(object));

    }

    public static void displayWorld(WorldDO world) {
        System.out.printf("%-20s %-10d %-10d%n", world.getPrimaryKey(), world.getWidth(), world.getHeight());
    }

    public static void displayObjects(ObjectsDO object) {
        System.out.printf("%-20s %-10d %-10s%n", object.getType(), object.getSize(), (object.getXPosition() +","+object.getYPosition()));
    }

}
