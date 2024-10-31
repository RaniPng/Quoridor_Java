package Main;

import javax.swing.JPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;

import Handlers.Keys;

import GameState.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

	// dimensions
	public static int WIDTH = 1;
	public static int HEIGHT = 1;
	public static final int SCALE = 2;// make images bigger

	// game thread
	private Thread _thread;
	private boolean _running;
	private int _FPS = 60;
	private long _targetTime = 1000 / _FPS;

	// image
	private BufferedImage _image;
	private Graphics2D _g;

	// game state manager
	private GameStateManager _gsm;

	public GamePanel(int w, int h) {
		super();
		WIDTH = w / SCALE;
		HEIGHT = h / SCALE;
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}

	public void addNotify() {
		// init threat and listeners
		super.addNotify();
		if (_thread == null) {
			_thread = new Thread(this);
			addKeyListener(this);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					MouseClicked(e);
				}
			});
			_thread.start();
		}

	}

	private void init() {
		// in my game I have a global Graphics variable that I use it to draw the pages
		// on the screen
		// the use of it help to define which page is the one that should draw
		// it helps and more comfortable for me to work like it
		// so every frame the current page is drawn to the graphics of the global g
		// and then the g graphics is drawn to the screen
		// it also can benefit when changing the resolution
		// or to choose different color approach
		// which in my case its a basic RGB
		_image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		_g = (Graphics2D) _image.getGraphics();
		_running = true;
		_gsm = new GameStateManager();
	}

	public void run() {

		init();

		long start;
		long elapsed;
		long wait;

		// game loop
		while (_running) {

			start = System.nanoTime();

			// the threat should always update the game draw the page to the global g and
			// then to the screen
			update();
			draw();
			drawToScreen();

			elapsed = System.nanoTime() - start;

			wait = _targetTime - elapsed / 1000000;
			if (wait < 0)
				wait = 5;

			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void update() {
		// update the page/state manager and the keys
		_gsm.update();
		Keys.update();
	}

	private void draw() {
		// draw to the global g
		_gsm.draw(_g);
	}

	private void drawToScreen() {
		// draw to the screen in the size of the screen
		Graphics g2 = getGraphics();
		g2.drawImage(_image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}

	public void keyTyped(KeyEvent key) {
	}

	public void keyPressed(KeyEvent key) {
		// set key pressed
		Keys.keySet(key.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent key) {
		// set key released
		Keys.keySet(key.getKeyCode(), false);
	}

	public void MouseClicked(MouseEvent e) {
		// set mouse clicked
		_gsm.MouseClicked(e);
	}

}