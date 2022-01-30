package blockr.block;

import blockr.connector.LeftConnector;
import blockr.connector.RightConnector;

import java.awt.*;

public class NotBlock extends BooleanBlock {
    public NotBlock(int[] dimensions) {
        super(dimensions);
        addConnector(new LeftConnector(this));
        addConnector(new RightConnector(this));
    }

}
