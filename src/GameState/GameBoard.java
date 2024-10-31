package GameState;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.Random;
import Board.Board;
import Board.Directions;
import Entity.Barrier;
import Graphics.Background;
import Graphics.Graphics;
import Graphics.Img;
import Graphics.PopMessage;
import Handlers.Keys;
import Handlers.Players;
import Handlers.Turns;
import Logic.AI;
import Logic.Graph;
import Logic.LegalChecks;
import Main.GamePanel;

public class GameBoard extends GameState {
	private Background _bg;// board background
	private Board _board;// the actual game board
	private Graph _graph;// the graph of the board
	private int _playerWon = 0;// 0 = havent win yet , other= the winner
	private Img _exitButton;// button to exit the game
	private boolean pause = false;// pause

	// pop messages
	private PopMessage _restart;
	private PopMessage _changeSettings;
	private PopMessage _playerWonMessage;
	private PopMessage _playerWonNumerMessage;// init to null then a player win the image change
	private PopMessage _wonMessage;

	public GameBoard(GameStateManager gsm) {
		this._gsm = gsm;
		init();
	}

	@Override
	public void init() {

		// background
		_bg = new Background("/Backgrounds/board.png");

		// winning buttons and UI
		_changeSettings = new PopMessage("/buttons/Win/Back.png", 0 - 258, GamePanel.HEIGHT / 2 + 90, 220, 0, 1, 0);
		_restart = new PopMessage("/buttons/Win/Restart.png", GamePanel.WIDTH, GamePanel.HEIGHT / 8 + 120, 250, 0, -1,
				0);

		_playerWonMessage = new PopMessage("/buttons/Win/PlayerWon.png", -200, GamePanel.HEIGHT / 10,
				GamePanel.WIDTH / 45, 0, 5, 0);
		_playerWonNumerMessage = null;
		_wonMessage = new PopMessage("/buttons/Win/Won.png", GamePanel.WIDTH - 320, 0 - 360, 0, GamePanel.HEIGHT / 9, 0,
				2);

		// init board and graph
		_board = new Board();
		_graph = new Graph(_board);
		AI.initBoard(_board);

		// init players location
		for (int i = 0; i < Players._players.length; i++)
			Players._players[i].setBoard(_board);
		setPlayersPositions();

		LegalChecks.init(Players._players, _graph);
		_exitButton = new Img("/buttons/settings/exit.png", GamePanel.WIDTH - 100, GamePanel.HEIGHT - 90, 90, 13);
		_playerWon = 0;
		// current turn set -1 in order to start need to set it to next turn (0)
		nextTurn();
	}

	public void setPlayersPositions() {
		// init the play positions to their start and their goal to the opposite side
		int x1 = 0, y1 = _board._cols / 2, x2 = _board._rows - 1, y2 = _board._cols / 2, x3 = _board._rows / 2, y3 = 0,
				x4 = _board._rows / 2, y4 = _board._cols - 1;

		// -1 mean the whole col or row
		_board.setPlayerPosition(Players._players[0], x1, y1);
		Players._players[0].setGoal(x2, -1);
		_board.setPlayerPosition(Players._players[1], x2, y2);
		Players._players[1].setGoal(x1, -1);
		if (Turns.numTurns >= 3) {
			_board.setPlayerPosition(Players._players[2], x3, y3);
			Players._players[2].setGoal(-1, y4);
		}
		if (Turns.numTurns >= 4) {
			_board.setPlayerPosition(Players._players[3], x4, y4);
			Players._players[3].setGoal(-1, y3);
		}
	}

	@Override
	public void update() {

		// pause
		if (Keys.isPressed(Keys.ESCAPE))
			pause = !pause;
		if (pause)
			return;

		// handle game management if the game havent finish
		if (_playerWon == 0) {
			// can play when the previous player finished playing
			if (Players._players[Turns.getPrevTurn()].isFinishedPlay()) {

				// if the current palyer isnt ai so the game is waiting for the player input
				// and checking if the player finish his turn
				if (!Turns.getCurrTurnPlayer().isAI()) {
					if (Turns.getCurrTurnPlayer().isFinishedPlay())
						nextTurn();
					// if the player isnt moving he should input the move
					else if (!Turns.getCurrTurnPlayer().is_moving())
						handleInput();
				}
				// AI turn
				else {
					// update the AI
					AI.update();
					if (AI.isFinishedTurn())
						nextTurn();
				}
			}
		}

		// update all players
		for (int i = 0; i < Players._players.length; i++) {
			Players._players[i].update();
			// checking if the player is won
			if (Players._players[i].isFinishedPlay() && Players._players[i].isWin()) {
				Won(i);
			}
		}
	}

