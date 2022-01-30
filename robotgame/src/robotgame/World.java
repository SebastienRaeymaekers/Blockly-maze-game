package robotgame;

import GameWorldApi.ExecuteResult;
import GameWorldApi.FileToDataReader;
import GameWorldApi.GameWorld;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.Double.min;

public class World implements GameWorld {

    private static final Color airColor = new Color(200, 200, 200);
    private static final Color wallColor = new Color(40, 40, 40);
    private static final Color goalColor = new Color(86, 180, 92);
    private static final Color robotOutlineColor = Color.BLACK;
    private static final Color robotBodyColor = Color.WHITE;
    private static final Color gameWorldColor = new Color(40, 40, 40);

    private ArrayList<ArrayList<GridObject>> grid;
    private int[] botSpawn; // These two bot variables keep track of the initial position and rotation of the robot
    private Robot bot;
    private int[] paintSettings; // contains in order: the gridSquareSize (how large should each square be drawn),
    //                    the x_offset for the grid, and the y_offset. Updated automatically
    private int[] bounds;
    private int maxBlocks;
    
    private Type type;

    public World(int[] bounds) {
        this.bounds = bounds;
        // example grid
        ArrayList<ArrayList<GridObject>> newGrid = new ArrayList<>();
        newGrid.add(new ArrayList<>(Arrays.asList(new Air(), new Air(), new Air(), new Air())));
        newGrid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall(), new Air(), new Goal())));
        newGrid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall(), new Wall(), new Wall())));
        newGrid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall(), new Air(), new Air())));
        newGrid.add(new ArrayList<>(Arrays.asList(new Air(), new Air(), new Air(), new Air())));
        setGrid(newGrid);
        botSpawn = new int[]{3, 3};
        bot = new Robot(Direction.LEFT, botSpawn);
        
        type = new Type();
    }

    // copy constructor instead of clone
    public World(int[] bounds, ArrayList<ArrayList<GridObject>> grid, int[] spawnPos, Robot robot, int maxBlocks) {
        this.bounds = bounds;
        this.grid = grid;
        this.bot = new Robot(robot.getDirection(), robot.getPosition());
        this.botSpawn = spawnPos;
        this.maxBlocks = maxBlocks;
    }

    /**
     * Checks if the tile at a given location is passable. This means it can be occupied by the Robot.
     * @param x The X-coordinate
     * @param y The Y-coordinate
     * @return True if the tile is passable (e.g. air)
     */
    public boolean isPassable(int x, int y) {
        if (outOfBounds(x, y)) return false;
        return grid.get(y).get(x).isPassable();
    }

    /**
     * Verifies whether there is a wall on the tile towards which the robot is facing.
     * If this tile is out of bounds, this function returns true as well.
     * @return True if there is a wall or the game border in front of the robot.
     */
    public boolean wallInFront() {
        int[] nextPos = bot.findForwardPosition();
        if (outOfBounds(nextPos[0], nextPos[1])) return true;
        return (grid.get(nextPos[1]).get(nextPos[0]) instanceof Wall);
    }

    public boolean outOfBounds(int x, int y) {
        return (y < 0 || y >= grid.size() || x < 0 || x >= grid.get(0).size());
    }

    public ArrayList<ArrayList<GridObject>> getGrid() {
        return grid;
    }

    private void setGrid(ArrayList<ArrayList<GridObject>> newGrid) {
        grid = newGrid;
        updatePaintSettings();
    }

    public Robot getRobot() {
        return bot;
    }

    /**
     * Updates the variable paintSettings.
     * This variable determines how wide and high the current world should be drawn.
     * The advantage of dynamically calculating this is that we can made the world as large as possible without
     * having to stretch it, instead of picking a set size which might be too large or too small.
     */
    private void updatePaintSettings() {
        paintSettings = new int[]{0, 0, 0};
        double gridPercentage = 0.9; // how much space does the grid take in from the GameWorld. Ranges from 0-1.
        paintSettings[0] = (int) min(bounds[3] * gridPercentage / grid.size(),
                bounds[2] * gridPercentage / grid.get(0).size());

        if (paintSettings[0] == (int) (bounds[3] * gridPercentage / grid.size())) {
            // we are limited by height of the GameWorld, the grid is too high
            paintSettings[1] = bounds[2] / 2 - paintSettings[0] * grid.get(0).size() / 2;
            paintSettings[2] = (int) (bounds[3] * (1 - gridPercentage) / 2);
        } else {
            // we are limited by the width of the GameWorld, the grid is too wide
            paintSettings[1] = (int) (bounds[2] * (1 - gridPercentage) / 2);
            paintSettings[2] = bounds[3] / 2 - paintSettings[0] * grid.size() / 2;
        }
    }


    /**
     * Execute the action corresponding to the given string.
     * @param s The given action. This action might not be supported by this world.
     */
    @Override
    public ExecuteResult execute(String s) {
        switch (s) {
            case "Move Forward":
                int [] oldPos = getRobot().getPosition();
                getRobot().moveForward(this);
                if (Arrays.equals(getRobot().getPosition(), oldPos)) return ExecuteResult.FAILURE;
                else if (isGameCompleted()) return ExecuteResult.COMPLETED;
                else return ExecuteResult.SUCCESS;
            case "Move Left":
                getRobot().rotate(false);
                return ExecuteResult.SUCCESS;
            case "Move Right":
                getRobot().rotate(true);
                return ExecuteResult.SUCCESS;
        }
        return ExecuteResult.FAILURE;
    }

    /**
     * Evaluate the predicate corresponding to the given string.
     * @param s The predicate which should be evaluated. Note that this predicate might not be supported by this world.
     * @return true if the evaluation of the expression corresponding to the given string is true. Else return false.
     */
    @Override
    public boolean eval(String s) {
        if (s.equals("WallInFront")) return wallInFront();
        else return false;
    }


    /**
     * Returns a snapshot of this object. This snapshot is a copy without reference.
     * @return a copy of the GameWorld instance at this moment.
     */
    @Override
    public GameWorld getSnapShot() {
        return new World(this.bounds, this.grid, this.botSpawn, this.bot, this.maxBlocks);
    }

    /**
     * Replace the current world with the given snapshot. Note that this will not copy the world, so take care when
     * passing in a world you do not want to be altered by this world.
     * @param world The new world for this GameWorld.
     */
    @Override
    public void setSnapShot(GameWorld world) {
        World robotWorld = (World) world;
        this.bounds = robotWorld.bounds;
        this.bot = robotWorld.getRobot();
        this.botSpawn = robotWorld.botSpawn;
        setGrid(robotWorld.getGrid());
    }


    /**
     * Paints the GameWorld within the bounds given on construction of this world.
     * @param g The graphics object which should be drawn onto.
     */
    @Override
    public void paint(Graphics g) {
        g.translate(bounds[0], bounds[1]);
        g.setColor(gameWorldColor);
        g.fillRect(0, 0, bounds[2], bounds[3]);
        paintGrid(g);
        g.translate(-bounds[0], -bounds[1]);
    }

    private void paintGrid(Graphics g) {
        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(y).size(); x++) {
                int[] drawBounds = {x * paintSettings[0] + paintSettings[1], y * paintSettings[0] + paintSettings[2], paintSettings[0], paintSettings[0]};
                paint(g, grid.get(y).get(x), drawBounds);
            }
        }
        int[] drawBounds = {getRobot().getPosition()[0] * paintSettings[0] + paintSettings[1],
                getRobot().getPosition()[1] * paintSettings[0] + paintSettings[2],
                paintSettings[0], paintSettings[0]};
        paint(g, getRobot(), drawBounds);
    }

    @Override
    public void loadGame(){loadGame(FileToDataReader.loadGameData());}
    public void loadGame(File file){loadGame(FileToDataReader.loadGameData(file));}

    /**
     * Load a game into the program by reading a txt file containing data about the gameObjects.
     * @param data A HashMap containing various keys and values needed to load in a world. For example, (width, 5).
     */
    public void loadGame(HashMap<String, String> data) throws IllegalArgumentException {
        try {
            int[] pos = WorldLoader.DataToPos(data);
            Direction dir = WorldLoader.DataToDirection(data);
            ArrayList<ArrayList<GridObject>> world = WorldLoader.DataToGrid(data);
            int maxBlocks = WorldLoader.DataToMaxBlocks(data);
            WorldLoader.GameDataIsCorrect(pos, dir, world, maxBlocks);

            botSpawn = pos;
            bot = new Robot(dir, botSpawn);
            setGrid(world);
            setMaxBlocks(maxBlocks);

        } catch (IllegalArgumentException e) {
            // We show a pop up message with the error to the user.
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * @return True when the robot is currently situated on a Goal tile.
     */
    public boolean isGameCompleted() {
        return grid.get(bot.getPosition()[1]).get(bot.getPosition()[0]) instanceof Goal;
    }

    /**
     * @return the maximum number of blocks which can be used for completing this world.
     */
    @Override
    public int getMaxBlocks() {
        return maxBlocks;
    }

    public void setMaxBlocks(int blocks) {
        maxBlocks = blocks;
    }


    private void paint(Graphics g, Robot robot, int[] bounds) {
        g.translate(bounds[0], bounds[1]);
        g.setColor(robotOutlineColor);
        g.fillOval((int) (bounds[2] * 0.05), (int) (bounds[3] * 0.05), (int) (bounds[2] * 0.9), (int) (bounds[3] * 0.9));
        g.setColor(robotBodyColor);
        g.fillOval((int) (bounds[2] * 0.12), (int) (bounds[3] * 0.12), (int) (bounds[2] * 0.76), (int) (bounds[3] * 0.76));
        g.setColor(robotOutlineColor);
        switch (robot.getDirection()) {
            case UP:
                g.fillRect(bounds[2] / 2 - 3, (int) (bounds[3] * 0.12), 6, (int) (bounds[3] * 0.38));
                break;
            case LEFT:
                g.fillRect((int) (bounds[2] * 0.12), bounds[3] / 2 - 3, (int) (bounds[2] * 0.38), 6);
                break;
            case RIGHT:
                g.fillRect(bounds[2] / 2, bounds[3] / 2 - 3, (int) (bounds[2] * 0.38), 6);
                break;
            case DOWN:
                g.fillRect(bounds[2] / 2 - 3, bounds[3] / 2, 6, (int) (bounds[3] * 0.38));
                break;
        }
        g.translate(-bounds[0], -bounds[1]);
    }

    private void paint(Graphics g, GridObject go, int[] bounds) {
        if (go instanceof Air) g.setColor(airColor);
        else if (go instanceof Wall) g.setColor(wallColor);
        else if (go instanceof Goal) g.setColor(goalColor);
        g.fillRect(bounds[0], bounds[1], bounds[2], bounds[3]);
    }
}
