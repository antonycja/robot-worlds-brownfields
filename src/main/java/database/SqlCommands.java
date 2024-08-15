package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlCommands {
    private final String DBURL = "jdbc:sqlite:database/";
    private final String DBNAME = "robot_worlds.db";
    private String worldName;
    private String typesTableName;
    private String worldTableName;
    private String obstaclesTableName;


    private Connection conn;
    private int width;
    private Object height;

    public SqlCommands() {
        try {
            this.conn = DriverManager.getConnection(DBURL + DBNAME);
            if (this.conn != null) {
                System.out.println("A connection to the database has been established.");
                // Enable foreign key support
                try (Statement statement = conn.createStatement()) {
                    statement.execute("PRAGMA foreign_keys = ON;");
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // General method to execute a SQL command
    private void executeSQL(String sql) {
        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);
//            System.out.println("SQL command executed successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to close the connection
    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Connection to the database has been closed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Create table for storing the worlds
    public void createWorldTable(String tableName) {
        this.worldTableName = tableName;
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + " name text PRIMARY KEY,\n"
                + " width integer NOT NULL,\n"
                + " height integer NOT NULL\n"
                + ");";
        executeSQL(sql);
    }

    // Create table for obstacles
    public void createObstacleTable(String tableName) {
        this.obstaclesTableName = tableName;
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + " _id integer PRIMARY KEY,\n"
                + " world_name text NOT NULL,\n"
                + " x_position integer NOT NULL,\n"
                + " y_position integer NOT NULL,\n"
                + " size integer NOT NULL,\n"
                + " type integer NOT NULL,\n"
                + " FOREIGN KEY (type) REFERENCES " + typesTableName + "(_id)\n" // Foreign key reference
                + ");";
        executeSQL(sql);
    }

    // Create table for keeping the types of objects in the world
    public void createTypesTable(String tableName) {
        this.typesTableName = tableName;
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + " _id integer PRIMARY KEY,\n"
                + " type text UNIQUE\n"
                + ");";
        executeSQL(sql);
    }

    // Insert a new world record
    public void insertWorld(String name, int width, int height) {
        this.worldName = name;
        String sql = "INSERT INTO " + this.worldTableName + " (name, width, height) VALUES(?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, width);
            statement.setInt(3, height);
            statement.executeUpdate();

//            System.out.println("Record inserted into table '" + tableName + "'.");
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            System.out.println(e.getErrorCode());
            if (e.getErrorCode() != 19) {
                throw new IllegalArgumentException();
            }
        }
    }

    // Insert a new obstacle record
    public void insertObstacle(int xPosition, int yPosition, int size, int type) {
        String sql = "INSERT INTO " + this.obstaclesTableName + " (world_name, x_position, y_position, size, type) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, this.worldName);
            statement.setInt(2, xPosition);
            statement.setInt(3, yPosition);
            statement.setInt(4, size);
            statement.setInt(5, type);
            statement.executeUpdate();
//            System.out.println("Record inserted into table '" + tableName + "'.");
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
        }
    }

    // Insert a new type record
    public void insertType(String type) {
        String sql = "INSERT INTO " + this.typesTableName + " (type) VALUES(?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, type);
            statement.executeUpdate();
//            System.out.println("Record inserted into table '" + tableName + "'.");
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
        }
    }

    // Get worlds
    public ArrayList<String> getWorlds(String worldTableName) {
        ArrayList<String> worlds = new ArrayList<>();
        String sql = "SELECT name FROM " + worldTableName;

        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                worlds.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        return worlds;
    }


    // Get world data
    public Map<String, Object> restoreWorldData(String worldTableName, String obstacleTableName, String typeTableName, String worldName) {
        String sql = "SELECT " +
                worldTableName + ".name, " + worldTableName + ".width, " + worldTableName + ".height, " +
                obstacleTableName + ".x_position, " + obstacleTableName + ".y_position, " +
                obstacleTableName + ".size, " + typeTableName + ".type " +
                "FROM " + worldTableName +
                " JOIN " + obstacleTableName + " ON " + worldTableName + ".name = " + obstacleTableName + ".world_name " +
                " JOIN " + typeTableName + " ON " + obstacleTableName + ".type = " + typeTableName + "._id " +
                "WHERE " + worldTableName + ".name = ?";

        Map<String, Object> worldData = new HashMap<>();
        List<Map<String, Object>> obstacles = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, worldName);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                // Get world data
                worldData.put("name", rs.getString("name"));
                worldData.put("width", rs.getInt("width"));
                worldData.put("height", rs.getInt("height"));

                do {
                    // Get obstacle data
                    Map<String, Object> obstacleData = new HashMap<>();
                    obstacleData.put("x_position", rs.getInt("x_position"));
                    obstacleData.put("y_position", rs.getInt("y_position"));
                    obstacleData.put("size", rs.getInt("size"));
                    obstacleData.put("type", rs.getString("type"));

                    obstacles.add(obstacleData);
                } while (rs.next());
            }

            // Add obstacles list to world data
            worldData.put("obstacles", obstacles);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return worldData;
    }



    // Getters and Setters
//    public String getTypesTableName() {
//        return typesTableName;
//    }
//
//    public String getWorldTableName() {
//        return worldTableName;
//    }
//
//    public String getObstaclesTableName() {
//        return obstaclesTableName;
//    }
    public String getDbUrl() {
        return DBURL + DBNAME;
    }

    public Connection getConnection() {
        return conn;
    }
}
