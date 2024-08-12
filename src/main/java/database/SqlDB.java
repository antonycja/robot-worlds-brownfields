package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlDB {
    public static void main(String[] args) {

        SqlCommands sqlCommands = new SqlCommands();
        sqlCommands.createWorldTable("world");
        sqlCommands.insertWorld("world", "tom", 50, 5,4000);

    }
}
