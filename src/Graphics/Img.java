package Graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import Main.GamePanel;

public class Img {
	//image
	private BufferedImage _image;
	//acord and size
	private int x, y, width, height;

	public Img(String s, int x, int y, int width, int height) {
		//init image to the size and location
		try {
			_image = ImageIO.read(getClass().getResourceAsStream(s));
		} catch (Exception e) {
			e.printStackTrace();
		}
		setImgCords(x, y);
		setImgSize(width, height);
	}

	public Img(BufferedImage img, int x, int y, int width, int height) {
		//init image size and location
		_image = img;
		setImgCords(x, y);
		setImgSize(width, height);
	}

	public void drawImg(Graphics g) {
		//Drawing the image according to its info
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(_image, x, y, width, height, null);
	}

	public void drawImg(Graphics g, int x1, int y1) {
		//Drawing the image in given location
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(_image, x1, y1, width, height, null);
	}

	public void setImgCords(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setImgSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void setImg(BufferedImage image) {
		_image = image;
	}

	public void setImg(String s) {
		try {
			_image = ImageIO.read(getClass().getResourceAsStream(s));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getImage() {
		return _image;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isClicked(MouseEvent e) {
		int ex = e.getX() / GamePanel.SCALE, ey = e.getY() / GamePanel.SCALE;
		return this.x <= ex && this.x + this.width >= ex && this.y <= ey && this.y + this.height >= ey;
	}
}