package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.Random;
import Graphics.Background;
import Graphics.Graphics;
import Graphics.Img;
import Graphics.Sprites;
import Handlers.ColorPicker;
import Handlers.Keys;
import Handlers.Players;
import Handlers.Turns;
import Logic.AI;
import Main.GamePanel;

public class SettingsState extends GameState {
	// images of all the optional players\sprites to play as
	private Img _sprites[][];
	private ColorPicker _cp;// tool for choosing a color
	// imgs size
	private final int imgBoxWidth = 30;
	private final int imgBoxHeight = 51;
	// current row and col of player
	private int _currentRow = 0;
	private int _currentCol = 0;
	// current players in the game
	private int _numOfPlayers = 2;
	// the player who we set their settings for
	private int _currPlayerSettings = 1;
	private boolean canStart = false;// if we set the settings for all the player we can start the game
	// images and graphics
	private Font _font;
	private Font _bigFont;
	private Img _exitButton;
	private Img _AddRemovePlayer[];
	private Img _AI[];
	private Img _red;
	private Img _black;
	private Img _changeColor;
	private Background _bg;

	public SettingsState(GameStateManager gsm) {
		this._gsm = gsm;
		init();
	}

	@Override
	public void init() {
		// init background and buttons
		_bg = new Background("/Backgrounds/sttings.jpg");
		_sprites = Sprites.load("/Sprites/sprites.png", imgBoxWidth, imgBoxHeight, 100, 5);
		_red = new Img("/Backgrounds/red.png", 0, 0, 30, 45);
		_black = new Img("/Backgrounds/black.png", 0, 0, 30, 45);
		_changeColor = new Img("/buttons/settings/color.png", GamePanel.WIDTH - 130, 20, 90, 13);
		_exitButton = new Img("/buttons/settings/exit.png", GamePanel.WIDTH - 100, GamePanel.HEIGHT - 60, 90, 13);

		// init all the handlers that we will use during the game and set their options
		// in this page
		Turns.init();
		AI.init();
		Players.init(_numOfPlayers);
		Graphics.setPlayers();
		_font = new Font("Arial", Font.PLAIN, 10);
		_bigFont = new Font("Arial", Font.BOLD, 100);

		_AddRemovePlayer = new Img[2];
		_AddRemovePlayer[0] = new Img("/buttons/settings/Plus.png", 0, 0, 35, 26);
		_AddRemovePlayer[1] = new Img("/buttons/settings/Minus.png", 0, 0, 35, 26);
		_AI = new Img[4];
		_AI[0] = new Img("/buttons/settings/Player.png", 0, 0, 73, 13);// player on set to Human at start
																		// (can be changed)
		_AI[1] = new Img("/buttons/settings/CPU.png", 0, 0, 73, 13);
		_AI[2] = new Img("/buttons/settings/CPU.png", 0, 0, 73, 13);
		_AI[3] = new Img("/buttons/settings/CPU.png", 0, 0, 73, 13);
		_cp = new ColorPicker();
		randomColor();// set random color at start

		// player 1 is init to be human player
		changePlayerCPU(0);
	}

	@Override
	public void update() {
		handleInput();
	}

