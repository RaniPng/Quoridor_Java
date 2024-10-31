package Board;

public class Directions {

	public static final int block = Block.size;// graphic size of a block

	private static int counter = 0;// direction counter - works as an Enum /*
	public static final int UP = counter++;
	public static final int RIGHTUP = counter++;
	public static final int RIGHT = counter++;
	public static final int RIGHTDOWN = counter++;
	public static final int DOWN = counter++;
	public static final int DOWNLEFT = counter++;
	public static final int LEFT = counter++;
	public static final int LEFTUP = counter++;
	public static final int NumDirs = counter;
	// */ Enums of directions

	// arrays of the physic moving
	// last cells-0,0 is for same place
	public static final int Dirsx[] = { 0, 1, 1, 1, 0, -1, -1, -1, 0 };// x/cols dir
	public static final int Dirsy[] = { -1, -1, 0, 1, 1, 1, 0, -1, 0 };// y/rows dir

	public static int fromPointDir(int r1, int c1, int r2, int c2) {
		// input- to positions in the board
		// output- the direction to reach the second position from the first one *only
		// works horizontal and vertical not between (no need between)
		int r = r1 - r2;
		int c = c1 - c2;
		if (r > 0)
			return UP;
		else if (r < 0)
			return DOWN;
		else if (c > 0)
			return LEFT;
		else
			return RIGHT;
	}

	public static int oppositeDir(int d) {
		//input- a direction
		//output- the opposite direction
		if (d == UP)
			return DOWN;
		if (d == DOWN)
			return UP;
		if (d == RIGHT)
			return LEFT;
		if (d == LEFT)
			return RIGHT;
		return -1;
	}

}
