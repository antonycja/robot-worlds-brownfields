package servertests.robottests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import robot_worlds_13.server.robot.Command;
import robot_worlds_13.server.robot.ForwardCommand;
import robot_worlds_13.server.robot.HelpCommand;
import robot_worlds_13.server.robot.ShutdownCommand;

class CommandTest {

    @Test
    void getShutdownName() {
        Command test = new ShutdownCommand();
        assertEquals("quit", test.getName());
    }


    @Test
    void getForwardName() {
        ForwardCommand test = new ForwardCommand("100");
        assertEquals("forward", test.getName());
        assertEquals("100", test.getArgument());
    }



    @Test
    void getHelpName() {
        Command test = new HelpCommand();                                                               
        assertEquals("help", test.getName());
    }


    @Test
    void createCommand() {
        // Command forward = Command.create("forward 10");                                                
        // assertEquals("forward", forward.getName());
        // assertEquals("10", forward.getArgument());

        Command shutdown = Command.create("shutdown");                                                  
        assertEquals("quit", shutdown.getName());

        Command help = Command.create("help");                                                          
        assertEquals("help", help.getName());
    }

    @Test
    void createInvalidCommand() {
        try {
            Command forward = Command.create("say hello");                                              
            fail("Should have thrown an exception");                                                    
        } catch (IllegalArgumentException e) {
            assertEquals("Unsupported command: say hello", e.getMessage());                             
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