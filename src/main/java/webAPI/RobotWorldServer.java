package webAPI;

import io.javalin.Javalin;
import database.orm.ConnectDB;

public class RobotWorldServer {

    private Javalin server;
    private ConnectDB connectDB;

    public RobotWorldServer() {
        // Initialize the database connection
        connectDB = new ConnectDB();
        connectDB.connectDB();  // Connect to the database

        // Create the Javalin server
        server = Javalin.create();

        // Set up the routes for the API
        this.server.get("/world", RobotWorldApi::getCurrentWorld);
        this.server.get("/world/{world}", RobotWorldApi::restoreSavedWorld);
    }

    public void start(int port) {
        this.server.start(port);
        System.out.println("Server started on port " + port);
    }

    public void stop() {
        this.server.stop();
        connectDB.closeConnection();  // Close the database connection when the server stops
        System.out.println("Server stopped and database connection closed.");
    }

    public static void main(String[] args) {
        RobotWorldServer server = new RobotWorldServer();
        server.start(3000);  // Start the server on port 3000
    }
}
