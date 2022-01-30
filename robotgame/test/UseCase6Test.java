import org.junit.jupiter.api.Test;
import robotgame.Direction;
import robotgame.World;

import java.io.File;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UseCase6Test {

    private String path = System.getProperty("user.dir") + "/gameworlds/robotGameWorld/test/worlds/";

    @Test
    public void loadTest1(){
        File file = new File(path + "World1");
        World gw = new World(new int[]{1, 1, 1, 1});
        gw.loadGame(file);
        assertEquals(10, gw.getMaxBlocks());

        // assertArrayEquals doesn't work with arrays of gridObject
        assertEquals(3, gw.getGrid().size());
        assertEquals(2, gw.getGrid().get(0).size());

        assertEquals(Direction.LEFT, gw.getRobot().getDirection());
        assertArrayEquals(new int[] {1,2}, gw.getRobot().getPosition());
    }

}
