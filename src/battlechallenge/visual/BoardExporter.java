package battlechallenge.visual;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import battlechallenge.ActionResult;
import battlechallenge.ActionResult.ShotResult;
import battlechallenge.Coordinate;
import battlechallenge.Ship;

public class BoardExporter {

	public static String DEFAULT_FILENAME;

	static {
		DEFAULT_FILENAME = "boards/curr.out";
	}
	
	public static char[][] getBoard(List<Ship> ships,
			List<ActionResult> actionResults, int width, int height) {
		char[][] board = new char[height][width];
		for (int i=0;i<board.length;i++)
			for(int j=0;j<board[i].length;j++)
				board[i][j] = '.';
		for (ActionResult ar : actionResults) {
			if (ar.getResult() == ShotResult.MISS) {
				fillSpot(board, 'X', ar.getCoordinate());
			}
		}
		for (Ship s : ships) {
			if (s.isSunken()) {
				for (String c : s.getCoordinateStrings())
					fillSpot(board, 'S', c);
			} else {
				for (String c : s.getHitStrings())
					fillSpot(board, 'O', c);
			}
		}
		return board;
	}

	private static void fillSpot(char[][] board, char c, Coordinate coord) {
		fillSpot(board,c, coord.getRow(), coord.getCol());
	}
	
	private static void fillSpot(char[][] board, char c, String coord) {
		String[] arr = coord.split(",");
		fillSpot(board,c, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
	}
	
	private static void fillSpot(char[][] board, char c, int row, int col) {
		board[row][col] = c;
	}

	public static boolean exportBoard(List<Ship> ships,
			List<ActionResult> actionResults, int width, int height) {
		return exportBoard("", ships, actionResults, width, height);
	}

	public static boolean exportBoard(String filename, List<Ship> ships,
			List<ActionResult> actionResults, int width, int height) {
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(filename));
			char[][] board = getBoard(ships, actionResults, width, height);
			for (int i=0;i<board.length;i++) {
				for(int j=0;j<board[i].length;j++) {
					bf.append(board[i][j]);
				}
				bf.newLine();
			}
			bf.close();
			return true;
		} catch (IOException e) { /* Ignore exceptions and return false */}
		return false;
	}
}
