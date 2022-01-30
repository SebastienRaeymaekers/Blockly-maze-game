package blockr;

import blockr.block.Block;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class TestHelper {

    public static AreaController createAreaController() {return new AreaController(1200, 900, "robotgame");}

    // Returns an eventHandler which contains an entire program,
    // with the programArea containing a move block connected with a turn right block.
    public static EventHandler getSampleProgram() {
        AreaController areaController = TestHelper.createAreaController();
        EventHandler eventHandler = new EventHandler(areaController);
        int[] programAreaBounds = areaController.getProgramArea().getBounds();

        Block block = areaController.getPalette().getBlocks().get(0);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, block.getPosition()[0], block.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10);

        Block block2 = areaController.getPalette().getBlocks().get(1);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, block2.getPosition()[0], block2.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, programAreaBounds[0] + 10, programAreaBounds[1] + 10 + block.getPosition()[3]);

        return eventHandler;
    }

    // We simulate a ctrl-z keypress, which is tricky as we have to pass on the ctrl modifier.
    public static KeyEvent getUndoKeyEvent() {
        return new KeyEvent(new Button(""), KeyEvent.KEY_PRESSED, 20, InputEvent.CTRL_MASK, KeyEvent.VK_Z, 'z');
    }

    // We simulate a ctrl-shift-z keypress, which is tricky as we have to pass on the ctrl and shift modifiers.
    public static KeyEvent getRedoKeyEvent() {
        return new KeyEvent(new Button(""), KeyEvent.KEY_PRESSED, 20, InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK, KeyEvent.VK_Z, 'z');
    }
}
