package database.orm;

import net.lemnik.eodsql.ResultColumn;

public class ObjectsDO {
//    TODO: FINISH THIS PART HERE
    private int xPosition;
    private int yPosition;
    private int size;
    private String type; // Stores the actual type value

    public ObjectsDO(){}

    @ResultColumn("x_position")
    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }
    public int getXPosition() {
        return xPosition;
    }

    @ResultColumn("y_position")
    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }
    public int getYPosition() {
        return yPosition;
    }

    @ResultColumn("size")
    public void setSize(int size) {
        this.size = size;
    }
    public int getSize() {
        return size;
    }

    @ResultColumn("type")
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}

