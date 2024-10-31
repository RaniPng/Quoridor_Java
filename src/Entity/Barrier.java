package Entity;

import Board.Block;
import Board.Board;
import Board.Directions;

public class Barrier extends BoardItem {
	// the graphics sizes of the barrier
	public static final int thickness = 7;// the space between two blocks
	public static final int placeHolder = (Block.size) * 2 + thickness;// the length of the whole barrier(the part that
																		// should block)

	private int _blockDir;// the direction of the barrier(towards the player) which I decided it can only
							// be set to up(rotate=false) or left(rotate=true)

	// width and height are depend on whether the barrier is rotate or not (when not
	// rotated width = placeHolder, height = thickness)
	private int width;
	private int height;

	// the Holders are supposed to be the extended of the barrier
	// (one should be 0 and the other 1)
	// they are like booleans but I choosed to use them as integer for calculations
	private int _rowHolder;
	private int _colHolder;

	private boolean _rotated;// is the barrier rotated
	private int _playerNum;// the player who put the barrier's num- used inorder to pick the player color

	public Barrier(int row, int col, Board b, int num, boolean rotated) {
		// input- the barrier information

		_board = b;

		// fix vertical limts
		if (!rotated && row == 0)
			row++;
		if (!rotated && col + 1 >= _board._cols)
			col -= 1;

		// fix horizontal limits
		if (rotated && row == Board._rows - 1)
			row--;
		if (rotated && col == 0)
			col++;

		_rotated = rotated;
		// set the information according to the rotating
		// *explanation is in the variables statement*
		if (!_rotated) {
			_blockDir = Directions.UP;
			width = placeHolder;
			height = thickness;
			_rowHolder = 0;
			_colHolder = 1;
		} else {
			_blockDir = Directions.LEFT;
			width = thickness;
			height = placeHolder;
			_rowHolder = 1;
			_colHolder = 0;
		}
		_playerNum = num;
		b.setBarrierPosition(this, row, col);
	}

	public int getDir() {
		return _blockDir;
	}

	public int get_playerNum() {
		return _playerNum;
	}

	public void rotateBarrier() {
		// the function purpose is to liken a barrier rotation by changing it variables
		// the variables values are swapping and keeping the symmetry
		_rotated = !_rotated;

		_rowHolder += _colHolder;
		_colHolder = _rowHolder - _colHolder;
		_rowHolder -= _colHolder;

		width += height;
		height = width - height;
		width -= height;

		if (_rotated) {
			_x -= thickness;
			_y += thickness;
			_blockDir = Directions.LEFT;
		}

		else {
			_x += thickness;
			_y -= thickness;
			_blockDir = Directions.UP;
		}

		// out of bound fixes
		if (!_rotated && _row <= 0)
			mov(Directions.DOWN);

		if (_rotated && _row + 1 >= Board._rows)
			mov(Directions.UP);

		if (_rotated && _col == 0)
			mov(Directions.RIGHT);

		if (!_rotated && _col + 1 >= Board._cols)
			mov(Directions.LEFT);

	}

	public Boolean canGoTo(int x, int y) {
		// input- x direction and y direction
		// output- whether the barrier can move to this position (_row + y, _col + x)
		Block b = _board.getBlock(_row + y, _col + x);
		Block b1 = _board.getBlock(_row + y + _rowHolder, _col + x + _colHolder);
		if (b == null || b1 == null || _rotated && _col + x == 0 || !_rotated && _row + y == 0)
			return false;
		return true;
	}

	public Boolean setDir(int d) {
		// input- direction that we want to move the barrier to
		// output- moving the barrier to this direction
		// or if the barrier is in the first/last index of the row/col
		// so the function localize the barrier in the end/start of the row/col
		// (from end to start or reverse)
		// the function doesnt in charge to change the row col x y ,
		// only to manage this situation and change the moving direction
		// or keep it the same and let it change location
		int len = 1;
		if (!canGoTo(Directions.Dirsx[d], Directions.Dirsy[d])) {
			d = Directions.oppositeDir(d);
			len = _board._rows - 2;
		}
		for (int i = 0; i < len; i++)
			mov(d);

		return true;
	}

	public void mov(int d) {
		// input- the direction to move the barrier to
		//outcome- the values updated according to the move
		int x = Directions.Dirsx[d], y = Directions.Dirsy[d];
		_x += x * (Directions.block + thickness);
		_y += y * (Directions.block + thickness);
		_row += y;
		_col += x;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int get_rowHolder() {
		return _rowHolder;
	}

	public int get_colHolder() {
		return _colHolder;
	}

}
