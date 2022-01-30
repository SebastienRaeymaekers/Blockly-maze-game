package blockr.block;
import GameWorldApi.ExecuteResult;
import GameWorldApi.GameWorld;

public abstract class ExecutionBlock extends ActionBlock {
    ExecutionBlock(int[] dimensions) {
        super(dimensions);
    }

    public abstract ExecuteResult execute(GameWorld gw);
}
