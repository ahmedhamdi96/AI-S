package search;

import java.util.ArrayList;
import java.util.Comparator;

import node.Cell;
import node.CellObject;
import node.Node;
import node.Orientation;
import node.WesterosState;
import problem.Problem;
import problem.SaveWesteros;

public class Search {
	static int i = 0;
	static Problem problem;
	static int depth = 0;
	public static Strategy usedStrategy;
	public static int totalNumOfWWs;
	private int countNodes = 0;
	
	public int getCountNodes() {
		return countNodes;
	}
	public Node genericSearch(Problem p, Strategy qingF) {
		usedStrategy = qingF;
		totalNumOfWWs = ((SaveWesteros)(p)).getNumberOfWhiteWalkers();
		problem = p;
		ArrayList<Node> nodes = new ArrayList<Node>();
		ArrayList<Node> children = new ArrayList<Node>();
		//Node initialNode = new WesterosNode((WesterosState)p.getInitialState(), null, 0, 0, null);
		Node initialNode = new Node(p.getInitialState(), null, 0, 0, null);
		nodes.add(initialNode);
		while(! nodes.isEmpty()) {
			Node node = nodes.get(0);
			nodes.remove(0);
			countNodes++;
			if(p.goalTest(node.getState())) {
				return node;
			}
				
			children = expand(node, p.getOperators());
			switch(qingF) {
				case BF: nodes.addAll(nodes.size(), children); break;
				case DF:nodes.addAll(0, children); break;
				case UC: {
					nodes.addAll(children);
					nodes.sort(null);
					
				}break;
				case ID: {
					if(depth < 2000)
					{
						for(int i = children.size()-1; i >= 0; i-- ) {
							if(children.get(i).getDepth() <= depth) {
								nodes.add(0, children.get(i));
							}
						}
					}else 
						return null;
					
				}break;
				case GR1:{
					nodes.addAll(children);
					sortByHeuristic(nodes);
				}break;
				case GR2:{
					nodes.addAll(children);
					sortByHeuristic(nodes);
				}break;
				case AS1:{
					nodes.addAll(children);
					nodes.sort(null);
				}break;
				case AS2:{
					nodes.addAll(children);
					nodes.sort(null);
				}break;
			}

		} 
		if(nodes.isEmpty() && qingF == Strategy.ID) {
			
			depth++;
			return genericSearch(p, qingF);
		}
		return null;
	}
	public static void sortByHeuristic(ArrayList<Node> nodes){
		Comparator<Node> heuristicComparator = new Comparator<Node>() {
		    @Override
		    public int compare(Node n1, Node n2) {
				int heuristic1 = ((WesterosState) n1.getState()).getHeuristic();
				int heuristic2 = ((WesterosState) n2.getState()).getHeuristic();
				return heuristic1 - heuristic2;
		    }
		};
		
		nodes.sort(heuristicComparator);
	}
	public static int EuclideanDistance(boolean ww, int row, int col, Cell[][] grid, int objects)
	{
		//It's called objects because it indicates no. of wws or dragonstones
		int d = 0;
		for(int i = 0; i < grid.length; i++)
		{
			for(int j = 0; j < grid[0].length; j++)
			{
				if(grid[i][j] != null && ww &&
						grid[i][j].getCellObject() == CellObject.WHITEWALKER)
				{
					objects--;
					int xdiff = Math.abs(row+1 - (i+1));
					int ydiff = Math.abs(col+1 - (j+1));
					int temp = (int) Math.ceil(Math.sqrt((xdiff*xdiff) + (ydiff* ydiff)));
					if(temp < d)
						d = temp;
				}
				else if(grid[i][j] != null && (!ww) &&
						grid[i][j].getCellObject() == CellObject.DRAGONSTONE){
					objects--;
					int xdiff = Math.abs(row+1 - (i+1));
					int ydiff = Math.abs(col+1 - (j+1));
					d = (int) Math.ceil(Math.sqrt((xdiff*xdiff) + (ydiff* ydiff)));
				}
				if(objects == 0)
					return d/2;
			}
		}
		
		return d/2;
	}
	//This heuristic is admissible as it is completely relaxed
	//It's always assumed that J has dragonglass + he can shoot from anywhere and kill 3 wws if any
	public static int secondHeuristic(WesterosState state, int totalNumberOfWWs)
	{
		int cost = 0;
		int remainedWWs = totalNumberOfWWs - state.getKilledWhiteWalkers();
		short width = (short) state.getGrid().length;
		short height = (short) state.getGrid()[0].length;
		while(remainedWWs > 0)
		{
			if(remainedWWs == 1)
				cost += (int) (Math.ceil( width * height * 0.5));
			else if (remainedWWs == 2)
				cost += (int) (Math.ceil( width * height * 0.25));
			else 
				cost += 1;
			remainedWWs-=3;
			
		}
		return cost;
	}
	//This heuristic is really admissible
	//1) Once jon reaches a dragonstone we assume that he can kill the max. number of wws right away, unless there are some wws around jon. Regardless of whether there are wws around or not.
	//2) We always calculate the euclidean distance to the nearest dragon stone or ww cell which is a straight line.
	public static int firstHeuristic(WesterosState state, int totalNumberOfWWs){
		Cell[][] grid = state.getGrid();
		int row = state.getRow();
		int col = state.getColumn();
		int cost = 0;
		int dragonGlassAtHand = state.getDragonGlass();
		short width = (short) state.getGrid().length;
		short height = (short) state.getGrid()[0].length;
		//This condition to satisfy the centring property
		if(totalNumberOfWWs - state.getKilledWhiteWalkers() == 0){
			return cost;
		}
		if(dragonGlassAtHand > 0)
		{
			
			boolean WWright = col == grid[0].length-1 ? false : 
				( grid[row][col+1] != null && 
				grid[row][col+1].getCellObject() == CellObject.WHITEWALKER);
			boolean WWleft = col == 0 ? false : ( grid[row][col-1] != null &&
					grid[row][col-1].getCellObject() == CellObject.WHITEWALKER);
			boolean WWup = row == 0 ? false : ( grid[row-1][col] != null &&
					grid[row-1][col].getCellObject() == CellObject.WHITEWALKER);
			boolean WWdown = row == grid.length-1 ? false : (grid[row+1][col] != null &&
					grid[row+1][col].getCellObject() == CellObject.WHITEWALKER);
			
			if( !(WWright || WWleft || WWup || WWdown))
				cost += EuclideanDistance(true, row, col, grid, totalNumberOfWWs - state.getKilledWhiteWalkers()) * 1;
			
//			For this part we neglect the condition on the remaining no. of dragonglass
//			Which will remove the calculations of +ve distances
			int remainedWWs = totalNumberOfWWs - state.getKilledWhiteWalkers();
			
			while(remainedWWs > 0)
			{
				if(remainedWWs == 1)
					cost += (int) (Math.ceil( width * height * 0.5));
				else if (remainedWWs == 2)
					cost += (int) (Math.ceil( width * height * 0.25));
				else
					cost += 1;
				remainedWWs-=3;
			}
		}
		else
		{
			//For this part we neglect the condition on the location of J relative to WWs
			//Which will remove the calculations of +ve distances
			//+1 for the cost of collecting dragon glass
			cost += EuclideanDistance(false, row, col, grid, 1) * 1 + 1;
			int remainedWWs = totalNumberOfWWs - state.getKilledWhiteWalkers();
			while(remainedWWs > 0)
			{
				if(remainedWWs == 1)
					cost += (int) (Math.ceil( width * height * 0.5));
				else if (remainedWWs == 2)
					cost += (int) (Math.ceil( width * height * 0.25));
				else
					cost += 1;
				remainedWWs-=3;
			}
			
		}
		return cost;
	}
	
