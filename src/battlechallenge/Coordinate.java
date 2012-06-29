package battlechallenge;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Coordinate.
 */
public class Coordinate implements Serializable {
	
	/** The Constant serialVersionUID. */
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
		return this.row >= rowMin && this.row <= rowMax && this.col >= colMin && this.col <= colMax;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return row+ "," + col;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (this.row + "," + this.col).hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof Coordinate) {
			Coordinate that = (Coordinate) obj;
			result = (that.getRow() == this.getRow() && that.getCol() == this.getCol());
		}	
 		return result;	
	}
	
	/**
	 * The euclidian distance between two coordinates
	 * @param coord the coordinate to get the euclidian distance to
	 * @return the euclidian distance between two coordinates
	 */
	public double distanceTo(Coordinate coord) {
		return Math.sqrt(Math.pow(this.getRow() - coord.getRow(), 2) + Math.pow(this.getCol() - coord.getCol(), 2));
	}
	
} // End Coordinate Class
