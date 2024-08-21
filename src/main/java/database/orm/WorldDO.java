package database.orm;

import net.lemnik.eodsql.ResultColumn;

public class WorldDO
{

    private String worldName;

    private int worldWidth;

    private int worldHeight;

    public WorldDO(){}

    public String getPrimaryKey() {
        return worldName;
    }

    @ResultColumn( value = "name" )
    public void setPrimaryKey( String key ) {
        this.worldName = key;
    }

    public String getName() {
        return worldName;
    }

    public void setName( String name ) {
        this.worldName = name;
    }

    public int getWidth() {
        return worldWidth;
    }
    @ResultColumn( value = "width" )
    public void setWorldWidth( int width ) {
        this.worldWidth = width;
    }

    public int getHeight() {
        return worldHeight;
    }

    @ResultColumn( value = "height" )
    public void setWorldHeight( int height ) {
    this.worldHeight = height;
}
}