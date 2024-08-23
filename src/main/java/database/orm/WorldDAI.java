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

    @Update("CREATE TABLE IF NOT EXISTS "+obstacleTableName
            + " (_id integer PRIMARY KEY,"
            + " world_name text NOT NULL,"
            + " x_position integer NOT NULL,"
            + " y_position integer NOT NULL,"
            + " size integer NOT NULL,"
            + " type integer NOT NULL,"
            + " FOREIGN KEY (type) REFERENCES "+typeTableName+" (_id)" // Foreign key reference
            + ")")
    void createObstacleTable();

    @Update("CREATE TABLE IF NOT EXISTS "+typeTableName+" ("
            + " _id integer PRIMARY KEY,"
            + " type text UNIQUE"
            + ")")
     void createTypesTable();

    @Update("INSERT INTO "+worldTableName+" (name, width, height) VALUES(?{1}, ?{2}, ?{3})")
    void addWorld(String name, int width, int height);

    @Update("INSERT INTO "+obstacleTableName+" (world_name, x_position, y_position, size, type)" +
            " VALUES(?{1}, ?{2}, ?{3}, ?{4}, ?{5})")
    void addObstacle(String worldName, int xPosition, int yPosition, int size, String type);

    @Update("INSERT INTO "+typeTableName+" (type) VALUES(?{1})")
    void addType(String typeName);

    @Select("SELECT * FROM "+ worldTableName)
    public List<WorldDO> getAllWorlds();

    @Select( "SELECT count(*) FROM " + worldTableName )
    public int getNumberOfWorlds();

    @Select("SELECT " + worldTableName + ".name, " + worldTableName + ".width, " + worldTableName + ".height, " +
            obstacleTableName + ".x_position, " + obstacleTableName + ".y_position, " +
            obstacleTableName + ".size, " + typeTableName + ".type " +
            "FROM " + worldTableName +
            " JOIN " + obstacleTableName + " ON " + worldTableName + ".name = " + obstacleTableName + ".world_name " +
            " JOIN " + typeTableName + " ON " + obstacleTableName + ".type = " + typeTableName + "._id " +
            "WHERE " + worldTableName + ".name = ?{1}")
    WorldDO getWorldData(String worldName);
}
