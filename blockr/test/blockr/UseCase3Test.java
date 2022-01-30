package blockr;

import blockr.block.Block;
import blockr.connector.DownConnector;
import blockr.connector.UpConnector;
import org.junit.jupiter.api.Test;

import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UseCase3Test {
    // ------------------------------------
    // -- Use Case 3: Move Program Block --
    // ------------------------------------

    @Test
    void RemoveAndAddBlockDragged() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);
        int[] programAreaBounds = areaController.getProgramArea().getBounds();

        Block newBlock = areaController.getPalette().getBlocks().get(0);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, newBlock.getPosition()[0], newBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);

        // 1. The user drags a program block currently in the Program Area.
        // 2. The block gets removed as normal, as well as any blocks below within the same scope.
        // 3. The user stops dragging while still in the Program Area.
        // 4. The removed blocks now reappear at the location of the cursor.

        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, newBlock.getPosition()[0], newBlock.getPosition()[1]);
        assertEquals(areaController.getDraggedBlock().getClass(), newBlock.getClass());
        assertEquals(0, areaController.getProgramArea().getBlocks().size());
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, newBlock.getPosition()[0] + 10, newBlock.getPosition()[1] + 10);

        assertNull(areaController.getDraggedBlock());
        assertEquals(1, areaController.getProgramArea().getBlocks().size());
    }

    @Test
    void MoveBlock_Connect() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);
        int[] programAreaBounds = areaController.getProgramArea().getBounds();

        Block firstBlock = areaController.getPalette().getBlocks().get(0);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, firstBlock.getPosition()[0], firstBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);

        Block secondBlock = areaController.getPalette().getBlocks().get(0);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, secondBlock.getPosition()[0], secondBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 80, programAreaBounds[1] + 10);

        assertEquals(2, areaController.getProgramArea().getBlocks().size());

        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, firstBlock.getPosition()[0], firstBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, secondBlock.getPosition()[0], secondBlock.getPosition()[1] + secondBlock.getPosition()[3]);

        // 3a. When the user releases the mouse key, one of the top-level block’s connectors is near a compatible opposite connector of another block.
        // 1. The top-level block is connected at the matching connection point, the other moved blocks remain connected to this top-level block in the same way as before.

        assertEquals(areaController.getProgramArea().getBlocks().size(), 2);
        assertEquals(areaController.getProgramArea().getBlocks().get(0).getBlock(DownConnector.class), areaController.getProgramArea().getBlocks().get(1));
        assertEquals(areaController.getProgramArea().getBlocks().get(0), areaController.getProgramArea().getBlocks().get(1).getBlock(UpConnector.class));
    }

    @Test
    void MoveNothing() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);

        int[] programAreaBounds = areaController.getProgramArea().getBounds();
        int[] paletteBounds = areaController.getPalette().getBounds();

        assertEquals(0, areaController.getProgramArea().getBlocks().size());

        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);
        assertNull(areaController.getDraggedBlock());
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, paletteBounds[0] + 10, paletteBounds[1] + 10);

        assertEquals(0, areaController.getProgramArea().getBlocks().size());
    }

}
