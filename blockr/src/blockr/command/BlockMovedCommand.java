package blockr.command;

import blockr.AreaController;
import blockr.block.Block;
import blockr.connector.Connector;

import java.util.*;

/**
 * An implementation of the {@link Command} interface.
 * This command is responsible for tracking the relocation of a single block, and needs to have the ability to
 * revert any (dis)connections made by this block movement.
 */
public class BlockMovedCommand implements Command {

    private Map<Connector, Connector> clonedConnectorMap = new HashMap<>(); // maps connectors with their cloned version

    private AreaController areaController;
    private ArrayList<Block> originalBlocks;
    private Map<String, Set<Connector>> originalFreeConnectors;
    private int originalAvailableBlocks;
    private boolean initialized;

    private ArrayList<Block> newBlocks;
    private Map<String, Set<Connector>> newFreeConnectors;
    private int newAvailableBlocks;

    /**
     * Creates this block moved command. This constructor should be used right at the start of a block being <i>dragged</i>,
     * not when the block is released (see the {@link #saveCurrentState()} method for that).
     * This way, it is possible for this command to remember the state it was in before the block has begun to move.
     * @param ac the {@link AreaController} in which blocks are being moved. (Blockr only has 1 single AreaController)
     */
    public BlockMovedCommand(AreaController ac) {
        areaController = ac;
        originalBlocks = cloneBlocks(ac.getProgramArea().getBlocks());
        originalFreeConnectors = cloneFreeConnectors(ac.getProgramArea().getFreeConnectors());
        originalAvailableBlocks = ac.getAvailableBlocks();
    }

    /**
     * Clones all given {@link Block}s, respecting the values of their connectors. This is a tricky function, as every block
     * has multiple connectors, each of which may be connected with another connector. If we simply clone the blocks
     * and their connectors one by one, we get a problem: if a connector was connected to another connector, how do we
     * replace that connector? Perhaps we have cloned it earlier, and we should replace the connector with the corresponding clone.
     * It comes down to making sure all connections between blocks are cloned as well.
     * @param normalBlocks The blocks which need to be cloned
     * @return A list of cloned blocks, with each connector and connection intact.
     */
    private ArrayList<Block> cloneBlocks(ArrayList<Block> normalBlocks) {
        ArrayList<Block> clonedBlocks = new ArrayList<>();
        clonedConnectorMap = new HashMap<>(); // We make sure the map is currently empty, as this function might have been called before.

        for (Block b : normalBlocks) {
            Block clonedBlock = b.clone();
            ArrayList<Connector> clonedConnectors = new ArrayList<>();
            for (Connector c : clonedBlock.getConnectors()) { // For each block, we clone every connector as well
                Connector clonedConnector = c.clone();
                clonedConnector.setBlock(clonedBlock);
                clonedConnectors.add(clonedConnector);
                clonedConnectorMap.put(c, clonedConnector);
                if (clonedConnector.getConnectedTo() != null && clonedConnectorMap.get(clonedConnector.getConnectedTo()) != null) {
                    clonedConnectorMap.get(clonedConnector.getConnectedTo()).connect(clonedConnector);
                    clonedConnector.connect(clonedConnectorMap.get(clonedConnector.getConnectedTo()));
                }
            }
            clonedBlock.setConnectors(clonedConnectors);
            clonedBlocks.add(clonedBlock);
        }
        return clonedBlocks;
    }

    /**
     * We need to save the freeConnector list from {@link blockr.ProgramArea} as well. Since we cloned all blocks,
     * all connectors are cloned as well, and thus this ArrayList needs to contain all the corresponding cloned connectors.
     * @param normalConnectors The initial freeConnector list which needs to be cloned
     * @return The same freeConnector list, with each connector replaced with its newly cloned copy.
     */
    private Map<String, Set<Connector>> cloneFreeConnectors(Map<String, Set<Connector>> normalConnectors) {
        Map<String, Set<Connector>> clonedFreeConnectors = new HashMap<>();
        normalConnectors.forEach((name, freeConnectors) -> {
            clonedFreeConnectors.put(name, new HashSet<>());
            freeConnectors.forEach((freeConnector -> {
                if (clonedConnectorMap.get(freeConnector) != null) clonedFreeConnectors.get(name).add(clonedConnectorMap.get(freeConnector));
                else System.out.println("Could not find corresponding cloned connector while copying free connectors!");
            }));
        });
        return clonedFreeConnectors;
    }

    /**
     * Saves the current state of blocks, so we can later return to it by executing this command.
     * The state needs to be saved before this Command can be executed.
     * Note that this function does not clone the current blocks, as there is no need to. When this function is executed,
     * it will replace all the blocks with a clone of the current state, so the saved state can't be changed.
     */
    public void saveCurrentState() {
        newBlocks = areaController.getProgramArea().getBlocks();
        newFreeConnectors = areaController.getProgramArea().getFreeConnectors();
        newAvailableBlocks = areaController.getAvailableBlocks();
        initialized = true;
    }

    /**
     * Executes this command. However, if the {@link #saveCurrentState()} method has not been called yet, this function
     * throws an error. The reason is that we need to know both the state the program was in before the block was being moved,
     * as well as the state it was in right as the block was released. Without these two states saved, it is impossible
     * to determine which blocks were connected with which other blocks.
     *
     * If this command has been initalized, we set the blockList and freeConnector array of the program area to the state
     * it was in after the block had been moved.
     */
    @Override
    public void execute() {
        if (!initialized) throw new IllegalStateException("This command has not been properly initialized yet!");
        areaController.getProgramArea().setBlocks(cloneBlocks(newBlocks));
        areaController.getProgramArea().setFreeConnectors(cloneFreeConnectors(newFreeConnectors));
        areaController.setAvailableBlocks(newAvailableBlocks);
    }

    /**
     * Undoes this block move action. Since we have saved both the before and after state of the program, we can simply
     * revert to a copy of the initial state. While perhaps not very memory-efficient, it is incredibly robust and
     * prevents a lot of edge cases, as well as making this Command future-proof for any later changes to the mechanics
     * of block-dragging and connecting in Blockr.
     */
    @Override
    public void undo() {
        areaController.getProgramArea().setBlocks(cloneBlocks(originalBlocks));
        areaController.getProgramArea().setFreeConnectors(cloneFreeConnectors(originalFreeConnectors));
        areaController.setAvailableBlocks(originalAvailableBlocks);
    }
}