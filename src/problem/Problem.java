package problem;

import java.util.ArrayList;

import node.Node;

public abstract class Problem {
	private Object initialState; 
	private Object [] stateSpace;
	private ArrayList<String> operators;
	
	public Problem(Object initialState, Object[] stateSpace, ArrayList<String> operators) {
		super();
		this.initialState = initialState;
		this.stateSpace = stateSpace;
		this.operators = operators;
	}
	public ArrayList<String> getOperators() {
		return operators;
	}

	public Object getInitialState() {
		return initialState;
	}
	

	public abstract int costFunction(Node node, String operator, int number);
	
	
	public abstract Boolean goalTest(Object state);

}
