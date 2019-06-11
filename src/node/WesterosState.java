package node;

public class WesterosState {
	private int row;
	private int column;
	private Orientation orientation;
	private int dragonGlass;
	private int maxDragonGlass;
	private Cell [][] grid;
	private int killedWhiteWalkers;
	private int heuristic = 0;
	
	public int getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(int heuristic) {
		this.heuristic = heuristic;
	}

	public String toString() {
		return "killed: " + killedWhiteWalkers + ", row: " + row + ", column: " + column;
	}
	
	public WesterosState(int row, int column, Orientation orientation, int dragonGlass, Cell[][] grid, int maxDragonGlass, int killedWhiteWalkers) {
		this.row = row;
		this.column = column;
		this.orientation = orientation;
		this.dragonGlass = dragonGlass;
		this.grid = grid;
		this.maxDragonGlass = maxDragonGlass;
		this.killedWhiteWalkers = killedWhiteWalkers;
	}
	
	public int getRow() {
		return row;
	}
	public int getColumn() {
		return column;
	}
	public Orientation getOrientation() {
		return orientation;
	}
	public int getDragonGlass() {
		return dragonGlass;
	}
	public Cell[][] getGrid() {
		return grid;
	}

	public int getMaxDragonGlass() {
		return maxDragonGlass;
	}
	
	public void printGrid() {
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

	public int getKilledWhiteWalkers() {
		return killedWhiteWalkers;
	}

	public void setKilledWhiteWalkers(int killedWhiteWalkers) {
		this.killedWhiteWalkers = killedWhiteWalkers;
	}
	
	
	
}
