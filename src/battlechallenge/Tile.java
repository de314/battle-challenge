package battlechallenge;

public class Tile {
	
	boolean hit;
	boolean miss;
	boolean boat;
	boolean sunken;
	
	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public boolean isMiss() {
		return miss;
	}

	public void setMiss(boolean miss) {
		this.miss = miss;
	}

	public boolean isBoat() {
		return boat;
	}

	public void setBoat(boolean boat) {
		this.boat = boat;
	}

	public boolean isSunken() {
		return sunken;
	}

	public void setSunken(boolean sunken) {
		this.sunken = sunken;
	}
	
	public boolean isGuessed() {
		return (hit || miss);
	}
}
