package blockr;

import blockr.block.Block;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

public class UseCase4Test {
    // ---------------------------------------------------------
    // -- Use Case 4: Player Executes One Step Of The Program --
    // ---------------------------------------------------------

    @Test
    void ExecuteOneStep_2Groups() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);
        int[] programAreaBounds = areaController.getProgramArea().getBounds();

        Block firstBlock = areaController.getPalette().getBlocks().get(0);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, firstBlock.getPosition()[0], firstBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);

        Block secondBlock = areaController.getPalette().getBlocks().get(2);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, secondBlock.getPosition()[0], secondBlock.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, firstBlock.getPosition()[1] + firstBlock.getPosition()[3] + 10);


        // 1. The user presses on the F5 key when there is not exactly one group of connected blocks
        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_F5,  'a', null);
        // 2. Nothing happens.
        assertFalse(firstBlock.isHighlighted());
        assertFalse(secondBlock.isHighlighted());

    }
}
