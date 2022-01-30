package blockr.connector;

import blockr.block.Block;
import blockr.block.ConditionBlock;

public class BodyConnector extends DownConnector {

    public BodyConnector(Block block) {
        super(block);
        updatePosition();
    }

    public BodyConnector(Block block, int[] relativePos) {
        super(block, relativePos);
    }

    // We want this connector to stay in place, regardless of how large the block becomes.
    @Override
    public void updatePosition() {
        int x = getBlock().getPosition()[0] + getRelativePos()[0];
        int y = getBlock().getPosition()[1] + getRelativePos()[1];
        setPosition(new int[] {x, y, getRelativePos()[2], getRelativePos()[3]});
    }

    @Override
    public String getType() {
        return "Down";
    }

    @Override
    public String getCompatibleType() {
        return "Up";
    }

}
