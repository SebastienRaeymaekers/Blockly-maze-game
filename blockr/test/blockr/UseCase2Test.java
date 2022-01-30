package blockr;

import blockr.block.Block;
import org.junit.jupiter.api.Test;

import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UseCase2Test {
    // --------------------------------------
    // -- Use Case 2: Remove Program Block --
    // --------------------------------------

    @Test
    void RemoveByDraggingBlockToPalette() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);
        int[] programAreaBounds = areaController.getProgramArea().getBounds();
        int[] paletteBounds = areaController.getPalette().getBounds();

        Block firstBlock = areaController.getPalette().getBlocks().get(0);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, firstBlock.getPosition()[0], firstBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);

        Block secondBlock = areaController.getPalette().getBlocks().get(0);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, secondBlock.getPosition()[0], secondBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10 + firstBlock.getPosition()[3]);

        assertEquals(2, areaController.getProgramArea().getBlocks().size());

        // 1. The user moves the mouse cursor over a block in the ProgramArea, then presses the left mouse key, then moves the mouse cursor to the Palette, and then releases the left mouse key.
        // 2. The system removes the selected block, as well as any blocks below within the same scope.

        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, firstBlock.getPosition()[0], firstBlock.getPosition()[1]);
        assertEquals(areaController.getDraggedBlock().getClass(), firstBlock.getClass());
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, paletteBounds[0] + 10, paletteBounds[1] + 10);
        assertNull(areaController.getDraggedBlock());
        assertEquals(0, areaController.getProgramArea().getBlocks().size());
    }

    @Test
    void RemoveByDraggingBlockToPalette_MaxBlocks() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);
        int[] programAreaBounds = areaController.getProgramArea().getBounds();
        int[] paletteBounds = areaController.getPalette().getBounds();
        int maxBlocks = areaController.getAvailableBlocks();

        for (int i = 0; i < maxBlocks; i++) {
            Block newBlock = areaController.getPalette().getBlocks().get(0);
            eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, newBlock.getPosition()[0], newBlock.getPosition()[1]);
            eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);
        }
        assertEquals(maxBlocks, areaController.getProgramArea().getBlocks().size());
        assertEquals(0, areaController.getPalette().getBlocks().size());

        Block someBlock = areaController.getProgramArea().getBlocks().get(0);

        // 1a. The user releases the mouse key, while the maximum number of blocks has already been reached.
        // 1. The system removes the selected block; all blocks reappear again in the Palette.

        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, someBlock.getPosition()[0], someBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, paletteBounds[0] + 10, paletteBounds[1] + 10);

        assertNull(areaController.getDraggedBlock());
        assertNotEquals(0, areaController.getPalette().getBlocks().size());
    }

    @Test
    void DragNothingToPalette() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);
        int[] programAreaBounds = areaController.getProgramArea().getBounds();
        int[] paletteBounds = areaController.getPalette().getBounds();
        int maxBlocks = areaController.getAvailableBlocks();

        assertEquals(0, areaController.getProgramArea().getBlocks().size());

        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);
        assertNull(areaController.getDraggedBlock());
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, paletteBounds[0] + 10, paletteBounds[1] + 10);

        assertEquals(maxBlocks, areaController.getAvailableBlocks());
    }

}
