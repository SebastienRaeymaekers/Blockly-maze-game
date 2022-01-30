import org.junit.jupiter.api.Test;
import robotgame.Direction;
import robotgame.World;

import java.io.File;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ExecutionTest {

    private String path = System.getProperty("user.dir") + "/gameworlds/robotGameWorld/test/worlds/";

    @Test
    void ExecuteOneStep() {
        File file = new File(path + "World1");
        World gw = new World(new int[] {1,1,1,1});
        gw.loadGame(file);

        gw.execute("Move Forward");
        assertArrayEquals(new int[] {0,2}, gw.getRobot().getPosition());
        gw.execute("Move Right");
        assertEquals(gw.getRobot().getDirection(), Direction.UP);
        gw.execute("Move Forward");
        assertArrayEquals(new int[] {0,1}, gw.getRobot().getPosition());
        gw.execute("Move Forward");
        assertArrayEquals(new int[] {0,0}, gw.getRobot().getPosition());
    }
}
