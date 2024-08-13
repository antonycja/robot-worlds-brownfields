package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlDB {
    public static void main(String[] args) {

        SqlCommands sqlCommands = new SqlCommands();

        // First create types table
        sqlCommands.createTypesTable("types");
        sqlCommands.insertType("types", "pit");
        sqlCommands.insertType("types", "lake");
        sqlCommands.insertType("types", "mountain");

        // create world table
        sqlCommands.createWorldTable("world");

        // Create obstacle table
        sqlCommands.createObstacleTable("obstacles");

        // Insert a world
        sqlCommands.insertWorld("world", "tom", 50, 5);
        sqlCommands.insertObstacle("obstacles", 5, 5, 4, 1);
        sqlCommands.insertObstacle("obstacles", 5, 5, 4, 2);
        sqlCommands.insertObstacle("obstacles", 5, 5, 4, 1);


        // Another world
        sqlCommands.insertWorld("world", "lutho", 100, 100);
        sqlCommands.insertObstacle("obstacles", 50, 20, 5, 3);
        sqlCommands.insertObstacle("obstacles", 5, 35,5,  3);
        sqlCommands.insertObstacle("obstacles", 2, 5,5,  1);

    }
}
