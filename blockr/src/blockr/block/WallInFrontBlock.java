package blockr.block;
import GameWorldApi.GameWorld;

import blockr.connector.LeftConnector;

public class WallInFrontBlock extends BooleanBlock {
    public WallInFrontBlock(int[] dimensions) {
        super(dimensions);
        addConnector(new LeftConnector(this));
    }

    public boolean execute(GameWorld gw) {
        return gw.eval("WallInFront");
    }
}
