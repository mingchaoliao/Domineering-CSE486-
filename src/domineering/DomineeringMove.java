
package domineering;
import game.*;

public class DomineeringMove extends GameMove {
	public int row1, col1;
	public int row2, col2;

	public DomineeringMove() {
		super();
	}
	
	@Override
	public String toString() { 
		return row1 + " " + col1 + " " + row2 + " " + col2; 
	}
	
	@Override
	public void parseMove(String s) {}
	@Override
	public Object clone() {return null;}

}