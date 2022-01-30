package blockr.connector;

import blockr.block.Block;

public class LeftConnector extends Connector{

    public LeftConnector(Block block) {
        super(block);
        setRelativePos(new int[] {-10, 15, 10, 20});
        updatePosition();
    }


    public LeftConnector(Block block, int[] relativePos) {
        super(block);
        setRelativePos(relativePos);
        updatePosition();
    }

    @Override
    public void updatePosition(){
        int x = getBlock().getPosition()[0] + getRelativePos()[0];
        int y = getBlock().getPosition()[1] + getRelativePos()[1];
        setPosition(new int[] {x, y, getRelativePos()[2], getRelativePos()[3]});
    }
    
    public String getType() {
        return "Left";
    }

    @Override
    public String getCompatibleType() {
        return "Right";
    }

    @Override
    public void updateBlockPosition() {
        if (getConnectedTo() == null) return;
        Block toConnect = getConnectedTo().getBlock();
        getBlock().setCoords(toConnect.getPosition()[0] + toConnect.getPosition()[2],
                            getConnectedTo().getPosition()[1] - getRelativePos()[1]);
    }

}
