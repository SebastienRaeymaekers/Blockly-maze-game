package GameWorldApi;

import java.awt.*;

/**
 * This interface imposes methods to the class implementing it, so the top-level application
 * can express its results on this implementing class.
 *
 * The GameWorld interface provides a method to execute a given action represented as a string.
 * The GameWorld interface provides a method to eval a given predicate represented as string.
 * The GameWorld interface provides a method to get a snapshot from the object implementing this
 * interface, which is returned as a GameWorld object.
 * The GameWorld interface provides a method to set the object implementing this interface
 * using a given GameWorld snapshot.
 * The GameWorld interface provides a method to paint the object implementing this interface with
 * given Graphics.
 * The GameWorld interface provides a method to load a game. This way, different looking GameWorlds can
 * be loaded using the {@link FileToDataReader} class and the object implementing this interface can choose
 * its own representation of how to store worlds in a string format.
 * The GameWorld interface provides a method to check if the game is completed.
 * The GameWorld interface provides a method to get the max blocks available set by the object implementing
 * this interface.
 */
public interface GameWorld {
    ExecuteResult execute(String action);
    boolean eval(String predicate);
    GameWorld getSnapShot();
    void setSnapShot(GameWorld world);
    void paint(Graphics g);
    void loadGame();
    int getMaxBlocks();
}

