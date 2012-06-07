package battlechallenge;

/**
 * The Class Coordinate.
 */
public class Coordinate {
	
	/** The row */
	final int row;
	
	/** The col */
	final int col; 
	
	/**
	 * Gets the col.
	 *
	 * @return the x
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Gets the col.
	 *
	 * @return the y
	 */
	public int getCol() {
		return col;
	}
	
	/**
	 * Instantiates a new coordinate.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Coordinate(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/**
	 * Used to check if a coordinate is between A and B
	 * @param A coordinate 1
	 * @param B coordinate 2
	 * @return true if the coordinates are between coordinates A and B
	 */
	public boolean isBetween(Coordinate A, Coordinate B) {
		
		if (A.row == B.row && A.row == this.row) { // coordinates on same row
			if (Math.min(A.col, B.col) <= this.col  && this.col <= Math.max(A.col, B.col))
				return true;
		}
		if (A.col == B.col && A.col == this.col) { // coordinates on same col
			if (Math.min(A.row, B.col) <= this.row && this.row <= Math.max(A.row, B.row)) 
				return true;
		}
		return false;
	}
	
} // End Coordinate Class
