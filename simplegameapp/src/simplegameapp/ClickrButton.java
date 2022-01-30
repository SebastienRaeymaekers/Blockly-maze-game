package simplegameapp;

import java.awt.Rectangle;

public class ClickrButton {
	private Rectangle shape;
	private String command;
	
	public ClickrButton(Rectangle rectangle, String string) {
		shape = rectangle;
		command = string;
	}
	
	/**
	 * @return the rectangle that represents the button in the application
	 */
	public Rectangle getShape() {
		return shape;
	}
	
	/**
	 * @return the command tied to the button
	 */
	public String getCommand() {
		return command;
	}
}
