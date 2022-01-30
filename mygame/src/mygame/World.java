package mygame;

import GameWorldApi.FileToDataReader;
import GameWorldApi.GameWorld;
import GameWorldApi.ExecuteResult;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Double.min;

public class World implements GameWorld {

    private static final Color bombColor = new Color(219, 27, 15);
    private static final Color cursorColor = new Color(200, 10, 10);
    private static final Color flagColor = new Color(21, 200, 9);
    private static final Color unrevealedTileColor = new Color(80, 80, 80);
    private static final Color revealedTileColor = new Color(200, 200, 200);
    private static final Color gameWorldColor = new Color(40, 40, 40);

    private boolean gameOver;
    private final float bombChance = 0.16f;
    private ArrayList<ArrayList<Tile>> grid;
    private int[] cursorPos; // holds x and y pos of cursor in the grid
    private int[] paintSettings; // contains in order: the gridSquareSize (how large should each square be drawn),
                                 //                    the x_offset for the grid, and the y_offset. Updated automatically
    private int[] bounds;
    
    private Type type;

    public World(int[] worldBounds) {
        bounds = worldBounds;
        startNewGame(10, 10);
        
        this.type = new Type();
    }

    private void startNewGame(int x, int y) {
        grid = createRandomGrid(x, y);
        cursorPos = new int[] {grid.get(0).size() / 2, grid.size() / 2};
        gameOver = false;
        updatePaintSettings();
    }

    private ArrayList<ArrayList<Tile>> createRandomGrid(int x, int y) {
        ArrayList<ArrayList<Tile>> newGrid = new ArrayList<>();
        for (int i = 0; i < y; i++) {
            ArrayList<Tile> newLine = new ArrayList<>();
            for (int j = 0; j < x; j++) {
                newLine.add(new Tile(Math.random() < bombChance));
            }
            newGrid.add(newLine);
        }
        return newGrid;
    }

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

    private void moveCursor(int xChange, int yChange) {
        cursorPos[0] += xChange;
        cursorPos[1] += yChange;

        // If we leave the grid, we loop around
        cursorPos[0] = Math.floorMod(cursorPos[0], grid.get(0).size());
        cursorPos[1] = Math.floorMod(cursorPos[1], grid.size());
    }

    private void revealTile(int x, int y) {
        Tile curTile = grid.get(y).get(x);
        if (curTile.getNeighbourAmount() != -1) return; // this tile has been revealed already
        if (curTile.isBomb() && !curTile.hasFlag()) {gameOver = true; return;}  // the player has clicked on a bomb
        if (curTile.hasFlag()) curTile.toggleFlag();
        ArrayList<int[]> adjacentPositions = getNeighbouringPositions(x, y);
        int bombAmount = 0;
        for (int[] pos : adjacentPositions) if (grid.get(pos[1]).get(pos[0]).isBomb()) bombAmount++;
        curTile.setNeighbourAmount(bombAmount);
        if (bombAmount == 0) for (int[] pos : adjacentPositions) revealTile(pos[0], pos[1]);
    }

    private ArrayList<int[]> getNeighbouringPositions(int x, int y) {
        ArrayList<int[]> neighbourPositions = new ArrayList<>();
        for (int j = Math.max(0, y-1); j <= Math.min(grid.size()-1, y+1); j++) {
            for (int i = Math.max(0, x-1); i <= Math.min(grid.get(j).size()-1, x + 1); i++) {
                if (j == y && i == x) continue;
                neighbourPositions.add(new int[] {i, j});
            }
        }
        return neighbourPositions;
    }

    private void putFlag(int x, int y) {
        if (grid.get(y).get(x).getNeighbourAmount() != -1) return; // Can't toggle flag on revealed squares
        grid.get(y).get(x).toggleFlag();
    }

    @Override
    public ExecuteResult execute(String action) {
        switch (action) {
            case "Move Up":
                moveCursor(0,  -1);
                break;
            case "Move Left":
                moveCursor(-1,  0);
                break;
            case "Move Right":
                moveCursor(1,  0);
                break;
            case "Move Down":
                moveCursor(0,  1);
                break;
            case "Reveal":
                if (gameOver) return ExecuteResult.FAILURE;
                if (grid.get(cursorPos[1]).get(cursorPos[0]).getNeighbourAmount() != -1) return ExecuteResult.FAILURE;
                revealTile(cursorPos[0], cursorPos[1]);
                break;
            case "Put Flag":
                if (gameOver) return ExecuteResult.FAILURE;
                putFlag(cursorPos[0], cursorPos[1]);
                break;
            case "Restart":
                startNewGame(grid.get(0).size(), grid.size());
                break;
        }
        return isGameCompleted() ? ExecuteResult.COMPLETED : ExecuteResult.SUCCESS;
    }

