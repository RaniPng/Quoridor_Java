package Logic;

import java.util.ArrayList;
import Board.Board;
import Board.Directions;
import Entity.Barrier;

public class Graph {

	private Vertex[][] _matVert;// the Dijkstra map
	private int _rows;// the rows
	private int _cols;// the colums
	public static Board _Board;

	public Graph(Board b) {
		_Board = b;
		_rows = b._rows;
		_cols = b._cols;
		_matVert = new Vertex[_rows][_cols];

		for (int i = 0; i < _rows; i++) {
			for (int j = 0; j < _cols; j++) {
				_matVert[i][j] = new Vertex(i, j);
			}
		}
		buildGraph();
	}
	
	public int get_rows() {
		return _rows;
	}

	public int get_cols() {
		return _cols;
	}

	public Vertex[][] getGraph() {
		//PrintGraphics();
		return _matVert;
	}


	public void buildGraph() {
		//init all the vertexes and edges in the graph 
		for (int i = 0; i < _rows; i++) {
			for (int j = 0; j < _cols; j++) {
				_matVert[i][j].getAdjacencies().clear();
				ArrayList<Edge> adjacencies = new ArrayList<Edge>();
				for (int d = Directions.NumDirs - 2; d >= 0; d -= 2) {	
					if (!_Board.canGoTo(i, j, d))
						continue;
					int rr = i + Directions.Dirsy[d];
					int cc = j + Directions.Dirsx[d];
					adjacencies.add(new Edge(_matVert[rr][cc]));		
				}
				_matVert[i][j].setAdjacencies(adjacencies);
			}
		}
	}

	public void initBarrier(Barrier b) {
		//input- a barrier
		//outcome- the graph is disconnect/connect all the vertexes and edges in the area of the barrier
		int row = b.get_row();
		int col = b.get_col();	
		for (int i = -1; i < 2; i++)
			for (int j = -1; j < 2; j++) {
				int r = row+i;
				int c =col+j;	
				if(r<0 || c<0 ||r>=_rows ||c>=_cols)
					continue;
				_matVert[r][c].getAdjacencies().clear();
				ArrayList<Edge> adjacencies = new ArrayList<Edge>();
				for (int d = Directions.NumDirs - 2; d >= 0; d -= 2) {
					if (!_Board.canGoTo(r, c, d))
						continue;		
					int rr = r + Directions.Dirsy[d];
					int cc = c + Directions.Dirsx[d];
					adjacencies.add(new Edge(_matVert[rr][cc]));
				}
				_matVert[r][c].setAdjacencies(adjacencies);
			}
	}


}
