package simplegameapp;

import java.awt.Rectangle;
import java.util.ArrayList;

public class ButtonArea {
	
	private ClickrButton[] buttons;
	private int[] bounds;

	public ButtonArea(int[] bounds, ArrayList<String> commands) {
		this.bounds = bounds;

		buttons = new ClickrButton[commands.size()];
		int x = bounds[2] / 6;
		int y = 25;
		int width = 2 * bounds[2] / 3;
		int height = 50;
		int i = 0;
		for (String string : commands) {
			Rectangle rectangle;
			rectangle = new Rectangle(x,y,width,height);
			ClickrButton clickrButton = new ClickrButton(rectangle, string);
			buttons[i] = clickrButton;
			i++;
			y += 75;
		}
	}
	
	/**
	 * @return a list of every button in the buttonArea
	 */
	public ClickrButton[] getButtons() {
		return buttons;
	}
	
	/**
	 * @return an array of integers which depicts the bounds of the buttonArea
	 */
	public int[] getBounds() {
		return bounds;
	} 
}

