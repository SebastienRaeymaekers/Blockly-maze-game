import robotgame.World;
import org.junit.jupiter.api.Assertions;



class GameWorldTest {

    @org.junit.jupiter.api.Test
    void isPassableAir() {
        World gw = getExampleWorld1();
        Assertions.assertTrue(gw.isPassable(0, 0));
    }

    @org.junit.jupiter.api.Test
    void isPassableWall() {
        World gw = getExampleWorld1();
        Assertions.assertFalse(gw.isPassable(1, 1));
    }

    @org.junit.jupiter.api.Test
    void isPassableOutOfBounds() {
        World gw = getExampleWorld1();
        Assertions.assertFalse(gw.isPassable(-1, 1));
    }

    @org.junit.jupiter.api.Test
    void wallInFrontAir() {
        World gw = getExampleWorld1();
        Assertions.assertFalse(gw.wallInFront());
    }

    @org.junit.jupiter.api.Test
    void outOfBoundsOutside() {
        World gw = getExampleWorld1();
        Assertions.assertTrue(gw.outOfBounds(1, 13));
    }

    @org.junit.jupiter.api.Test
    void outOfBoundsInside() {
        World gw = getExampleWorld1();
        Assertions.assertFalse(gw.outOfBounds(1, 1));
    }

    static World getExampleWorld1() {
        return new World(new int[]{1, 1, 1, 1});

    }
}