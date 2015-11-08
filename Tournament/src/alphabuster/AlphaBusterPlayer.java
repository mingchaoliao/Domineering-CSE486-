package alphabuster;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import alphabuster.TTEntry.NT;
import domineering.*;
import game.*;

public class AlphaBusterPlayer extends GamePlayer {
	public static final int FIRST_LAYER_DEBUG = 1;

	public long total = 0;
	public int MAX_DEPTH = -1;
	public static final double MAX_SCORE = 999999;
	boolean isHome;
	public static char homeSym;
	public static char emptySym;
	public static char awaySym;
	public static long[][][] zobristArray;
	private TT tt;

	public static void main(String [] args) throws FileNotFoundException {
		GamePlayer p = new AlphaBusterPlayer("Alpha Buster Player"); //create player with nikename
		p.compete(args, 1);
	}

	public AlphaBusterPlayer(String n) throws FileNotFoundException {
		super(n, "Domineering"); //n: nikename
		zobristArray = new long[8][8][2];
		initZobristArray(zobristArray);
		tt = new TT(1560241);
	}

	private void initZobristArray(long[][][] zobristArray) throws FileNotFoundException {
		Scanner sc = new Scanner(new File("zobrist.txt"));
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				for(int k = 0; k < 2; k++) {
					zobristArray[i][j][k] = sc.nextLong();
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
			MAX_DEPTH = 6;
			if(board.numMoves>=6) MAX_DEPTH=8;
			if(board.numMoves>=12) MAX_DEPTH=10;
			if(board.numMoves>=14) MAX_DEPTH=12;
		} else {
			MAX_DEPTH = 7;
			if(board.numMoves>=6) MAX_DEPTH=9;
			if(board.numMoves>=12) MAX_DEPTH=11;
			if(board.numMoves>=14) MAX_DEPTH=13;
		}
		
		Move[] stack = new Move[MAX_DEPTH+1]; 
		init(stack);
		
		if(isHome == true) {
			if(board.numMoves == 0) {
				Move[] temp = new Move[4];
				temp[0] = new Move(1,2,1,3);
				temp[1] = new Move(1,4,1,5);
				temp[2] = new Move(6,2,6,3);
				temp[3] = new Move(6,4,6,5);
				Random rnd = new Random();
				return temp[rnd.nextInt(4)];
			} else if(board.numMoves == 2) {
				Map map = new HashMap();
				Scanner sc;
				try {
					sc = new Scanner(new File("home3.txt"));
					while(sc.hasNextLine()) {
						String s = sc.nextLine();
						String[] sp = s.split("\\s+");
						long key = Long.parseLong(sp[0]);
						Move value = new Move(Integer.parseInt(sp[1]),Integer.parseInt(sp[2]),Integer.parseInt(sp[3]),Integer.parseInt(sp[4]));
						map.put(key, value);
					}
					long hash = new Board(board.board, isHome, "alphabetatt").getHashKey();
					Move rtn = (Move) map.get(hash);
					if(rtn != null) return rtn;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		} else {
			if(board.numMoves == 1) {
				Map map = new HashMap();
				Scanner sc;
				try {
					sc = new Scanner(new File("away2.txt"));
					while(sc.hasNextLine()) {
						String s = sc.nextLine();
						String[] sp = s.split("\\s+");
						long key = Long.parseLong(sp[0]);
						Move value = new Move(Integer.parseInt(sp[1]),Integer.parseInt(sp[2]),Integer.parseInt(sp[3]),Integer.parseInt(sp[4]));
						map.put(key, value);
					}
					long hash = new Board(board.board, isHome, "alphabetatt").getHashKey();
					Move rtn = (Move) map.get(hash);
					if(rtn != null) return rtn;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		long start = System.currentTimeMillis();
		alphabetaTT(board.board,0,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,isHome,stack); 
		long end = System.currentTimeMillis();
		
		total += (end-start);
		System.out.printf("%s %3d Time: %-4d Total:%d\n",stack[0],(int) stack[0].score,(end-start),total);
		tt.print();
		
		System.out.println("Move: "+board.numMoves);
		
		return stack[0];
	}

	public void alphabetaTT(char[][] map, int d, double a, double b, boolean isHome, Move[] stack) {
		double ev_tem = 0;
		ev_tem = ev2(map,isHome);
		if(d == MAX_DEPTH || Math.abs(ev_tem) == MAX_SCORE) {
			stack[d].score = ev_tem;
			return ;
		}
		Board board = new Board(map,isHome,"alphabetatt");
		TTEntry tte = tt.getEntry(board.getHashKey());
		if(tte != null && tte.getDepth()<=d) {
			if(tte.getType() == TTEntry.NT.EXACT) {
				stack[d].score = tte.getScore();
				return;
			}
			if(tte.getType() == TTEntry.NT.LOWERBOUND && tte.getScore() > a) a = tte.getScore();
			else if(tte.getType() == TTEntry.NT.UPPERBOUNT && tte.getScore() < b) b = tte.getScore();
			if(a >= b) {
				stack[d].score = tte.getScore();
				return;
			}
		}
		
		if(board.getSize() == 0) System.out.println(isHome+"\t"+d);
		Move move;
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
				
				if(stack[d].score>=b || stack[d].score== MAX_SCORE) return;
			}
			if(stack[d].score < a) tt.store(new TTEntry(board.getHashKey(),stack[d].score,TTEntry.NT.LOWERBOUND,d)); 
			else if(stack[d].score > b) tt.store(new TTEntry(board.getHashKey(),stack[d].score,TTEntry.NT.UPPERBOUNT,d)); 

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
				
				if(stack[d].score<=a || stack[d].score==-MAX_SCORE) return;
			}
			if(stack[d].score < a) tt.store(new TTEntry(board.getHashKey(),stack[d].score,TTEntry.NT.LOWERBOUND,d)); 
			else if(stack[d].score > b) tt.store(new TTEntry(board.getHashKey(),stack[d].score,TTEntry.NT.UPPERBOUNT,d)); 
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

	public void init(Move[] stack) {
		for(int i = 0; i < stack.length; i++) {
			stack[i] = new Move(0,0,0,0,0);
		}
	}

}
