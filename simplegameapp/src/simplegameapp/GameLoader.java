package simplegameapp;

import GameWorldApi.GameWorld;
import GameWorldApi.GameWorldType;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class GameLoader{

	/**
	 * @param jarName the name of the wanted gameWorld type
	 * @return a GameWorldType of the requested gameWorld
	 * @throws ClassNotFoundException if the application can't find a gameWorld with the requested name
	 */
    public GameWorldType loadGame(String jarName) throws ClassNotFoundException {
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
            return gameWorldType;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
