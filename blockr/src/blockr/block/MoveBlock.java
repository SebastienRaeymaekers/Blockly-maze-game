package blockr.block;

import GameWorldApi.ExecuteResult;
import GameWorldApi.GameWorld;

public class MoveBlock extends ExecutionBlock {

    public MoveBlock(int[] dimensions) {
        super(dimensions);
    }

    @Override
    public ExecuteResult execute(GameWorld gw) {
        return gw.execute("Move Forward");
    }
}
