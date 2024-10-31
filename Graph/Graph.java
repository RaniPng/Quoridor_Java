package logic;

import java.util.ArrayList;

import interfaces.EdgeInt;
import interfaces.GraphInt;
import interfaces.VertexInt;
/**
 * the Graph for dijkstra
 * @author David Nachimov
 *
 */
public class Graph implements GraphInt
{

	private int[][] _mat;// the xml map
	private VertexInt[][] _matVert;// the Dijkstra map
	private int _size;//the rows
	private int _sizeW;//the colums

	/**
	 * Ctor
	 * @param size
	 *            i rows
	 * @param sizeW
	 *            j col
	 * @param mat
	 *            - xml mat
	 */
	public Graph(int size, int sizeW, int[][] mat)
	{
		_size = size;
		_sizeW = sizeW;
		_mat = mat;
		_matVert = new VertexInt[_size][_sizeW];

		for (int i = 0; i < _size; i++)
		{
			for (int j = 0; j < _sizeW; j++)
			{
				_matVert[i][j] = new Vertex(i, j);
			}
		}
		buildGraph();
	}

	/**
	 * gets the map graph
	 */
	public VertexInt[][] getGraph()
	{
		return _matVert;
	}

	/**
	 * builds the graph
	 */
	public void buildGraph()
	{
		for (int i = 0; i < _size; i++)
		{
			for (int j = 0; j < _sizeW; j++)
			{
				if (_mat[i][j] == 0)
				{
					_matVert[i][j].getAdjacencies().clear();
					ArrayList<EdgeInt> adjacencies = new ArrayList<EdgeInt>();
					// right
					if ((j + 1) < _sizeW && _mat[i][j + 1] == 0)
					{
						adjacencies.add(new Edge(_matVert[i][j + 1]));
					}
					// left
					if ((j - 1) >= 0 && _mat[i][j - 1] == 0)
					{
						adjacencies.add(new Edge(_matVert[i][j - 1]));
					}
					// up
					if ((i - 1) >= 0 && _mat[i - 1][j] == 0)
					{
						adjacencies.add(new Edge(_matVert[i - 1][j]));
					}
					// down
					if ((i + 1) < _size && _mat[i + 1][j] == 0)
					{
						adjacencies.add(new Edge(_matVert[i + 1][j]));
					}
					_matVert[i][j].setAdjacencies(adjacencies);
				}
			}
		}
	}
}
