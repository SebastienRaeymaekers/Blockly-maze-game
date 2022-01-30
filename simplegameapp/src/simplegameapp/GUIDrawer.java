package simplegameapp;

import java.awt.*;

// Responsible for drawing all objects stored in the backend.
public class GUIDrawer {

    private static final Color clickrColor = new Color(50, 50, 50);
    
    private static final Color clickrArrowColor = new Color(250, 250, 250);

    private static final float defaultFontSize = 12;
    private static final float clickrFontSize = 40;

    public static void paintGame(Graphics g, AreaController areaController) {paint(g, areaController);}

    private static void paint(Graphics g, AreaController areaController) {
        setFontSize(g, defaultFontSize);
        areaController.getGameWorld().paint(g);

        setFontSize(g, clickrFontSize);
        paint(g, areaController.getButtonArea());
    }
    
    private static void paint(Graphics g, ButtonArea buttonArea) {
        g.setColor(clickrColor);
        g.fillRect(buttonArea.getBounds()[0], buttonArea.getBounds()[1], buttonArea.getBounds()[2], buttonArea.getBounds()[3]);
        if(buttonArea.getButtons() != null) {
            for (ClickrButton button : buttonArea.getButtons()) {
                g.setColor(clickrArrowColor);
    			g.fillRect(button.getShape().x,button.getShape().y,button.getShape().width,button.getShape().height);
    	        g.setColor(clickrColor);
    			g.drawString(button.getCommand(), button.getShape().x, button.getShape().y+45);
    		}        	
        }
    }

    private static void setFontSize(Graphics g, float fontSize) {
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(fontSize);
        g.setFont(newFont);
    }
}
