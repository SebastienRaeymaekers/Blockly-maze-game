package blockr;

import blockr.block.*;
import blockr.connector.*;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;

public class ProgramArea extends Area {

    private Map<String, Set<Connector>> freeConnectors = new HashMap<>();

    public ProgramArea(int[] bounds) {
        super(bounds);
        freeConnectors.put("Left", new HashSet<>());
        freeConnectors.put("Right", new HashSet<>());
        freeConnectors.put("Down", new HashSet<>());
        freeConnectors.put("Up", new HashSet<>());
    }

    /**
     * Called when a block is released on top of the program area.
     * This function adds the block to the blockList, as well as its free connectors to the freeConnectors list.
     * When this is done, it looks for any nearby compatible connectors. If any are available, it connects the block.
     * Finally, the height of this block (and that of all above it) is updated, as it is possible this block was released in a nestedBlock.
     *
     * @param block the dragged block which is now released in this program area.
     */
    public void release(Block block) {
        addBlock(block);
        tryToConnectBlock(block);
        updateHeights(block);
    }

    /**
     * Replaces the current blocks with another block list. This can be used to revert to a previous state, or to load
     * in an entire program at once.
     *
     * @param newBlocks The blocks this program area should replace the current block list with.
     */
    public void setBlocks(ArrayList<Block> newBlocks) {
        blocks = newBlocks;
    }

    /**
     * Add {@code block}, all its free connectors and all its connected right/lower/body blocks
     *
     * @param block the to be added block
     */
    private void addBlock(Block block) {
        blocks.add(block);
        addFreeConnectors(block);
        for (Block connectedBlock : block.getNextConnectedBlocks()) {
            addBlock(connectedBlock);
        }
    }

    /**
     * Remove {@code block}, all its free connectors and all its connected right/lower/body blocks
     *
     * @param block the to be removed block
     */
    private void removeBlock(Block block) {
        blocks.remove(block);
        removeFreeConnectors(block);
        for (Block connectedBlock : block.getNextConnectedBlocks()) {
            removeBlock(connectedBlock);
        }
    }

    /**
     * Removes a given function call block by properly disconnecting it from any blocks above or below it, and
     * connects the top block to the block below if both exist.
     *
     * @param block The function call block which should be removed from the entire program area.
     */
    private void removeFunctionCallBlock(FunctionCallBlock block) {
        Block aboveBlock = block.getBlock(UpConnector.class);
        Block belowBlock = block.getBlock(DownConnector.class);
        if (aboveBlock != null) {
            Connector matchingConnector = block.getConnector(UpConnector.class).getConnectedTo();

            if (belowBlock != null) {
                block.getConnector(DownConnector.class).connect(null);
                matchingConnector.connect(belowBlock.getConnector(UpConnector.class));
                belowBlock.getConnector(UpConnector.class).connect(matchingConnector);
                aboveBlock.updateConnectedBlocksPositions();
                updateHeights(belowBlock);
            } else {
                disconnectConnector(matchingConnector);
                updateHeights(aboveBlock);
            }

        } else if (belowBlock != null) {
            disconnectConnector(block.getConnector(DownConnector.class));
        }
        removeFreeConnectors(block);
    }

    /**
     * Removes all function call blocks related to the given ID.
     *
     * @param toRemoveID the ID of the call blocks which should be removed
     * @return the amount of blocks which were removed
     */
    int removeFunctionCallBlocks(int toRemoveID) {
        int blocksRemoved = 0;
        ArrayList<Block> callsToRemove = new ArrayList<>();
        for (Block block : blocks) {
            if (block.getClass() != FunctionCallBlock.class || ((FunctionCallBlock) block).getUniqueID() != toRemoveID)
                continue;

            callsToRemove.add(block);
            removeFunctionCallBlock((FunctionCallBlock) block);
            blocksRemoved++;
        }
        blocks.removeAll(callsToRemove);
        return blocksRemoved;
    }

