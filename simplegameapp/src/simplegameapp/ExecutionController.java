package simplegameapp;
import GameWorldApi.GameWorld;

import java.util.ArrayList;

public class ExecutionController {

    private GameWorld gameWorld;
    private GameWorld initialGameWorld;
    
    private ArrayList<String> actions;
    private int currentStep;

    public ExecutionController(GameWorld gw) {
    	actions = new ArrayList<>();
        gameWorld = gw;
        initialGameWorld = gameWorld.getSnapShot();
        currentStep = 0;
    }

    /**
     * This is used to execute an action in the gameworld
     * The action is then saved so it can be undone later
     * @param action the action the gameworld has to execute
     */
    public void execute(String action) {
    	gameWorld.execute(action);
        if (actions.size() > currentStep) actions.subList(currentStep, actions.size()).clear();
    	currentStep++;
    	actions.add(action);
    }

    /**
     * undoes the last executed action
     */
    public void undo() {
		if (currentStep <= 0) return;
		currentStep--;
		gameWorld.setSnapShot(initialGameWorld.getSnapShot());
		for (int i = 0; i < currentStep; i++) {
			gameWorld.execute(actions.get(i));
		}
	}
    
    /**
     * if the last action was undone, execute this action again
     */
    public void redo() {
    	if(currentStep < actions.size()) {
        	gameWorld.execute(actions.get(currentStep));
        	currentStep++;
    	}
    }
    
    /**
     * resets the gameworld to its initial state
     */
    public void reset() {
    	gameWorld.setSnapShot(initialGameWorld.getSnapShot());
    }

}
