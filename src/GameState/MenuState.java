package GameState;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import Graphics.Background;
import Handlers.Animation;
import Handlers.Keys;
import Main.GamePanel;

public class MenuState extends GameState {

	private Background _bg;// the menu background
	private BufferedImage _button;// a button image
	private BufferedImage _selcted;// selected button image
	private Animation _glow;// glow effect/ animation
	private BufferedImage _recB;// rectangle button

	private int _currentChoice = 0;// the current button choice
	private String[] _options = { "Start", "Help", "Quit" };// buttons strings

	private Font _font;

	public MenuState(GameStateManager gsm) {
		// input- the main page manger

		this._gsm = gsm;
		init();

	}

	public void init() {
		// init all the variables
		try {
			_bg = new Background("/Backgrounds/menu.png");
			_button = ImageIO.read(getClass().getResourceAsStream("/buttons/Title/btn.png"));
			_selcted = ImageIO.read(getClass().getResourceAsStream("/buttons/Title/Selected.png"));

			_recB = ImageIO.read(getClass().getResourceAsStream("/buttons/Title/recB.png"));
			_font = new Font("Arial", Font.PLAIN, 12);

			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/buttons/Title/glow.png"));
			BufferedImage glow[] = new BufferedImage[8];
			for (int i = 0; i < glow.length; i++)
				glow[i] = spritesheet.getSubimage(i * 50, 0, 50, 50);

			_glow = new Animation();
			_glow.setDelay(150);
			_glow.setFrames(glow);
			_glow.set_revloop(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
		_glow.update();// update the glow animation
		handleInput();// check keys
	}

	public void draw(Graphics2D g) {
		// draw the page
		// draw bg
		_bg.draw(g);

		// draw menu options
		g.setFont(_font);
		g.setColor(Color.BLACK);
		int x = 10, y = GamePanel.HEIGHT / 2;

		for (int i = 0; i < _options.length; i++) {
			g.drawImage(_recB, x, y + 30 * i, 20, 20, null);

			if (i == _currentChoice) {
				g.drawImage(_selcted, x, y + 30 * i, GamePanel.WIDTH - x, 20, null);
				g.drawImage(_glow.getImage(), x, y + 30 * i, 20, 20, null);
			} else {
				g.drawImage(_button, x, y + 30 * i, GamePanel.WIDTH - x, 20, null);
			}
			g.drawString(_options[i], x + 30, y + 15 + 30 * i);
		}

	}

	private void select() {
		// start settings page
		if (_currentChoice == 0) {
			_gsm.setState(GameStateManager.SETTINGS);
		}
		if (_currentChoice == 1) {
			// help page
			_gsm.setState(GameStateManager.HELPSTATE);
		}
		// exit game
		if (_currentChoice == 2) {
			System.exit(0);
		}
	}

	@Override
	public void handleInput() {
		//init animation
		Boolean glowreset = false;

		//pressed enter so need to make a selection
		if (Keys.isPressed(Keys.ENTER))
			select();

		//swapping to the next option
		if (Keys.isPressed(Keys.UP)) {
			if (_currentChoice > 0)
				_currentChoice--;

			else
				_currentChoice = _options.length - 1;
			glowreset = true;
		}
		if (Keys.isPressed(Keys.DOWN)) {
			if (_currentChoice < _options.length - 1)
				_currentChoice++;
			else
				_currentChoice = 0;
			glowreset = true;
		}
		//rest animation
		if (glowreset)
			_glow.init();
	}

	@Override
	public void MouseClicked(MouseEvent e) {
		//none
	}

}
