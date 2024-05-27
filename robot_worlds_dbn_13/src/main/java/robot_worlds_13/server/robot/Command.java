package robot_worlds_13.server.robot;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    private final String name;
    private String argument;
    static private boolean replayFlag;
    static private boolean reverseFlag;

    //
    static List<String> commandList = new ArrayList<>();



    public abstract boolean execute(Robot target);

    public Command(String name){
        this.name = name.trim().toLowerCase();
        this.argument = "";
    }

    public Command(String name, String argument) {
        this(name);
        this.argument = argument.trim();
    }

    public String getName() {                                                                           //<2>
        return name;
    }

    public String getArgument() {
        return this.argument;
    }

    public static Command create(String instruction) {
        //  The 2 parameter specifies that the split operation should be performed at most once, resulting in two parts.
        String[] args = instruction.toLowerCase().trim().split(" ", 2);

        // for replay
        if (args.length == 1) {
            args = new String[] {instruction, ""};
        }

        switch (args[0]){
            case "shutdown":
            case "off":
                // reset the history list as a result of errors when running all the tests
                commandList = new ArrayList<>();
                return new ShutdownCommand();
            case "help":
                return new HelpCommand();
            case "forward":
                if (!replayFlag) {commandList.add(instruction);}
                return new ForwardCommand(args[1]);

            // ADDED
            case "left":
                if (!replayFlag) {commandList.add(instruction);}
                return new LeftCommand();
            case "right":
                if (!replayFlag) {commandList.add(instruction);}
                return new RightCommand();
            case "back":
                if (!replayFlag) {commandList.add(instruction);}
                return new BackCommand(args[1]);
            case "look":
                return new LookCommand();

            default:
                throw new IllegalArgumentException("Unsupported command: " + instruction);
        }
    }

    public static List<String> getCommandList() {
        return commandList;
    }

    static public boolean getReplayFlag () {
        return Command.replayFlag;
    }

    static public void setReplayFlag(Boolean state) {
        Command.replayFlag = state;
    }

    static public boolean getReverseFlag () {
        return Command.reverseFlag;
    }

    static public void setReverseFlag(Boolean state) {
        Command.reverseFlag = state;
    }
}

