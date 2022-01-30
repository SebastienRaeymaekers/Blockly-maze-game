package mygame;

import GameWorldApi.GameWorldType;

import java.util.ArrayList;

public class Type implements GameWorldType {
    public ArrayList<String> getActions() {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("Move Up");
        actions.add("Move Left");
        actions.add("Move Right");
        actions.add("Move Down");
        actions.add("Reveal");
        actions.add("Put Flag");
        actions.add("Restart");
        return actions;
    }

    public ArrayList<String> getPredicates() {
        ArrayList<String> predicates = new ArrayList<>();
        predicates.add("HasFlag");
        return predicates;
    }

    public World createWorld(int[] bounds) { //return null;
        return new World(bounds);
    }
}
