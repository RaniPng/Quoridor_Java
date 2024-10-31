package logic;

import java.util.ArrayList;

import interfaces.EdgeInt;
import interfaces.VertexInt;

/**
 * class for vertex
 * @author David Nachimov
 *
 */
public class Vertex implements VertexInt
{
	private ArrayList<EdgeInt> _adjacencies;//the adjacencies
	private double _minDistance = Double.POSITIVE_INFINITY;//the minimum distance
	private VertexInt _previous;//the previous vertex
	private int _i, _j;

	/**
	 * Ctor
	 * @param i - x location
	 * @param j - y location
	 */
	public Vertex(int i, int j)
	{
		_adjacencies = new ArrayList<EdgeInt>();
		_i = i;
		_j = j;
	}
	
	/**
	 * return the i location in the matrix
	 */
	public int getI()
	{
		return _i;
	}
	
	/**
	 * return the j location in the matrix
	 */
	public int getJ()
	{
		return _j;
	}
	
	/**
	 * return all the edges that are connected to this vertex
	 */
	public ArrayList<EdgeInt> getAdjacencies()
	{
		return _adjacencies;
	}
	
	/**
	 * set the adjacencies of the vertex
	 */
	public void setAdjacencies(ArrayList<EdgeInt> adjacencies)
	{
		_adjacencies = adjacencies;
	}
	
	/**
	 * gets the min distance  
	 */
	public double getMinDistance()
	{
		return _minDistance;
	}
	
	/**
	 * set new min distance
	 */
	public void setMinDistance(double minDistance)
	{
		_minDistance = minDistance;
	}
	
	/**
	 * gets the previous vertex that have the min distance
	 */
	public VertexInt getPrevious()
	{
		return _previous;
	}
	
	/**
	 * sets the new previous vertex that have min distance
	 */
	public void setPrevious(VertexInt previous)
	{
		_previous = previous;
	}
	
	/**
	 * compare the two vertexes
	 */
	public int compareTo(VertexInt other)
	{
		return Double.compare(_minDistance, other.getMinDistance());
	}
}
