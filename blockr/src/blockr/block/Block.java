package blockr.block;
import blockr.ProgramArea;
import blockr.connector.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Block implements Cloneable {

    private ArrayList<Connector> connectors = new ArrayList<>();
    private int[] position; // consists of {x1, y1, width, height}
    private boolean highlighted = false;

    Block(int[] dimensions) {
        position = dimensions;
    }

    /**
     * Clones this block, but does not clone the connectors.
     * That should be handled by caller of this function, as it is impossible for this clone function to derive the clone
     * of another block that might be connected to one of these connectors.
     * @return A newly cloned block with references to the old connectors
     */
    @Override
    public Block clone() {
        Block clonedBlock;
        try {
            clonedBlock = (Block) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        clonedBlock.position = clonedBlock.position.clone();
        clonedBlock.highlighted = this.highlighted;
        return clonedBlock;
    }

    public int[] getPosition() {
        return position;
    }

    /**
     * Sets the coordinates for this block by updating the position variable.
     * @param newX the new x coordinate
     * @param newY the new y coordinate
     */
    public void setCoords(int newX, int newY) {
        this.setDimensions(new int[] {newX, newY, getPosition()[2], getPosition()[3]});
        updateConnectorPositions();
    }

    /**
     * @param dimensions the new dimensions of the block. Dimensions uses following order [x, y, width, height]
     */
    public void setDimensions(int[] dimensions) {
        position = dimensions;
        updateConnectorPositions();
        updateConnectedBlocksPositions();
    }

    // todo ideally this should be based on the connector's positions, not arbitrarily half the width or height
    /**
     * Update the position of the lower/right/body blocks that are connected to this block.
     */
    public void updateConnectedBlocksPositions() {
        for (Connector c : getConnectors()) {
            int[] pos = c.getPosition();
            if (c instanceof RightConnector && getBlock(c.getClass()) != null)
                getBlock(RightConnector.class).setCoords(pos[0] + pos[2], (pos[1] + pos[3]/2) - getBlock(RightConnector.class).getPosition()[3]/2);
            else if(c instanceof DownConnector && getBlock(c.getClass()) != null)
                getBlock(c.getClass()).setCoords((pos[0] + pos[2]/2) - getBlock(c.getClass()).getPosition()[2]/2, pos[1]);
        }
    }

    public boolean inRange(double x, double y) {
        return x >= position[0] && x <= position[0] + position[2] && y >= position[1] && y <= position[1] + position[3];
    }

    public ArrayList<Connector> getConnectors() {
        return connectors;
    }

    public void setConnectors(ArrayList<Connector> newConnectors) {
        connectors = newConnectors;
    }

    public ArrayList<Connector> getFreeConnectors() {
        ArrayList<Connector> freeConnectors = new ArrayList<>();
        for (Connector c : connectors) {
            if (c.getConnectedTo() == null) freeConnectors.add(c);
        }
        return freeConnectors;
    }

    /**
     * @return all blocks which are below, to the right, or in a body of this block.
     */
    public ArrayList<Block> getBlocksInScope() {
        ArrayList<Block> scopeBlocks = new ArrayList<>();
        scopeBlocks.add(this);

        Block downBlock = getBlock(DownConnector.class);
        if (downBlock != null) {
            scopeBlocks.addAll(downBlock.getBlocksInScope());
        }

        Block rightBlock = getBlock(RightConnector.class);
        if (rightBlock != null) {
            scopeBlocks.addAll(rightBlock.getBlocksInScope());
        }

        Block bodyBlock = getBlock(BodyConnector.class);
        if (bodyBlock != null) {
            scopeBlocks.addAll(bodyBlock.getBlocksInScope());
        }
        return scopeBlocks;
    }

    /**
     * @param cls the class of the connector
     * @return a connector of the given connector class or if no such connector exists return null
     */
    public Connector getConnector(Class<?> cls) {
        for (Connector connector : getConnectors()) {
            if (connector.getClass().equals(cls)) return connector;
        }
        return null;
    }

    /**
     * Given the class of a connector, return the block (if any) that is connected by a connector of that class to this block.
     * @param cls a connector class used to determine the type of a block.
     * @return the block of the given type to which this block is connected or null if this block does not exists
     */
    public Block getBlock(Class<?> cls){
        Connector connector =  getConnector(cls);
        if (connector == null) return null;
        Connector matchingConnector = connector.getConnectedTo();
        if (matchingConnector == null) return null;
        return matchingConnector.getBlock();
    }

    /**
     * @return all blocks connected to this block, in a downward direction. This means that the lower, right and
     * body blocks will be returned, but any blocks higher or more to the left won't be.
     */
    public ArrayList<Block> getNextConnectedBlocks() {
        ArrayList<Block> allConnected = new ArrayList<>();
        if (getBlock(RightConnector.class) != null) allConnected.add(getBlock(RightConnector.class));
        if (getBlock(DownConnector.class) != null) allConnected.add(getBlock(DownConnector.class));
        if (getBlock(BodyConnector.class) != null) allConnected.add(getBlock(BodyConnector.class));
        return allConnected;
    }

    public void addConnector(Connector connector){connectors.add(connector);}

    /**
     * @param otherBlock the block to be checked
     * @return true if {@code otherBlock} is a conditionBlock and this block is its bodyBlock
     */
    public boolean isBodyBlockOf(Block otherBlock) {
        if (!(otherBlock instanceof NestedBlock)) return false;
        return ((NestedBlock) otherBlock).getBodyBlock() == this;
    }

    public void updateConnectorPositions() {
        for (Connector connector: connectors) {
            connector.updatePosition();
        }
    }

    /**
     * Gives an integer representing the depth of a block. Since for a block to be deeper in scope than another block,
     * it has to lie within another block (a wrapper block such as a While or If block). This means that the height of
     * a block can be used to determine if it is deeper than another block.
     * @return The depth of a block. Depth is used in {@link ProgramArea} to help determine which block should be dragged
     */
    public int getDepth() {
        return -getPosition()[3]; // todo maybe map this to a number
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean newHighlight) {
        highlighted = newHighlight;
    }

}
