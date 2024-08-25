/*
package webapitest;

import database.orm.ConnectDB;
import database.orm.ObjectsDO;
import database.orm.WorldDAI;
import database.orm.WorldDO;
import io.javalin.Javalin;
import io.javalin.http.Context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webAPI.RobotWorldApi;

import java.util.*;

public class RobotWorldApiTest {

    private Javalin app;
    private ConnectDB connectDB;
    private WorldDAI worldDAO;

    @BeforeEach
    void setUp() {
        // Initialize the Javalin app
        app = Javalin.create().start(0); // Start on a random port

        // Mock ConnectDB and WorldDAI
        connectDB = Mockito.mock(ConnectDB.class);
        worldDAO = Mockito.mock(WorldDAI.class);

        // Set up the routes
        app.get("/world", RobotWorldApi::getCurrentWorld);
        app.get("/world/{world}", RobotWorldApi::restoreSavedWorld);
        app.post("/world", RobotWorldApi::saveWorld);

        // Initialize the mocked DAO
        Mockito.when(connectDB.worldDAO).thenReturn(worldDAO);
    }

    @Test
    void testGetCurrentWorldWithWorldName() {
        // Mock data
        WorldDO world = new WorldDO();
        world.setName("TestWorld");
        world.setWidth(100);
        world.setHeight(100);

        Mockito.when(worldDAO.getWorldData("TestWorld")).thenReturn(world);

        List<ObjectsDO> obstacles = new ArrayList<>();
        ObjectsDO obstacle = new ObjectsDO();
        obstacle.setType("Rock");
        obstacle.setXPosition(10);
        obstacle.setYPosition(20);
        obstacles.add(obstacle);

        Mockito.when(worldDAO.getObjectData("TestWorld")).thenReturn(obstacles);

        // Test
        Context ctx = Mockito.mock(Context.class);
        Mockito.when(ctx.queryParam("world")).thenReturn("TestWorld");

        RobotWorldApi.getCurrentWorld(ctx);

        Mockito.verify(ctx).json(Mockito.argThat(arg ->
                arg instanceof Map && ((Map<?, ?>) arg).get("worldName").equals("TestWorld")
        ));

    }

    @Test
    void testSaveWorld() {
        // Mock data
        Mockito.doNothing().when(worldDAO).saveWorld(Mockito.any(WorldDO.class));
        Mockito.when(worldDAO.getWorldData("NewWorld")).thenReturn(null); // No existing world

        Context ctx = Mockito.mock(Context.class);
        Mockito.when(ctx.formParam("worldName")).thenReturn("NewWorld");
        Mockito.when(ctx.formParam("width")).thenReturn("200");
        Mockito.when(ctx.formParam("height")).thenReturn("200");

        RobotWorldApi.saveWorld(ctx);


        Mockito.verify(ctx).json(Mockito.argThat(arg ->
                arg instanceof Map && ((Map<?, ?>) arg).get("message").equals("World saved successfully")
        ));
    }

    @Test
    void testLaunchRobot() {
        // Mock data
        WorldDO world = new WorldDO();
        world.setName("TestWorld");
        Mockito.when(worldDAO.getWorldData("TestWorld")).thenReturn(world);

        // Mock request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("command", "Launch");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("worldName", "TestWorld");
        requestBody.put("parameters", parameters);

        Context ctx = Mockito.mock(Context.class);
        Mockito.when(ctx.pathParam("name")).thenReturn("TestRobot");
        Mockito.when(ctx.bodyAsClass(Map.class)).thenReturn(requestBody);

        RobotWorldApi.launchRobot(ctx);


        Mockito.verify(ctx).json(Mockito.argThat(arg ->
                arg instanceof Map && ((Map<?, ?>) arg).get("message").equals("Robot launched successfully in world TestWorld")
        ));
    }
}
*/
