package blockr;

import blockr.block.*;

public class Palette extends Area {

    private int nextFunctionID = 1;

    public Palette(int[] bounds) {
        super(bounds);
        refreshBlocks();
    }

    /**
     * Resets the blockList by adding new blocks to it.
     */
    public void refreshBlocks() {
        blocks.clear();
        blocks.add(new MoveBlock(new int[] {30, 30, 100, 50}));
        blocks.add(new TurnRightBlock(new int[] {180, 30, 100, 50}));

        blocks.add(new TurnLeftBlock(new int[] {30, 130, 100, 50}));
        blocks.add(new WhileBlock(new int[] {180, 130, 100, 80}));

        blocks.add(new IfBlock(new int[] {30, 230, 100, 80}));
        blocks.add(new WallInFrontBlock(new int[] {180, 240, 100, 50}));

        blocks.add(new NotBlock(new int[] {50, 340, 60, 50}));
        blocks.add(new FunctionDefinitionBlock(new int[] {180, 330, 100, 80}, getNextFunctionID()));

        for (int i = nextFunctionID - 1; i >= 1; i--)
            blocks.add(new FunctionCallBlock(new int[] {30+(i*20), 420, 100, 50}, i));

    }

    public void clearBlocks() {
        blocks.clear();
    }

    public int getNextFunctionID() {
        return nextFunctionID;
    }

    public void incrementNextFunctionID(){nextFunctionID++;}
    public void decrementNextFunctionID(){nextFunctionID--;}

}
