package blockr.connector;

import blockr.block.Block;
import blockr.block.BooleanBlock;

public class RightConnector extends Connector{

    public RightConnector(Block block) {
        super(block);
        setRelativePos(new int[] {-10, 15, 10, 20});
        updatePosition();
    }

    public RightConnector(Block block, int[] relativePos) {
        super(block);
        setRelativePos(relativePos);
        updatePosition();
    }

    @Override
    public void updatePosition(){
        int x = getBlock().getPosition()[0] + getBlock().getPosition()[2] + getRelativePos()[0];
        int y = getBlock().getPosition()[1] + getRelativePos()[1];
        setPosition(new int[] {x, y, getRelativePos()[2], getRelativePos()[3]});
    }
    
    public String getType() {
        return "Right";
    }

    @Override
    public String getCompatibleType() {
        return "Left";
    }

    @Override
    public void updateBlockPosition() {
        if (getConnectedTo() == null) return;
        BooleanBlock toConnect = (BooleanBlock) getConnectedTo().getBlock();
        getBlock().setCoords(toConnect.getPosition()[0] - toConnect.getPosition()[2], toConnect.getPosition()[1]);
    }

}
