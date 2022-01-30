package blockr.connector;

import blockr.block.Block;

public class DownConnector extends Connector{

    public DownConnector(Block block) {
        super(block);
        setRelativePos(new int[] {40, 0, 20, 10});
        updatePosition();
    }

    public DownConnector(Block block, int[] relativePos) {
        super(block);
        setRelativePos(relativePos);
        updatePosition();
    }

    @Override
    public void updatePosition(){
        int x = getBlock().getPosition()[0] + getRelativePos()[0];
        int y = getBlock().getPosition()[1] + getBlock().getPosition()[3] + getRelativePos()[1];
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

    @Override
    public void updateBlockPosition() {
        if (getConnectedTo() == null) return;
        Block toConnect = getConnectedTo().getBlock();
        getBlock().setCoords(toConnect.getPosition()[0], toConnect.getPosition()[1] - getBlock().getPosition()[3]);
    }

}
