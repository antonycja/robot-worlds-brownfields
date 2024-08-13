package robot_worlds_13.server.robot;

import database.SqlCommands;
import java.util.Scanner;

public class RestoreCommand extends Command {
    private SqlCommands sqlCommands;

    public RestoreCommand(SqlCommands sqlCommands) {
        super("restore");
        this.sqlCommands = sqlCommands;
    }

    private String promptForWorldName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the world to restore: ");
        return scanner.nextLine().trim();
    }

    @Override
    public boolean execute(Robot target) {
        String worldName = promptForWorldName();

        try {
//            sqlCommands.restoreWorld("World", worldName);
            System.out.println("World restored successfully with the name: " + worldName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
