package Logic;

import Entity.Player;

public class LegalChecks {
	private static Player _players[];// players array
	public static Graph _Graph;// the game graph

	public static void init(Player p[], Graph b) {
		// init
		_players = p;
		_Graph = b;
	}

	public static boolean barrierPositionLegal() {
		// go throh all the players and check if they have a way to reach their goal
		// if not return false so the game want let the last barrier position to be
		// legal
		boolean flag = true;
		for (int i = 0; i < _players.length; i++) {
			if (BFS.bFS(_players[i].get_row(), _players[i].get_col(), _players[i].getGoal(), _Graph) == -1) {
				flag = false;
			}
		}
		return flag;
	}

}
