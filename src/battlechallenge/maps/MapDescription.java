package battlechallenge.maps;

public class MapDescription {
	
	private final String name;
	private final String filepath;
	private final int numPlayers;
	private final int width;
	private final int height;
	private final String map;
	
	public String getName() {
		return name;
	}

	public String getFilepath() {
		return filepath;
	}

	public int getNumPlayers() {
		return numPlayers;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeigth() {
		return height;
	}
	
	public String getMap() {
		return map;
	}

	public MapDescription(String name, String filepath, int numPlayers, int width, int height, String map) {
		super();
		this.name = name;
		this.filepath = filepath;
		this.numPlayers = numPlayers;
		this.width = width;
		this.height = height;
		this.map = map;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public String infoString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n  Name: ").append(this.name);
		sb.append("  \n  Number of players: ").append(this.numPlayers);
		sb.append("  \n  Width: ").append(this.width);
		sb.append("  \n  Height: ").append(this.height).append("  \n");
		return sb.toString();
	}
}