	private void Won(int i) {
		// input- the winning player index
		// outcome- pop message of the winning player and show the popping messages
		_playerWon = i + 1;

		if (_playerWonNumerMessage == null)
			_playerWonNumerMessage = new PopMessage(Graphics.numbers[i], GamePanel.WIDTH + 3, GamePanel.HEIGHT / 10,
					GamePanel.WIDTH / 33, 0, -5, 0);

		_playerWonMessage.update();
		if (_playerWonMessage.is_isFinishMoving())
			_playerWonNumerMessage.update();

		if (_playerWonNumerMessage.is_isFinishMoving())
			_wonMessage.update();

		if (_wonMessage.is_isFinishMoving()) {
			_changeSettings.update();
			_restart.update();
		}

	}

	@Override
	public void draw(Graphics2D g) {
		// input- Graphics variable
		// outcome- the function is drawing the whole game

		// background
		_bg.draw(g);
		// board
		Graphics.drawBoard(g, _board.get_board().length, _board.get_board()[0].length);

		// ui
		if (Turns._currentTurn != -1)
			Graphics.drawCurrPlayerTurn(g);
		_exitButton.drawImg(g);

		// players draws
		int len = Players._players.length;
		for (int i = 0; i < len; i++) {
			// barrier if player is bulding
			if (Players._players[i].is_settingABarrier())
				Graphics.drawBarierr(g, Players._players[i].get_toPut().get_x(),
						Players._players[i].get_toPut().get_y(), Players._players[i].get_toPut().getWidth(),
						Players._players[i].get_toPut().getHeight(), i + 1);
			// player
			Graphics.drawPlayer(Players._players[i].get_x(), Players._players[i].get_y(), i, g);
		}

		// draw the barrier stations
		for (int i = 0; i < 2; i++) {
			Graphics.drawPlayerBarriers(g, Players._players[i].get_barriersLeft(),
					Players._players[i].get_barrierStation(), 20, 0, Barrier.thickness, Barrier.placeHolder, i + 1);
		}
		for (int i = 2; i < len; i++) {
			Graphics.drawPlayerBarriers(g, Players._players[i].get_barriersLeft(),
					Players._players[i].get_barrierStation(), 0, 20, Barrier.placeHolder, Barrier.thickness, i + 1);
		}

		// Barriers on the board
		Graphics.drawBarriers(_board.get_obstacles(), g);
		Graphics.drawCurrPlayerUI(g);

		// player won
		if (_playerWon != 0) {
			_playerWonMessage.draw(g);
			_wonMessage.draw(g);
			if (_playerWonNumerMessage != null)
				_playerWonNumerMessage.draw(g);

			_changeSettings.draw(g);
			_restart.draw(g);
		}

	}

	public boolean handleLogic() {
		// output- is the previous act was legal
		boolean legal = true;
		if (Turns._curAction == 2) {
			// check if the last barrier that was placed position's is legal
			Barrier b = _board.get_obstacles().get(_board.get_obstacles().size() - 1);
			_graph.initBarrier(b);// init the graph

			if (!LegalChecks.barrierPositionLegal()) {
				// the act was ileagal
				// need to take of the barrier and "give it back to the player"
				_board.removeBarrier(b);
				_graph.initBarrier(b);
				Turns.getCurrTurnPlayer().addBarrierLeft();
				Turns.getCurrTurnPlayer().startPlay();// init the player turn
				AI.init();// init the AI and let it know that a barrier was try to be puted
				AI.tryToPutBarrier();
				legal = false;
			}
		}
		return legal;
	}

	public boolean nextTurn() {
		// set to the next turn only if the last action was legal
		// output- did the turn moved to the next player
		boolean res = false;
		if (handleLogic()) {
			Turns.nextTurn();
			res = true;
		}
		return res;
	}

	@Override
	public void handleInput() {

		// if player trying to put barrier so the game wait untill he press enter and
		// puts it
		// else the game check if the player pressed any key

		int cur = Turns._currentTurn;

		// change to barrier mode/move mode
		if (Keys.isPressed(Keys.BB))
			Players._players[cur].setSettingABarrier((new Random()).nextBoolean());
		// rotate the barrier
		if (Keys.isPressed(Keys.RB))
			Players._players[cur].rotateBarrier();

		if (Keys.isPressed(Keys.ENTER))
			Players._players[cur].puttingBarrier();

		// move player/barrier
		if (Keys.isPressed(Keys.UP))
			Players._players[cur].setDir(Directions.UP);

		if (Keys.isPressed(Keys.RIGHT))
			Players._players[cur].setDir(Directions.RIGHT);

		if (Keys.isPressed(Keys.DOWN))
			Players._players[cur].setDir(Directions.DOWN);

		if (Keys.isPressed(Keys.LEFT))
			Players._players[cur].setDir(Directions.LEFT);

	}

	@Override
	public void MouseClicked(MouseEvent e) {
		// is clicked the exit button then exit
		if (_exitButton.isClicked(e))
			_gsm.setState(_gsm.MENUSTATE);

		// if the player won and clicked on one of the pop messages then
		if (_playerWon != 0) {
			// restart the game
			if (_restart.isClicked(e)) {
				Players.initPlayers();
				AI.init();
				Turns.init();
				init();

			}
			// exit back to settings
			if (_changeSettings.isClicked(e))
				_gsm.setState(_gsm.SETTINGS);

		}

	}

}
