package Logic;

import java.util.ArrayList;
import java.util.Random;
import Board.Board;
import Board.Directions;
import Entity.Barrier;
import Entity.Player;
import Handlers.Players;
import Handlers.Turns;

public class AI {
	// length and counter of how long it seems that the player is thinking
	private static int _AIThink;
	private static Long _AIThinkTime;

	private static boolean _finishedTurn;// did the AI finished their act
	private static boolean _tryedToPutBarrier;// did the AI tryed to put barrier but the position was ileagal
	private static int _lastBestRow[] = new int[5], _lastBestCol[] = new int[5];// last best row and col that were found
																				// in the algorithm
	private static Board _board;// the game board

	private static void decideNextAction() {
		// the function purpose is to make the AI play
		// in a way that he choose the best act he can do in the current state

		Player p = Turns.getCurrTurnPlayer();
		_finishedTurn = p.isFinishedPlay();
		// choose and check the state only if the player isnt moving and havnt finished
		// the act
		if (!(p.is_moving() || _finishedTurn)) {
			int curturn = Turns._currentTurn;
			int arr[] = new int[Turns.numTurns];// array of all players shortest path
			int min = 0;// the index of the player with min path

			// get the len and dir of each player's shortest path to their goal
			// and save the index of the player with the shortest path between all players
			// in min
			for (int i = 0; i < arr.length; i++) {
				arr[i] = getPlayerShortestPathLen(Players._players[i], i);
				if (arr[i] < arr[min])
					min = i;
				else if (arr[i] == arr[min] && (new Random()).nextBoolean()) {
					min = i;
				}
			}

			if (!blockShortestPathPlayer(min, curturn, arr))// if the AI didnt block/couldnt block then make a move
															// to his shortest path
				p.setDir(Directions.fromPointDir(p.get_row(), p.get_col(), _lastBestRow[curturn],
						_lastBestCol[curturn]));
		}

	}

	private static boolean blockShortestPathPlayer(int min, int curturn, int arr[]) {
		// input- the index of the player with the min path, the player who's current
		// turn and an array of the shortest path of each players according to the index
		// output- flag whether the current player found a best spot to block and
		// Proceed to block it
		boolean put = false;
		Random rnd = new Random();
		int ranks[] = new int[Turns.numTurns];
		int enemysSum = 0;
		int mySum = 0;

		// sum all the players state rank
		for (int i = 0; i < ranks.length; i++)
			ranks[i] = shouldPutBarrier(i, arr);
		// set AI rank
		mySum = ranks[curturn];
		// set enemys rank sum
		for (int i = 0; i < ranks.length; i++)
			enemysSum += (i == curturn) ? 0 : ranks[i];

		// if the AI Rank is that high then he must take a move
		boolean should = mySum * Turns.numTurns - enemysSum * 2 < 0;
		// decide whether the player should try to block or not

		if (arr[curturn] > 1 && !_tryedToPutBarrier && (min != curturn) && (should || rnd.nextBoolean())) {
			// try to block the way
			put = bestSpotToPutBarrier(min, curturn, arr);

			// if the AI cant block the way for the player with the shortest path he will
			// search for the next player with the shortest path (who isnt them)
			if (!put) {
				int num = curturn;
				for (int i = 0; i < arr.length; i++)
					if (i != curturn && i != min && arr[i] < arr[num])
						num = i;
				if (num != curturn)
					put = bestSpotToPutBarrier(num, curturn, arr);
			}
		}

		return put;
	}

