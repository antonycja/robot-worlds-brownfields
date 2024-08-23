package database.orm;

import net.lemnik.eodsql.QueryTool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static database.orm.ConnectDB.worldDAO;

public class OrmDB {
    public static void main(String[] args) {
        ConnectDB connectDB = new ConnectDB();
//        worldDAO.addType("pit");
//        worldDAO.addWorld("tom", 50,50);
//        worldDAO.addObstacle("tom", 5,5,4,"pit");
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
