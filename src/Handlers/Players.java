package Handlers;

import Entity.Player;

public class Players {
	// game players class
	// array of the players
	public static Player _players[];

	public static void init(int num) {
		// input- num of players in the game
		// init the array and players states
		_players = new Player[num];
		for (int i = 0; i < _players.length; i++)
			_players[i] = new Player(i + 1);

	}

	public static void initPlayers() {
		// init players
		for (int i = 0; i < _players.length; i++)
			_players[i].init();
	}

	public static void setAI(int num, boolean flag) {
		// input- a player num and a flag if he is an AI or not
		// the function set him to AI/human according to the flag
		_players[num].setAI(flag);
	}

	public static boolean addOrRemovePlayer(int amount) {
		// input- players amount
		// output- if the players amount is leagal then change the players array size
		// and keep the players data unless we want to remove then the one that we
		// remove
		boolean flag = false;
		if (!(amount > 4 || amount <= 1)) {
			int len = _players.length;
			if (amount < len)
				len = amount;

			Player p[] = new Player[amount];
			for (int i = 0; i <= len - 1; i++)
				p[i] = _players[i];
			for (int i = len; i < amount; i++)
				p[i] = new Player(i + 1);
			_players = p;
			flag = true;
		}
		return flag;
	}

}
