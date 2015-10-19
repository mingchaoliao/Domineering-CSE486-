package domineering;

import java.util.ArrayList;
import java.util.Iterator;

public class DomineeringBoard implements Iterator{
	private char[][] board;
	private boolean isHome;
	private int hashKey;
	private ArrayList<DomineeringMove> moves; //0:i(y1), 1:j(x2), 2:y2, 3:x2
	
	private static char homeSym = AlphaBetaPlayer.homeSym;;
	private static char emptySym = AlphaBetaPlayer.emptySym;
	private static char awaySym = AlphaBetaPlayer.awaySym;
	private static int[][][] zobristArray = AlphaBetaPlayer.zobristArray;
	
	public DomineeringBoard(char[][] board, boolean isHome) {
		this.board = board;
		this.isHome = isHome;
		this.hashKey = 0;
		this.moves = new ArrayList<DomineeringMove>();
		possibleMoves();
	}

	public void possibleMoves() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j] != emptySym) hashKey ^= zobristArray[board[i][j] == homeSym?1:0][i][j];
				if(isHome && (board[i][j] == emptySym) && inRange(i,j+1,board) && (board[i][j+1] == emptySym)) {
					moves.add(new DomineeringMove(i,j,i,j+1));
					
				}
				if(!isHome && (board[i][j] == emptySym) && inRange(i+1,j,board) && (board[i+1][j] == emptySym)) {
					moves.add(new DomineeringMove(i,j,i+1,j));
					
				}
			}
		}
	}
	
	@Override 
	public String toString() {
		return moves.size()+ "\t" + moves.toString();
	}
	
	public void getOrderMoves() {
		//change order of moves
	}
	
	public boolean inRange(int row, int col, char[][] board) {
		return row >= 0 && col >= 0 && row < board.length && col < board[0].length;
	}
	
	@Override
	public boolean hasNext() {
		return moves.size() != 0;
	}

	@Override
	public DomineeringMove next() {
		DomineeringMove temp = (DomineeringMove) moves.get(0).clone();
		moves.remove(0);
		return temp;
	}

	@Override
	public void remove() {}

	public int getHashKey() {
		return hashKey;
	}

	

	
}
