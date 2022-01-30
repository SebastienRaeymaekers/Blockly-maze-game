package GameWorldApi;

import java.util.ArrayList;

/**
 * This interface imposes methods to the class implementing it, so the top-level application
 * has some management over this implementing class.
 *
 * The GameWorldType interface provides a method to retrieve the list of Actions supported by the GameWorldType.
 * The GameWorldType interface provides a method to retrieve the list of Predicates supported by the GameWorldType.
 * The GameWorldType interface provides a method to create a new game world instance, which implements interface GameWorld.
 */
public interface GameWorldType {
    public ArrayList<String> getActions();
    public ArrayList<String> getPredicates();
    public <T extends GameWorld> T createWorld(int[] bounds); //extends = interface apparently: https://stackoverflow.com/questions/976441/java-generics-why-is-extends-t-allowed-but-not-implements-t/40392671
}

