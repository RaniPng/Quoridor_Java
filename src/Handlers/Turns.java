
package Handlers;

import Entity.Player;
import Logic.AI;

public class Turns {
	public static int _currentTurn=-1;//-1 means that the game is done
	public static int numTurns;//num of turns
	private static Player _currentPlayer;//the current player
	public static int _curAction=0;//0- none, 1-moved, 2-barrier
	
	public static int getNextTurn() {
		//get the index of the player in the next turn
		return (_currentTurn+1<numTurns)?_currentTurn+1:0;
	}
	public static int getPrevTurn() {
		//get the index of the player in the previous turn
		return (_currentTurn-1>=0)?_currentTurn-1:numTurns-1;
	}
	
	public static void setCurrAction(int n) {
		//input- number of the last action that were take
		_curAction=n;
	}
	
	public static void nextTurn() {
		//set data to the next turn
		_curAction=0;
		_currentTurn=getNextTurn();
		_currentPlayer=Players._players[_currentTurn];
		_currentPlayer.startPlay();//start player turn
		AI.startThink();//init AI
	}
	public static void init() {
		_currentTurn=-1;
		_currentPlayer = null;
	}
	public static void setTurns(int _numOfPlayers) {
		numTurns=_numOfPlayers;	
	}
	public static Player getCurrTurnPlayer() {
		return _currentPlayer;
	}
	
}
