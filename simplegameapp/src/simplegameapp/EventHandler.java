package simplegameapp;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class EventHandler {

    private AreaController areaController;
    private ExecutionController executionController;

    public EventHandler(AreaController areaController) {
        this.areaController = areaController;
        this.executionController = new ExecutionController(areaController.getGameWorld());
    }

    public AreaController getAreaController() {
        return areaController;
    }

    /**
     * Executes a case depending on what mouse action {@code id} was performed and on what area the mouse is positioned.
     *
     * @param id The received type of mouse event. We distinguish 3 types: 1) mouse click 2) mouse drag 3) mouse release
     * @param x  The x coordinate of the mouse
     * @param y  the y coordinate of the mouse
     */
    public void handleMouseEvent(int id, int x, int y) {
        if (id == MouseEvent.MOUSE_PRESSED) {
            handleMousePressed(x, y);
        }
    }

    /**
     * Handles a mouse press. If the position of the cursor was inside the game world area upon clicking, we open
     * the interface for loading in a game, and load the game in once it was chosen.
     */
    private void handleMousePressed(int x, int y) {
    	Point point = new Point(x,y);
		for (ClickrButton clickrButton : areaController.getButtonArea().getButtons()) {
			if(clickrButton.getShape().contains(point))
				executionController.execute(clickrButton.getCommand());
		}
        if (!areaController.inRangeWorld(x, y)) return;
        try {
            areaController.getGameWorld().loadGame();
        } catch (Exception e) {
            System.out.println("Error occurred while loading new world:");
            System.out.println(e.getMessage());
        }

    }


    /**
     * @param id The received type of key event. Only key presses(id == KeyEvent.KEY_PRESSED) are accepted.
     * @param keyCode The code of the key being pressed
     * @param keyChar The character of the key currently being typed
     * @param e The entire keyEvent, which can be used to detect whether control or shift is held currently.
     */
    public void handleKeyEvent(int id, int keyCode, char keyChar, KeyEvent e) {
        if (id != KeyEvent.KEY_PRESSED) return;

        //Essential key events
        if (keyCode == KeyEvent.VK_ESCAPE) executionController.reset();
        if (keyCode == KeyEvent.VK_Z && e.isControlDown() && e.isShiftDown()) executionController.redo();
        if (keyCode == KeyEvent.VK_Z && e.isControlDown() && !e.isShiftDown()) executionController.undo();
    }
}