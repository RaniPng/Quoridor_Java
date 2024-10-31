package Logic;

import java.util.ArrayList;

public class Vertex {
	private ArrayList<Edge> _adjacencies;// the adjacencies
	private double _minDistance = Double.POSITIVE_INFINITY;// the minimum distance
	private Vertex _previous;// the previous vertex
	private int _i, _j;

	public Vertex(int i, int j) {
		_adjacencies = new ArrayList<Edge>();
		_i = i;
		_j = j;
	}

	public int getI() {
		return _i;
	}

	public int getJ() {
		return _j;
	}

	public ArrayList<Edge> getAdjacencies() {
		return _adjacencies;
	}

	public void setAdjacencies(ArrayList<Edge> adjacencies) {
		_adjacencies = adjacencies;
	}

	public double getMinDistance() {
		return _minDistance;
	}

	public void setMinDistance(double minDistance) {
		_minDistance = minDistance;
	}

	public Vertex getPrevious() {
		return _previous;
	}

	public void setPrevious(Vertex previous) {
		_previous = previous;
	}

	public int compareTo(Vertex other) {
		return Double.compare(_minDistance, other.getMinDistance());
	}
}
