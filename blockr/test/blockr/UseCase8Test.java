package blockr;

import blockr.block.Block;
import blockr.block.FunctionCallBlock;
import blockr.block.FunctionDefinitionBlock;
import blockr.block.WhileBlock;
import blockr.connector.BodyConnector;
import blockr.connector.UpConnector;

import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UseCase8Test {

    // -------------------------------------------------------------------
    // -- Use Case 8: Add Function Definition And Execute Function Call --
    // -------------------------------------------------------------------

    @Test
    void DragFunctionDefinitionBlock_CorrespondingCallAppears() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);
        int[] programAreaBounds = areaController.getProgramAreaBounds();

        // 1. The user drags and releases a Function Definition Block into the Program Area.
        // 2. The corresponding Function Call Block appears in the Palette.
        // 3. The user drags and releases the corresponding Function Call Block into the Program Area.

        //test block 1

        Block block1 = areaController.getPalette().getBlocks().get(7); //take Function Definition Block
        assertNotEquals(block1, null);
        assertEquals(block1.getClass(), FunctionDefinitionBlock.class);
        FunctionDefinitionBlock fdBlock1 = (FunctionDefinitionBlock) block1;

        //performs correctly dragged test fdBlock1
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, fdBlock1.getPosition()[0], fdBlock1.getPosition()[1]);
        assertEquals(areaController.getDraggedBlock(), fdBlock1);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);
        assertNull(areaController.getDraggedBlock());
        assertEquals(1, areaController.getProgramArea().getBlocks().size());
        assertEquals(fdBlock1.getClass(), areaController.getProgramArea().getBlocks().get(0).getClass());
        assertEquals(1, fdBlock1.getUniqueID());
        //check if corresponding function calls appeared.
        Block block4 = areaController.getPalette().getBlocks().get(8); //take Function Call Block 1
        assertNotEquals(block4, null);
        assertEquals(block4.getClass(), FunctionCallBlock.class);
        FunctionCallBlock fcBlock1 = (FunctionCallBlock) block4;
        assertEquals(fdBlock1.getUniqueID(), fcBlock1.getUniqueID());


        //test block 2

        Block block2 = areaController.getPalette().getBlocks().get(7); //take Function Definition Block
        assertNotEquals(block2, null);
        assertEquals(block2.getClass(), FunctionDefinitionBlock.class);
        FunctionDefinitionBlock fdBlock2 = (FunctionDefinitionBlock) block2;

        //performs correctly dragged test fdBlock2
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, fdBlock2.getPosition()[0], fdBlock2.getPosition()[1]);
        assertEquals(areaController.getDraggedBlock(), fdBlock2);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 30, programAreaBounds[1] + 30);
        assertNull(areaController.getDraggedBlock());
        assertEquals(2, areaController.getProgramArea().getBlocks().size());
        assertEquals(fdBlock2.getClass(), areaController.getProgramArea().getBlocks().get(1).getClass());
        assertEquals(2, fdBlock2.getUniqueID());
        //check if corresponding function calls appeared.
        Block block5 = areaController.getPalette().getBlocks().get(8); //take Function Call Block 2 (remember: we added function call blocks backwards)
        assertNotEquals(block5, null);
        assertEquals(block5.getClass(), FunctionCallBlock.class);
        FunctionCallBlock fcBlock2 = (FunctionCallBlock) block5;
        assertEquals(fdBlock2.getUniqueID(), fcBlock2.getUniqueID());

        //test block 3

        Block block3 = areaController.getPalette().getBlocks().get(7); //take Function Definition Block
        assertNotEquals(block3, null);
        assertEquals(block3.getClass(), FunctionDefinitionBlock.class);
        FunctionDefinitionBlock fdBlock3 = (FunctionDefinitionBlock) block3;
        //performs correctly dragged test block3
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, fdBlock3.getPosition()[0], fdBlock3.getPosition()[1]);
        assertEquals(areaController.getDraggedBlock(), fdBlock3);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 50, programAreaBounds[1] + 50);
        assertNull(areaController.getDraggedBlock());
        assertEquals(3, areaController.getProgramArea().getBlocks().size());
        assertEquals(fdBlock3.getClass(), areaController.getProgramArea().getBlocks().get(2).getClass());
        //check if corresponding function calls appeared.
        Block block6 = areaController.getPalette().getBlocks().get(8); //take Function Call Block 3 (remember: we added function call blocks backwards)
        assertNotEquals(block6, null);
        assertEquals(block6.getClass(), FunctionCallBlock.class);
        FunctionCallBlock fcBlock3 = (FunctionCallBlock) block6;
        assertEquals(fdBlock3.getUniqueID(), fcBlock3.getUniqueID());
    }

    @Test
    void ExecuteFunctionCall() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);
        int[] programAreaBounds = areaController.getProgramAreaBounds();

        // 4. The User presses F5 to execute the next step in the program. When the function call is to be executed, the blocks that are attached to the body of the Function
        // Definition Block are executed.

        Block functionDef = areaController.getPalette().getBlocks().get(7);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, functionDef.getPosition()[0], functionDef.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);
        
        Block functionCall = areaController.getPalette().getBlocks().get(8);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, functionCall.getPosition()[0], functionCall.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 110, programAreaBounds[1] + 10);
        
        Block firstBlock = areaController.getPalette().getBlocks().get(0);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, firstBlock.getConnector(UpConnector.class).getPosition()[0], firstBlock.getConnector(UpConnector.class).getPosition()[1]);  
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, functionDef.getConnector(BodyConnector.class).getPosition()[0], functionDef.getConnector(BodyConnector.class).getPosition()[1]);

        functionCall = areaController.getProgramArea().getBlocks().get(1);
        firstBlock = areaController.getProgramArea().getBlocks().get(2);
        
        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_F5,  'a', null);
        assertTrue(functionCall.isHighlighted());
        assertFalse(firstBlock.isHighlighted());
        
        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_F5,  'a', null);
        assertFalse(functionCall.isHighlighted());
        assertTrue(firstBlock.isHighlighted());
        
        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_F5,  'a', null);
        assertFalse(firstBlock.isHighlighted());
    }


}
