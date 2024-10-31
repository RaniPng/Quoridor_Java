package Board;

public class Block {
	//the graphics size of a block
	public static final int size = 45;
	//booleans flag whether the direction(by its name) is blocked
	private Boolean _up;
	private Boolean _down;
	private Boolean _left;
	private Boolean _right;
	//Temporary there can be two players in a block
	private int _player;

	//init all sides to be unblocked
	public Block() {
		_up = _down = _left = _right  = false;
		_player=0;
	}

	public void setBlocked(int dir) {
		// input- the direction the player wants to block
		// output- void
		if (dir == Directions.UP) {
			set_up(true);
		}
		if (dir == Directions.DOWN) {
			set_down(true);
		}
		if (dir == Directions.RIGHT) {
			set_right(true);

		}
		if (dir == Directions.LEFT) {
			set_left(true);
		}
	}

	public void removeBlocked(int dir) {
		// input- the direction to unblock
		// output- void
		if (dir == Directions.UP) {
			set_up(false);
		}
		if (dir == Directions.DOWN) {
			set_down(false);
		}
		if (dir == Directions.RIGHT) {
			set_right(false);

		}
		if (dir == Directions.LEFT) {
			set_left(false);
		}
	}

	public Boolean get_up() {
		return _up;
	}

	private void set_up(boolean b) {
		this._up = b;
	}

	public Boolean get_down() {
		return _down;
	}

	private void set_down(boolean b) {
		this._down = b;
	}

	public Boolean get_left() {
		return _left;
	}

	private void set_left(boolean b) {
		this._left = b;
	}

	public Boolean get_right() {
		return _right;
	}

	private void set_right(boolean b) {
		this._right = b;
	}

	public int get_player() {
		return _player;
	}

	public void set_player(Boolean flag) {
		_player+=flag?1:-1;
		if(_player<0)
			_player=0;

	}

}
