package blockr;

import blockr.block.Block;

import java.awt.*;
import java.util.ArrayList;

public abstract class Area {
    private int[] bounds;
    ArrayList<Block> blocks = new ArrayList<>();

    public Area(int[] bounds) {
        this.bounds = bounds;
    }

    public int[] getBounds() {
        return bounds;
    }

    public boolean inRange(double x, double y) {
        return x >= bounds[0] && x <= bounds[0] + bounds[2] && y >= bounds[1] && y <= bounds[1] + bounds[3];
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    /**
     * @param mouseX x coordinate of the mouse
     * @param mouseY y coordinate of the mouse
     * @param blocks A list of blocks
     * @return The block which is below the cursor with the most depth or null if no block was below the cursor
     */
    public static Block findClickedBlock(double mouseX, double mouseY, ArrayList<Block> blocks) {
        Block candidate = null;
        int highestDepth = Integer.MIN_VALUE;
        for (Block blockToCheck : blocks){
            if (!blockToCheck.inRange(mouseX, mouseY)) continue;
            if (blockToCheck.getDepth() < highestDepth) continue;
            highestDepth = blockToCheck.getDepth();
            candidate = blockToCheck;
        }
        return candidate;
    }
}
