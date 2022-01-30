package robotgame;

import java.util.ArrayList;
import java.util.HashMap;

public class WorldLoader {

    public static ArrayList<ArrayList<GridObject>> DataToGrid(HashMap<String, String> data)
            throws IllegalArgumentException{

        if(!data.containsKey("grid") ) {
            throw new IllegalArgumentException("Please add a \"Grid\" field. \n"
                    + "Use: \n"
                    + " \"a\" for a square with air \n"
                    + " \"w\" for a square with a wall \n"
                    + " \"g\" for the goal \n"
                    + "a 3 x 3 grid filled with air is for example: \n"
                    + "aaa \n"
                    + "aaa \n"
                    + "aaa \n");
        }
        if(!data.containsKey("width") || !data.containsKey("height")) {
            throw new IllegalArgumentException("Please add a \"Width\" and a \"Height\" to the file.");
        }

        int width, height;
        int index = 0;
        String text = data.get("grid");
        ArrayList<ArrayList<GridObject>> grid = new ArrayList<>();
        ArrayList<GridObject> gameRow;

        try {
            width = Integer.parseInt(data.get("width"));
            height = Integer.parseInt(data.get("height"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please use a number to indicate the grid width and height.");
        }

        //check if gridlength is correct.
        if(text.length() != height * width) {
            System.out.println(text);
            System.out.println(height * width);
            throw new IllegalArgumentException("The height and width don't correspond with the size of the grid. \n"
                    + "Use: \n"
                    + " \"a\" for a square with air \n"
                    + " \"w\" for a square with a wall \n"
                    + " \"g\" for the goal \n"
                    + "a 3 x 3 grid filled with air is for example: \n"
                    + "aaa \n"
                    + "aaa \n"
                    + "aaa \n");
        }

        for (int h = 0; h < height; h++) {
            gameRow = new ArrayList<>();
            for (int w = 0; w < width; w++) {
                gameRow.add(StringToGridObject(text.charAt(index)));
                index++;
            }
            grid.add(gameRow);
        }

        return grid;
    }

    public static int[] DataToPos(HashMap<String, String> data)
            throws IllegalArgumentException{

        if(!data.containsKey("x") || !data.containsKey("y")) {
            throw new IllegalArgumentException("Please add a \"X\" and a \"Y\" to the file to indicate where the robot should start.");
        }

        int[] startPos = new int[2];
        try {
            startPos[0] = Integer.parseInt(data.get("x"));
            startPos[1] = Integer.parseInt(data.get("y"));
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("Please use a number to indicate the x and y value of the robot.");
        }

        return startPos;
    }

    public static int DataToMaxBlocks(HashMap<String, String> data)
            throws IllegalArgumentException{
        if(!data.containsKey("maxblocks")) {
            throw new IllegalArgumentException("Please add \"maxblocks\" to the file to indicate how many blocks are allowed to be used.");
        }

        int maxBlocks;
        try {
            maxBlocks = Integer.parseInt(data.get("maxblocks"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please use a number to indicate the maximum amount of blocks that can be used");
        }
        return maxBlocks;
    }

    public static Direction DataToDirection(HashMap<String, String> data)
            throws IllegalArgumentException{
        if(!data.containsKey("direction")) {
            throw new IllegalArgumentException("Please add a \"Direction\"  to the file to indicate where the robot should Face when the game starts. \n"
                    + "The valid Directions are \"Left\",\"Right\",\"Up\" or \"Down\" ");
        }

        return StringToDirection(data.get("direction"));
    }

    public static void GameDataIsCorrect(int[] pos, Direction dir, ArrayList<ArrayList<GridObject>> grid, int maxBlocks)
            throws IllegalArgumentException{
        boolean hasAGoal = false;

        if(pos == null || dir == null || grid == null || grid.contains(null))
            throw new IllegalArgumentException("Not all necessary values are given.");
        if(pos[1] >= grid.size() || pos[0] >= grid.get(0).size()  || pos[0] < 0 || pos[1] < 0)
            throw new IllegalArgumentException("The robot has outside the play area. \n"
                    + "The X and Y position start counting from 0.");
        if(grid.get(pos[1]).get(pos[0]) instanceof Wall)
            throw new IllegalArgumentException("The robot has started into a wall. \n"
                    + "The X and Y position start counting from 0 and from left to right and top to bottom.");
        if(maxBlocks <= 0)
            throw new IllegalArgumentException("The max amount of blocks has to be higher than 0.");
        for (ArrayList<GridObject> line : grid) {
            for (GridObject block : line) {
                if (block instanceof Goal) {
                    hasAGoal = true;
                    break;
                }
            }
        }
        if(!hasAGoal) {
            throw new IllegalArgumentException("The given grid doesn't have a goal."
                    + "Use: \n"
                    + " \"a\" for a square with air \n"
                    + " \"w\" for a square with a wall \n"
                    + " \"g\" for the goal \n");
        }
    }

    private static GridObject StringToGridObject(Character gridObject)
            throws IllegalArgumentException{
        switch (Character.toLowerCase(gridObject)) {
            case 'w':
                return new Wall();
            case 'g':
                return new Goal();
            case 'a':
                return new Air();
        }
        throw new IllegalArgumentException("The Grid is not valid. \n"
                + "Please use only \n"
                + " \"a\" for a square with air \n"
                + " \"w\" for a square with a wall \n"
                + " \"g\" for the goal \n");
    }

    private static Direction StringToDirection(String dir)
            throws IllegalArgumentException{
        switch (dir.toLowerCase()) {
            case "left":
                return Direction.LEFT;
            case "right":
                return Direction.RIGHT;
            case "up":
                return Direction.UP;
            case "down":
                return Direction.DOWN;
        }
        throw new IllegalArgumentException("The direction is not valid. \n"
                + "Please use either \"Left\",\"Right\",\"Up\" or \"Down\" ");
    }

}
