package ServerTests;

import org.junit.jupiter.api.Test;
import robot_worlds_13.server.robot.Direction;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @Test
    public void testEnumValues() {
        // Test that the enum contains the correct constants
        Direction[] expectedValues = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        Direction[] actualValues = Direction.values();

        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testValueOf() {
        // Test the valueOf method for each enum constant
        assertEquals(Direction.NORTH, Direction.valueOf("NORTH"));
        assertEquals(Direction.SOUTH, Direction.valueOf("SOUTH"));
        assertEquals(Direction.EAST, Direction.valueOf("EAST"));
        assertEquals(Direction.WEST, Direction.valueOf("WEST"));
    }

    @Test
    public void testToString() {
        // Test the toString method for each enum constant
        assertEquals("NORTH", Direction.NORTH.toString());
        assertEquals("SOUTH", Direction.SOUTH.toString());
        assertEquals("EAST", Direction.EAST.toString());
        assertEquals("WEST", Direction.WEST.toString());
    }
}
