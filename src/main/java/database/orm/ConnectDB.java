package database.orm;

import net.lemnik.eodsql.QueryTool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {
    private Connection connection;
     String DBURL = "jdbc:sqlite:database/";
     String DBNAME = "robot_worlds.db";
     public static WorldDAI worldDAO;

     public void connectDB(){
         try {
             connection = DriverManager.getConnection(DBURL + DBNAME);
             worldDAO = QueryTool.getQuery(connection, WorldDAI.class);
             if (connection != null) {
                 System.out.println("A connection to the database has been established.");
                 // Enable foreign key support
                 try (Statement statement = connection.createStatement()) {
                     statement.execute("PRAGMA foreign_keys = ON;");
                 }
                 worldDAO.createWorldTable();
                 worldDAO.createObstacleTable();
                 worldDAO.createTypesTable();
             }

         } catch (Exception e) {
             System.out.println(e.getMessage());
         }
     }
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection to the database has been closed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
