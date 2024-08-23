package webAPI;

import io.javalin.Javalin;

public class RobotWorldServer {

    private Javalin server;

    public RobotWorldServer() {
        server = Javalin.create();

        // Ensure that RobotWorldApi methods are correctly referenced
        this.server.get("/world", RobotWorldApi::getCurrentWorld);
        this.server.get("/world/{world}", RobotWorldApi::restoreSavedWorld);
    }

    public void start(int port) {
        this.server.start(port);
    }

    public void stop() {
        this.server.stop();
    }

    public static void main(String[] args) {
        RobotWorldServer server = new RobotWorldServer();
        server.start(3000);
    }
}
