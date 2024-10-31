package Entity;

import java.awt.Point;
import Board.Block;
import Board.Board;
import Board.Directions;
import Handlers.Turns;

public class Player extends BoardItem {

	private int _playerNum;// the player number
	private int _steps;
	private int _speed;// how fast the x and y of the player will change
						// (make the player faster or slower visuality)

	private boolean _AI = true;// whether the player is AI or not

	// moving vectors
	private int _dx;// direction x
	private int _dy;// direction y

	// position to reach
	private int _px;// x to reach
	private int _py;// y to reach

	// the len left to move
	private int _MovingXToGo;// the x len
	private int _MovingYToGo;// the y len

	private boolean _moving = false;// is the player moving now or not

	private boolean _canPutBarrier = true;
	private boolean _settingABarrier = false;// is the player is trying to put a barrier
	private Barrier _toPut;// the player's barrier
	private int _barriersLeft;// how many barriers left in the barrier station/ the player barriers
	private Point _barrierStation;// where the visuality point of the player barriers is locate and starting from
	private int movsCount;// how many moves do the player have
	// when the player moving toward a cell that already have a player
	// and there is a barrier behind them so the player(this) should be able to move
	// again
	private boolean _finishedPlay = true;// is the player finish his turn
	private Point _goal;// x= row y= col
	// the goal is where the player should reach to win

	public Player(int num) {
		// input- the player number

		// init turn/act variables
		init();
		// init settings variables
		_playerNum = num;
		_speed = 3;
		_barrierStation = new Point();
		setStation();
	}

	public void init() {
		_barriersLeft = 10;
		_MovingXToGo = 0;
		_MovingYToGo = 0;
		_canPutBarrier = true;
	}

	// set the position of the station according to the player num
	public void setStation() {
		int x = _board._boardXOffset, y = _board._boardYOffset;
		int height, width;
		height = _board._rows * (Barrier.thickness + Block.size);
		width = _board._cols * (Barrier.thickness + Block.size);
		if (_playerNum == 1)
			_barrierStation.setLocation(x + width / 3.5, y - 105);
		else if (_playerNum == 2) {
			_barrierStation.setLocation(x + width / 3.5, y + height);
		} else if (_playerNum == 3) {
			_barrierStation.setLocation(x - 105, y + height / 3.5);
		} else {
			_barrierStation.setLocation(x + width, y + height / 3.5);
		}
	}

	public Point get_barrierStation() {
		return _barrierStation;
	}

	public int get_barriersLeft() {
		return _barriersLeft;
	}

	public void addBarrierLeft() {
		_barriersLeft++;
	}

	public void setBoard(Board b) {
		_board = b;
	}

	public void startPlay() {
		// init variables for a new turn
		_canPutBarrier = true;
		_finishedPlay = false;
		movsCount = 1;
		Turns.setCurrAction(1);
	}

	public int getPlayerNum() {
		return _playerNum;
	}

	public void setGoal(int row, int col) {
		_goal = new Point(row, col);
	}

	public Point getGoal() {
		return _goal;
	}

	public void setSettingABarrier(int row, int col, boolean rotate) {
		// input- where to init the barrier at and is it rotated
		// outcome- the player will have a barrier in the board which he will try to put
		// or he wont have it anymore (depend on his previous choice)
		// the function switching between put/barrier mode
		// on= try to place a barrier
		// off= not trying

		// if the player doesnt have barriers left
		// or if the player is stadning in other player block
		// that means he cant try to put a barrier

		if ((_barriersLeft > 0) && _canPutBarrier) {
			_settingABarrier = !_settingABarrier;
			// if the is now trying to set a new barrier we need to create a barrier to put
			if (_settingABarrier)
				_toPut = new Barrier(row, col, _board, _playerNum, rotate);
			else// else the player doesnt need a barrier
				_toPut = null;
		}

	}

	public void setSettingABarrier(boolean rotate) {
		// set the barrier start position in the player current location
		setSettingABarrier(_row, _col, rotate);
	}

	public boolean puttingBarrier() {
		// output- did the barrier Place successfully

		boolean put = false;
		if (_settingABarrier && _board.addBarrier(_toPut)) {
			// if the player is in put mode and the barrier successfully placed

			// make the barrier dissapear/exit barrier mode
			setSettingABarrier(false);

			_finishedPlay = true;
			Turns.setCurrAction(2);
			_barriersLeft--;
			put = true;
		}
		return put;
	}

	public void rotateBarrier() {
		// rotate the barrier
		if (_settingABarrier)
			_toPut.rotateBarrier();
	}

