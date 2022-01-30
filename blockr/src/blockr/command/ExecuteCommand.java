package blockr.command;

import GameWorldApi.GameWorld;
import blockr.ExecutionController;
import blockr.block.Block;

import java.util.Stack;

/**
 * An implementation of the {@link Command} interface.
 * This command is responsible for executing steps in the user-made program in the program area.
 * It needs to update the {@link GameWorld}, as well as highlight the correct block in the program area.
 */
public class ExecuteCommand implements Command {

    private ExecutionController executionController;
    private GameWorld originalGameWorld;
    private Stack<Block> originalStack;
    private boolean originalLastBoolean;

    private boolean stopExecution; // If true, this command ends the execution. Otherwise, it executes a single step.

    /**
     * Initialises this ExecuteCommand.
     * @param ec The {@link ExecutionController} this command was created from.
     * @param endExecution A boolean indicating whether this command was a single-step execute, or a stop-executing command.
     */
    public ExecuteCommand(ExecutionController ec, boolean endExecution) {
        executionController = ec;
        originalGameWorld = ec.getGameWorldSnapShot();
        originalStack = (Stack<Block>) ec.executionStack.clone();
        originalLastBoolean = ec.lastBoolean;
        stopExecution = endExecution;
    }

    /**
     * Executing this command means we need to execute a single step or stop altogether with executing.
     * Luckily, these methods are already present in {@link ExecutionController}, so simply calling these is sufficient.
     */
    @Override
    public void execute() {
        if (stopExecution) executionController.stopExecuting();
        else executionController.executeOneStep();
    }

    /**
     * Undoes this execute action.
     * This function stops highlighting the current block, and highlights the block which was highlighted when this command
     * was created. Furthermore, it reverts the executionStack of the {@link ExecutionController} back to the state it was in
     * before this command was executed.
     * Lastly, it elegantly resets the {@link GameWorld} by using the snapshot we obtained by calling the
     * {@link GameWorld#getSnapShot()} method to revert it to the state it was in before this command was executed.
     */
    @Override
    public void undo() {
        // We stop highlighting the current top-most block on the stack.
        if (!executionController.executionStack.isEmpty()) executionController.executionStack.peek().setHighlighted(false);
        executionController.executionStack = (Stack<Block>) originalStack.clone();  // We replace the stack with the one it was before this command
        // We highlight the new top-block fo this stack.
        if (!executionController.executionStack.isEmpty()) executionController.executionStack.peek().setHighlighted(true);
        executionController.lastBoolean = originalLastBoolean;

        executionController.setGameWorldSnapShot(originalGameWorld.getSnapShot());
    }
}