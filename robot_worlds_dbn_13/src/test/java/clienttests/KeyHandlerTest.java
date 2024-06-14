package clienttests;
import robot_worlds_13.client.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;

class KeyHandlerTest {

    private KeyHandler keyHandler;

    @BeforeEach
    void setUp() {
        keyHandler = new KeyHandler();
    }

    /**
    * Tests that pressing the UP key sets the upPressed flag to true.
    */

    @Test
    void testKeyPressedUp() {
        KeyEvent keyEvent = new KeyEvent(new java.awt.Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED);
        keyHandler.keyPressed(keyEvent);
        assertTrue(keyHandler.upPressed, "The up key should be marked as pressed.");
    }

    /**
    * Tests that pressing the DOWN key sets the downPressed flag to true.
    */

    @Test
    void testKeyPressedDown() {
        KeyEvent keyEvent = new KeyEvent(new java.awt.Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED);
        keyHandler.keyPressed(keyEvent);
        assertTrue(keyHandler.downPressed, "The down key should be marked as pressed.");
    }

    /**
    * Tests that releasing the LEFT key sets the leftPressed flag to false.
    */

    @Test
    void testKeyPressedLeft() {
        KeyEvent keyEvent = new KeyEvent(new java.awt.Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
        keyHandler.keyPressed(keyEvent);
        assertTrue(keyHandler.leftPressed, "The left key should be marked as pressed.");
    }

    /**
    * Tests that releasing the RIGHT key sets the rightPressed flag to false.
    */

    @Test
    void testKeyPressedRight() {
        KeyEvent keyEvent = new KeyEvent(new java.awt.Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
        keyHandler.keyPressed(keyEvent);
        assertTrue(keyHandler.rightPressed, "The right key should be marked as pressed.");
    }

    /**
    * Tests that releasing the UP key sets the upPressed flag to false.
    */

    @Test
    void testKeyReleasedUp() {
        keyHandler.upPressed = true;
        KeyEvent keyEvent = new KeyEvent(new java.awt.Label(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED);
        keyHandler.keyReleased(keyEvent);
        assertFalse(keyHandler.upPressed, "The up key should be marked as released.");
    }


    /**
    * Tests that releasing the DOWN key sets the downPressed flag to false.
    */

    @Test
    void testKeyReleasedDown() {
        keyHandler.downPressed = true;
        KeyEvent keyEvent = new KeyEvent(new java.awt.Label(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED);
        keyHandler.keyReleased(keyEvent);
        assertFalse(keyHandler.downPressed, "The down key should be marked as released.");
    }

    @Test
    void testKeyReleasedLeft() {
        keyHandler.leftPressed = true;
        KeyEvent keyEvent = new KeyEvent(new java.awt.Label(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
        keyHandler.keyReleased(keyEvent);
        assertFalse(keyHandler.leftPressed, "The left key should be marked as released.");
    }

    @Test
    void testKeyReleasedRight() {
        keyHandler.rightPressed = true;
        KeyEvent keyEvent = new KeyEvent(new java.awt.Label(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
        keyHandler.keyReleased(keyEvent);
        assertFalse(keyHandler.rightPressed, "The right key should be marked as released.");
    }
}

