package robot_worlds_13.server.robot;

public class ReloadCommand extends Command{
    private Robot robot;

    public ReloadCommand(Robot robot) {
        super("reload"); // Call to Command constructor
        this.robot = robot;
    }

    public ReloadCommand() {
        super("reload");
    }

    @Override
    public boolean execute(Robot target) {
        System.out.println("Reloading...");
        // simulate reload time
        try {
            Thread.sleep(target.getReloadTime() * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        target.reload();
        System.out.println("Reload complete!");
        return true;
    }
}