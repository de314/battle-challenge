/**
 * 
 */
package battlechallenge.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import battlechallenge.Coordinate;
//import battlechallenge.bot.BoardLocation;
//import battlechallenge.bot.GameState;
import battlechallenge.maps.BattleMap;

/**
 * @author Daniel Nussenbaum
 *
 */
public class GameState {
	private final String separator = "\n===\n";
	HashMap<String, List<BoardLocation>> playerShipLocations;
	HashMap<String, int[]> playerScoreMinerals;
	public GameState(HashMap<String, List<BoardLocation>> gplayerShipLocations, HashMap<String, int[]> gplayerScoreMinerals){
		playerShipLocations = gplayerShipLocations;
		playerScoreMinerals = gplayerScoreMinerals;
	}
	public String getDifferenceToGameState(GameState gs) throws Exception{
		String a = getShipLocationDifference(gs.playerShipLocations);
		String b = getscoreMineralDifference(gs.playerScoreMinerals);
		return a.concat(b).concat(separator);
	}
	
	//TODO which class will we get information from?? for current state...
	public GameState(){
		HashMap<String, List<BoardLocation>> gplayerShipLocations = new HashMap<String, List<BoardLocation>>(); 
		HashMap<String, int[]> gplayerScoreMinerals = new HashMap<String, int[]>();
		playerShipLocations = gplayerShipLocations;
		playerScoreMinerals = gplayerScoreMinerals;
//		ClientGame.
//		GameState gs = new GameState(gplayerShipLocations, gplayerScoreMinerals);
		
	}
	
	//TODO
	private String getscoreMineralDifference(HashMap<String, int[]> otherScoreMinerals){ 
		String a = "";
		int scoreDiff = 0;
		int mineralDiff = 0;
		if(!otherScoreMinerals.keySet().equals(playerScoreMinerals.keySet())){
			for(String s: otherScoreMinerals.keySet()){System.out.println(s);}
			System.out.println("different from");
			for(String s: playerScoreMinerals.keySet()){System.out.println(s);}
////			throw new Exception("Invalid Comparison!");
			}
		for(String k : otherScoreMinerals.keySet()){
			if(!otherScoreMinerals.get(k).equals(playerScoreMinerals.get(k))){
				scoreDiff = (playerScoreMinerals.get(k)[0] - otherScoreMinerals.get(k)[0]);
				mineralDiff = (playerScoreMinerals.get(k)[0] - otherScoreMinerals.get(k)[0]);
				if(scoreDiff != 0)
					a = a.concat(k + "+score:" + scoreDiff+"\n");
				if(mineralDiff != 0)
					a = a.concat(k + "+mineral:" + mineralDiff+"\n");
			}
		}
		return a;
	}
	
	
	private String getShipLocationDifference(HashMap<String, List<BoardLocation>> otherShipLocations) throws Exception{
		String a = "\n";
		if(!otherShipLocations.keySet().equals(playerShipLocations.keySet())){
			for(String s: otherShipLocations.keySet()){System.out.println(s);}
			System.out.println("different from");
			for(String s: playerShipLocations.keySet()){System.out.println(s);}
////			throw new Exception("Invalid Comparison!");
			}
		for(String k : otherShipLocations.keySet()){
			if(!otherShipLocations.get(k).equals(playerShipLocations.get(k))){
				a = a.concat(k + ":" + getListBoardLocationsDifference(playerShipLocations.get(k), otherShipLocations.get(k))+"\n");
			}
		}
		return a;
	}
	

	private String getListBoardLocationsDifference(List<BoardLocation> first, List<BoardLocation> other){
		String a = "";
		for(BoardLocation bl : first){
			if(!other.contains(bl)){
				a = a.concat(" " + bl.row + "," + bl.col);
			}
		}
		for(BoardLocation bl : other){
			if(!first.contains(bl)){
				a = a.concat(" !" + bl.row + "," + bl.col);
			}
		}
		return a;
	}
	
	@Override
	public String toString() {
		String a = "";
		String b;
		if(!playerScoreMinerals.keySet().equals(playerShipLocations.keySet())){
			for(String s: playerScoreMinerals.keySet()){System.out.println(s);}
			System.out.println("different from");
			for(String s: playerShipLocations.keySet()){System.out.println(s);}
			a = "\nthere seems to be an issue with the game state\n";
			}
		for(String k : playerShipLocations.keySet()){
			b = "";
			for(BoardLocation bl : playerShipLocations.get(k)){
				b = b.concat(" " + bl.row + "," + bl.col);
			}
			
			a = a.concat(k + ":" + b + "\n");
			a = a.concat(k + " score:" + playerScoreMinerals.get(k)[0] + "\n");
			a = a.concat(k + " minerals:" + playerScoreMinerals.get(k)[1] + "\n");
		}
		//TODO add equivalent to getscoreMineralDifference
		return a.concat(separator);
	}

}





class BoardLocation {
	int row, col; double value;
//	BattleMap bm;
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj.getClass() != this.getClass()) return false;
//		return super.equals(obj);
		if(((BoardLocation) obj).row != this.row) return false;
		if(((BoardLocation) obj).col != this.col) return false;
		return true;
	}
//	public BoardLocation(int grow, int gcol, BattleMap gbm){row = grow; col = gcol; bm = gbm;}
//	public BoardLocation(Coordinate coord, BattleMap gbm){row = coord.getRow(); col = coord.getCol(); bm = gbm;}
	public BoardLocation(int grow, int gcol){row = grow; col = gcol;}
	public BoardLocation(Coordinate coord){row = coord.getRow(); col = coord.getCol();}
//	public int getManhattanDistanceTo(BoardLocation bl){return 1;} // TODO
//	public boolean isValid(){return !(row < 0 || row >= bm.getNumRows() || col < 0 || col >= bm.getNumCols());}
//	public List<BoardLocation> getAdjacent(){
//		ArrayList<BoardLocation> adj = new ArrayList<BoardLocation>();
//		BoardLocation north = (new BoardLocation(row-1, col, bm));
//		BoardLocation south =(new BoardLocation(row+1, col, bm));
//		BoardLocation east = (new BoardLocation(row, col+1, bm));
//		BoardLocation west = (new BoardLocation(row, col-1, bm));
//		if (north.isValid()) adj.add(north);
//		if (south.isValid()) adj.add(south);
//		if (east.isValid()) adj.add(east);
//		if (west.isValid()) adj.add(west);	
//		return adj;
//	}
}















