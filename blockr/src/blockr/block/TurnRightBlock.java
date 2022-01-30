package blockr.block;
import GameWorldApi.ExecuteResult;
import GameWorldApi.GameWorld;

public class TurnRightBlock extends ExecutionBlock {
    public TurnRightBlock(int[] dimensions) {
        super(dimensions);
    }

    @Override
    public ExecuteResult execute(GameWorld gw) {
        return gw.execute("Move Right");
    }
}
