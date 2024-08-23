package database.orm;

import net.lemnik.eodsql.ResultColumn;

public class WorldDO {

    private String name;   // Maps to the 'name' column in the database
    private int width;     // Maps to the 'width' column in the database
    private int height;    // Maps to the 'height' column in the database

    public WorldDO() {}

    // Getter for the name column (Primary Key)
    public String getName() {
        return name;
    }

    // Annotate to map 'name' column in the database to this field
    @ResultColumn( value = "name" )
    public void setName( String name ) {
        this.name = name;
    }

    // Getter for width
    public int getWidth() {
        return width;
    }

    // Annotate to map 'width' column in the database to this field
    @ResultColumn( value = "width" )
    public void setWidth( int width ) {
        this.width = width;
    }

    // Getter for height
    public int getHeight() {
        return height;
    }

    // Annotate to map 'height' column in the database to this field
    @ResultColumn( value = "height" )
    public void setHeight( int height ) {
        this.height = height;
    }

    public String getPrimaryKey() {
        return name;
    }

}
