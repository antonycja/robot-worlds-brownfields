package servertests.robottests;
import robot_worlds_13.server.robot.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CommandFunctionsTest {

    /**
    * Sets up the environment for each test by resetting the Command replay and reverse flags
    * and clearing the command list.
    */

    @BeforeEach
    void setUp() {
        Command.setReplayFlag(false);
        Command.setReverseFlag(false);
        Command.setCommandList(new ArrayList<>());
    }

    /**
    * Tests the creation of a ShutdownCommand and verifies the command list remains empty.
    */

    @Test
    void testCreateShutdownCommand() {
        Command command = Command.create("shutdown");
        assertTrue(command instanceof ShutdownCommand);
        assertEquals(0, Command.getCommandList().size());
    }

    /**
    * Tests the creation of a HelpCommand.
    */

    @Test
    void testCreateHelpCommand() {
        Command command = Command.create("help");
        assertTrue(command instanceof HelpCommand);
    }

    /**
    * Tests the creation of a ForwardCommand with an argument and verifies the command properties.
    */

    @Test
    void testCreateForwardCommandWithArgument() {
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("10");
        Command command = Command.create("forward", arguments);
        assertTrue(command instanceof ForwardCommand);
        assertEquals("forward", command.getName());
        assertEquals("10", command.getArgument());
        assertEquals(1, Command.getCommandList().size());
    }

    @Test
    void testCreateLeftCommand() {
        Command command = Command.create("left");
        assertTrue(command instanceof LeftCommand);
        assertEquals(1, Command.getCommandList().size());
    }

    

    @Test
    void testCreateRightCommand() {
        Command command = Command.create("right");
        assertTrue(command instanceof RightCommand);
        assertEquals(1, Command.getCommandList().size());
    }



    @Test
    void testCreateBackCommand() {
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("5");
        Command command = Command.create("back", arguments);
        assertTrue(command instanceof BackCommand);
        assertEquals("back", command.getName());
        assertEquals("5", command.getArgument());
        assertEquals(1, Command.getCommandList().size());
    }

    /**
    * Tests the creation of a LookCommand.
    */

    @Test
    void testCreateLookCommand() {
        Command command = Command.create("look");
        assertTrue(command instanceof LookCommand);
    }

    /**
    * Tests the creation of a StateCommand.
    */

    @Test
    void testCreateStateCommand() {
        Command command = Command.create("state");
        assertTrue(command instanceof StateCommand);
    }

    /**
    * Tests the creation of a FireCommand.
    */

    @Test
    void testCreateFireCommand() {
        Command command = Command.create("fire");
        assertTrue(command instanceof FireCommand);
    }

    /**
    * Tests the creation of an OrientationCommand.
    */

    @Test
    void testCreateOrientationCommand() {
        Command command = Command.create("orientation");
        assertTrue(command instanceof OrientationCommand);
    }

    /**
    * Tests the creation of a RepairCommand.
    */

    @Test
    void testCreateRepairCommand() {
        Command command = Command.create("repair");
        assertTrue(command instanceof RepairCommand);
    }

    /**
    * Tests the creation of a ReloadCommand.
    */

    @Test
    void testCreateReloadCommand() {
        Command command = Command.create("reload");
        assertTrue(command instanceof ReloadCommand);
    }

    /**
    * Tests the creation of an invalid command and verifies that an IllegalArgumentException is thrown.
    */


    @Test
    void testCreateInvalidCommand() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Command.create("invalid");
        });
        assertEquals("Unsupported command: invalid", exception.getMessage());
    }

    /**
    * Tests setting and getting the reverse flag.
    */

    @Test
    void testSetAndGetReplayFlag() {
        Command.setReplayFlag(true);
        assertTrue(Command.getReplayFlag());
        Command.setReplayFlag(false);
        assertFalse(Command.getReplayFlag());
    }

    /**
    * Tests setting and getting the reverse flag.
    */

    @Test  
    void testSetAndGetReverseFlag() {
        Command.setReverseFlag(true);
        assertTrue(Command.getReverseFlag());
        Command.setReverseFlag(false);
        assertFalse(Command.getReverseFlag());
    }
}