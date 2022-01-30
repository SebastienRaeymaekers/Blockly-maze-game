package blockr;

import GameWorldApi.GameWorld;
import blockr.block.Block;
import blockr.block.FunctionCallBlock;
import blockr.block.FunctionDefinitionBlock;
import blockr.command.*;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class EventHandler {

    private AreaController areaController;
    private ExecutionController executionController;
    private Stack<Command> commandHistoryStack = new Stack<>(); // Contains all previous commands, top being most recent
    private Stack<Command> undoHistoryStack = new Stack<>(); // Contains all commands we undid, top being most recent undo
    private int[] mouseDragOffset = new int[]{0, 0};

    public EventHandler(AreaController areaController) {
        this.areaController = areaController;
        this.executionController = new ExecutionController(areaController.getGameWorld(), areaController.getProgramArea());
        this.currentBlockMovedCommand = new BlockMovedCommand(areaController);
    }

    public ExecutionController getExecutionController() {
        return executionController;
    }

    public AreaController getAreaController() {
        return areaController;
    }

    /**
     * Executes a case depending on what mouse action {@code id} was performed and on what area the mouse is positioned.
     *
     * @param id The received type of mouse event. We distinguish 3 types: 1) mouse click 2) mouse drag 3) mouse release
     * @param x  The x coordinate of the mouse
     * @param y  the y coordinate of the mouse
     */
    public void handleMouseEvent(int id, int x, int y) {
        switch (id) {
            case MouseEvent.MOUSE_PRESSED:
                handleMousePressed(x, y);
                break;

            case MouseEvent.MOUSE_DRAGGED:
                handleMouseDragged(x, y);
                break;

            case MouseEvent.MOUSE_RELEASED:
                handleMouseReleased(x, y);
                break;
        }
    }

    /**
     * Handles a mouse press. If the position of the cursor was inside the game world area upon clicking, we open
     * the interface for loading in a game, and load the game in once it was chosen.
     */
    private void handleMousePressed(int x, int y) {
        if (areaController.inRangeWorld(x, y)) {
            GameWorld gw = areaController.loadGame();
            executionController.setInitialGameWorld(gw);
        }
    }

    /**
     * Since control-Z needs to revert to a previous state, handling dragged blocks becomes a bit tricky.
     * Dragging is not an instantaneous action, so we need to "remember" what state we were in when we start the dragging,
     * so we can return to that state once we release the blocks. That's why we have a blockMovedCommand stored at all times,
     * which we first initialize with the current state, and later update with the info it needs for the release of a block
     * (so first we give it the info for its undo command, later we give it the info for its execute command)
     */
    private BlockMovedCommand currentBlockMovedCommand;

    /**
     * Called on each mouse drag update.
     * If draggedBlock is currently null, we look for any block below the position of the cursor to start dragging them.
     * If a block was already being dragged, we update its position to the cursor and highlight any nearby compatible connectors.
     */
    private void handleMouseDragged(int x, int y) {
        if (executionController.isExecuting()) return;
        if (areaController.getDraggedBlock() == null) {
            // We might start dragging something, we create a new command to save the current state of the blocks.
            currentBlockMovedCommand = new BlockMovedCommand(areaController);
            areaController.startDraggingBlocks(x, y);
            if (areaController.getDraggedBlock() != null)
                mouseDragOffset = new int[]{areaController.getDraggedBlock().getPosition()[0] - x, areaController.getDraggedBlock().getPosition()[1] - y};
        } else areaController.updateDraggedBlocks(x + mouseDragOffset[0], y + mouseDragOffset[1]);
    }

    /**
     * Handles a mouse release. If no block was being dragged, this function returns instantly.
     * Otherwise, if the block was being dragged over the program area, we add the block to the area.
     * If the cursor position was anywhere else, we remove the dragged blocks from the game.
     * This function finalizes the currentBlockMoved command, executes it and creates a new one for the next movement.
     */
    private void handleMouseReleased(int x, int y) {
        if (areaController.getDraggedBlock() == null) return;
        areaController.releaseBlocks(x, y, mouseDragOffset);
        currentBlockMovedCommand.saveCurrentState();
        executeNewCommand(currentBlockMovedCommand);
    }

    /**
     * Handles a given KeyEvent. This function is used to redirect any inputs to the program to the corresponding function,
     * and has no knowledge about the game itself.
     *
     * @param id      The received type of key event. Only key presses(id == KeyEvent.KEY_PRESSED) are accepted.
     * @param keyCode The code of the key being pressed
     * @param keyChar The character of the key being typed
     * @param e       The entire keyEvent, which can be used to detect whether control or shift is held currently.
     */
    public void handleKeyEvent(int id, int keyCode, char keyChar, KeyEvent e) {
        if (id != KeyEvent.KEY_PRESSED) return;

        // Essential key events
        if (keyCode == KeyEvent.VK_F5) executeNewCommand(new ExecuteCommand(executionController, false));
        else if (keyCode == KeyEvent.VK_ESCAPE) executeNewCommand(new ExecuteCommand(executionController, true));
        else if (keyCode == KeyEvent.VK_Z && e.isControlDown() && e.isShiftDown()) redoAction();
        else if (keyCode == KeyEvent.VK_Z && e.isControlDown() && !e.isShiftDown()) undoAction();

        // These are for easy debugging, should perhaps be removed in final build. TODO: remove these lines?
        else if (keyCode == KeyEvent.VK_UP) areaController.getGameWorld().execute("Move Up");
        else if (keyCode == KeyEvent.VK_LEFT) areaController.getGameWorld().execute("Move Left");
        else if (keyCode == KeyEvent.VK_RIGHT) areaController.getGameWorld().execute("Move Right");
        else if (keyCode == KeyEvent.VK_DOWN) areaController.getGameWorld().execute("Move Down");
        else if (keyCode == KeyEvent.VK_SPACE) areaController.getGameWorld().execute("Reveal");
        else if (keyCode == KeyEvent.VK_F) areaController.getGameWorld().execute("Put Flag");
        else if (keyCode == KeyEvent.VK_R) areaController.getGameWorld().execute("Restart");
        else if (keyCode == KeyEvent.VK_F9) showDebug();
    }

    // Wrapper function for calling the handleKeyEvent with multiple parameters
    public void handleKeyEvent(KeyEvent e) {
        handleKeyEvent(e.getID(), e.getKeyCode(), e.getKeyChar(), e);
    }

    /**
     * Executes a given command and removes any undo-history as we are starting a new one.
     *
     * @param command The command which should be executed
     */
    private void executeNewCommand(Command command) {
        if (!undoHistoryStack.empty())
            undoHistoryStack.clear(); // We are executing a new command, we delete undo history
        commandHistoryStack.push(command);
        command.execute();
    }

    /**
     * Redoes an action which the user undid in the past. This function takes the topmost {@link Command} from the undoHistoryStack
     * and executes it once again. If there is no undo history, this function has no effect.
     * Furthermore, this function cannot be called while the user is currently performing an input (e.g. dragging blocks)
     * <p>
     * After this function is called, the command will be moved from the top of the undoHistoryStack
     * to the top of the commandHistoryStack.
     */
    private void redoAction() {
        if (areaController.getDraggedBlock() != null) return; // we prevent edge cases created by undoing while dragging
        if (undoHistoryStack.empty()) return;
        undoHistoryStack.peek().execute();
        commandHistoryStack.push(undoHistoryStack.pop());
    }

    /**
     * Undoes the most recent action the user has performed. Every action is stored as a {@link Command} and put on a
     * commandHistoryStack. This function takes the topmost (the most recent) command from this stack, calls its
     * {@link Command#undo()}, and puts it on the undoHistoryStack.
     * This function cannot be called while the user is currently performing an input (e.g. dragging blocks)
     * <p>
     * When this function is completed, the state of the program will be back to the state it was in before the most recent
     * action of the user.
     */
    private void undoAction() {
        if (areaController.getDraggedBlock() != null) return; // we prevent edge cases created by undoing while dragging
        if (commandHistoryStack.empty()) return;
        commandHistoryStack.peek().undo();
        undoHistoryStack.push(commandHistoryStack.pop());
    }

    private void showDebug() {
        System.out.println(areaController.getProgramArea());
    }
}