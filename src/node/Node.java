package node;

public class Node implements Comparable<Node>{
	private Object state;
	private Node parent;
	private int cost;
	private int depth;
	private String operator;
	
	public Node(Object state, Node parent, int cost, int depth, String operator) {
		super();
		this.state = state;
		this.parent = parent;
		this.cost = cost;
		this.depth = depth;
		this.operator = operator;
	}
	public String toString() {
		return this.operator + " , " + this.cost;
	}
	public Object getState() {
		return state;
	}

	public Node getParent() {
		return parent;
	}

	public int getCost() {
		return cost;
	}

	public int getDepth() {
		return depth;
	}

	public String getOperator() {
		return operator;
	}
	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub
		int heuristic1 = ((WesterosState) this.getState()).getHeuristic();
		int heuristic2 = ((WesterosState) o.getState()).getHeuristic();
		return (this.getCost() + heuristic1) - (o.getCost() + heuristic2);
	}
	

	
	
}
