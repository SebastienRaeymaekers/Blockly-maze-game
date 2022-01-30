package blockr;


import GameWorldApi.GameWorld;
import blockr.block.Block;
import blockr.block.FunctionDefinitionBlock;

public class AreaController {
    private Palette palette;
    private ProgramArea prgArea;
    private GameWorld gameWorld;

    private int[] paletteBounds;
    private int[] prgAreaBounds;
    private int[] gameWorldBounds;

    private Block draggedBlock;
    private int availableBlocks;

    public AreaController(int width, int height, String gwPath) {
        setPaletteBounds(new int[]{0, 0, width / 3, height});
        setProgramAreaBounds(new int[]{width / 3, 0, width / 3, height});
        setGameWorldBounds(new int[]{2 * width / 3, 0, width / 3, height});

        palette = new Palette(getPaletteBounds());
        prgArea = new ProgramArea(getProgramAreaBounds());

        try {
            gameWorld = new GameLoader().loadGame(gwPath, getGameWorldBounds());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        availableBlocks = 30;
    }

    /**
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return true if a point with a given x-coordinate and y-coordinate is located within the bounds of gameWorld
     */
    public boolean inRangeWorld(double x, double y) {
        return x >= getGameWorldBounds()[0] && x <= getGameWorldBounds()[0] + getGameWorldBounds()[2] && y >= getGameWorldBounds()[1] && y <= getGameWorldBounds()[1] + getGameWorldBounds()[3];
    }

    public int[] getPaletteBounds() {
        return paletteBounds;
    }

    public int[] getProgramAreaBounds() {
        return prgAreaBounds;
    }

    public int[] getGameWorldBounds() {
        return gameWorldBounds;
    }

    public void setPaletteBounds(int[] bounds) {
        paletteBounds = bounds;
    }

    public void setProgramAreaBounds(int[] bounds) {
        prgAreaBounds = bounds;
    }

    public void setGameWorldBounds(int[] bounds) {
        gameWorldBounds = bounds;
    }

    public ProgramArea getProgramArea() {
        return prgArea;
    }

    public Palette getPalette() {
        return palette;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public Block getDraggedBlock() {
        return draggedBlock;
    }

    public void setDraggedBlock(Block draggedBlock) {
        this.draggedBlock = draggedBlock;
    }

    /**
     * @return the amount of blocks the user can add to its program before reaching the maximum amount of blocks allowed.
     */
    public int getAvailableBlocks() {
        return availableBlocks;
    }

    public void setAvailableBlocks(int availableBlocks) {
        this.availableBlocks = availableBlocks;
        if (this.availableBlocks > 0) {
            palette.refreshBlocks();
        } else palette.clearBlocks();
    }

    public GameWorld loadGame() {
        try {
            getGameWorld().loadGame();
            setAvailableBlocks(getGameWorld().getMaxBlocks());
            getProgramArea().resetProgramArea();
            return getGameWorld().getSnapShot();
        } catch (Exception e) {
            System.out.println("Error occurred while loading new world:");
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void startDraggingBlocks(int x, int y) {
        if (getProgramArea().inRange(x, y)) {
            setDraggedBlock(Area.findClickedBlock(x, y, getProgramArea().blocks));
            if (getDraggedBlock() != null) getProgramArea().startDraggingBlock(getDraggedBlock());
        } else if (getPalette().inRange(x, y)) {
            setDraggedBlock(Area.findClickedBlock(x, y, getPalette().blocks));
            if (getDraggedBlock() != null) setAvailableBlocks(getAvailableBlocks() - 1);
        }
    }

    public void updateDraggedBlocks(int x, int y) {
        if (getDraggedBlock() == null) return;
        getDraggedBlock().setCoords(x, y);
        getProgramArea().setConnectorHighlight(getDraggedBlock());
    }

    public void releaseBlocks(int x, int y, int[] mouseDragOffset) {
        if (getDraggedBlock() == null) return;
        getDraggedBlock().setCoords(x + mouseDragOffset[0], y + mouseDragOffset[1]);
        if (getProgramArea().inRange(x, y)) {
            getProgramArea().release(getDraggedBlock());
            if (getDraggedBlock().getClass() == FunctionDefinitionBlock.class)
                addFunctionBlock(((FunctionDefinitionBlock) getDraggedBlock()));

        } else {
            setAvailableBlocks(getAvailableBlocks() + getDraggedBlock().getBlocksInScope().size());
            if (getDraggedBlock().getClass() == FunctionDefinitionBlock.class)
                removeFunctionBlock((FunctionDefinitionBlock) getDraggedBlock());
        }
        setDraggedBlock(null);
    }

    private void removeFunctionBlock(FunctionDefinitionBlock functionDefBlock) {
        int toRemoveID = functionDefBlock.getUniqueID();
        if (toRemoveID == getPalette().getNextFunctionID()) return; //def blocks not in the programarea should not be removed
        int amountRemoved = getProgramArea().removeFunctionCallBlocks(toRemoveID); //remove all calls with ID
        getProgramArea().decrementHigherIDs(toRemoveID); //all functionblocks with a higher id than toRemoveID decrement their id
        getPalette().decrementNextFunctionID();
        setAvailableBlocks(getAvailableBlocks() + amountRemoved);
    }

    private void addFunctionBlock(FunctionDefinitionBlock functionDefBlock) {
        if (functionDefBlock.getUniqueID() < getPalette().getNextFunctionID()) return;
        getPalette().incrementNextFunctionID();
    }

}
