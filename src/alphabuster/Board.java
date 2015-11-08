package alphabuster;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import domineering.*;

public class Board implements Iterator{
	private char[][] board;
	private boolean isHome;
	private long hashKey;
	public static Random rnd = new Random();
	private ArrayList<Move> moves; //0:i(y1), 1:j(x2), 2:y2, 3:x2
	
	private static char homeSym = DomineeringState.homeSym;;
	private static char emptySym = DomineeringState.emptySym;
	private static char awaySym = DomineeringState.awaySym;
	private String playerType;
	private static long[][][] zobristArray = AlphaBuster.zobristArray;
	
	public Board(char[][] board, boolean isHome) {
		this(board,isHome,"alphabeta");
		
	}
	
	public Board(char[][] board, boolean isHome, String playType) {
		this.board = board;
		this.isHome = isHome;
		this.hashKey = 0;
		this.moves = new ArrayList<Move>();
		this.playerType = playType;
		possibleMoves();
		
	}

	public void possibleMoves() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(playerType.equals("alphabetatt") && board[i][j] != emptySym) hashKey ^= zobristArray[i][j][board[i][j] == homeSym?1:0];
				if(isHome && (board[i][j] == emptySym) && inRange(i,j+1,board) && (board[i][j+1] == emptySym)) {
					moves.add(new Move(i,j,i,j+1));
					moves.get(moves.size()-1).score = evl(moves.get(moves.size()-1));
					
				}
				if(!isHome && (board[i][j] == '.') && inRange(i+1,j,board) && (board[i+1][j] == '.')) {
					moves.add(new Move(i,j,i+1,j));
					
				}
			}
		}
	}
	
	private double evl(Move dm) {
		board[dm.row1][dm.col1] = isHome?homeSym:awaySym;
		board[dm.row2][dm.col2] = isHome?homeSym:awaySym;
		int realH = 0, realV = 0, safeH = 0, safeV = 0;
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if((board[i][j] == emptySym) && inRange(i,j+1,board) && (board[i][j+1] == emptySym)) {
					realH++;
					if((!inRange(i-1,j,board) && !inRange(i-1,j+1,board)) || (board[i-1][j] != emptySym && board[i-1][j+1] != emptySym)) {
						if((!inRange(i+1,j,board) && !inRange(i+1,j+1,board)) || (board[i+1][j] != emptySym && board[i+1][j+1] != emptySym)) {
							safeH++;
						}
					}
				}

				if((board[i][j] == emptySym) && inRange(i+1,j,board) && (board[i+1][j] == emptySym)) {
					realV++;
					if((!inRange(i,j-1,board) && !inRange(i+1,j-1,board)) || (board[i][j-1] != emptySym && board[i+1][j-1] != emptySym)) {
						if((!inRange(i,j+1,board) && !inRange(i+1,j+1,board)) || (board[i][j+1] != emptySym && board[i+1][j+1] != emptySym)) {
							safeV++;
						}
					}
				}
			}
		}
		board[dm.row1][dm.col1] = emptySym;
		board[dm.row2][dm.col2] = emptySym;
		if(isHome) {
			if(realH == 0) return -999999;
			return realH-realV+safeH-safeV;
		} else {
			if(realV == 0) return 999999;
			return realV-realH+safeV-safeH;
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
	
	public int getSize() {
		return moves.size();
	}

	@Override
	public Move next() {
		int pos;
		
		//pos = random(); //random choice
		pos = best(); //possible best
		//pos = 0; //FIFO
		
		Move temp = (Move) moves.get(pos).clone();
		moves.remove(pos);
		return temp;
	}
	
	//testing======================
	public int best() {
		int pos = -1;
		double v;
		if(isHome) v = Double.NEGATIVE_INFINITY;
		else v = Double.POSITIVE_INFINITY;
		for(int i = 0; i < moves.size();i++) {
			if((isHome && moves.get(i).score > v)||(!isHome && moves.get(i).score < v)) {
				v = moves.get(i).score;
				pos = i;
			}
			
		}
		return pos;
	}
	
	public int random() {
		return rnd.nextInt(moves.size());
	}
	
	//==============================

	@Override
	public void remove() {}

	public long getHashKey() {
		return hashKey;
	}

	
}
