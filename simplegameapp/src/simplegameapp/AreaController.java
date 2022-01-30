package simplegameapp;

import GameWorldApi.GameWorld;
import GameWorldApi.GameWorldType;

public class AreaController {
	private ButtonArea buttonArea = null;
    private GameWorld gameWorld;

    private int[] buttonBounds;
    private int[] gameWorldBounds;

    public AreaController(int width, int height, String gwPath) {
        buttonBounds = new int[] {0, 0 , width / 2, height};
        gameWorldBounds = new int[] {width / 2, 0, width / 2, height};

        try {
        	GameWorldType type = new GameLoader().loadGame(gwPath);
            gameWorld = type.createWorld(gameWorldBounds);
            buttonArea = new ButtonArea(buttonBounds, type.getActions());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return true if a point with a given x-coordinate and y-coordinate is located within the bounds of gameWorld 
     */
    public boolean inRangeWorld(double x, double y) {
        return x >= gameWorldBounds[0] && x <= gameWorldBounds[0] + gameWorldBounds[2] && y >= gameWorldBounds[1] && y <= gameWorldBounds[1] + gameWorldBounds[3];
    }
    
    public ButtonArea getButtonArea() {
    	return buttonArea;
    }
    public GameWorld getGameWorld() {
        return gameWorld;
    }
}
