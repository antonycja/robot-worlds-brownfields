package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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


    // Getters and Setters
    public String getDbUrl() {
        return DBURL + DBNAME;
    }

    public Connection getConnection() {
        return conn;
    }
}