	public static ArrayList<Node> expand(Node node, ArrayList<String> operators) {
		ArrayList<Node> children = new ArrayList<Node>();
		ArrayList<String> tempOperators = new ArrayList<String>();
		int cost = 0;
		for (String s : operators) {
			tempOperators.add(s);
		}
		tempOperators = westerosConstraints(tempOperators, node);
		for (String op : tempOperators) {
			WesterosState currentState = ((WesterosState) node.getState());
			WesterosState nextState;
			Node child;
			if(op.equals("Right") || op.equals("Left")) {
				nextState = new WesterosState(currentState.getRow(), currentState.getColumn(), nextOrientation(currentState.getOrientation(), op), currentState.getDragonGlass(), currentState.getGrid(), currentState.getMaxDragonGlass(), currentState.getKilledWhiteWalkers());
				if(usedStrategy == Strategy.GR1)
				{
					cost = problem.costFunction(node, op, 0);
					nextState.setHeuristic(firstHeuristic(nextState, totalNumOfWWs));
				}
				else if(usedStrategy == Strategy.GR2)
				{
					cost = problem.costFunction(node, op, 0);
					nextState.setHeuristic(secondHeuristic(nextState, totalNumOfWWs));
				}
				else if (usedStrategy == Strategy.AS1){
					
					cost = problem.costFunction(node, op, 0);
					nextState.setHeuristic(firstHeuristic(nextState, totalNumOfWWs));
				}
				else if(usedStrategy == Strategy.AS2)
				{
					cost = problem.costFunction(node, op, 0);
					nextState.setHeuristic(secondHeuristic(nextState, totalNumOfWWs));
				}
				else{
					cost = problem.costFunction(node, op, 0);
				}
				
				child = new Node(nextState, node, cost, ( node).getDepth() + 1, op);
				children.add(child);
			} else if (op.equals("Forward")) {
				int[] nextCell = nextCell(currentState.getOrientation(), currentState.getRow(), currentState.getColumn());
				nextState = new WesterosState(nextCell[0], nextCell[1], currentState.getOrientation(), currentState.getDragonGlass(), updateGrid(nextCell[0], nextCell[1], currentState.getGrid()), currentState.getMaxDragonGlass(), currentState.getKilledWhiteWalkers());
				if(usedStrategy == Strategy.GR1)
				{
					cost = problem.costFunction(node, op, 0);
					nextState.setHeuristic(firstHeuristic(nextState, totalNumOfWWs));
				}
				else if(usedStrategy == Strategy.GR2)
				{
					cost = problem.costFunction(node, op, 0);
					nextState.setHeuristic(secondHeuristic(nextState, totalNumOfWWs));
				}
				else if (usedStrategy == Strategy.AS1){
					
					cost = problem.costFunction(node, op, 0);
					nextState.setHeuristic(firstHeuristic(nextState, totalNumOfWWs));
				}
				else if(usedStrategy == Strategy.AS2)
				{
					cost = problem.costFunction(node, op, 0);
					nextState.setHeuristic(secondHeuristic(nextState, totalNumOfWWs));
				}
				else{
					cost = problem.costFunction(node, op, 0);
				}
				
				child = new Node(nextState,  node, cost, ( node).getDepth() + 1, op);
				children.add(child);
			} else if (op.equals("Collect")) {
				nextState = new WesterosState(currentState.getRow(), currentState.getColumn(), currentState.getOrientation(), currentState.getMaxDragonGlass(), currentState.getGrid(), currentState.getMaxDragonGlass(), currentState.getKilledWhiteWalkers());
				if(usedStrategy == Strategy.GR1)
				{
					cost = problem.costFunction(node, op, 0);
					nextState.setHeuristic(firstHeuristic(nextState, totalNumOfWWs));
				}
				else if(usedStrategy == Strategy.GR2)
				{
					cost = problem.costFunction(node, op, 0);
					nextState.setHeuristic(secondHeuristic(nextState, totalNumOfWWs));
				}
				else if (usedStrategy == Strategy.AS1){
					
					cost = problem.costFunction(node, op, 0);
					nextState.setHeuristic(firstHeuristic(nextState, totalNumOfWWs));
				}
				else if(usedStrategy == Strategy.AS2)
				{
					cost = problem.costFunction(node, op, 0);
					nextState.setHeuristic(secondHeuristic(nextState, totalNumOfWWs));
				}
				else{
					cost = problem.costFunction(node, op, 0);
				}
				child = new Node(nextState,  node, cost, ( node).getDepth() + 1, op);
				children.add(child);
			}
			else if(op.equals("Kill")) {
				Object[] output= killWWs(currentState.getGrid(), currentState.getRow(), currentState.getColumn());
				int killedWhiteWalkers = (Integer) output[1];
				Cell[][] newGrid = (Cell[][]) output[0];
				nextState = new WesterosState(currentState.getRow(), currentState.getColumn(), currentState.getOrientation(), currentState.getDragonGlass()-1, newGrid, currentState.getMaxDragonGlass(), currentState.getKilledWhiteWalkers()+ killedWhiteWalkers);
				if(usedStrategy == Strategy.GR1)
				{
					cost = problem.costFunction(node, op, killedWhiteWalkers);
					nextState.setHeuristic(firstHeuristic(nextState, totalNumOfWWs));
				}
				else if(usedStrategy == Strategy.GR2)
				{
					cost = problem.costFunction(node, op, killedWhiteWalkers);
					nextState.setHeuristic(secondHeuristic(nextState, totalNumOfWWs));
				}
				else if (usedStrategy == Strategy.AS1){
					
					cost = problem.costFunction(node, op, killedWhiteWalkers);
					nextState.setHeuristic(firstHeuristic(nextState, totalNumOfWWs));
				}
				else if(usedStrategy == Strategy.AS2)
				{
					cost = problem.costFunction(node, op, killedWhiteWalkers);
					nextState.setHeuristic(secondHeuristic(nextState, totalNumOfWWs));
				}
				else{
					cost = problem.costFunction(node, op, killedWhiteWalkers);
				}
				child = new Node(nextState,  node, cost, (node).getDepth() + 1, op);
				children.add(child);
			}

		}
		return children;
	}
	// TODO Usage of numberOfKilledWhiteWalkers
	public static Object[] killWWs(Cell[][] grid, int row, int col) {
		Object[] output = new Object[2];
		Cell[][] newGrid = new Cell[grid.length][grid[0].length];
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++)
			{
				newGrid[i][j] = grid[i][j] != null? new Cell(grid[i][j].getCellObject()):null;
			}
		}

		int numberOfKilledWhiteWalkers = 0;
		Cell right = col == grid[0].length-1 ? null : grid[row][col+1];
		Cell left = col == 0 ? null : grid[row][col-1];
		Cell up = row == 0 ? null : grid[row-1][col];
		Cell down = row == grid.length-1 ? null : grid[row+1][col];

		if (right != null && right.getCellObject() == CellObject.WHITEWALKER) {
			numberOfKilledWhiteWalkers++;
			newGrid[row][col+1] = null;
		}

		if (left != null && left.getCellObject() == CellObject.WHITEWALKER) {
			numberOfKilledWhiteWalkers++;
			newGrid[row][col-1] = null;
		}

		if (up != null && up.getCellObject() == CellObject.WHITEWALKER) {
			numberOfKilledWhiteWalkers++;
			newGrid[row-1][col] = null;
		}

		if (down != null && down.getCellObject() == CellObject.WHITEWALKER) {
			numberOfKilledWhiteWalkers++;
			newGrid[row+1][col] = null;
		}


		output[0] = newGrid;
		output[1] = numberOfKilledWhiteWalkers;
		return output;

	}
	public static Cell[][] updateGrid(int row, int column, Cell[][] grid){
		Cell[][] newGrid = new Cell[grid.length][grid[0].length];
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++)
			{
				if(grid[i][j] != null && grid[i][j].getCellObject() == CellObject.JON )
					newGrid[i][j] = null;
				else
					newGrid[i][j] = grid[i][j] != null? new Cell(grid[i][j].getCellObject()):null;
			}
		}
		if(newGrid[row][column]!= null && newGrid[row][column].getCellObject()==CellObject.DRAGONSTONE)
			newGrid[row][column] = new Cell(CellObject.DRAGONSTONE);
		else 
			newGrid[row][column] = new Cell(CellObject.JON);
		return newGrid;

	}

	public static int[] nextCell(Orientation orientation, int row, int col) {
		switch(orientation) {
		case EAST: {
			return new int[] {row, col+1};
		}
		case NORTH:{
			return new int[] {row-1, col};
		}
		case SOUTH:{
			return new int[] {row+1, col};
		}
		case WEST:{
			return new int[] {row, col-1};
		}
		default: return null;
		}
	}

	public static Orientation nextOrientation(Orientation orientation, String action) {
		switch(orientation) {
		case EAST: {
			if (action.equals("Right")) {
				return Orientation.SOUTH;
			} else {
				return Orientation.NORTH;
			}
		}
		case NORTH:{
			if (action.equals("Right")) {
				return Orientation.EAST;
			} else {
				return Orientation.WEST;
			}
		}
		case SOUTH:{
			if (action.equals("Right")) {
				return Orientation.EAST;
			} else {
				return Orientation.WEST;
			}
		}
		case WEST:{
			if (action.equals("Right")) {
				return Orientation.NORTH;
			} else {
				return Orientation.SOUTH;
			}
		}
		default: return null;

		}
	}

	public static ArrayList<String> westerosConstraints(ArrayList<String> operators, Node node) {
		//Check right/left rotation. -- 1,2
		Node parent = node.getParent();
		if(parent != null)
		{
			
			Node grandParent = parent.getParent();
			if(grandParent != null)
			{
				String operator = parent.getOperator().equals(grandParent.getOperator()) ? parent.getOperator() : null;
				if(operator != null && (operator.equals("Right") || operator.equals("Left")))
					operators.remove(operator);
			}
			
			String parentOperator = parent.getOperator();
			
			if(parentOperator != null) {
				if(parentOperator.equals("Left"))
					operators.remove("Right");
				if(parentOperator.equals("Right"))
					operators.remove("Left");
			}
			

		}
		
		
			

		// Don't collect if you are not at the dragon stone spot. -- 3
		WesterosState currentState = (WesterosState) node.getState();
		Cell currentCell = currentState.getGrid()[currentState.getRow()][currentState.getColumn()];
		if(currentCell != null && currentCell.getCellObject() != CellObject.DRAGONSTONE) {
			operators.remove("Collect");
		}

		// Don't collect if you have the maximum allowed number of dragon glass. -- 4
		if(currentState.getDragonGlass() == currentState.getMaxDragonGlass()) {
			operators.remove("Collect");
		}

		// Don't Kill if you don't have dragon glass. -- 5
		if(currentState.getDragonGlass() == 0) {
			operators.remove("Kill");
		}

		// Don't cross borders. -- 6
		//Don't expand to a cell if a white walker/obstacle exits. -- 7,8
		Orientation orientation = currentState.getOrientation();
		int currRow = currentState.getRow();
		int currCol = currentState.getColumn();
		int gridWidth = currentState.getGrid()[0].length;
		int gridHeight = currentState.getGrid().length;
		Boolean borderCondition, obstacleCondition;
		Cell nextStep;
		switch(orientation) {
		case EAST: {
			borderCondition = currCol == gridWidth-1;
			nextStep = borderCondition? null:currentState.getGrid()[currRow][currCol+1];
			obstacleCondition = nextStep != null && (nextStep.getCellObject() == CellObject.OBSTACLE || 
					nextStep.getCellObject() == CellObject.WHITEWALKER);
			if(borderCondition || obstacleCondition)
				operators.remove("Forward");
		}
		break;
		case NORTH:{
			borderCondition = currRow == 0;
			nextStep = borderCondition? null: currentState.getGrid()[currRow-1][currCol];
			obstacleCondition = nextStep != null && (nextStep.getCellObject() == CellObject.OBSTACLE || 
					nextStep.getCellObject() == CellObject.WHITEWALKER);
			if(borderCondition || obstacleCondition)
				operators.remove("Forward");
		}
		break;
		case SOUTH: {
			borderCondition = currRow == gridHeight-1;
			nextStep = borderCondition? null:currentState.getGrid()[currRow+1][currCol];
			obstacleCondition = nextStep != null && (nextStep.getCellObject() == CellObject.OBSTACLE || 
					nextStep.getCellObject() == CellObject.WHITEWALKER);
			if(borderCondition || obstacleCondition)
				operators.remove("Forward");
		}
		break;
		case WEST: {
			borderCondition = currCol == 0;
			nextStep = borderCondition? null:currentState.getGrid()[currRow][currCol-1];
			obstacleCondition = nextStep != null && (nextStep.getCellObject() == CellObject.OBSTACLE || 
					nextStep.getCellObject() == CellObject.WHITEWALKER);
			if(borderCondition || obstacleCondition)
				operators.remove("Forward");
		}
		break;
		default:
			break;

		}
		//Check on the surronded cells for ww to remove kill or not
		Cell front = (currRow-1 != -1)? currentState.getGrid()[currRow-1][currCol]:null;
		Cell back = (currRow+1 != gridHeight)? currentState.getGrid()[currRow+1][currCol]:null;
		Cell right = (currCol+1 != gridWidth)? currentState.getGrid()[currRow][currCol+1]:null;
		Cell left = (currCol-1 != -1)? currentState.getGrid()[currRow][currCol-1]:null;
		
		Boolean frontWW = (front!=null && front.getCellObject() == CellObject.WHITEWALKER);
		Boolean backWW = (back!=null && back.getCellObject() == CellObject.WHITEWALKER);
		Boolean rightWW = (right!=null && right.getCellObject() == CellObject.WHITEWALKER);
		Boolean leftWW = (left!=null && left.getCellObject() == CellObject.WHITEWALKER);
		
		if(!(frontWW || backWW || rightWW || leftWW) ) {
			operators.remove("Kill");
		}
		
		
		//Boundary Constraints
		Orientation currentOrientation = ((WesterosState) node.getState()).getOrientation();
		if(currentOrientation == Orientation.NORTH && currCol == currentState.getGrid()[0].length-1){
			operators.remove("Right");
		}
		else if(currentOrientation == Orientation.SOUTH && currCol == currentState.getGrid()[0].length-1){
			operators.remove("Left");
		}
		else if(currentOrientation == Orientation.NORTH && currCol == 0){
			operators.remove("Left");
		}
		else if(currentOrientation == Orientation.NORTH && currCol == 0){
			operators.remove("Right");
		}
		else if(currentOrientation == Orientation.WEST && currRow == 0){
			operators.remove("Right");
		}
		else if(currentOrientation == Orientation.EAST && currRow == 0){
			operators.remove("Left");
		}
		else if(currentOrientation == Orientation.EAST && currRow == currentState.getGrid().length-1){
			operators.remove("Right");
		}
		else if(currentOrientation == Orientation.WEST && currRow == currentState.getGrid().length-1){
			operators.remove("Let");
		}

		return operators;
	}
}
