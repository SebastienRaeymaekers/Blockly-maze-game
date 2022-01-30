package robotgame;

interface GridObject {
    boolean isPassable();
}

class Wall implements GridObject {
    public boolean isPassable() {
        return false;
    }
}

// we could ignore air objects and simply add nothing to the grid in this space
class Air implements GridObject {
    public boolean isPassable() {
        return true;
    }
}

class Goal implements GridObject {
    public boolean isPassable() {
        return true;
    }
}

