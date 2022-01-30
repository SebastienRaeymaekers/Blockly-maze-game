package blockr;

import GameWorldApi.GameWorld;
import GameWorldApi.GameWorldType;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class GameLoader{

	/**
	 * creates a gameworld of the given type
	 * @param jarName the name of the wanted gameWorld type
	 * @param bounds an array of integers that represents the bounds of the gameWorld
	 * @return a gameworld of the requested type
	 * @throws ClassNotFoundException if the application can't find a gameWorld of the requested type
	 */
    public GameWorld loadGame(String jarName, int[] bounds) throws ClassNotFoundException {
        File file = new File(jarName + ".jar");
        URLClassLoader child = null;
        try {
            child = new URLClassLoader(
                    new URL[] {file.toURI().toURL()},
                    this.getClass().getClassLoader()
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String className;
        if (jarName.contains("Type")) className = jarName; else className = jarName + "." + "Type";
        Class classToLoad = Class.forName(className, true, child);
        try {
            GameWorldType gameWorldType = (GameWorldType) classToLoad.newInstance();
            return gameWorldType.createWorld(bounds);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
