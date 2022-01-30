package robotgame;

import java.util.Arrays;

public class Robot {
    private int[] pos;
    private Direction direction;

    public Robot(Direction d, int[] spawnPos) {
        direction = d;
        pos = spawnPos;
    }

    public int[] getPosition() {
        return pos;
    }


    public int[] findForwardPosition() {
        int newX = pos[0];
        int newY = pos[1];
        switch (direction) {
            case UP:
                newY--;
                break;
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newX++;
                break;
            case DOWN:
                newY++;
                break;
        }
        return new int[] {newX, newY};
    }

    public void moveForward(World gw) {
        int[] nextPos = findForwardPosition();
        if (gw.isPassable(nextPos[0], nextPos[1])) pos = nextPos;
    }

    /**
     * Change the rotation of the robot
     * @param right the direction in which the robot should rotate. if true the robot will turn right, else left.
     */
    public void rotate(boolean right) {
        Direction[] rotations = {Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP};
        direction = rotations[right ? (Arrays.asList(rotations).indexOf(direction) + 1) : (Arrays.asList(rotations).lastIndexOf(direction) - 1)];
    }

    public Direction getDirection() {
        return direction;
    }
    
    public Robot clone() {
    	return (new Robot(this.direction, this.pos.clone()));
    }
}
