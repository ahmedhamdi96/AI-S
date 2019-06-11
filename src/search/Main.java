package search;

import java.util.ArrayList;
import java.util.Random;

import com.sun.org.apache.xpath.internal.operations.Bool;

import node.Cell;
import node.CellObject;
import node.Node;
import node.Orientation;
import node.WesterosState;
import problem.SaveWesteros;

public class Main {
	private static Cell[][] grid;
	private static int rows;
	private static int cols;
	static Random r = new Random();
	//r.nextInt((max-min) +1) + min)
	private static int dragonGlass = r.nextInt((4 - 2) + 1) + 2;
	private static Search search = new Search();
	private static int whiteWalkers;
	

	public static void genGrid(){
		rows = 4;//r.nextInt((7 - 4) + 1) + 4;
		cols = 4;//r.nextInt((7 - 4) + 1) + 4;
		whiteWalkers = ( (int) (Math.random() * 5)) + 1;
		int temp = whiteWalkers;
		int randomRow, randomCol;
		int dragonstones = 1;
		int obstacles = (int) (Math.random()*4) + 1;
		grid = new Cell[rows][cols];
		grid[rows-1][cols-1] = new Cell(CellObject.JON);
		while(whiteWalkers > 0 || obstacles > 0 || dragonstones > 0) {
			randomRow = (int) (Math.random() * (rows-1));
			randomCol = (int) (Math.random() * (cols-1));
			if(grid[randomRow][randomCol] == null && whiteWalkers > 0)
			{
				grid[randomRow][randomCol] = new Cell(CellObject.WHITEWALKER);
				whiteWalkers--;
			}
			else if(grid[randomRow][randomCol] == null && obstacles > 0)
			{
				grid[randomRow][randomCol] = new Cell(CellObject.OBSTACLE);
				obstacles--;
			}
			else if (grid[randomRow][randomCol] == null && dragonstones > 0) {
				grid[randomRow][randomCol] = new Cell(CellObject.DRAGONSTONE);
				dragonstones--;
			}
		}
		whiteWalkers = temp;
	}
	public static void testGrid() {
		int r = 4;
		int c = 4;
		int wws = 3;
		grid = new Cell[r][c];
		grid[r-1][c-1]= new Cell(CellObject.JON);
		grid[2][1] = new Cell(CellObject.DRAGONSTONE);
		grid[0][2] = new Cell(CellObject.WHITEWALKER);
		grid[1][1] = new Cell(CellObject.WHITEWALKER);
		grid[2][2] = new Cell(CellObject.WHITEWALKER);
		//grid[2][2] = new Cell(CellObject.WHITEWALKER);
		//grid[1][2] = new Cell(CellObject.WHITEWALKER);
		grid[0][1] = new Cell(CellObject.OBSTACLE);
		//grid[0][3] = new Cell(CellObject.OBSTACLE);
		//grid[0][2] = new Cell(CellObject.OBSTACLE);
		whiteWalkers = wws;
		rows = r;
		cols = c;
	}
	
	// Method used to print the whole grid.
	public static void printGrid() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if(grid[i][j] != null)
					System.out.print(grid[i][j].toString()+" ");
				else
					System.out.print("E ");
			}
			System.out.println();
		}
	}
	
	
	
	
	public static Object[] Search(Cell[][] grid, Strategy strategy, Boolean visualize) {
		Object[] output = new Object[3];
		WesterosState initialState = new WesterosState(rows-1, cols-1, Orientation.NORTH, 0, grid, dragonGlass, 0);
		ArrayList<String> operators = new ArrayList<String>();
		operators.add("Right");
		operators.add("Left");
		operators.add("Forward");
		operators.add("Kill");
		operators.add("Collect");
		SaveWesteros problem = new SaveWesteros(initialState, null, operators, whiteWalkers);
		Node goal= search.genericSearch(problem, strategy);
		if(goal != null){
			//((WesterosState)goal.getState()).printGrid();
			output[0] = backtrack(goal, visualize);
			output[1] = goal.getCost();
			output[2] = search.getCountNodes();
		}
		
		return output;
		
	}
	
	public static ArrayList<String> backtrack(Node node, Boolean visualize) {
		ArrayList<String> ops = new ArrayList<String>();
		
		ArrayList<WesterosState> states = new ArrayList<WesterosState>();
		while(node!=null) {
			states.add(0, (WesterosState)node.getState());
			ops.add(0, node.getOperator());
			node = node.getParent();
		}
		if(visualize)
		{
			System.out.println("Path to Goal:");
			for (WesterosState state:states) {
				state.printGrid();
				System.out.println();
			}
		}
		return ops;
	}
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		System.out.println("Initial Grid:");
		//genGrid();
		testGrid();
		printGrid();
		System.out.println();
		Object[] result = Search(grid, Strategy.AS2, true);
		System.out.print("Actions: ");
		for(int i = 1; i < ((ArrayList<String>) (result[0])).size() ; i++){
			System.out.print(((ArrayList<String>) (result[0])).get(i)+ ", ");
		}
		System.out.println();
		System.out.println("Cost to Goal: " + result[1]);
		System.out.println("Number of Expanded Nodes: "+result[2]);
	
		
	}
}
