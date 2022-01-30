package blockr.block;

import blockr.connector.BodyConnector;
import blockr.connector.DownConnector;
import blockr.connector.RightConnector;

import java.util.ArrayList;

public abstract class NestedBlock extends Block {

    private int startHeight; // since NestedBlocks have dynamic height, we need to remember how large this block is when empty

    NestedBlock (int [] dimensions){
        super(dimensions);
        startHeight = dimensions[3];
    }

    @Override
    // returns all blocks connected to this block, in a downward direction. This means that the lower, right and
    // body blocks will be returned, but any blocks higher or more to the left won't be.
    public ArrayList<Block> getNextConnectedBlocks() {
        ArrayList<Block> allConnected = new ArrayList<>();
        if (getBlock(RightConnector.class) != null) allConnected.add(getBlock(RightConnector.class));
        if (getBlock(DownConnector.class) != null) allConnected.add(getBlock(DownConnector.class));
        if (getBlock(BodyConnector.class) != null) allConnected.add(getBlock(BodyConnector.class));
        return allConnected;
    }

    public void updateHeight(int bodyHeight) {
        int[] curPos = getPosition();
        curPos[3] = bodyHeight + startHeight;
        setDimensions(curPos);
    }

    public ArrayList<Block> getBodyBlocks() {
        ArrayList<Block> bodyBlocks = new ArrayList<>();
        Block currentBlock = getBodyBlock();
        while (currentBlock != null) {
            bodyBlocks.add(currentBlock);
            currentBlock = currentBlock.getBlock(DownConnector.class);
        }
        return bodyBlocks;
    }

    public Block getBodyBlock() {
        return getBlock(BodyConnector.class);
    }
}
