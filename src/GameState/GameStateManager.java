package GameState;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import Graphics.Background;

public class GameStateManager {

	// game states array
	private GameState[] _gameStates;
	// the index of the current state/page
	private int _currentState;
	// load screen image
	public Background _loadScreen;

	// enum of game states
	public static int _statCounter = 0;
	public static final int MENUSTATE = _statCounter++;
	public static final int SETTINGS = _statCounter++;
	public static final int GAMESTATE = _statCounter++;
	public static final int HELPSTATE = _statCounter++;
	public static final int NUMGAMESTATES = _statCounter;

	public GameStateManager() {
		// init the game pages array
		_gameStates = new GameState[NUMGAMESTATES];

		_loadScreen = new Background("/Backgrounds/load.gif");

		_currentState = MENUSTATE;
		loadState(_currentState);

	}

	private void loadState(int state) {
		// input- a page num from the enum
		// outcome- load the page according to the enum
		if (state == MENUSTATE)
			_gameStates[state] = new MenuState(this);
		else if (state == SETTINGS)
			_gameStates[state] = new SettingsState(this);
		else if (state == GAMESTATE)
			_gameStates[state] = new GameBoard(this);
		else if (state == HELPSTATE)
			_gameStates[state] = new HelpState(this);
	}

	private void unloadState(int state) {
		// input- page from the dnum
		// unload the given page
		_gameStates[state] = null;
	}

	public void setState(int state) {
		// input- a page num from the enum
		// outcome- unload the current page and load the given one
		unloadState(_currentState);
		_currentState = state;
		loadState(_currentState);
	}

	public void update() {
		// update the current page
		if (_gameStates[_currentState] != null)
			_gameStates[_currentState].update();
	}

	public void draw(Graphics2D g) {
		// draw the current page
		// if it havent load then draw the loading screen
		if (_gameStates[_currentState] != null)
			_gameStates[_currentState].draw(g);

		else {
			_loadScreen.draw(g);
		}
	}

	public void MouseClicked(MouseEvent e) {
		// handle mouse click in the page
		_gameStates[_currentState].MouseClicked(e);
	}

}
