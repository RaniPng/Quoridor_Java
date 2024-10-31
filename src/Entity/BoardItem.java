package Entity;

import Board.Board;

public abstract class BoardItem {
	// a class of a board item
	// contains basic variables that a board item should have
	protected Board _board;// the board that the item is in
	protected int _col;
	protected int _row;
	protected int _x;
	protected int _y;

	public void setPosition(int x, int y) {
		_x = x;
		_y = y;
	}

	public void setRowCol(int row, int col) {
		this._row = row;
		this._col = col;
	}

	public int get_col() {
		return _col;
	}

	public int get_row() {
		return _row;
	}

	public int get_x() {
		return _x;
	}

	public int get_y() {
		return _y;
	}

}