	@Override
	public void draw(Graphics2D g) {
		_bg.draw(g);
		Font currentFont = g.getFont();

		g.setFont(_font);
		_changeColor.drawImg(g);
		_exitButton.drawImg(g);

		int x = 100, y;
		g.setColor(Color.black);
		// drawing the sprites and a box behind them
		// (red = current row and col, black = other)
		for (int i = 0; i < _sprites[0].length - 1; i++) {
			y = 5;
			for (int j = 0; j < _sprites.length; j++) {
				if (!canStart && j == _currentRow && i == _currentCol)
					_red.drawImg(g, x, y);
				else
					_black.drawImg(g, x, y);
				_sprites[j][i].drawImg(g);
				y += 50;
			}
			x += 50;
		}

		// draw the level row bar
		int height = 500;
		int point = (int) (height / (_sprites.length - 1)) * _currentRow;
		point = (point == 0) ? 5 : point;
		g.fillRect(40, 5, 30, height);
		g.setColor(Color.red);
		g.fillRect(40, point, 30, 10);

		// settings
		x += 390;
		y = 50;
		// draw the choosed sprites of the players
		for (int i = 0; i < _numOfPlayers; i++) {
			y += 50;
			_black.drawImg(g, x, y);
			// draw the current row col sprite and mark the current player who choosing his
			// player
			if (i + 1 == _currPlayerSettings) {
				_red.drawImg(g, x, y);
				_sprites[_currentRow][_currentCol].drawImg(g, x, y);
			}
			g.drawImage(Graphics.player[i], x, y, null);
			g.drawString("Player: " + String.valueOf(i + 1).toString(), x + 40, y + 22);
			_AI[i].setImgCords(x - 90, y);
			_AI[i].drawImg(g);
		}
		// draw the minus button only if there are more than the minimum players
		if (_numOfPlayers != 2) {
			_AddRemovePlayer[1].setImgCords(x + 50, y + 60);
			_AddRemovePlayer[1].drawImg(g);
		}
		// draw the plus button only if there are less than the maximum players
		if (_numOfPlayers != 4) {
			_AddRemovePlayer[0].setImgCords(x, y + 60);
			_AddRemovePlayer[0].drawImg(g);
		}

		// start UI
		if (canStart) {
			g.setFont(_bigFont);
			int w = 300;
			int h = 100;
			x = GamePanel.WIDTH / 2 - w / 2;
			y = GamePanel.HEIGHT / 2 - h;
			g.setColor(Color.red);
			g.fillRoundRect(x, y, w, h, 30, 30);
			g.setColor(Color.white);
			g.drawString("Start?", x + 10, y + 80);
		}
		// back the the default font
		g.setFont(currentFont);
	}

	private void select(boolean exit) {
		// input- boolean that flag is the selection was to cancel
		// outcome- set player settings or cancel them according to exit
		// if the first player didnt set the screen set back to the menu

		Random rnd = new Random();
		// start choose sprite
		// canel options
		if (exit) {

			// get back to menu
			if (_currPlayerSettings == 1)
				_gsm.setState(GameStateManager.MENUSTATE);

			else {
				// set the previus player settings
				_currPlayerSettings -= 1;
				Graphics.setSprite(null, _currPlayerSettings);
				canStart = false;
			}
		} // enter opptions
		else if (canStart) {
			// if the game can start then set the numer of turns and move to the board panel
			Turns.setTurns(_numOfPlayers);
			_gsm.setState(GameStateManager.GAMESTATE);
		} else {
			// set the current player's settings
			// set their sprite
			Graphics.setSprite(Sprites.cutBufferedImage(_sprites[_currentRow][_currentCol].getImage()),
					_currPlayerSettings);
			// move to the next player to set their settings
			_currPlayerSettings++;
			// random color
			randomColor();
			// random position
			int scrolls = rnd.nextInt(8) * 2;
			int movenext = rnd.nextInt(3) * 2;
			for (int i = 0; i < scrolls; i++)
				scroll(1);
			for (int i = 0; i < movenext; i++)
				movNext(1);
			// if the maximum of player that set to play in the game settings were set then
			// that means we can start the game now
			if (_currPlayerSettings > _numOfPlayers)
				canStart = true;
		}
	}

	public void ChangeColor() {
		// open a tool kit that let the player choose their sprite color
		Color c = _cp.ColorChoose();
		Graphics.setColor(c, _currPlayerSettings);// set the player color in the graphics
		// load the sprites again so the color will be easyer to change
		_sprites = Sprites.load("/Sprites/sprites.png", imgBoxWidth, imgBoxHeight, 100, 5);
		// set the choosed color to all the sprites
		for (int i = 0; i < _sprites[0].length - 1; i++) {
			for (int j = 0; j < _sprites.length; j++) {
				_sprites[j][i].setImg(Sprites.setImgColor(c, _sprites[j][i].getImage()));
			}
		}
	}

