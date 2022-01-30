package blockr;

import blockr.block.Block;
import blockr.connector.UpConnector;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

public class UseCase7Test {
    // -----------------------------------
    // -- Use Case 7: Undo and redo --
    // -----------------------------------

    @Test
    void undoExecuteAction() {
        EventHandler eventHandler = TestHelper.getSampleProgram();
        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_F5, 'a', null);
        assertTrue(eventHandler.getAreaController().getProgramArea().getBlocks().get(0).isHighlighted());

        eventHandler.handleKeyEvent(TestHelper.getUndoKeyEvent());
        assertFalse(eventHandler.getAreaController().getProgramArea().getBlocks().get(0).isHighlighted());
    }

    @Test
    void redoExecuteAction() {
        EventHandler eventHandler = TestHelper.getSampleProgram();
        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_F5, 'a', null);
        assertTrue(eventHandler.getAreaController().getProgramArea().getBlocks().get(0).isHighlighted());

        eventHandler.handleKeyEvent(TestHelper.getUndoKeyEvent());
        assertFalse(eventHandler.getAreaController().getProgramArea().getBlocks().get(0).isHighlighted());

        eventHandler.handleKeyEvent(TestHelper.getRedoKeyEvent());
        assertTrue(eventHandler.getAreaController().getProgramArea().getBlocks().get(0).isHighlighted());
    }

    @Test
    void undoBlockMoveAction() {
        EventHandler eventHandler = TestHelper.getSampleProgram();
        Block block = eventHandler.getAreaController().getProgramArea().getBlocks().get(1);
        assertEquals(block.getConnector(UpConnector.class).getConnectedTo().getBlock(), eventHandler.getAreaController().getProgramArea().getBlocks().get(0));
        int[] initialPos = block.getPosition().clone();

        // Disconnect the turn block from the move block
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, block.getPosition()[0], block.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, block.getPosition()[0], block.getPosition()[1] + 50);
        assertNull(block.getConnector(UpConnector.class).getConnectedTo());

        // Undo the action
        eventHandler.handleKeyEvent(TestHelper.getUndoKeyEvent());
        block = eventHandler.getAreaController().getProgramArea().getBlocks().get(1);
        assertEquals(block.getConnector(UpConnector.class).getConnectedTo().getBlock(), eventHandler.getAreaController().getProgramArea().getBlocks().get(0));
        assertArrayEquals(initialPos, block.getPosition());
    }

    @Test
    void redoBlockMoveAction() {
        EventHandler eventHandler = TestHelper.getSampleProgram();
        Block block = eventHandler.getAreaController().getProgramArea().getBlocks().get(1);
        int[] initialPos = block.getPosition().clone();

        // Disconnect the turn block from the move block
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, block.getPosition()[0], block.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, block.getPosition()[0], block.getPosition()[1] + 50);
        int[] newPos = eventHandler.getAreaController().getProgramArea().getBlocks().get(1).getPosition().clone();

        // Undo and redo the action
        eventHandler.handleKeyEvent(TestHelper.getUndoKeyEvent());
        assertArrayEquals(initialPos, eventHandler.getAreaController().getProgramArea().getBlocks().get(1).getPosition());
        eventHandler.handleKeyEvent(TestHelper.getRedoKeyEvent());
        assertArrayEquals(newPos, eventHandler.getAreaController().getProgramArea().getBlocks().get(1).getPosition());
    }

    @Test
    void undoRedoMultiple() {
        EventHandler eventHandler = TestHelper.getSampleProgram();
        ProgramArea programArea = eventHandler.getAreaController().getProgramArea();
        Block block = programArea.getBlocks().get(0);
        int[] initialPos = block.getPosition().clone();

        // Move both blocks a bit
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, block.getPosition()[0], block.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, block.getPosition()[0], block.getPosition()[1] + 20);
        int[] secondPos = programArea.getBlocks().get(0).getPosition().clone();

        // Execute two steps
        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_F5, 'a', null);
        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_F5, 'a', null);
        assertTrue(programArea.getBlocks().get(1).isHighlighted());

        // Undo last execute step
        eventHandler.handleKeyEvent(TestHelper.getUndoKeyEvent());
        assertTrue(programArea.getBlocks().get(0).isHighlighted());
        assertFalse(programArea.getBlocks().get(1).isHighlighted());

        // Undo first execute step
        eventHandler.handleKeyEvent(TestHelper.getUndoKeyEvent());
        assertArrayEquals(secondPos, programArea.getBlocks().get(0).getPosition());

        // Undo first move step
        eventHandler.handleKeyEvent(TestHelper.getUndoKeyEvent());
        assertArrayEquals(initialPos, programArea.getBlocks().get(0).getPosition());
    }

    @Test
    void actionClearsUndoHistory() {
        EventHandler eventHandler = TestHelper.getSampleProgram();
        ProgramArea programArea = eventHandler.getAreaController().getProgramArea();

        // Execute a step
        eventHandler.handleKeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.VK_F5, 'a', null);
        assertTrue(programArea.getBlocks().get(0).isHighlighted());

        // Undo last execute step
        eventHandler.handleKeyEvent(TestHelper.getUndoKeyEvent());
        assertFalse(programArea.getBlocks().get(0).isHighlighted());

        // Clear history by doing another action, here we move both blocks
        Block block = programArea.getBlocks().get(0);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, block.getPosition()[0], block.getPosition()[1]);
        eventHandler.handleMouseEvent(MouseEvent.MOUSE_RELEASED, block.getPosition()[0], block.getPosition()[1] + 20);

        // Now we have moved the blocks, a redo action should not be possible
        eventHandler.handleKeyEvent(TestHelper.getRedoKeyEvent());
        assertFalse(programArea.getBlocks().get(0).isHighlighted());
    }
}
