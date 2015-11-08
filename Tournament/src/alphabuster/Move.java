
package alphabuster;
import domineering.*;
import game.*;

public class Move extends DomineeringMove {
	public double score;

	public Move() {}
	
	public Move(Move d) {
		this(d.row1,d.col1,d.row2,d.col2);
	}
	
	public Move(int row1, int col1, int row2, int col2) {
		this.row1 = row1;
		this.col1 = col1;
		this.row2 = row2;
		this.col2 = col2;
	}
	
	public Move(int row1, int col1, int row2, int col2, double score) {
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
	
	public boolean compare(int r1,int c1,int r2,int c2) {
		return r1==row1&&row2==r2&&col1==c1&&col2==c2;
	}

	@Override
	public String toString() { 
		return row1 + " " + col1 + " " + row2 + " " + col2; 
	}
	
	@Override
	public void parseMove(String s) {}
	@Override
	public Object clone() {return new Move(this);}

}