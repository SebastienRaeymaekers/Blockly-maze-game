package blockr.block;

import blockr.connector.BodyConnector;
import blockr.connector.DownConnector;
import blockr.connector.RightConnector;
import blockr.connector.UpConnector;

import java.awt.*;
import java.util.ArrayList;

public abstract class ConditionBlock extends NestedBlock {

    ConditionBlock(int[] dimensions) {
        super(dimensions);
        addConnector(new RightConnector(this));
        addConnector(new DownConnector(this));
        addConnector(new BodyConnector(this, new int[] {50, 50, 20, 10}));
        addConnector(new UpConnector(this));
    }

}

