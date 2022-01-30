package mygame;

// A tile can either be a flag, unrevealed, or revealed, in which case it shows the amount of nearby bombs.
public class Tile implements Cloneable {

    private final boolean bomb;
    private boolean hasFlag;
    private int neighbourAmount = -1; // Number from -1 to 8 indicating how many bombs neighbour this tile.
                                      // -1 means the number is unknown, 0 means no neighbours, etc

    @Override
    public Tile clone() {
        Tile clonedTile;
        try {
            clonedTile = (Tile) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        return clonedTile;
    }

    public Tile(boolean isBomb) {
        bomb = isBomb;
    }

    public boolean isBomb() {
        return bomb;
    }

    public void toggleFlag() {
        hasFlag = !hasFlag;
    }

    public boolean hasFlag() {
        return hasFlag;
    }

    public void setNeighbourAmount(int amount) {
        if (neighbourAmount != -1) return; // We can only update this value once
        neighbourAmount = amount;
    }

    public int getNeighbourAmount() {
        return neighbourAmount;
    }
}
