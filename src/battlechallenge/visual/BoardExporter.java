package battlechallenge.visual;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import battlechallenge.ActionResult;
import battlechallenge.ActionResult.ShotResult;
import battlechallenge.server.ServerPlayer;
import battlechallenge.Coordinate;
import battlechallenge.Ship;

public class BoardExporter {

	public static String DEFAULT_FILENAME;

	static {
		// FIXME: relative path assuming base dir is in bin directory
		DEFAULT_FILENAME = "../boards/curr.out";
	}
	
	public static void exportBoards(ServerPlayer p1, ServerPlayer p2, int width, int height) {
		exportBoards(DEFAULT_FILENAME, p1, p2, width, height);
	}
	
	public static void exportBoards(String filename, ServerPlayer p1, ServerPlayer p2, int width, int height) {
		char[][] b1 = getBoard(p1.getShips(), p2.getActionLog(), width, height);
		char[][] b2 = getBoard(p2.getShips(), p1.getActionLog(), width, height);
		exportBoards(filename, b1, b2, p1.getName(), p2.getName());
	}
	
	public static char[][] getBoard(List<Ship> ships,
			List<ActionResult> actionResults, int width, int height) {
		char[][] board = new char[height][width];
		for (int i=0;i<board.length;i++)
			for(int j=0;j<board[i].length;j++)
				board[i][j] = 'l';
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
				for (String c : s.getCoordinateStrings())
					fillSpot(board, 'B', c);
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
	


	public static boolean exportBoards(String filename, char[][] p1, char[][] p2, String n1, String n2) {
		try {
			File f = new File(filename);
			if (!f.exists()) f.createNewFile();
			BufferedWriter bf = new BufferedWriter(new FileWriter(f));
			bf.append(p1.length+"").append(",").append(p1[0].length+"");
			bf.append(",").append(n1);
			bf.newLine();
			for (int i=0;i<p1.length;i++) {
				for(int j=0;j<p1[i].length;j++) {
					bf.append(p1[i][j]);
				}
				bf.newLine();
			}
			bf.append(p2.length+"").append(",").append(p2[0].length+"");
			bf.append(",").append(n2);
			bf.newLine();
			for (int i=0;i<p2.length;i++) {
				for(int j=0;j<p2[i].length;j++) {
					bf.append(p2[i][j]);
				}
				bf.newLine();
			}
			bf.close();
			return true;
		} catch (IOException e) { e.printStackTrace();/* Ignore exceptions and return false */}
		return false;
	}
}