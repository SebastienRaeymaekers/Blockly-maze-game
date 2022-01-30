package blockr;

import blockr.block.Block;
import blockr.block.FunctionCallBlock;
import blockr.block.FunctionDefinitionBlock;
import blockr.block.WhileBlock;
import org.junit.jupiter.api.Test;

import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UseCase9Test {

    // ----------------------------------------------------------------------------
    // -- Use Case 9: Remove Function Definition And Corresponding Function Call --
    // ----------------------------------------------------------------------------

    @Test
    void removeFunctionDefinitionBlock_CorrespondingCallsDisappears() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);
        int[] programAreaBounds = areaController.getProgramAreaBounds();
        int[] paletteAreaBounds = areaController.getPaletteBounds();


        // 1. The user drags and releases a Function Definition Block into the Palette.
        // 2. The corresponding function call blocks disappear from the Program Area.

        //drag a function def block
        Block block1 = areaController.getPalette().getBlocks().get(7); //take Function Definition Block
        assertNotEquals(block1, null);
        assertEquals(block1.getClass(), FunctionDefinitionBlock.class);
        FunctionDefinitionBlock fdBlock1 = (FunctionDefinitionBlock) block1;
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, fdBlock1.getPosition()[0], fdBlock1.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);
        assertEquals(fdBlock1.getClass(), areaController.getProgramArea().getBlocks().get(0).getClass());


        //drag the function call
        Block block2 = areaController.getPalette().getBlocks().get(8); //take Function Call Block 1
        assertNotEquals(block2, null);
        assertEquals(block2.getClass(), FunctionCallBlock.class);
        FunctionCallBlock fcBlock1 = (FunctionCallBlock) block2;
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, fcBlock1.getPosition()[0], fcBlock1.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 20, programAreaBounds[1] + 20);


        //drag a while block
        Block block3 = areaController.getPalette().getBlocks().get(3); //take While Block
        assertNotEquals(block3, null);
        assertEquals(block3.getClass(), WhileBlock.class);
        WhileBlock whileBlock = (WhileBlock) block3;
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, whileBlock.getPosition()[0], whileBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 30, programAreaBounds[1] + 30);

        //attach another function call to it
        Block block4 = areaController.getPalette().getBlocks().get(8); //take Function Call Block 1
        assertNotEquals(block4, null);
        assertEquals(block4.getClass(), FunctionCallBlock.class);
        FunctionCallBlock fcBlock2 = (FunctionCallBlock) block4;
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, fcBlock2.getPosition()[0], fcBlock2.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED,  whileBlock.getPosition()[0], whileBlock.getPosition()[1] + whileBlock.getPosition()[3]);


        // remove the function def block
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, fdBlock1.getPosition()[0], fdBlock1.getPosition()[1]);
        assertEquals(areaController.getDraggedBlock().getClass(), fdBlock1.getClass());
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, paletteAreaBounds[0] + 10, paletteAreaBounds[1] + 10);
        assertNull(areaController.getDraggedBlock());

        // check if every function call was correctly removed.
        assertEquals(1, areaController.getProgramArea().getBlocks().size());
        assertEquals(WhileBlock.class, areaController.getProgramArea().blocks.get(0).getClass());
        assertFalse(areaController.getProgramArea().blocks.contains(fcBlock1));
        assertFalse(areaController.getProgramArea().blocks.contains(fcBlock2));

        // 1. The user drags and releases a Function Definition Block into the Palette.
        // 2. No corresponding Function Call Blocks existed, so no Function Call Blocks are removed.

        //drag a function def block
        Block block5 = areaController.getPalette().getBlocks().get(7); //take Function Definition Block
        assertNotEquals(block5, null);
        assertEquals(block5.getClass(), FunctionDefinitionBlock.class);
        FunctionDefinitionBlock fdBlock5 = (FunctionDefinitionBlock) block5;
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, fdBlock5.getPosition()[0], fdBlock5.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 40, programAreaBounds[1] + 40);
        assertEquals(fdBlock5.getClass(), areaController.getProgramArea().getBlocks().get(1).getClass());

        // remove the function def block
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, fdBlock5.getPosition()[0], fdBlock5.getPosition()[1]);
        assertEquals(areaController.getDraggedBlock().getClass(), fdBlock5.getClass());
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, paletteAreaBounds[0] + 40, paletteAreaBounds[1] + 40);
        assertNull(areaController.getDraggedBlock());

        // check if no function call was removed.
        assertEquals(1, areaController.getProgramArea().getBlocks().size());
        assertEquals(WhileBlock.class, areaController.getProgramArea().blocks.get(0).getClass());
        assertFalse(areaController.getProgramArea().blocks.contains(fcBlock1));
        assertFalse(areaController.getProgramArea().blocks.contains(fcBlock2));

    }

}
