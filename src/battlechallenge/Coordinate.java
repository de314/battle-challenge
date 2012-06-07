package battlechallenge;

import java.io.Serializable;

/**
 * The Class Coordinate.
 */
public class Coordinate implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;

	/** The row. */
	final int row;
	
	/** The col. */
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
	 * @param row the row
	 * @param col the col
	 */
	public Coordinate(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/**
	 * Used to check if a coordinate is between A and B. Used to test that
	 * a coordinate lies on a horizontal or vertical line of coordinates by
	 * specifying the starting and ending position.
	 *
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
		// handle diagonal cases?
		return false;
	}
	
	/**
	 * In bounds inclusive. Checks that a coordinate is in a box 
	 * created by the given bounds.
	 *
	 * @param rowMin the row min
	 * @param rowMax the row max
	 * @param colMin the col min
	 * @param colMax the col max
	 * @return true, if successful
	 */
	public boolean inBoundsInclusive(int rowMin, int rowMax, int colMin, int colMax) {
		return this.row >= rowMin && this.row <= rowMax && this.col >= colMin && this.col >= colMax;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder(this.row).append(",").append(this.col).toString();
	}
	
} // End Coordinate Class
