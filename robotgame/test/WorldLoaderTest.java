import org.junit.jupiter.api.Test;
import robotgame.Direction;
import robotgame.WorldLoader;

import java.util.HashMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WorldLoaderTest {

    @Test
    void DataToDirection_Left() {
        HashMap<String, String> data = new HashMap<>();
        data.put("direction", "left");
        Direction direction = WorldLoader.DataToDirection(data);
        assertEquals(direction, Direction.LEFT);
    }

    @Test
    void DataToDirection_Right() {
        HashMap<String, String> data = new HashMap<>();
        data.put("direction", "right");
        Direction direction = WorldLoader.DataToDirection(data);
        assertEquals(direction, Direction.RIGHT);

    }

    @Test
    void DataToDirection_Up() {
        HashMap<String, String> data = new HashMap<>();
        data.put("direction", "up");
        Direction direction = WorldLoader.DataToDirection(data);
        assertEquals(direction, Direction.UP);

    }

    @Test
    void DataToDirection_Down() {
        HashMap<String, String> data = new HashMap<>();
        data.put("direction", "down");
        Direction direction = WorldLoader.DataToDirection(data);
        assertEquals(direction, Direction.DOWN);

    }

    @Test
    void DataToDirection_Other() {
        HashMap<String, String> data = new HashMap<>();
        data.put("direction", "nonsense");
        assertThrows(
                IllegalArgumentException.class,
                () -> WorldLoader.DataToDirection(data)
        );

    }

    @Test
    void DataToDirection_NoDirection() {
        HashMap<String, String> data = new HashMap<>();
        assertThrows(
                IllegalArgumentException.class,
                () -> WorldLoader.DataToDirection(data)
        );

    }

    @Test
    void DataToGrid_NoHeight() {
        HashMap<String, String> data = new HashMap<>();
        data.put("width", "3");
        data.put("grid", "aawawgaaa");

        assertThrows(
                IllegalArgumentException.class,
                () -> WorldLoader.DataToGrid(data)
        );
    }


    @Test
    void DataToGrid_NoWidth() {
        HashMap<String, String> data = new HashMap<>();
        data.put("height", "3");
        data.put("grid", "aawawgaaa");

        assertThrows(
                IllegalArgumentException.class,
                () -> WorldLoader.DataToGrid(data)
        );
    }


    @Test
    void DataToGrid_NoGrid() {
        HashMap<String, String> data = new HashMap<>();
        data.put("height", "3");
        data.put("width", "3");

        assertThrows(
                IllegalArgumentException.class,
                () -> WorldLoader.DataToGrid(data)
        );
    }

    @Test
    void DataToGrid_InvalidHeight() {
        HashMap<String, String> data = new HashMap<>();
        data.put("height", "a");
        data.put("width", "3");
        data.put("grid", "aawawgaaa");

        assertThrows(
                IllegalArgumentException.class,
                () -> WorldLoader.DataToGrid(data)
        );
    }

    @Test
    void DataToGrid_InvalidWidth() {
        HashMap<String, String> data = new HashMap<>();
        data.put("height", "3");
        data.put("width", "b");
        data.put("grid", "aawawgaaa");

        assertThrows(
                IllegalArgumentException.class,
                () -> WorldLoader.DataToGrid(data)
        );
    }

    @Test
    void DataToGrid_InvalidGrid() {
        HashMap<String, String> data = new HashMap<>();
        data.put("height", "3");
        data.put("width", "3");
        data.put("grid", "zzzyyyxxx");

        assertThrows(
                IllegalArgumentException.class,
                () -> WorldLoader.DataToGrid(data)
        );
    }

    @Test
    void DataToGrid_InvalidSize() {
        HashMap<String, String> data = new HashMap<>();
        data.put("height", "3");
        data.put("width", "3");
        data.put("grid", "aaaa");

        assertThrows(
                IllegalArgumentException.class,
                () -> WorldLoader.DataToGrid(data)
        );
    }

    @Test
    void DataToPos() {
        HashMap<String, String> data = new HashMap<>();
        data.put("x", "0");
        data.put("y", "0");
        int[] pos = WorldLoader.DataToPos(data);

        int[] expected = new int[]{0, 0};

        assertArrayEquals(pos, expected);
    }

    @Test
    void DataToPos_NoX() {
        HashMap<String, String> data = new HashMap<>();
        data.put("y", "0");

        assertThrows(
                IllegalArgumentException.class,
                () -> WorldLoader.DataToPos(data)
        );
    }

    @Test
    void DataToPos_NoY() {
        HashMap<String, String> data = new HashMap<>();
        data.put("x", "0");

        assertThrows(
                IllegalArgumentException.class,
                () -> WorldLoader.DataToPos(data)
        );
    }

    @Test
    void DataToPos_InvalidX() {
        HashMap<String, String> data = new HashMap<>();
        data.put("x", "a");
        data.put("y", "0");

        assertThrows(
                IllegalArgumentException.class,
                () -> WorldLoader.DataToPos(data)
        );
    }

    @Test
    void DataToPos_InvalidY() {
        HashMap<String, String> data = new HashMap<>();
        data.put("x", "0");
        data.put("y", "b");

        assertThrows(
                IllegalArgumentException.class,
                () -> WorldLoader.DataToPos(data)
        );
    }

    //TODO Make objects public or move test to package
//    @Test
//    void GameDataIsCorrect() {
//        int[] pos = new int[]{0, 0};
//        ArrayList<ArrayList<GridObject>> grid = new ArrayList<>();
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Goal())));
//        Direction dir = Direction.UP;
//        int maxBlocks = 10;
//
//        WorldLoader.GameDataIsCorrect(pos, dir, grid, maxBlocks);
//    }
//
//    @Test
//    void GameDataIsCorrect2() {
//        int[] pos = new int[]{1, 1};
//        ArrayList<ArrayList<GridObject>> grid = new ArrayList<>();
//        grid.add(new ArrayList<>(Arrays.asList(new Wall(), new Wall())));
//        grid.add(new ArrayList<>(Arrays.asList(new Wall(), new Goal())));
//        Direction dir = Direction.DOWN;
//        int maxBlocks = 10;
//
//        WorldLoader.GameDataIsCorrect(pos, dir, grid, maxBlocks);
//    }
//
//    @Test
//    void GameDataIsCorrect3() {
//        int[] pos = new int[]{0, 1};
//        ArrayList<ArrayList<GridObject>> grid = new ArrayList<>();
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        grid.add(new ArrayList<>(Arrays.asList(new Goal(), new Air())));
//        Direction dir = Direction.RIGHT;
//        int maxBlocks = 10;
//
//        WorldLoader.GameDataIsCorrect(pos, dir, grid, maxBlocks);
//    }
//
//    @Test
//    void GameDataIsCorrect_NoPosition() {
//        int[] pos = null;
//        ArrayList<ArrayList<GridObject>> grid = new ArrayList<>();
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        Direction dir = Direction.RIGHT;
//        int maxBlocks = 10;
//
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> WorldLoader.GameDataIsCorrect(pos, dir, grid, maxBlocks)
//        );
//    }
//
//    @Test
//    void GameDataIsCorrect_NoGrid() {
//        int[] pos = new int[]{1, 0};
//        ArrayList<ArrayList<GridObject>> grid = null;
//        Direction dir = Direction.RIGHT;
//        int maxBlocks = 10;
//
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> WorldLoader.GameDataIsCorrect(pos, dir, grid, maxBlocks)
//        );
//    }
//
//    @Test
//    void GameDataIsCorrect_NonComplete() {
//        int[] pos = new int[]{1, 0};
//        ArrayList<ArrayList<GridObject>> grid = new ArrayList<>();
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), null)));
//        Direction dir = Direction.RIGHT;
//        int maxBlocks = 10;
//
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> WorldLoader.GameDataIsCorrect(pos, dir, grid, maxBlocks)
//        );
//    }
//
//    @Test
//    void GameDataIsCorrect_NoDirection() {
//        int[] pos = new int[]{1, 0};
//        ArrayList<ArrayList<GridObject>> grid = new ArrayList<>();
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        Direction dir = null;
//        int maxBlocks = 10;
//
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> WorldLoader.GameDataIsCorrect(pos, dir, grid, maxBlocks)
//        );
//    }
//
//
//    @Test
//    void GameDataIsCorrect_StartInBlock() {
//        int[] pos = new int[]{1, 0};
//        ArrayList<ArrayList<GridObject>> grid = new ArrayList<>();
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Goal())));
//        Direction dir = Direction.RIGHT;
//        int maxBlocks = 10;
//
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> WorldLoader.GameDataIsCorrect(pos, dir, grid, maxBlocks)
//        );
//    }
//
//    @Test
//    void GameDataIsCorrect_NoGoal() {
//        int[] pos = new int[]{0, 0};
//        ArrayList<ArrayList<GridObject>> grid = new ArrayList<>();
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        Direction dir = Direction.RIGHT;
//        int maxBlocks = 10;
//
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> WorldLoader.GameDataIsCorrect(pos, dir, grid, maxBlocks)
//        );
//    }
//
//    @Test
//    void GameDataIsCorrect_BotStarts_BelowGrid() {
//        int[] pos = new int[]{0, -1};
//        ArrayList<ArrayList<GridObject>> grid = new ArrayList<>();
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        Direction dir = Direction.RIGHT;
//        int maxBlocks = 10;
//
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> WorldLoader.GameDataIsCorrect(pos, dir, grid, maxBlocks)
//        );
//    }
//
//    @Test
//    void GameDataIsCorrect_BotStarts_AboveGrid() {
//        int[] pos = new int[]{0, 2};
//        ArrayList<ArrayList<GridObject>> grid = new ArrayList<>();
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        Direction dir = Direction.RIGHT;
//        int maxBlocks = 10;
//
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> WorldLoader.GameDataIsCorrect(pos, dir, grid, maxBlocks)
//        );
//    }
//
//    @Test
//    void GameDataIsCorrect_BotStarts_LeftOf() {
//        int[] pos = new int[]{-1, 0};
//        ArrayList<ArrayList<GridObject>> grid = new ArrayList<>();
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        Direction dir = Direction.RIGHT;
//        int maxBlocks = 10;
//
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> WorldLoader.GameDataIsCorrect(pos, dir, grid, maxBlocks)
//        );
//    }
//
//    @Test
//    void GameDataIsCorrect_BotStarts_RightOf() {
//        int[] pos = new int[]{1, 0};
//        ArrayList<ArrayList<GridObject>> grid = new ArrayList<>();
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        grid.add(new ArrayList<>(Arrays.asList(new Air(), new Wall())));
//        Direction dir = Direction.RIGHT;
//        int maxBlocks = 10;
//
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> WorldLoader.GameDataIsCorrect(pos, dir, grid, maxBlocks)
//        );
//    }
}
