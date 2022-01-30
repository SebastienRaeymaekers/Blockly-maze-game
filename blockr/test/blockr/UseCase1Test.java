package blockr;

import blockr.block.Block;
import blockr.connector.DownConnector;
import blockr.connector.UpConnector;
import org.junit.jupiter.api.Test;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UseCase1Test {
    // -----------------------------------
    // -- Use Case 1: Add Program Block --
    // -----------------------------------

    @Test
    void DragBlock() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);

        Block block = areaController.getPalette().getBlocks().get(0);
        int[] programAreaBounds = areaController.getProgramArea().getBounds();
        assertNotEquals(block, null);

        // 1  The user moves the mouse cursor over a block in the Palette,
        // then presses the left mouse key, then moves the mouse cursor to the Program Area,
        // and then releases the left mouse key.
        // 2. The system adds a new block of the same type to the ProgramArea.

        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, block.getPosition()[0], block.getPosition()[1]);
        assertEquals(areaController.getDraggedBlock(), block);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);
        assertNull(areaController.getDraggedBlock());
        assertEquals(1, areaController.getProgramArea().getBlocks().size());
        assertEquals(block.getClass(), areaController.getProgramArea().getBlocks().get(0).getClass());
    }

    @Test
    void DragBlock_connect() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);

        Block firstBlock = areaController.getPalette().getBlocks().get(0);
        int[] programAreaBounds = areaController.getProgramArea().getBounds();

        // 1  The user moves the mouse cursor over a block in the Palette,
        // then presses the left mouse key, then moves the mouse cursor to the Program Area,
        // and then releases the left mouse key.
        // 2. The system adds a new block of the same type to the ProgramArea.

        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, firstBlock.getPosition()[0], firstBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);

        // 1a. When the user releases the mouse key,
        // one of the blocks connectors is near a compatible opposite connector of another block.
        // The system adds a new block of the same type to the ProgramArea;
        // the new block is inserted into an existing group of connected blocks at the matching connection point.

        Block secondBlock = areaController.getPalette().getBlocks().get(1);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, secondBlock.getPosition()[0], secondBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, firstBlock.getPosition()[0], firstBlock.getPosition()[1] + firstBlock.getPosition()[3]);

        assertEquals(areaController.getProgramArea().getBlocks().size(), 2);
        ArrayList<Block> currentBlocks = areaController.getProgramArea().getBlocks();
        assertEquals(currentBlocks.get(1).getClass(), currentBlocks.get(0).getBlock(DownConnector.class).getClass());
        assertEquals(currentBlocks.get(0).getClass(), currentBlocks.get(1).getBlock(UpConnector.class).getClass());
    }

    @Test
    void DragBlock_DontConnect() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);
        int[] programAreaBounds = areaController.getProgramArea().getBounds();

        // 1  The user moves the mouse cursor over a block in the Palette,
        // then presses the left mouse key, then moves the mouse cursor to the Program Area,
        // and then releases the left mouse key.
        // 2. The system adds a new block of the same type to the ProgramArea.

        Block firstBlock = areaController.getPalette().getBlocks().get(0);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, firstBlock.getPosition()[0], firstBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);

        // 1a. When the user releases the mouse key,
        // one of the blocks connectors is near a compatible opposite connector of another block.
        // The system adds a new block of the same type to the ProgramArea;
        // the new block is inserted into an existing group of connected blocks at the matching connection point.

        Block secondBlock = areaController.getPalette().getBlocks().get(0);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, secondBlock.getPosition()[0], secondBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);

        assertEquals(areaController.getProgramArea().getBlocks().size(), 2);
        assertNull(secondBlock.getBlock(UpConnector.class));
        assertNull(firstBlock.getBlock(DownConnector.class));
    }

    @Test
    void DragBlock_MaxBlocks() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);
        Block currentBlock;
        int[] programAreaBounds = areaController.getProgramArea().getBounds();
        int maxBlocks = areaController.getAvailableBlocks();

        // 1  The user moves the mouse cursor over a block in the Palette,
        // then presses the left mouse key, then moves the mouse cursor to the Program Area,
        // and then releases the left mouse key.
        // 2. The system adds a new block of the same type to the ProgramArea.

        for (int i = 0; i < maxBlocks; i++) {
            currentBlock = areaController.getPalette().getBlocks().get(0);
            eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, currentBlock.getPosition()[0], currentBlock.getPosition()[1]);
            eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);
        }

        // 1b. When the user releases the mouse key,
        // causing the maximum number of blocks to be reached.
        // The system adds a new block of the same type to the ProgramArea;
        // all the blocks of the palette are removed.

        assertEquals(0, areaController.getPalette().getBlocks().size());
    }

}
