package problem;

import java.util.ArrayList;

import node.Node;
import node.WesterosNode;
import node.WesterosState;

public class SaveWesteros extends Problem{
	public int getNumberOfWhiteWalkers() {
		return numberOfWhiteWalkers;
	}

	private int numberOfWhiteWalkers;
	public SaveWesteros(WesterosState initialState, WesterosState [] stateSpace, ArrayList<String> operators, int numberOfWhiteWalkers) {
		super(initialState, stateSpace, operators);
		this.numberOfWhiteWalkers = numberOfWhiteWalkers;
	}

	@Override
	public int costFunction(Node node, String operator, int numberofWWKilled) {
		int cost = 0;
		WesterosState state = (WesterosState) node.getState();
		short width = (short) state.getGrid().length;
		short height = (short) state.getGrid()[0].length;
		if(numberofWWKilled == 3)
		{
			cost = 1;
		}else if(numberofWWKilled == 2)
		{
			//cost = 17;
			cost = (int) (Math.ceil( width * height * 0.25));
		}
		else if(numberofWWKilled == 1)
		{
			//cost = 17;
			cost = (int) (Math.ceil( width * height * 0.5));
		}
		switch (operator) {
			case "Left": return node.getCost()+1;
			case "Right": return node.getCost()+1;
			case "Forward": return node.getCost()+1;
			case "Collect": return node.getCost()+1;
			case "Kill": return node.getCost()+cost;
			default: return 0;
		}
	}

	@Override
	public Boolean goalTest(Object state) {
		return numberOfWhiteWalkers == ((WesterosState) state).getKilledWhiteWalkers();
	}

	

	
	
}
