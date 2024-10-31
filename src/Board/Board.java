package Board;

import java.util.ArrayList;

import Entity.Barrier;
import Entity.Player;
import Main.GamePanel;

public class Board {
	private Block _board[][];
	public static final int _rows = 9, _cols = 9;// blocks size
	public final static int _boardXOffset = calcXOffset(), _boardYOffset = 40;// graphics board start offset
	private ArrayList<Barrier> _obstacles;// list of barriers in the board

	private static final int calcXOffset() {
		// calculate the offset of the board to make the board be in the screen half
		int res = GamePanel.WIDTH / 2 - (_cols / 2 + _cols % 2) * (Block.size + Barrier.thickness)
				+ (Block.size + Barrier.thickness) / 2;
		return res;
	}

	public Board() {
		_obstacles = new ArrayList<Barrier>();
		_board = new Block[_rows][_cols];
		// init rows
		for (int i = 0; i < _board.length; i++) {
			// init cols
			for (int j = 0; j < _board[0].length; j++) {
				_board[i][j] = new Block();
			}
		}

		// blocking the out of Board/Range areas

		// Horizontal
		for (int i = 0; i < _board[0].length; i++) {
			_board[0][i].setBlocked(Directions.UP);
			_board[_rows - 1][i].setBlocked(Directions.DOWN);
		}
		// Vertical
		for (int i = 0; i < _board.length; i++) {
			_board[i][0].setBlocked(Directions.LEFT);
			_board[i][_cols - 1].setBlocked(Directions.RIGHT);
		}
	}

	public Block[][] get_board() {
		return _board;
	}

	public ArrayList<Barrier> get_obstacles() {
		return _obstacles;
	}

	public Block getBlock(int row, int col) {
		// input- the row and col of a block
		// output- the block itself
		if (row >= 0 && row < _rows && col >= 0 && col < _cols)
			return _board[row][col];
		// the block isnt in the range
		return null;
	}

	public void setPlayerPosition(Player p, int row, int col) {
		// input- the player and the start position to set him in the board
		// the function set his row and col and his graphics x and y
		// output- void

		if (row >= 0 && row < _rows && col >= 0 && col < _cols) {
			p.setRowCol(row, col);

			// calc player position
			int x = _boardXOffset + (col) * (Block.size + Barrier.thickness);
			int y = _boardYOffset + (row) * (Block.size + Barrier.thickness);

			p.setPosition(x, y);
			_board[row][col].set_player(true);
		}
	}

	public void setBarrierPosition(Barrier b, int row, int col) {
		// input- the barrier and the first position to set him in the board
		// the function set his row and col and his graphics x and y
		// output- void
		if (row >= 0 && row < _rows && col >= 0 && col < _cols) {
			b.setRowCol(row, col);

			int x = _boardXOffset + (col) * (Block.size + Barrier.thickness) - Barrier.thickness * b.get_rowHolder();
			int y = _boardYOffset + (row) * (Block.size + Barrier.thickness) - Barrier.thickness * b.get_colHolder();
			b.setPosition(x, y);
		}
	}

	public boolean canPutBarrier(Barrier b, int row, int col) {
		// input- a barrier , row and col we want to set it
		// output- is it possible to put the barrier in this position

		// *the barrier direction=d
		// if the position out of index or the d side in the square is blocked (there is
		// already a barrier) then it means the position is unavailable
		return !(col < 0 || row < 0 || col >= _cols || row >= _rows
				|| getBlock(row, col).get_up() && b.getDir() == Directions.UP
				|| getBlock(row, col).get_down() && b.getDir() == Directions.DOWN
				|| getBlock(row, col).get_right() && b.getDir() == Directions.RIGHT
				|| getBlock(row, col).get_left() && b.getDir() == Directions.LEFT);

	}

	public Boolean canGoTo(int row, int col, int dir) {
		// input- row and col (of the player) and a direction the player wants to move
		// output- if the player can move to this direction(the way doesnt blocked)

		int x = Directions.Dirsx[dir], y = Directions.Dirsy[dir];

		Block b = getBlock(row + y, col + x);
		boolean res = false;
		if (b != null) {
			if (dir == Directions.UP)
				res = !b.get_down();
			else if (dir == Directions.DOWN)
				res = !b.get_up();
			else if (dir == Directions.RIGHT)
				res = !b.get_left();
			else if (dir == Directions.LEFT)
				res = !b.get_right();
		}
		return res;
	}

	public boolean addBarrier(Barrier b) {
		// input- a barrier that the player wants to add
		// output- is it possible to put it ( add it to the barrier list and block the
		// surround area of the barrier)

		int dir = b.getDir();
		// first
		int row = b.get_row(), col = b.get_col();
		// second part
		int colH = b.get_colHolder(), rowH = b.get_rowHolder();

		// check the position of both part of the barrier if its legal position the
		// place it
		if (canPutBarrier(b, row, col) && canPutBarrier(b, row + rowH, col + colH)) {

			// first pos
			_board[row][col].setBlocked(dir);
			_board[row + rowH][col + colH].setBlocked(dir);
			// second pos
			dir = Directions.oppositeDir(dir);

			_board[row - colH][col - rowH].setBlocked(dir);
			_board[row + rowH - colH][col + colH - rowH].setBlocked(dir);
			_obstacles.add(b);
			return true;
		}
		return false;
	}

	public void removeBarrier(Barrier b) {
		// input- barrier to remove
		// output- void
		// the function remove the barrier if the list contains it
		if (_obstacles.contains(b)) {

			int dir = b.getDir();
			// first
			int row = b.get_row(), col = b.get_col();
			// second part
			int colH = b.get_colHolder(), rowH = b.get_rowHolder();

			// first pos
			_board[row][col].removeBlocked(dir);
			_board[row + rowH][col + colH].removeBlocked(dir);
			// second pos

			dir = Directions.oppositeDir(dir);
			_board[row - colH][col - rowH].removeBlocked(dir);
			_board[row + rowH - colH][col + colH - rowH].removeBlocked(dir);
			_obstacles.remove(b);
		}
	}

}
