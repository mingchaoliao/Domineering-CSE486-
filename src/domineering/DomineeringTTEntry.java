package domineering;

public class DomineeringTTEntry {
	public static enum NT {LOWERBOUND,UPPERBOUNT,EXACT}; 
	
	private int hashkey;
	private double score;
	private NT type;
	private int depth;
	
	public DomineeringTTEntry(int hashkey, double score, NT type,int depth) {
		this.hashkey = hashkey;
		this.score = score;
		this.type = type;
		this.depth = depth;
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof DomineeringTTEntry)) return false;
		DomineeringTTEntry d = (DomineeringTTEntry) o;
		return d.hashkey == hashkey;
	}

	public int getHashkey() {
		return hashkey;
	}

	public void setHashkey(int hashkey) {
		this.hashkey = hashkey;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public NT getType() {
		return type;
	}

	public void setType(NT type) {
		this.type = type;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	
	
	
}