	public boolean putBarrierIn(int row, int col, boolean rotate) {
		// input- specific location to place a barrier and its rotation
		boolean put = false;
		setSettingABarrier(row, col, rotate);// Switch into put mode
		if (_settingABarrier)
			put = puttingBarrier();

		return put;
	}

	public Barrier get_toPut() {
		return _toPut;
	}

	public boolean is_settingABarrier() {
		return _settingABarrier;
	}

	public Boolean setMovIfPlayerInfront(Block b, int dir) {
		// recursion function that handle a situation where the player want to go to a
		// direction and there is/are player in the direction
		// (also handle the situation where there is a barrier behind them)

		// if there is no player in this block that mean the way after the player(s) is
		// free
		if (b.get_player() == 0)
			return true;

		// if there is a block in the way that mean
		if (dir == Directions.UP && b.get_up() || dir == Directions.DOWN && b.get_down()
				|| dir == Directions.LEFT && b.get_left() || dir == Directions.RIGHT && b.get_right()) {
			movsCount = 2;
			_canPutBarrier = false;
			mov(dir);
			return false;
		}

		_steps++;
		int x = Directions.Dirsx[dir];
		int y = Directions.Dirsy[dir];
		return setMovIfPlayerInfront(_board.getBlock(_row + y * _steps, _col + x * _steps), dir);
	}

	public boolean is_moving() {
		return _moving;
	}

	public boolean isAI() {
		return _AI;
	}

	public void setAI(boolean _AI) {
		this._AI = _AI;
	}

	public boolean isFinishedPlay() {
		return _finishedPlay;
	}

	public Boolean canGoTo(int x, int y, int dir) {
		// input- direction and the x and y of the direction
		// output- whether the player can move to this dir or not
		boolean res = false;
		_steps = 1;
		Block b = _board.getBlock(_row + y, _col + x);
		if (b != null) {
			if (dir == Directions.UP)
				res = !b.get_down();
			else if (dir == Directions.DOWN)
				res = !b.get_up();
			else if (dir == Directions.RIGHT)
				res = !b.get_left();
			else if (dir == Directions.LEFT)
				res = !b.get_right();

			if (res && b.get_player() > 0)
				res = setMovIfPlayerInfront(b, dir);
		}
		return res;
	}

	public Boolean setDir(int d) {
		// input- direction that the player wants to go/move the barrier to
		// output- is the player / the barrier moving
		// the function proceed the move only if its leagal
		boolean res = false;
		if (!_moving) {
			if (_settingABarrier) {
				res = _toPut.setDir(d);
			} else if (canGoTo(Directions.Dirsx[d], Directions.Dirsy[d], d)) {
				mov(d);
				res = true;
			}
		}
		return res;
	}

	private void mov(int d) {
		// input- direction to move to
		// the function proceed the move even if its ileagal
		// the function change the player info according to the move
		int x = Directions.Dirsx[d], y = Directions.Dirsy[d];

		_dx = _speed * x;
		_dy = _speed * y;
		_MovingXToGo = x * (Directions.block + Barrier.thickness) * _steps;
		_MovingYToGo = y * (Directions.block + Barrier.thickness) * _steps;

		_px = _x + _MovingXToGo;
		_py = _y + _MovingYToGo;
		_MovingXToGo = Math.abs(_MovingXToGo);
		_MovingYToGo = Math.abs(_MovingYToGo);

		_moving = true;
		_board.get_board()[_row][_col].set_player(false);
		_row += y * _steps;
		_col += x * _steps;
		if (_row >= 0 && _col >= 0 && _row < Board._rows && _col < Board._cols)
			_board.get_board()[_row][_col].set_player(true);
	}

	public boolean isWin() {
		// output- is the player in the row/col of their goal
		return _goal.x == _row || _goal.y == _col;
	}

	public void update() {
		// updateing the player position to make it looks like he acctualy moving

		if (_moving) {
			if (_MovingXToGo > 0) {
				_x += _dx;
				_MovingXToGo -= Math.abs(_dx);
			}
			if (_MovingYToGo > 0) {
				_y += _dy;
				_MovingYToGo -= Math.abs(_dy);
			}
			// when the player is finished moving the turn move to the next player
			// he finish moving when he doesnt have anymore x and y to go
			if (_MovingXToGo <= 0 && _MovingYToGo <= 0) {
				_moving = false;
				_dx = _dy = 0;
				_x = _px;
				_y = _py;
				Turns.setCurrAction(1);
				movsCount--;
				if (movsCount == 0)
					_finishedPlay = true;
			}
		}

	}

}
