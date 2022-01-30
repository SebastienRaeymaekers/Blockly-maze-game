package simplegameapp;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MyCanvasWindow extends CanvasWindow {

    private EventHandler eventHandler;

    /**
     * Initializes a CanvasWindow object.
     *
     * @param title Window title
     * @param gameWorldPath the name of the GameWorld jar, in the same directory as the project jar or in the project files, that should be loaded in.
     *                      If no jar is found, it shall look for a package name inside the project instead
     */
    protected MyCanvasWindow(String title, String gameWorldPath) {
        super(title);
        eventHandler = new EventHandler(new AreaController(width, height, gameWorldPath));
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new MyCanvasWindow("Clickr", args[0]).show());
    }

    @Override
    public void paint(Graphics g) {
        ((Graphics2D) g).scale(widthScale, heightScale); // scale with the window
        GUIDrawer.paintGame(g, eventHandler.getAreaController());
    }

    /**
     * Called when the user presses (id == MouseEvent.MOUSE_PRESSED), releases (id == MouseEvent.MOUSE_RELEASED), or drags (id == MouseEvent.MOUSE_DRAGGED) the mouse.
     */
    @Override
    protected void handleMouseEvent(int id, int xNotScaled, int yNotScaled, int clickCount) {
        int x = (int) (xNotScaled / widthScale);
        int y = (int) (yNotScaled / heightScale);
        eventHandler.handleMouseEvent(id, x, y);
        super.repaint();
    }

    /**
     * Called when the user presses a key (id == KeyEvent.KEY_PRESSED) or enters a character (id == KeyEvent.KEY_TYPED).
     */
    @Override
    protected void handleKeyEvent(int id, int keyCode, char keyChar, KeyEvent e) {
        eventHandler.handleKeyEvent(id, keyCode, keyChar, e);
        super.repaint();
    }
}