    /**
     * Resets the program area by removing all blocks.
     * This is useful for when we want to load in a new game and clear the current program.
     */
    public void resetProgramArea() {
        while (!blocks.isEmpty()) removeBlock(blocks.get(0));
    }

    /**
     * Executes when a block in programArea starts being dragged. We disconnect it from any left or up connections it might have,
     * remove it from the list of blocks and update the height of wrapper blocks if necessary.
     *
     * @param block the clicked on block
     */
    public void startDraggingBlock(Block block) {
        Connector leftConnector = block.getConnector(LeftConnector.class);
        if (leftConnector != null) disconnectConnector(leftConnector);
        Connector upConnector = block.getConnector(UpConnector.class);
        if (upConnector != null) {
            Connector matchedConnector = upConnector.getConnectedTo();
            disconnectConnector(upConnector);
            if (matchedConnector != null) updateHeights(matchedConnector.getBlock());
        }

        removeBlock(block);
    }

    /**
     * Disconnects a connector with the connector it is currently matched with.
     * The reason ProgramArea manages this method is to ensure the freeConnector list is fully correct.
     *
     * @param c The to be disconnected connector
     */
    private void disconnectConnector(Connector c) {
        if (c.getConnectedTo() == null) return;

        freeConnectors.get(c.getType()).add(c);
        freeConnectors.get(c.getConnectedTo().getType()).add(c.getConnectedTo());

        c.getConnectedTo().connect(null);
        c.connect(null);
    }

    /**
     * Check for every open connector of a block whether a connection can be made.
     * If a connection is possible, we connect the block and remove the connectors from the freeConnector list.
     *
     * @param block The block for which we check any compatible nearby connections.
     */
    private void tryToConnectBlock(Block block) {
        for (Connector openConnector : block.getFreeConnectors()) {
            Connector closest = getClosestCompatibleConnector(openConnector);

            if (closest == null) continue;
            freeConnectors.get(openConnector.getType()).remove(openConnector);
            freeConnectors.get(closest.getType()).remove(closest);

            openConnector.connect(closest);
            closest.connect(openConnector);
            openConnector.updateBlockPosition();
            return;
        }
    }

    private void addFreeConnectors(Block block) {
        addFreeConnectors(block.getFreeConnectors());
    }

    private void addFreeConnectors(ArrayList<Connector> connectors) {
        for (Connector connector : connectors) {
            freeConnectors.get(connector.getType()).add(connector);
        }
    }

    private void removeFreeConnectors(Block block) {
        for (Connector connector : block.getConnectors()) {
            if (connector.getConnectedTo() == null) freeConnectors.get(connector.getType()).remove(connector);
        }
    }

    /**
     * Check for every open connector of a block whether the requirements of a connection are met.
     * If this is the case we highlight this connector and the connector it can connect to.
     *
     * @param block the to be checked block
     */
    public void setConnectorHighlight(Block block) {
        for (Connector c : block.getFreeConnectors()) {
            Connector closestConnector = getClosestCompatibleConnector(c);
            if (closestConnector == null) continue;
            c.setHighlight(true);
            closestConnector.setHighlight(true);
        }
    }

    /**
     * @param c1 the to be checked connector
     * @return the closest compatible connector where distance(c1.getPosition(), closest.getPosition()) <= 15}
     */
    private Connector getClosestCompatibleConnector(Connector c1) {
        Connector closestConnector = null;
        double closestDistance = Double.MAX_VALUE;
        for (Connector c2 : freeConnectors.get(c1.getCompatibleType())) {
            if (c1.getBlock() == c2.getBlock()) continue;
            double dist = distance(c1.getPosition(), c2.getPosition());
            if (dist >= closestDistance || dist > 15) continue;
            closestDistance = dist;
            closestConnector = c2;
        }
        return closestConnector;
    }

    public Map<String, Set<Connector>> getFreeConnectors() {
        return freeConnectors;
    }

