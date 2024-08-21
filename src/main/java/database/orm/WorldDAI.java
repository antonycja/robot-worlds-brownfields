package database.orm;

import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;

public interface WorldDAI extends BaseQuery {
    @Select("SELECT * FROM worlds")
    WorldDO getAllWorlds();

}
