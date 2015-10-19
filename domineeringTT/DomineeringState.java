

package domineering;
import game.*;

public class DomineeringState extends GameState {
	public static final Params gameParams = new Params(CONFIG_DIR + "domineering.txt");
	public static final int ROWS = gameParams.integer("ROWS");
	public static final int COLS = gameParams.integer("COLS");
	public static final char homeSym = gameParams.character("HOMESYM");
	public static final char awaySym = gameParams.character("AWAYSYM");
	public static final char emptySym = gameParams.character("EMPTYSYM");

	public char [][] board;

	public DomineeringState() {
		super();
		board = new char [ROWS][COLS];
		reset();
	}
	
	public void reset() {
		clear();
		for (int r=0; r<ROWS; r++) {
			for (int c=0; c<COLS; c++) {
				board[r][c] = emptySym;
			}
		}
	}
	
	public void parseMsgString(String s) {
		reset();
		Util.parseMsgString(s, board, emptySym);
		parseMsgSuffix(s.substring(s.indexOf('[')));
	}

	@Override
	public Object clone() {return null;}
	@Override
	public boolean makeMove(GameMove mv) {return false;}
	@Override
	public String toString() {return null;}
	@Override
	public String msgString() {return null;}
	@Override
	public boolean moveOK(GameMove mv) {return false;}
	
}
