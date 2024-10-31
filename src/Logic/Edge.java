package Logic;

public class Edge {
	private final Vertex _target;// the end of the edge
	private final double _weight = 1;// the weight

	public Edge(Vertex target) {
		_target = target;
	}

	public Vertex getTarget() {
		return _target;
	}

	public double getWeight() {
		return _weight;
	}
}
