package robotgame;

import GameWorldApi.GameWorldType;

import java.util.ArrayList;

public class Type implements GameWorldType {

    /**
     * Returns all the actions corresponding to the robotworld.
     * @return An array of the actions available in strings.
     */
    public ArrayList<String> getActions() {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("Move Forward");
        actions.add("Move Left");
        actions.add("Move Right");
        return actions;
    }

    /**
     * Returns all the predicates corresponding to the robotworld.
     * @return An array of the predicates in strings.
     */
    public ArrayList<String> getPredicates() {
        ArrayList<String> predicates = new ArrayList<>();
        predicates.add("WallInFront");
        return predicates;
    }

    /**
     * Creates a new GameWorld object with the given bounds and returns it.
     * @param bounds An array of 4 elements representing the following values: [x (top left), y (top left), width, height]
     * @return a new instance of a GameWorld with the given bounds.
     */
    public World createWorld(int[] bounds) { //return null;
        return new World(bounds);
    }
}
