package blockr.block;
import GameWorldApi.ExecuteResult;
import GameWorldApi.GameWorld;

public class TurnLeftBlock extends ExecutionBlock {
    public TurnLeftBlock(int[] dimensions) {
        super(dimensions);
    }


    @Override
    public ExecuteResult execute(GameWorld gw) {
        return gw.execute("Move Left");
    }
}
