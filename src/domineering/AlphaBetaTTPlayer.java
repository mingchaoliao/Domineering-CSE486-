package domineering;


import java.util.Random;

import domineering.DomineeringTTEntry.NT;
import game.*;

public class AlphaBetaTTPlayer extends GamePlayer {
	public static final int FIRST_LAYER_DEBUG = 1;

	public long total = 0;
	public int MAX_DEPTH = -1;
	public static final double MAX_SCORE = 999999;
	boolean isHome;
	public static char homeSym;
	public static char emptySym;
	public static char awaySym;
	public static int[][][] zobristArray;
	private DomineeringTT tt;

	public static void main(String [] args) {
		GamePlayer p = new AlphaBetaTTPlayer("liaom_Alpha_Beta_TT"); //create player with nikename
		p.compete(args, 1);
	}

	public AlphaBetaTTPlayer(String n) {
		super(n, "Domineering"); //n: nikename
		zobristArray = new int[2][8][8];
		initZobristArray(zobristArray);
		tt = new DomineeringTT(6000000);
	}

	private void initZobristArray(int[][][] zobristArray) {
		Random rnd = new Random(System.currentTimeMillis());
		for(int i = 0; i < zobristArray.length; i++) {
			for(int j = 0; j < zobristArray[0].length; j++) {
				for(int k = 0; k < zobristArray[0][0].length; k++) {
					zobristArray[i][j][k] = rnd.nextInt(Integer.MAX_VALUE/2);
				}
			}
		}
	}

	public GameMove getMove(GameState state, String lastMove) {
		DomineeringState board = (DomineeringState)state;
		homeSym = board.homeSym;
		awaySym = board.awaySym;
		emptySym = board.emptySym;
		isHome = state.who == GameState.Who.HOME; //HOME: true, AWAY: false
		if(isHome) {
			MAX_DEPTH = 8;
		} else {
			MAX_DEPTH = 7;
		}
		DomineeringMove[] stack = new DomineeringMove[MAX_DEPTH+1]; // stack, first 4 are row1,col1,row2,col2, this fifth is the best score
		init(stack);
		long start = System.currentTimeMillis();
		alphabetaTT(board.board,0,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,isHome,stack); 
		long end = System.currentTimeMillis();
		total += (end-start);
		System.out.printf("%s %3d Time: %-4d Total:%d\n",stack[0],(int) stack[0].score,(end-start),total);
		return stack[0];
	}

	public void alphabetaTT(char[][] map, int d, double a, double b, boolean isHome, DomineeringMove[] stack) {
		double ev_tem = 0;
		ev_tem = ev2(map,isHome);
		if(d == MAX_DEPTH || Math.abs(ev_tem) == MAX_SCORE) {
			stack[d].score = ev_tem;
			return ;
		}
		DomineeringBoard board = new DomineeringBoard(map,isHome,"alphabetatt");
		DomineeringTTEntry tte = tt.getEntry(board.getHashKey());
		if(tte != null && tte.getDepth()<=d) {
			if(tte.getType() == DomineeringTTEntry.NT.EXACT) {
				stack[d].score = tte.getScore();
				return;
			}
			if(tte.getType() == DomineeringTTEntry.NT.LOWERBOUND && tte.getScore() > a) a = tte.getScore();
			else if(tte.getType() == DomineeringTTEntry.NT.UPPERBOUNT && tte.getScore() < b) b = tte.getScore();
			if(a >= b) {
				stack[d].score = tte.getScore();
				return;
			}
		}
		
		if(board.getSize() == 0) System.out.println(isHome+"\t"+d);
		DomineeringMove move;
		if(isHome) {
			double v = Double.NEGATIVE_INFINITY;
			while(board.hasNext()) {
				if(FIRST_LAYER_DEBUG==1 && d==0) System.out.println("Home: 0 Total: "+board.getSize());
				move = board.next();
				map[move.row1][move.col1] = homeSym;
				map[move.row2][move.col2] = homeSym;
				if(d+1==MAX_DEPTH) {
					stack[d+1].set(move.row1, move.col1, move.row2, move.col2);
				}
				alphabetaTT(map,d+1,a,b,false,stack);
				if(stack[d+1].score > v) {
					v = stack[d+1].score;
					stack[d].set(move.row1, move.col1, move.row2, move.col2, v);
				}
				map[move.row1][move.col1] = emptySym;
				map[move.row2][move.col2] = emptySym;
				a = Math.max(a, stack[d].score);
				if(stack[d].score <= a) tt.store(new DomineeringTTEntry(board.getHashKey(),stack[d].score,DomineeringTTEntry.NT.LOWERBOUND,d)); 
				else if(stack[d].score >= b) tt.store(new DomineeringTTEntry(board.getHashKey(),stack[d].score,DomineeringTTEntry.NT.UPPERBOUNT,d)); 
				else tt.store(new DomineeringTTEntry(board.getHashKey(),stack[d].score,DomineeringTTEntry.NT.EXACT,d));
				if(stack[d].score>=b || stack[d].score== MAX_SCORE) return;
			}
			return;
		} else {
			double v = Double.POSITIVE_INFINITY;
			while(board.hasNext()) {
				if(FIRST_LAYER_DEBUG==1 && d==0) System.out.println("Away: 0 Total: "+board.getSize());
				move = board.next();
				map[move.row1][move.col1] = awaySym;
				map[move.row2][move.col2] = awaySym;
				if(d+1==MAX_DEPTH) {
					stack[d+1].set(move.row1, move.col1, move.row2, move.col2);
				}
				alphabetaTT(map,d+1,a,b,true,stack);
				if(stack[d+1].score<v) {
					v = stack[d+1].score;
					stack[d].set(move.row1, move.col1, move.row2, move.col2, v);
				}
				map[move.row1][move.col1] = emptySym;
				map[move.row2][move.col2] = emptySym;
				b = Math.min(b, stack[d].score);
				if(stack[d].score <= a) tt.store(new DomineeringTTEntry(board.getHashKey(),stack[d].score,DomineeringTTEntry.NT.LOWERBOUND,d)); 
				else if(stack[d].score >= b) tt.store(new DomineeringTTEntry(board.getHashKey(),stack[d].score,DomineeringTTEntry.NT.UPPERBOUNT,d)); 
				else tt.store(new DomineeringTTEntry(board.getHashKey(),stack[d].score,DomineeringTTEntry.NT.EXACT,d));
				if(stack[d].score<=a || stack[d].score==-MAX_SCORE) return;
			}
			return;
		}
	}

	public double ev2(char[][] board, boolean isHome) {
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
		if(isHome) {
			if(realH == 0) return -MAX_SCORE;
			return realH-realV+safeH-safeV;
		} else {
			if(realV == 0) return MAX_SCORE;
			return realV-realH+safeV-safeH;
		}
	}

	public boolean inRange(int row, int col, char[][] board) {
		return row >= 0 && col >= 0 && row < board.length && col < board[0].length;
	}

	public void init(DomineeringMove[] stack) {
		for(int i = 0; i < stack.length; i++) {
			stack[i] = new DomineeringMove(0,0,0,0,0);
		}
	}

}