	public void randomColor() {
		// set random color to the sprites
		Color c = Sprites.randomColor();// get a random color
		Graphics.setColor(c, _currPlayerSettings);// set the player color in the graphics
		// load the sprites again so the color will be easyer to change
		_sprites = Sprites.load("/Sprites/sprites.png", imgBoxWidth, imgBoxHeight, 100, 5);
		// set the choosed color to all the sprites
		for (int i = 0; i < _sprites[0].length - 1; i++) {
			for (int j = 0; j < _sprites.length; j++) {
				_sprites[j][i].setImg(Sprites.setImgColor(c, _sprites[j][i].getImage()));
			}
		}
	}

	public void changePlayerCPU(int n) {
		// input- number of player that we want to set as an AI
		Players._players[n].setAI(!Players._players[n].isAI());
		// change the picture
		if (Players._players[n].isAI())
			_AI[n].setImg("/buttons/settings/CPU.png");
		else
			_AI[n].setImg("/buttons/settings/Player.png");
	}

	public void changePlayerAmount(int n) {
		// input- 1 or 0, 1= add 0=remove;
		int amount = _numOfPlayers;
		if (n == 0) {
			amount++;
			canStart = false;
		} else {
			amount--;
			if (_currPlayerSettings >= _numOfPlayers)
				_currPlayerSettings -= 1;
		}
		// add or remove in the player class a player
		if (Players.addOrRemovePlayer(amount)) {
			_numOfPlayers = amount;
			Graphics.addOrRemovePlayer(amount);
		}
	}

	@Override
	public void MouseClicked(MouseEvent e) {
		// check the input
		// AI change
		for (int i = 0; i < _numOfPlayers; i++) {
			if (_AI[i].isClicked(e)) {
				changePlayerCPU(i);
			}
		}
		// add or remove player
		for (int i = 0; i < _AddRemovePlayer.length; i++) {
			if (_AddRemovePlayer[i].isClicked(e)) {
				changePlayerAmount(i);
			}
		}
		// color changer
		if (_changeColor.isClicked(e))
			ChangeColor();
		else if (_exitButton.isClicked(e))
			_gsm.setState(_gsm.MENUSTATE);// exit to menu
		else {
			// pressed on sprite
			// if so then set the current row col to the sprite
			for (int i = 0; i < _sprites[0].length - 1; i++)
				for (int j = 0; j < _sprites.length; j++)
					if (_sprites[j][i].isClicked(e)) {
						if (_currentRow == j && _currentCol == i) {
							select(false);
						} else {
							_currentRow = j;
							_currentCol = i;
						}
					}

		}

	}

	private void scroll(int goPlusMin) {
		//input- direction of the row to go up=-1 down=1
		//init to the end or the start if the row get out of range
		_currentRow += goPlusMin;
		// up
		if (_currentRow < 0) {
			_currentRow = _sprites.length + _currentRow;
		}
		// down
		if (_currentRow > _sprites.length - 1) {
			_currentRow = _currentRow - _sprites.length;
		}
	}

	private void movNext(int num) {
		//input- direction of the col to go up=-1 down=1
		//init to the end or the start if the col get out of range
		_currentCol += num;
		if (_currentCol < 0)
			_currentCol = _sprites[0].length - 2;
		if (_currentCol >= _sprites[0].length - 1)
			_currentCol = 0;
	}

	@Override
	public void handleInput() {
		//handel input
		if (Keys.isPressed(Keys.ENTER))
			select(false);
		if (Keys.isPressed(Keys.ESCAPE))
			select(true);

		if (!canStart) {
			if (Keys.isPressed(Keys.UP)) {
				scroll(-1);
			}
			if (Keys.isPressed(Keys.DOWN)) {
				scroll(1);
			}
			if (Keys.isPressed(Keys.LEFT)) {
				movNext(-1);
			}
			if (Keys.isPressed(Keys.RIGHT)) {
				movNext(1);
			}
		}

	}

}
