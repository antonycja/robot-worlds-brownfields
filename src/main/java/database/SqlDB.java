package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlDB {
    public static void main(String[] args) {
        // SQLite connection string
        String url = "jdbc:sqlite:database/robot_worlds.db";

        // Create a new database file if it doesn't exist
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("A new database has been created.");

                // Create a new table
                String sql = "CREATE TABLE IF NOT EXISTS worlds (\n"
                        + "	id integer PRIMARY KEY,\n"
                        + "	name text NOT NULL,\n"
                        + "	capability text\n"
                        + ");";

                try (Statement statement = conn.createStatement()) {
                    statement.execute(sql);
                    System.out.println("Table created successfully.");

                    statement.close();
                    conn.close();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