	private static boolean bestSpotToPutBarrier(int min, int curturn, int[] arr) {
		// input- the index of the player with the min path, the player who's current
		// turn and an array of the shortest path of each players according to the index
		// output- flag whether the current player found a best spot to block and
		// procced to block it

		double risk = 1.5;// how far the player will let the blocking spot impact hit path
		int row = -1, col = -1;
		boolean rotate = false;
		int shortestPath = arr[min];
		int myPath = arr[curturn];

		// horizontal
		for (int i = 1; i <= Board._rows - 1; i++) {
			for (int j = 0; j <= Board._cols - 2; j++) {

				Barrier b = new Barrier(i, j, _board, 1, false);
				if (_board.addBarrier(b)) {
					LegalChecks._Graph.initBarrier(b);
					if (LegalChecks.barrierPositionLegal()) {
						int temp = getPlayerShortestPathLen(Players._players[min], 4);
						int mytemp = getPlayerShortestPathLen(Players._players[curturn], 4);

						// does the barrier delay the other player
						if (temp > shortestPath) {
							// the barrier shouldnt delay the player who put it if it make a big impact
							if (myPath < arr[curturn] * risk) {
								myPath = mytemp;
								shortestPath = temp;
								row = i;
								col = j;
							}
						}

					}
					// remove the barrier that used for the checks
					_board.removeBarrier(b);
					LegalChecks._Graph.initBarrier(b);
				}
			}
		}

		// vertical
		for (int i = 0; i <= Board._rows - 2; i++) {
			for (int j = 1; j <= Board._cols; j++) {
				Barrier b = new Barrier(i, j, _board, 1, true);
				if (_board.addBarrier(b)) {
					LegalChecks._Graph.initBarrier(b);
					if (LegalChecks.barrierPositionLegal()) {
						int temp = getPlayerShortestPathLen(Players._players[min], 4);
						int mytemp = getPlayerShortestPathLen(Players._players[curturn], 4);

						// does the barrier delay the other player
						if (temp > shortestPath) {
							// the barrier shouldnt delay the player who put it if it make a big impact
							if (myPath < arr[curturn] * risk) {
								myPath = mytemp;
								shortestPath = temp;
								row = i;
								col = j;
								rotate = true;
							}
						}

					}
					// remove the barrier that used for the checks
					_board.removeBarrier(b);
					LegalChecks._Graph.initBarrier(b);
				}
			}
		}
		boolean res = false;
		if (row != -1 && col != -1)// if a best position was
			res = Players._players[curturn].putBarrierIn(row, col, rotate);

		return res;
	}

	private static int shouldPutBarrier(int playerNum, int[] arr) {
		// input the player num and array of len
		// output- the state rank of the player
		int decition = 8100;// 81 is the max steps player might need to take to reach the end (its a very
							// rare and unlikely to ever occur)
		int myBarrierWidth = 3;
		int enemyBarrierWidth = 1;

		// devide the decition by the length to go
		// (cant devide by zero and if it reach zero that mean that the player had won)
		int devide = arr[Turns._currentTurn] != 0 ? arr[Turns._currentTurn] : 1;
		// the shorter the len to the end the higher decision to move
		decition /= devide;

		for (int i = 0; i < arr.length; i++) {
			// the more barriers the player have left the more options to block he will have
			// later
			if (i == playerNum)
				decition += Players._players[playerNum].get_barriersLeft() * myBarrierWidth;
			// the more barriers they have the more the player should try to avoid getting
			// in a bed situation
			// so he might consider to block them instead of move
			else
				decition -= Players._players[i].get_barriersLeft() * enemyBarrierWidth;
		}

		return decition;
	}

	private static int getPlayerShortestPathLen(Player p, int num) {
		// input- a player and his index
		// output- his shortest path to his goal and the row and col he better move to
		ArrayList<Integer> dirs = new ArrayList<Integer>();
		Graph g = LegalChecks._Graph;
		ArrayList<Edge> e = g.getGraph()[p.get_row()][p.get_col()].getAdjacencies();
		// check all of the player's possible neighbors and use BFS from there to reach
		// their goal
		for (int i = 0; i < e.size(); i++) {
			int row = e.get(i).getTarget().getI(), col = e.get(i).getTarget().getJ();
			dirs.add(BFS.bFS(row, col, p.getGoal(), g));
		}
		// get the min len between all the neighbors
		int min = 0;
		for (int i = 0; i < dirs.size(); i++) {
			if (dirs.get(min) == -1 || dirs.get(i) != -1 && dirs.get(i) < dirs.get(min))
				if (e.get(i).getTarget().getI() >= 0 && e.get(i).getTarget().getJ() >= 0
						&& e.get(i).getTarget().getI() < Board._rows && e.get(i).getTarget().getJ() < Board._cols)
					min = i;
		}
		// set the neighbor with the shortest path row and col
		_lastBestRow[num] = e.get(min).getTarget().getI();
		_lastBestCol[num] = e.get(min).getTarget().getJ();
		// return the len of the shortest path
		return dirs.get(min);
	}

	public static void startThink() {
		// init the AI turn
		init();
		startThinking();
	}

	public static void update() {
		// doing the AI act after he had finish "thinking" and didnt finish an act
		if (!_finishedTurn && canDoNextAction())
			decideNextAction();
	}

	public static boolean isFinishedTurn() {
		return _finishedTurn;
	}

	// timer that make it seen that the ai is thinking
	private static boolean canDoNextAction() {
		long elapsed = (System.nanoTime() - _AIThinkTime) / 1000000;
		return (elapsed > _AIThink);
	}

	private static void startThinking() {
		_AIThink = 1000;
		_AIThinkTime = System.nanoTime();
	}

	public static void init() {
		_finishedTurn = false;
		_tryedToPutBarrier = false;
	}

	public static void initBoard(Board b) {
		_board = b;
	}

	public static void tryToPutBarrier() {
		_tryedToPutBarrier = true;
	}

}
