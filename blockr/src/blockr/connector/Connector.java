package blockr.connector;

import blockr.block.Block;
import blockr.block.ConditionBlock;

import java.awt.*;
import java.util.Arrays;

public abstract class Connector implements Cloneable {

    private Block block;
    private boolean highlight = false;
    private int[] position;
    private int[] relativePos; //Describes how this connector is positioned towards its block

    private Connector connectedTo = null;

    public abstract String getType();
    public abstract String getCompatibleType();
    public Connector(Block block) {
        this.block = block;
    }

    @Override
    public Connector clone() {
        Connector clonedConnector;
        try {
            clonedConnector = (Connector) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        clonedConnector.position = clonedConnector.position.clone();
        clonedConnector.relativePos = clonedConnector.relativePos.clone();
        return clonedConnector;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block b) {
        block = b;
    }

    public int[] getPosition() {
        return position;
    }

    public Connector getConnectedTo() {
        return connectedTo;
    }
    public void connect(Connector newConnector) {
        this.connectedTo = newConnector;
    }

    public abstract void updatePosition();

    /**
     * Update the position of the block of this connector to be adjacent to the block of the connected connector.
     */
    public abstract void updateBlockPosition();

    public void setPosition(int[] newPos) {
        this.position = newPos;
    }

    public int[] getRelativePos() {
        return relativePos;
    }

    public void setRelativePos(int[] relativePos) {
        this.relativePos = relativePos;
    }

    public boolean isHighlighted() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

}

