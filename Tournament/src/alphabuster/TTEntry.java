package alphabuster;

public class TTEntry {
	public static enum NT {LOWERBOUND,UPPERBOUNT,EXACT}; 
	
	private long hashkey;
	private double score;
	private NT type;
	private int depth;
	
	public TTEntry(long hashkey, double score, NT type,int depth) {
		this.hashkey = hashkey;
		this.score = score;
		this.type = type;
		this.depth = depth;
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof TTEntry)) return false;
		TTEntry d = (TTEntry) o;
		return d.hashkey == hashkey;
	}

	public long getHashkey() {
		return hashkey;
	}

	public void setHashkey(long hashkey) {
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
