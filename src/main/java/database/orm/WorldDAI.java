package database.orm;

import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;

public interface WorldDAI extends BaseQuery {
    @Update("CREATE TABLE IF NOT EXISTS worlds ("
            + " name text PRIMARY KEY,"
            + " width integer NOT NULL,"
            + " height integer NOT NULL"
            + ")")
    WorldDO createWorldTable();

    @Update("CREATE TABLE IF NOT EXISTS objects"
            + " (_id integer PRIMARY KEY,"
            + " world_name text NOT NULL,"
            + " x_position integer NOT NULL,"
            + " y_position integer NOT NULL,"
            + " size integer NOT NULL,"
            + " type integer NOT NULL,"
            + " FOREIGN KEY (type) REFERENCES types (_id)" // Foreign key reference
            + ")")
    ObstacleDO createObstacleTable();

    @Update("CREATE TABLE IF NOT EXISTS types ("
            + " _id integer PRIMARY KEY,"
            + " type text UNIQUE"
            + ")")
    ObstacleDO createTypesTable();

    @Update("INSERT INTO objects (world_name, x_position, y_position, size, type) VALUES(?{1}, ?{2}, ?{3}, ?{4}, ?{5})")
    ObstacleDO addObstacle(String worldName, int xPosition, int yPosition, int size, String type);

    @Update("INSERT INTO types (type) VALUES(?)")

    @Select("SELECT * FROM worlds")
    WorldDO getAllWorlds();

//    @Select("SELECT ")
}
