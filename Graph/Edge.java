package logic;

import interfaces.EdgeInt;
import interfaces.VertexInt;
/**
 * Class for edge for Dijkstra
 * @author PC 18
 *
 */
public class Edge implements EdgeInt
{
	private final VertexInt _target;//the end of the edge
    private final double _weight = 1;//the weight
    
    /**
     * Ctor
     * @param target - the vertex target
     */
    public Edge(VertexInt target)
    {
    	_target = target;
    }
    
    /**
     * gets the vertex target
     */
	public VertexInt getTarget()
	{
		return _target;
	}
	
	/**
	 * return the weight of the edge
	 */
	public double getWeight()
	{
		return _weight;
	}
}