    @Override
    public boolean eval(String predicate) {
        if (predicate.equals("hasFlag")) return grid.get(cursorPos[1]).get(cursorPos[0]).hasFlag();
        return false;
    }

    // copy constructor instead of clone
    public World(int[] bounds, ArrayList<ArrayList<Tile>> grid, int[] cursorLocation, boolean gameOver) {
        this.bounds = bounds;
        this.grid = grid;
        this.cursorPos = cursorLocation;
        this.gameOver = gameOver;
    }

    @Override
    public GameWorld getSnapShot() {
        ArrayList<ArrayList<Tile>> clonedGrid = new ArrayList<>();
        for (ArrayList<Tile> tiles : grid) {
            ArrayList<Tile> newLine = new ArrayList<>();
            for (Tile tile : tiles) newLine.add(tile.clone());
            clonedGrid.add(newLine);
        }
        return new World(this.bounds.clone(), clonedGrid, this.cursorPos.clone(), this.gameOver);
    }

    @Override
    public void setSnapShot(GameWorld world) {
        World bombWorld = (World) world;
        this.bounds = bombWorld.bounds;
        this.grid = bombWorld.grid;
        this.cursorPos = bombWorld.cursorPos;
        this.gameOver = bombWorld.gameOver;
        updatePaintSettings();
    }

    @Override
    public void paint(Graphics g) {
        g.translate(bounds[0], bounds[1]);
        g.setColor(gameOver ? bombColor : gameWorldColor);
        g.fillRect(0, 0, bounds[2], bounds[3]);
        paintGrid(g);
        paintCursor(g);
        g.translate(-bounds[0], -bounds[1]);
    }

    private void paintGrid(Graphics g) {
        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(y).size(); x++) {
                int[] drawBounds = {x * paintSettings[0] + paintSettings[1], y * paintSettings[0] + paintSettings[2], paintSettings[0], paintSettings[0]};
                paint(g, grid.get(y).get(x), drawBounds);
            }
        }
    }

    private void paintCursor(Graphics g) {
        g.setColor(cursorColor);
        g.drawRect( cursorPos[0] * paintSettings[0] + paintSettings[1],
                    cursorPos[1] * paintSettings[0] + paintSettings[2],
                       paintSettings[0], paintSettings[0]);
    }

    private void paint(Graphics g, Tile tile, int[] drawBounds) {
        g.setColor(tile.getNeighbourAmount() == -1 ? unrevealedTileColor : revealedTileColor);
        g.fillRect(drawBounds[0], drawBounds[1], drawBounds[2], drawBounds[3]);

        if (tile.isBomb() && gameOver) {
            g.setColor(bombColor);
            g.fillOval(drawBounds[0], drawBounds[1], drawBounds[2], drawBounds[3]);
        } else if (tile.hasFlag()) {
            g.setColor(flagColor);
            g.fillOval(drawBounds[0], drawBounds[1], drawBounds[2], drawBounds[3]);
        } else if (tile.getNeighbourAmount() > 0) { // we don't draw text when it's unrevealed or has no neighbours
            g.setColor(Color.BLACK);
            int xPos = drawBounds[0] + drawBounds[2] / 2;
            int yPos = drawBounds[1] + drawBounds[3] / 2;
            g.drawString(Integer.toString(tile.getNeighbourAmount()), xPos, yPos);
        }
    }

    @Override
    public void loadGame() {
        HashMap<String, String> data = FileToDataReader.loadGameData();
        int xSize, ySize;

        if(!data.containsKey("x") || !data.containsKey("y")) {
            throw new IllegalArgumentException("Please add a \"X\" and a \"Y\" to the file to indicate where the robot should start.");
        }

        try {
            xSize = Integer.parseInt(data.get("x"));
            ySize = Integer.parseInt(data.get("y"));
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("Please use a number to indicate the x and y value of the robot.");
        }

        startNewGame(xSize, ySize);
    }

    public boolean isGameCompleted() {
        if (gameOver) return false;
        for (ArrayList<Tile> tiles : grid) {
            for (Tile currentTile : tiles) {
                if (!currentTile.hasFlag() && currentTile.getNeighbourAmount() == -1) return false;
                if (currentTile.hasFlag() && !currentTile.isBomb()) return false;
                if (!currentTile.hasFlag() && currentTile.isBomb()) return false;
            }
        }
        return true;
    }

    @Override
    public int getMaxBlocks() {
        return 0;
    }
}
