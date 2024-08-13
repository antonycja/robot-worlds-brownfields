package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SqlCommands {
    private final String DBURL = "jdbc:sqlite:database/";
    private final String DBNAME = "robot_worlds.db";
    private Connection conn;

    public SqlCommands() {
        try {
            this.conn = DriverManager.getConnection(DBURL + DBNAME);
            if (this.conn != null) {
                System.out.println("A connection to the database has been established.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // General method to execute a SQL command
    private void executeSQL(String sql) {
        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);
            System.out.println("SQL command executed successfully.");
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
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + " name text PRIMARY KEY,\n"
                + " width integer NOT NULL,\n"
                + " height integer NOT NULL,\n"
                + " port integer NOT NULL\n"
                + ");";
        executeSQL(sql);
    }

    // Create table for obstacles
    public void createObstacleTable(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + " _id integer PRIMARY KEY,\n"
                + " position_x integer NOT NULL,\n"
                + " position_y integer NOT NULL,\n"
                + " type integer NOT NULL\n"
                + ");";
        executeSQL(sql);
    }

    // Create table for keeping the types of objects in the world
    public void createTypesTable(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + " _id integer PRIMARY KEY,\n"
                + " type text NOT NULL\n"
                + ");";
        executeSQL(sql);
    }

    // Insert a new world record
    public void insertWorld(String tableName, String name, int width, int height, int port) {
        String sql = "INSERT INTO " + tableName + " (name, width, height, port) VALUES(?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, width);
            statement.setInt(3, height);
            statement.setInt(4, port);
            statement.executeUpdate();
            System.out.println("Record inserted into table '" + tableName + "'.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Save a world if it doesn't already exist
    public void saveWorld(String tableName, String worldName, int width, int height, int port) {
        String checkSql = "SELECT COUNT(*) FROM " + tableName + " WHERE name = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, worldName);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Error: A world with this name already exists.");
                    return;
                }

                // Insert the new world
                insertWorld(tableName, worldName, width, height, port);
                System.out.println("World saved successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Restore a world from the database
    public void restoreWorld(String tableName, String worldName) {
        String sql = "SELECT width, height, port FROM " + tableName + " WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, worldName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int width = rs.getInt("width");
                    int height = rs.getInt("height");
                    int port = rs.getInt("port");
                    // Restore the world
                    System.out.println("World restored: Width = " + width + ", Height = " + height + ", Port = " + port);
                } else {
                    System.out.println("Error: World not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to generate a world name and save a world
    public void generateAndSaveWorld(String tableName) {
        String worldName = generateWorldName();


        int width = 100;
        int height = 100;
        int port = 8080;

        saveWorld(tableName, worldName, width, height, port);
    }

    // Method to generate a world name and restore a world
    public void generateAndRestoreWorld(String tableName) {
        String worldName = generateWorldName();
        restoreWorld(tableName, worldName);
    }

    // Generate a world name from user input
    private String generateWorldName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a name for the world: ");
        return scanner.nextLine().trim();
    }

    // Getters and Setters
    public String getDbUrl() {
        return DBURL + DBNAME;
    }

    public Connection getConnection() {
        return conn;
    }
}
