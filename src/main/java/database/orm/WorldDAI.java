package database.orm;

import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;

import java.util.List;

public interface WorldDAI extends BaseQuery {
    String worldTableName = "worlds";
    String obstacleTableName = "objects";
    String typeTableName = "types";
    @Update("CREATE TABLE IF NOT EXISTS "+worldTableName+" ("
            + " name text PRIMARY KEY,"
            + " width integer NOT NULL,"
            + " height integer NOT NULL"
            + ")")
    void createWorldTable();

    @Update("CREATE TABLE IF NOT EXISTS " + obstacleTableName
            + " (_id integer PRIMARY KEY,"
            + " world_name text NOT NULL,"
            + " x_position integer NOT NULL,"
            + " y_position integer NOT NULL,"
            + " size integer NOT NULL,"
            + " type integer NOT NULL,"
//            + " FOREIGN KEY (world_name) REFERENCES " + worldTableName + " (name) ON DELETE CASCADE,"  // Foreign key to world table with cascading delete
            + " FOREIGN KEY (type) REFERENCES " + typeTableName + " (_id)"                              // Foreign key to types table
            + ")")
    void createObstacleTable();


    @Update("CREATE TABLE IF NOT EXISTS "+typeTableName+" ("
            + " _id integer PRIMARY KEY,"
            + " type text UNIQUE"
            + ")")
     void createTypesTable();

    @Select("SELECT COUNT(*) FROM " + worldTableName + " WHERE name = ?{1}")
    int countWorldName(String worldName);

    @Update("INSERT INTO "+worldTableName+" (name, width, height) VALUES(?{1}, ?{2}, ?{3})")
    void addWorld(String name, int width, int height);

    // Updated method to add an obstacle using the type name
    @Update("INSERT INTO " + obstacleTableName + " (world_name, x_position, y_position, size, type)" +
            " VALUES(?{1}, ?{2}, ?{3}, ?{4}, (SELECT _id FROM " + typeTableName + " WHERE type = ?{5}))")
    void addObstacle(String worldName, int xPosition, int yPosition, int size, String typeName);

    @Update("INSERT OR IGNORE INTO "+typeTableName+" (type) VALUES(?{1})")
    void addType(String typeName);

    @Select("SELECT COUNT(*) FROM types WHERE type = ?{1}")
    int countTypeByName(String name);

    @Select("SELECT * FROM "+ worldTableName)
    List<WorldDO> getAllWorlds();

    @Select( "SELECT count(*) FROM " + worldTableName )
    public int getNumberOfWorlds();

    @Select("SELECT * FROM " + worldTableName + " WHERE name = ?{1}")
    WorldDO getWorldData(String worldName);

    @Select("SELECT " + obstacleTableName + ".*, " + typeTableName + ".type " +
            "FROM " + obstacleTableName +
            " JOIN " + typeTableName + " ON " + obstacleTableName + ".type = " + typeTableName + "._id " +
            "WHERE " + obstacleTableName + ".world_name = ?{1}")
    List<ObjectsDO> getObjectData(String worldName);

    @Update("DELETE FROM " + worldTableName + " WHERE name = ?{1}")
    void deleteWorld(String worldName);

    @Update("DELETE FROM " + obstacleTableName + " WHERE world_name = ?{1}")
    void deleteWorldObjects(String worldName);


    // New method to save a world using a WorldDO object
    @Update("INSERT INTO " + worldTableName + " (name, width, height) VALUES (?{1.name}, ?{1.width}, ?{1.height})")
    void saveWorld(WorldDO world);
}