    public void setFreeConnectors(Map<String, Set<Connector>> newConnectors) {
        freeConnectors = newConnectors;
    }

    /**
     * Update the height of all wrapper blocks {@link ConditionBlock} to match the amount of blocks in their body.
     * Given {@code startBlock}, this function finds the highest block in the same scope, then calculates the height of the scope.
     * Using this height we recursively call this function for the scope above until the heights of all above scopes have been updated.
     *
     * @param startBlock The block we start at
     */
    private void updateHeights(Block startBlock) {
        // If we start with an empty conditionBlock, it is likely this function was called upon disconnecting all
        // body blocks from this conditionBlock. We update its height with a 0 value.
        if (startBlock instanceof NestedBlock && ((NestedBlock) startBlock).getBodyBlock() == null)
            ((NestedBlock) startBlock).updateHeight(0);

        Block higherBlock = startBlock;
        while (higherBlock != null) {
            if (higherBlock.isBodyBlockOf(higherBlock.getBlock(UpConnector.class)))
                break; // The next block is a scope higher
            higherBlock = higherBlock.getBlock(UpConnector.class);
        }
        if (higherBlock == null) return; // we are at the top scope, all heights are updated

        // If we got here, we know we broke out of the while loop because the next UpperBlock is of another scope (we're the first block in a wrapper block)
        int scopeHeight = calculateHeightOfScope(higherBlock);
        NestedBlock wrapperBlock = (NestedBlock) higherBlock.getBlock(UpConnector.class);

        wrapperBlock.updateHeight(scopeHeight);

        // Now we have updated the wrapperBlock we were in the body of, but it might very well be possible this is another nested block.
        // We call updateHeights() again with the startBlock being the wrapperBlock we just updated the height of.
        updateHeights(wrapperBlock);
    }

    /**
     * @param startBlock the block we start at
     * @return the sum of heights of {@code startBlock} and all the blocks under it
     */
    private int calculateHeightOfScope(Block startBlock) {
        if (startBlock == null) return 0;
        int scopeHeight = startBlock.getPosition()[3];

        Block nextBlock = startBlock.getBlock(DownConnector.class);
        while (nextBlock != null) {
            scopeHeight += nextBlock.getPosition()[3];
            nextBlock = nextBlock.getBlock(DownConnector.class);
        }

        return scopeHeight;
    }

    private static double distance(int[] pos1, int[] pos2) {
        int[] mid1 = new int[]{(pos1[0] + pos1[2]) / 2, (pos1[1] + pos1[3]) / 2};
        int[] mid2 = new int[]{(pos2[0] + pos2[2]) / 2, (pos2[1] + pos2[3]) / 2};
        return Math.sqrt(Math.pow(mid1[0] - mid2[0], 2) + Math.pow(mid1[1] - mid2[1], 2));
    }

    FunctionDefinitionBlock getMatchingDefinitionBlock(int uniqueID) {
        for (Block block : blocks) {
            if (block instanceof FunctionDefinitionBlock && ((FunctionDefinitionBlock) block).getUniqueID() == uniqueID)
                return (FunctionDefinitionBlock) block;
        }
        return null;
    }

    /**
     * Decrement the ids of functionCallBlocks and functionDefinitionBlocks that have a higher id than {@code id}
     * @param id the id of a block that is about to be removed
     */
    void decrementHigherIDs(int id) {
        for (Block block : blocks) {
            if (block instanceof FunctionDefinitionBlock && ((FunctionDefinitionBlock) block).getUniqueID() > id)
                ((FunctionDefinitionBlock) block).setUniqueID(((FunctionDefinitionBlock) block).getUniqueID() - 1);
            else if (block instanceof FunctionCallBlock && ((FunctionCallBlock) block).getUniqueID() > id)
                ((FunctionCallBlock) block).setUniqueID(((FunctionCallBlock) block).getUniqueID() - 1);
        }
    }
}
