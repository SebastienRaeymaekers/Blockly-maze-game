import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import robotgame.Robot;
import robotgame.World;
import robotgame.Direction;
import java.io.File;

public class RobotTest {

    private String path = System.getProperty("user.dir") + "/gameworlds/robotGameWorld/test/worlds/";

    @Test
    public void BotMoveForward_FacingLeft() {
        File file = new File(path + "RobotTestWorld1");
        World gw = new World(new int[] {1,1,1,1});
        gw.loadGame(file);
        System.out.println(gw.getRobot());
        Robot bot = new Robot(Direction.LEFT, new int[] {1,0});
        bot.moveForward(gw);
        Assertions.assertArrayEquals(bot.getPosition() ,new int[] {0,0});
    }

    @Test
    public void BotMoveForward_FacingWall_FacingLeft() {
        File file = new File(path + "RobotTestWorld2");
        World gw = new World(new int[] {1,1,1,1});
        gw.loadGame(file);
        Robot bot = new Robot(Direction.LEFT, new int[] {0,0});
        bot.moveForward(gw);
        Assertions.assertArrayEquals(bot.getPosition() ,new int[] {0,0});
    }

    @Test
    public void BotMoveForward_FacingBorder_FacingLeft() {
        File file = new File(path + "RobotTestWorld3");
        World gw = new World(new int[] {1,1,1,1});
        gw.loadGame(file);
        Robot bot = new Robot(Direction.LEFT, new int[] {0,0});
        bot.moveForward(gw);
        Assertions.assertArrayEquals(bot.getPosition() ,new int[] {0,0});
    }



    @Test
    public void BotMoveForward_FacingRight() {
        File file = new File(path + "RobotTestWorld4");
        World gw = new World(new int[] {1,1,1,1});
        gw.loadGame(file);
        Robot bot = new Robot(Direction.RIGHT, new int[] {0,0});
        bot.moveForward(gw);
        Assertions.assertArrayEquals(bot.getPosition() ,new int[] {1,0});
    }

    @Test
    public void BotMoveForward_FacingWall_FacingRight() {
        File file = new File(path + "RobotTestWorld5");
        World gw = new World(new int[] {1,1,1,1});
        gw.loadGame(file);
        Robot bot = new Robot(Direction.RIGHT, new int[] {2,0});
        bot.moveForward(gw);
        Assertions.assertArrayEquals(bot.getPosition() ,new int[] {2,0});
    }

    @Test
    public void BotMoveForward_FacingBorder_FacingRight() {
        File file = new File(path + "RobotTestWorld6");
        World gw = new World(new int[] {1,1,1,1});
        gw.loadGame(file);
        Robot bot = new Robot(Direction.RIGHT, new int[] {0,0});
        bot.moveForward(gw);
        Assertions.assertArrayEquals(bot.getPosition() ,new int[] {0,0});
    }
}
