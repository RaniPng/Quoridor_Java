package Logic;

import java.awt.Point;
import java.util.ArrayList;


public class BFS {
	//is there away to reach the goal/ did it reach it
	private static boolean _reachEnd;
	//visited cells matrix
	private static boolean _visited[][];
	//how many move/ laps the algorithm took
	private static int movCount;
	//how many nodes/cells are currently in progres to reach the end in the current move/step/lap
	private static int nodesLeftInLayer;
	//how many nodes we can check in the next lap/step/mov
	private static int nodesInNextLayer;

	//init all the parameters for a new search, then call the actual BFS algorithm
	public static int bFS(int row,int col,Point goal, Graph g) {
		movCount = 0;
		nodesLeftInLayer = 1;//current point
		nodesInNextLayer = 0;
		_reachEnd = false;
		_visited = new boolean[g.get_rows()][g.get_cols()];
		//init visited matrix cells to false(we havent visited any cell yet)
		for (int i = 0; i < _visited.length; i++) {
			for (int j = 0; j < _visited.length; j++) {
				_visited[i][j] = false;
			}
		}
		//call the actual BFS 
		boolean flag = solve(row, col,goal, g);
		//if the algorithm found way to reach the end, return how many steps it took
		if (flag)
			return movCount;
		//if it didnt find a way then return -1
		return -1;
	}

	private static boolean solve(int row, int col,Point goal, Graph g) {
		// remove 0 and add last = Queue
		ArrayList<Integer> rq = new ArrayList<Integer>();//rows Queue
		ArrayList<Integer> cq = new ArrayList<Integer>();//cols Queue
		//add current point
		rq.add(row);
		cq.add(col);
		//mark this point as visited
		_visited[row][col] = true;
		
		//while there arent any points left and we havent reach the goal
		while (!_reachEnd&& rq.size() > 0 &&  cq.size() > 0) {
			//check the node in the current layer
			row = rq.remove(0);
			col = cq.remove(0);
			//if it acctualy the goal than exit with true
			if (row == goal.x||col==goal.y) {
				_reachEnd=true;
				return true;
			}
			//check what node's neighbours we can add to the next layer
			explore_neighbours(row, col, rq, cq, g);
			//moving to the next node in this layer
			nodesLeftInLayer--;
			//if the layer ended we need to check their childern
			if (nodesLeftInLayer == 0) {
				//the next layer turning into the current layer
				nodesLeftInLayer = nodesInNextLayer;
				//init next layer counter
				nodesInNextLayer = 0;
				//increase progres move
				movCount++;
			}

		}
		//if the algorithm reach this point it means it didnt find a way to reach the goal
		return _reachEnd;
	}

	//a function that adds the nodes the algorithm can visit in the next layer
	private static void explore_neighbours(int row, int col, ArrayList<Integer> rq, ArrayList<Integer> cq, Graph g) {
		ArrayList<Edge> e = g.getGraph()[row][col].getAdjacencies();//array of edges/cells that the player can go to from the row col- cell
		for (int i = 0; i < e.size(); i++) {
			//get the position of the neighbour
			int rr = e.get(i).getTarget().getI();
			int cc = e.get(i).getTarget().getJ();
			//if we visited the node then we shouldnt add it to the next layer again (we already treated this node)
			if (_visited[rr][cc])
				continue;
			//if we didnt visited the node then add its point
			rq.add(rr);
			cq.add(cc);
			//mark as visited
			_visited[rr][cc] = true;
			//the nodes in the next layer have been increased
			nodesInNextLayer++;
		}
	}

}
