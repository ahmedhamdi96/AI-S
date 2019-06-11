package node;

public class Cell {
	private CellObject cellObject;
	

	public Cell(CellObject cellObject) {
		this.cellObject = cellObject;
		
	}

	public CellObject getCellObject() {
		return cellObject;
	}
	public String toString() {
		switch(cellObject) {
		case DRAGONSTONE: return "D";
		case JON: return "J";
		case OBSTACLE: return "O";
		case WHITEWALKER: return "W";
		default: return "E";
		}
			
	}
	
	
}
