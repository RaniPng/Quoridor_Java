package Graphics;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class PopMessage {
	// pop message that show on screen
	// the picture
	private Img _pic;
	// accords
	private int _x;
	private int _y;
	// max len/distanse of moving to go
	private int _maxY;
	private int _maxX;
	// vectors of direction
	// Default speed is 3
	private int _dx = 3;
	private int _dy = 3;
	// size
	private int _width;
	private int _hight;
	// when finishing to move
	private boolean _isFinishMoving;

	public PopMessage(String path, int x, int y, int maxX, int maxY, int xdir, int ydir) {
		// input- info of the message
		// output- create
		_maxX = maxX;
		_maxY = maxY;
		_x = x;
		_y = y;
		_dx *= xdir;
		_dy *= ydir;
		_pic = new Img(path, _x, _y, 1, 1);
		_hight = _pic.getImage().getHeight();
		_width = _pic.getImage().getWidth();
		_pic.setImgSize(_width / 2, _hight / 2);
		_isFinishMoving = false;
	}

	public void draw(Graphics g) {
		// draw the image
		_pic.drawImg(g);
	}

	public void update() {
		// update the image to move if it havent finished
		if (!_isFinishMoving) {
			//more x to go
			if (_maxX > 0) {
				_maxX--;
				_x += _dx;
				_pic.setImgCords(_x, _y);
			}
			//more y to go
			if (_maxY > 0) {
				_maxY--;
				_y += _dy;
				_pic.setImgCords(_x, _y);
			}
			//finished
			if (_maxX == 0 && _maxY == 0)
				_isFinishMoving = true;
		}

	}

	public boolean is_isFinishMoving() {
		return _isFinishMoving;
	}

	public void set_pic(String path) {
		this._pic.setImg(path);

	}

	public boolean isClicked(MouseEvent e) {
		return _pic.isClicked(e);
	}

}
