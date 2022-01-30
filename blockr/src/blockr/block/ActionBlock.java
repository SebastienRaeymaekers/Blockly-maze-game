package blockr.block;

import blockr.connector.DownConnector;
import blockr.connector.UpConnector;

public class ActionBlock extends Block{
    ActionBlock(int[] dimensions) {
        super(dimensions);
        addConnector(new DownConnector(this));
        addConnector(new UpConnector(this));
    }


}
