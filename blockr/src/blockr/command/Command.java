package blockr.command;

/**
 * The interface for a command. A command is the representation of any user action which has an effect on the program.
 * For example, when the user presses the F5 key to execute a step of the program, an {@link ExecuteCommand} will be
 * created. This command contains two methods, {@link #execute()} and {@link #undo()}.
 * The execute method is responsible for altering the program as expected from the user input. In our example, a step is executed.
 * The undo method has the ability to revert from the post-execute state of the program to the state it was in before the
 * execute method was called.
 */
public interface Command {
    void execute();
    void undo();
}
