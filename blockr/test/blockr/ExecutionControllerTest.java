package blockr;

import org.junit.jupiter.api.Test;

import blockr.block.Block;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionControllerTest {

    @Test
    void isExecuting() {
        AreaController areaController = createAreaController();
		EventHandler eventHandler = new EventHandler(areaController);
        ExecutionController ec = eventHandler.getExecutionController();

		int[] programAreaBounds = areaController.getProgramArea().getBounds();

		Block block = areaController.getPalette().getBlocks().get(0);
		eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, block.getPosition()[0], block.getPosition()[1]);
		eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);

        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_F5, (char) 116, null);

        assertTrue(ec.isExecuting());
    }

    @Test
    void stopExecuting() {
        AreaController areaController = createAreaController();
		EventHandler eventHandler = new EventHandler(areaController);
        ExecutionController ec = eventHandler.getExecutionController();

		int[] programAreaBounds = areaController.getProgramArea().getBounds();

		Block block = areaController.getPalette().getBlocks().get(0);
		eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, block.getPosition()[0], block.getPosition()[1]);
		eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);

        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_F5, (char) 116, null);
        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_ESCAPE, (char) 27, null   );

        assertFalse(ec.isExecuting());
    }

    public AreaController createAreaController(){return new AreaController(1200, 300, "robotgame");}

}