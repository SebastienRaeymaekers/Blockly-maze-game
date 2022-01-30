package blockr;

import GameWorldApi.ExecuteResult;
import GameWorldApi.GameWorld;

import blockr.block.*;
import blockr.connector.DownConnector;
import blockr.connector.RightConnector;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;


public class ExecutionController {

    public Stack<Block> executionStack = new Stack<>();
    private GameWorld gameWorld;
    private GameWorld initialGameWorld;
    private ProgramArea programArea;
    public boolean lastBoolean;
    private ExecuteResult gameStatus = null;

    public ExecutionController(GameWorld gw, ProgramArea pa) {
        gameWorld = gw;
        programArea = pa;
        initialGameWorld = gameWorld.getSnapShot();
    }

    void setInitialGameWorld(GameWorld initialGameWorld) {
        this.initialGameWorld = initialGameWorld;
    }

    public ExecuteResult getGameStatus() {
        return gameStatus;
    }

    /**
     * Determines whether the current program in the program area is valid. This means there can only be one cluster for
     * the main program, along possibly some function definitions. This is checked through the amount of open connectors.
     * There can only be one free up connector, and there can no be open side connectors.
     *
     * @return true if the program is valid.
     */
    private boolean isValidProgram() {
        if (programArea.getFreeConnectors().get("Right").size() > 0) return false;
        if (programArea.getFreeConnectors().get("Left").size() > 0) return false;
        return programArea.getFreeConnectors().get("Up").size() <= 1;
    }

    private void startExecution() throws IllegalStateException {
        if (!isValidProgram()) throw new IllegalStateException("Current program is not valid!");
        initialiseStack();
    }

    /**
     * Creates the initial stack which we begin the program with. Retrieves all top-level blocks, and loops through them
     * in *reverse*, pushing block by block on the stack. If we come across a while or an if, we push the
     * boolean blocks one by one on the stack as well (so a while with an not-wallinfront will be pushed as a whileBlock, a notBlock, then the wall in front).
     */
    private void initialiseStack() {
        executionStack.clear();
        ArrayList<Block> topLevelBlocks = new ArrayList<>();
        // We start from the top, by looking at the only free up connector and getting its attached block
        Block currentBlock = programArea.getFreeConnectors().get("Up").iterator().next().getBlock();
        while (currentBlock != null) {
            topLevelBlocks.add(currentBlock);
            currentBlock = currentBlock.getBlock(DownConnector.class);
        }
        addBlocksToStack(topLevelBlocks);
        gameWorld.setSnapShot(initialGameWorld.getSnapShot());
    }

    /**
     * Push the contents of {@code blockList} onto the stack. This function should be used instead of the built-in
     * addAll() function to ensure the list is pushed in reverse, and right-connected blocks are added as well.
     *
     * @param blockList A list of blocks
     */
    private void addBlocksToStack(ArrayList<Block> blockList) {
        if (blockList == null) return;
        ListIterator li = blockList.listIterator(blockList.size());

        while (li.hasPrevious()) {
            Block newBlock = (Block) li.previous();
            executionStack.push(newBlock);
            if (newBlock instanceof ConditionBlock) addBooleans((ConditionBlock) newBlock);
        }
    }

    /**
     * push the conditions of {@code startBlock} step by step onto the stack
     *
     * @param startBlock a condition block
     */
    private void addBooleans(ConditionBlock startBlock) {
        BooleanBlock nextBlock = (BooleanBlock) startBlock.getBlock(RightConnector.class);
        while (nextBlock != null) {
            executionStack.push(nextBlock);
            nextBlock = (BooleanBlock) nextBlock.getBlock(RightConnector.class);
        }

    }

    /**
     * Execute one step of the program. We assume the first F5 key does not execute anything yet, but only starts the execution
     */
    public void executeOneStep() throws IllegalStateException {
        if (executionStack.isEmpty()) {
            startExecution();
        } else {
            Block topBlock = executionStack.pop();
            topBlock.setHighlighted(false); // We are executing this block now, it no longer needs to be highlighted
            executeBlock(topBlock);
        }
        if (executionStack.size() > 0) executionStack.peek().setHighlighted(true); // next block gets highlighted
    }

    /**
     * Executes {@code blockToExecute}. Each type of block has different behaviour:
     * 1) An ActionBlock directly edits the GameWorld with an action
     * 2) A not block flips the value of the last seen boolean
     * 3) A wall in front block sets the last seen boolean value depending on whether there is a wall in front of the robot
     * 4) A ConditionBlock looks at the last seen boolean (since we updated it in the previous execution step), and if it
     * was true, puts the body of the ConditionBlock on the stack. If it was a While, the while itself gets put on the bottom
     * of the stack as well, so we will execute it again once we finished executing the body.
     *
     * @param blockToExecute the to be executed block
     */
    private void executeBlock(Block blockToExecute) {
        if (blockToExecute instanceof ExecutionBlock) gameStatus = ((ExecutionBlock) blockToExecute).execute(gameWorld);
        else if (blockToExecute instanceof WallInFrontBlock)
            lastBoolean = ((WallInFrontBlock) blockToExecute).execute(gameWorld);
        else if (blockToExecute instanceof NotBlock) lastBoolean = !lastBoolean;
        else if (blockToExecute instanceof IfBlock && lastBoolean)
            addBlocksToStack(((ConditionBlock) blockToExecute).getBodyBlocks());
        else if (blockToExecute instanceof WhileBlock && lastBoolean) {
            executionStack.push(blockToExecute); // We push the while on top of the stack again, so we will check it once more
            addBooleans((ConditionBlock) blockToExecute);
            addBlocksToStack(((ConditionBlock) blockToExecute).getBodyBlocks());
        } else if (blockToExecute instanceof FunctionCallBlock) {
            FunctionDefinitionBlock definitionBlock = programArea.getMatchingDefinitionBlock(((FunctionCallBlock) blockToExecute).getUniqueID());
            if (definitionBlock != null) addBlocksToStack(definitionBlock.getBodyBlocks());
            else throw new IllegalStateException("Tried to execute function call with no corresponding definition!");
        }
    }

    /**
     * @return A boolean indicating whether this program is currently executing.
     */
    public boolean isExecuting() {
        return executionStack.size() > 0;
    }

    /**
     * Stop the execution of the program. If the user wants to stop while nothing is happening,
     * we assume they want to see the initial position of the robot
     */
    public void stopExecuting() {
        gameWorld.setSnapShot(initialGameWorld.getSnapShot());
        if (executionStack.isEmpty()) return;
        executionStack.peek().setHighlighted(false);
        executionStack.clear();
    }

    /**
     * @return A snapshot of the current gameworld. This snapshot can be used to revert to a previous state later on.
     */
    public GameWorld getGameWorldSnapShot() {
        return gameWorld.getSnapShot();
    }

    /**
     * Sets the current world to a given snapshot. Care must be taken as the gameworld will become this snapshot, and
     * any references to the new gameworld will affect the snapshot as well. If wanting to set a gameworld to a given
     * snapshot without the possibility of the snapshot changing because of any user input, pass this function a clone
     * of the snapshot.
     *
     * @param gw The snapshot which the gameworld will become
     */
    public void setGameWorldSnapShot(GameWorld gw) {
        gameWorld.setSnapShot(gw);
    }

}
