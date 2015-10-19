
package domineering;
import game.*;

public class DomineeringMove extends GameMove {
	public int row1, col1;
	public int row2, col2;
	public double score;

	public DomineeringMove() {}
	
	public DomineeringMove(DomineeringMove d) {
		this(d.row1,d.col1,d.row2,d.col2);
	}
	
	public DomineeringMove(int row1, int col1, int row2, int col2) {
		this.row1 = row1;
		this.col1 = col1;
		this.row2 = row2;
		this.col2 = col2;
	}
	
	public DomineeringMove(int row1, int col1, int row2, int col2, double score) {
		this.row1 = row1;
		this.col1 = col1;
		this.row2 = row2;
		this.col2 = col2;
		this.score = score;
	}

	public void set(int row1, int col1, int row2, int col2) {
		this.row1 = row1;
		this.col1 = col1;
		this.row2 = row2;
		this.col2 = col2;
	}
	
	public void set(int row1, int col1, int row2, int col2, double score) {
		this.row1 = row1;
		this.col1 = col1;
		this.row2 = row2;
		this.col2 = col2;
		this.score = score;
	}

	@Override
	public String toString() { 
		return row1 + " " + col1 + " " + row2 + " " + col2; 
	}
	
	@Override
	public void parseMove(String s) {}
	@Override
	public Object clone() {return new DomineeringMove(this);}

}