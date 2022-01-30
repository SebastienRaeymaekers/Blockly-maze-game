package blockr.connector;

import blockr.block.Block;

import java.util.Arrays;

public class UpConnector extends Connector {

    public UpConnector(Block block) {
        super(block);
        setRelativePos(new int[] {40, 0, 20, 10});
        updatePosition();
    }

    public UpConnector(Block block, int[] relativePos) {
        super(block);
        setRelativePos(relativePos);
        updatePosition();
    }

    @Override
    public void updatePosition() {
        int x = getBlock().getPosition()[0] + getRelativePos()[0];
        int y = getBlock().getPosition()[1] + getRelativePos()[1];
        setPosition(new int[] {x, y, getRelativePos()[2], getRelativePos()[3]});
    }
    
    public String getType() {
        return "Up";
    }

    @Override
    public String getCompatibleType() {
        return "Down";
    }

    @Override
    public void updateBlockPosition() {
        if (getConnectedTo() == null) return;
        getBlock().setCoords(getConnectedTo().getPosition()[0]-getRelativePos()[0], getConnectedTo().getPosition()[1]);
    }

}
