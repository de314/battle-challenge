/**
 * 
 */
package battlechallenge.server;

import java.util.HashMap;
import java.util.List;
import battlechallenge.Coordinate;

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
	
	public GameState(){
		HashMap<String, List<BoardLocation>> gplayerShipLocations = new HashMap<String, List<BoardLocation>>(); 
		HashMap<String, int[]> gplayerScoreMinerals = new HashMap<String, int[]>();
		playerShipLocations = gplayerShipLocations;
		playerScoreMinerals = gplayerScoreMinerals;
	}
	
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
		return a.concat(separator);
	}

}





class BoardLocation {
	int row, col; double value;
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() != this.getClass()) return false;
		if(((BoardLocation) obj).row != this.row) return false;
		if(((BoardLocation) obj).col != this.col) return false;
		return true;
	}
	public BoardLocation(int grow, int gcol){row = grow; col = gcol;}
	public BoardLocation(Coordinate coord){row = coord.getRow(); col = coord.getCol();}
}















