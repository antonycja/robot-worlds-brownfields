package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlCommands {
    private final String DBURL = "jdbc:sqlite:database/";
    private final String DBNAME = "robot_worlds.db";
    private String worldName;
    private String typesTableName;
    private Connection conn;

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
                + " height integer NOT NULL\n"
                + ");";
        executeSQL(sql);
    }

    // Create table for obstacles
    public void createObstacleTable(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + " _id integer PRIMARY KEY,\n"
                + " world_name text NOT NULL,\n"
                + " x_position integer NOT NULL,\n"
                + " y_position integer NOT NULL,\n"
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
    public void insertWorld(String tableName, String name, int width, int height) {
        this.worldName = name;
        String sql = "INSERT INTO " + tableName + " (name, width, height) VALUES(?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, width);
            statement.setInt(3, height);
            statement.executeUpdate();
            System.out.println("Record inserted into table '" + tableName + "'.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Insert a new obstacle record
    public void insertObstacle(String tableName, int xPosition, int yPosition, int type) {
        String sql = "INSERT INTO " + tableName + " (world_name, x_position, y_position, type) VALUES(?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, this.worldName);
            statement.setInt(2, xPosition);
            statement.setInt(3, yPosition);
            statement.setInt(4, type);
            statement.executeUpdate();
            System.out.println("Record inserted into table '" + tableName + "'.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Insert a new type record
    public void insertType(String tableName, String type) {
        String sql = "INSERT INTO " + tableName + " (type) VALUES(?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, type);
            statement.executeUpdate();
            System.out.println("Record inserted into table '" + tableName + "'.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    // Getters and Setters
    public String getDbUrl() {
        return DBURL + DBNAME;
    }

    public Connection getConnection() {
        return conn;
    }
}
