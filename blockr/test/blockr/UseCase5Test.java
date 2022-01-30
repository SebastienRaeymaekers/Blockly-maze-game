package blockr;

import blockr.block.Block;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

public class UseCase5Test {
    // -------------------------------------------------------
    // -- Use Case 5: Player Stops Execution Of The Program --
    // -------------------------------------------------------

    @Test
    void StopExecution(){
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);
        int[] programAreaBounds = areaController.getProgramArea().getBounds();

        Block firstBlock = areaController.getPalette().getBlocks().get(0);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, firstBlock.getPosition()[0], firstBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);

        Block secondBlock = areaController.getPalette().getBlocks().get(2);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, secondBlock.getPosition()[0], secondBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, firstBlock.getPosition()[1] + firstBlock.getPosition()[3] + 10);

        // 1. The user presses on the F5 key
        // 2. The user repeats step 1 until he wants to stop, while program execution has not finished yet.
        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_F5, 'a', null);
        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_F5, 'a', null);
        assertTrue(eventHandler.getExecutionController().isExecuting());
        // 3. The user presses the Escape key
        // 4. The program stops execution, and resets the grid.
        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_ESCAPE, 'a', null);
        assertFalse(secondBlock.isHighlighted());
        assertFalse(eventHandler.getExecutionController().isExecuting());

    }
}
