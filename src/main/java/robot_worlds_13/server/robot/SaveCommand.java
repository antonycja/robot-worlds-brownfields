package robot_worlds_13.server.robot;

import database.SqlCommands;
import java.util.Scanner;

public class SaveCommand extends Command {
    private SqlCommands sqlCommands;

    public SaveCommand(SqlCommands sqlCommands) {
        super("save");
        this.sqlCommands = sqlCommands;
    }

    private String promptForWorldName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a name for the world: ");
        return scanner.nextLine().trim();
    }

    @Override
    public boolean execute(Robot target) {
        String worldName = promptForWorldName();

        try {
            int width = 100;
            int height = 100;
            int port = 8080;
            sqlCommands.saveWorld("World", worldName, width, height, port);

            System.out.println("World saved successfully with the name: " + worldName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
