package simplegameapp;

public abstract class Area {
    private int[] bounds;

    public Area(int[] bounds) {
        this.bounds = bounds;
    }

    public int[] getBounds() {
        return bounds;
    }
}
