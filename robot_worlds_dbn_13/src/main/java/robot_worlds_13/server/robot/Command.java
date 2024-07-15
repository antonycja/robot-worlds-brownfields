package robot_worlds_13.server.robot;

import java.util.ArrayList;
import java.util.List;

/**
 * The `Command` class is an abstract base class that represents a command that
 * can be executed on a `Robot` object.
 * Each concrete subclass of `Command` represents a specific type of command,
 * such as `ForwardCommand`, `LeftCommand`, etc.
 * The `create` methods are used to create instances of the appropriate
 * `Command` subclass based on the input instruction.
 * The `execute` method is an abstract method that must be implemented by each
 * concrete subclass to define the behavior of the command.
 * The `commandList` static list is used to keep track of the commands that have
 * been executed, which can be used for replay functionality.
 * The `replayFlag` and `reverseFlag` static flags are used to control the
 * behavior of the `create` methods during replay.
 */
public abstract class Command {

    private final String name;
    private String argument;
    static private boolean replayFlag;
    static private boolean reverseFlag;

    //
    static List<String> commandList = new ArrayList<>();

    public abstract boolean execute(Robot target);

    public Command(String name) {
        this.name = name.trim().toLowerCase();
        this.argument = "";
    }

    public Command(String name, String argument) {
        this(name);
        this.argument = argument.trim();
    }

    public String getName() {
        return name;
    }

    public String getArgument() {
        return this.argument;
    }

    /**
     * Creates a new `Command` instance based on the given instruction string.
     * The instruction is converted to lowercase and split into an array of
     * arguments.
     * Depending on the instruction, a specific `Command` subclass is instantiated
     * and returned.
     * The `commandList` is updated with the instruction if it is not in replay
     * mode.
     * If the instruction is not recognized, an `IllegalArgumentException` is
     * thrown.
     *
     * @param instruction the instruction string to parse
     * @return a new `Command` instance based on the instruction
     * @throws IllegalArgumentException if the instruction is not supported
     */
    public static Command create(String instruction, ArrayList<String> arguments) {

        // Convert instruction to lowercase
        instruction = instruction.toLowerCase();

        switch (instruction) {
            case "shutdown":
            case "off":
                // reset the history list as a result of errors when running all the tests
                commandList = new ArrayList<>();
                return new ShutdownCommand();
            case "help":
                return new HelpCommand();
            case "look":
                return new LookCommand();
            case "state":
                return new StateCommand(instruction);
            case "fire":
                return new FireCommand();
            case "orientation":
                return new OrientationCommand();
            case "repair":
                return new RepairCommand("repair");
            case "reload":
                return new ReloadCommand();
            case "[A":
                return new ForwardCommand("10");
            case "[D":
                return new LeftCommand();
            case "[C":
                return new RightCommand();
            case "[B":
                return new BackCommand("10");
            case "left":
                if (!replayFlag) {
                    commandList.add(instruction);
                }
                return new LeftCommand();
            case "right":
                if (!replayFlag) {
                    commandList.add(instruction);
                }
                return new RightCommand();
            case "forward":
                if (!replayFlag) {
                    commandList.add(instruction);
                }
                return new ForwardCommand(arguments.isEmpty() ? "10" : arguments.get(0));
            case "back":
                if (!replayFlag) {
                    commandList.add(instruction);
                }
                return new BackCommand(arguments.isEmpty() ? "10" : arguments.get(0));
            case "turn":
                if (arguments.isEmpty()) {
                    throw new IllegalArgumentException("Missing argument for turn command");
                }
                if (arguments.get(0).equalsIgnoreCase("left")) {
                    return new LeftCommand();
                } else if (arguments.get(0).equalsIgnoreCase("right")) {
                    return new RightCommand();
                } else {
                    throw new IllegalArgumentException("Invalid argument for turn command: " + arguments.get(0));
                }
            default:
                throw new IllegalArgumentException("Unsupported command: " + instruction);
        }
    }


    public static List<String> getCommandList() {
        return commandList;
    }

    public static void setCommandList(List<String> commandList) {
        Command.commandList = commandList;
    }


    /**
     * Creates a new Command instance based on the provided instruction.
     *
     * The method takes an instruction string, converts it to lowercase, and then
     * uses a switch statement to create the appropriate Command implementation
     * based on the instruction. The method also handles special cases like
     * "shutdown", "off", and "help", as well as managing the commandList and
     * replayFlag for certain instructions.
     *
     * @param instruction The instruction string to be processed.
     * @return The appropriate Command implementation for the given instruction.
     * @throws IllegalArgumentException if the instruction is not supported.
     */

    public static Command create(String instruction) {

        String[] args = instruction.toLowerCase().trim().split(" ", 2);

        // Convert instruction to lowercase
        instruction = instruction.toLowerCase();

        // for replay
        if (args.length == 1) {
            args = new String[] { instruction, "" };
        }

        switch (instruction) {
            case "shutdown":
            case "off":
                // reset the history list as a result of errors when running all the tests
                commandList = new ArrayList<>();
                return new ShutdownCommand();
            case "help":
                return new HelpCommand();
            case "forward":
                if (!replayFlag) {
                    commandList.add(instruction);
                }
                return new ForwardCommand(args[0]);

            // ADDED
            case "left":
                if (!replayFlag) {
                    commandList.add(instruction);
                }
                return new LeftCommand();
            case "right":
                if (!replayFlag) {
                    commandList.add(instruction);
                }
                return new RightCommand();
            case "back":
                if (!replayFlag) {
                    commandList.add(instruction);
                }
                return new BackCommand(args[0]);
            case "look":
                return new LookCommand();
            case "state":
                return new StateCommand(instruction);
            case "fire":
                return new FireCommand();
            case "orientation":
                return new OrientationCommand();
            case "repair":
                return new RepairCommand("repair");
            case "reload":
                return new ReloadCommand();

            default:
                throw new IllegalArgumentException("Unsupported command: " + instruction);
        }
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




