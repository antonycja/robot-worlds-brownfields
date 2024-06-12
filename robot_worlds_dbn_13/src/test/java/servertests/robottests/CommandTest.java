package servertests.robottests;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import robot_worlds_13.server.robot.*;
import robot_worlds_13.server.robot.world.AbstractWorld;

class CommandTest {

    @Test
    void getShutdownName() {
        Command test = new ShutdownCommand();
        assertEquals("off", test.getName());
    }


    @Test
    void getForwardName() {
        ForwardCommand test = new ForwardCommand("100");
        assertEquals("forward", test.getName());
        assertEquals("100", test.getArgument());
    }



    @Test
    void getHelpName() {
        Command test = new HelpCommand();                                                               //<1>
        assertEquals("help", test.getName());
    }


    @Test
    void createCommand() {
        // Command forward = Command.create("forward 10");                                                 //<1>
        // assertEquals("forward", forward.getName());
        // assertEquals("10", forward.getArgument());

        Command shutdown = Command.create("shutdown");                                                  //<2>
        assertEquals("off", shutdown.getName());

        Command help = Command.create("help");                                                          //<3>
        assertEquals("help", help.getName());
    }

    @Test
    void createInvalidCommand() {
        try {
            Command forward = Command.create("say hello");                                              //<4>
            fail("Should have thrown an exception");                                                    //<5>
        } catch (IllegalArgumentException e) {
            assertEquals("Unsupported command: say hello", e.getMessage());                             //<6>
        }
    }

    @Test
    void commandsAreCaseInsensitive() {
        //Test if commands are case insensitive
        Command shutdownUpperCase = Command.create("OFF");
        assertEquals("off", shutdownUpperCase.getName());

        Command forwardLowerCase = Command.create("FOrward");
        assertEquals("forward", forwardLowerCase.getName());

        Command helpMixedCase = Command.create("HeLp");
        assertEquals("help", helpMixedCase.getName());
    }
}