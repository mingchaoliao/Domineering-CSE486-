package domineering;

import game.*;

import java.util.*;

public class AlphaBetaPlayer extends GamePlayer {

	final int MAX_DEPTH = 7;
	public static final double MAX_SCORE = 100000;
	boolean isHome;
	
	public static void main(String [] args) {
		GamePlayer p = new AlphaBetaPlayer("liaom"); //create player with nikename
		p.compete(args, 1);
	}
	
	public AlphaBetaPlayer(String n) {
		super(n, "Domineering"); //n: nikename
	}
	
	public GameMove getMove(GameState state, String lastMove) {
		DomineeringState board = (DomineeringState)state;
		isHome = state.who == GameState.Who.HOME; //HOME: true, AWAY: false
		DomineeringMove mve = new DomineeringMove();

		double[][] stack = new double[MAX_DEPTH+1][5]; // stack, first 4 are row1,col1,row2,col2, this fifth is the best score
		
		alphabeta(board.board,0,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,isHome,stack); //start alpha beta search
		
		//parse move
		mve.row1 = (int) stack[0][0];
		mve.col1 = (int) stack[0][1];
		mve.row2 = (int) stack[0][2];
		mve.col2 = (int) stack[0][3];
		
		//return move
		return mve;
	}
	public void alphabeta(char[][] map, int d, double a, double b, boolean isHome, double[][] stack) {
		if(d == MAX_DEPTH || Math.abs(h(map,isHome)) == MAX_SCORE) {
			stack[d][4]=h(map,isHome);
			return ;
		}

		if(isHome) {
			double v = Double.NEGATIVE_INFINITY;
			for(int i = 0; i < map.length; i++) {
				for(int j = 0; j < map[0].length; j++) {
					if(map[i][j]=='.'&&inRange(i,j+1,map)&&map[i][j+1]=='.') {
						map[i][j] = 'x';
						map[i][j+1] = 'x';
						if(d+1==MAX_DEPTH) {
							stack[d+1][0] = i;
							stack[d+1][1] = j;
							stack[d+1][2] = i;
							stack[d+1][3] = j+1;
						}
						alphabeta(map,d+1,a,b,false,stack);
						if(stack[d+1][4] > v) {
							v = stack[d+1][4];
							stack[d][4] = v;
							stack[d][0] = i;
							stack[d][1] = j;
							stack[d][2] = i;
							stack[d][3] = j+1;

						}
						map[i][j] = '.';
						map[i][j+1] = '.';
						a = Math.max(a, stack[d][4]);
						if(stack[d][4]>=b || stack[d][4]==200) return;
					}
				}
			}
			return;
		} else {
			double v = Double.POSITIVE_INFINITY;
			for(int i = 0; i < map.length; i++) {
				for(int j = 0; j < map[0].length; j++) {
					if(map[i][j]=='.'&&inRange(i+1,j,map)&&map[i+1][j]=='.') {
						map[i][j] = 'x';
						map[i+1][j] = 'x';
						if(d+1==MAX_DEPTH) {
							stack[d+1][0] = i;
							stack[d+1][1] = j;
							stack[d+1][2] = i+1;
							stack[d+1][3] = j;
						}
						alphabeta(map,d+1,a,b,true,stack);
						if(stack[d+1][4]<v) {
							v = stack[d+1][4];
							stack[d][4] = v;
							stack[d][0] = i;
							stack[d][1] = j;
							stack[d][2] = i+1;
							stack[d][3] = j;
						}
						map[i][j] = '.';
						map[i+1][j] = '.';
						b = Math.min(b, stack[d][4]);
						if(stack[d][4]<=a || stack[d][4]==-200) return;
					}
				}
			}
			return;
		}
	}

	//evaluation function
	public double h(char[][] board, boolean isHome) {
		double h = 0;
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(isHome) {
					if((board[i][j] == '.') && inRange(i,j+1,board) && (board[i][j+1] == '.')) h++;
				} else {
					if((board[i][j] == '.') && inRange(i+1,j,board) && (board[i+1][j] == '.')) h++;
				}
			}
		}
		if(h == 0) {
			if(isHome) {
				h = -MAX_SCORE;
			} else {
				h = MAX_SCORE;
			}
		} else {
			h *= ((isHome) ? 1 : -1);
		}
		return h;

	}

	public boolean inRange(int row, int col, char[][] board) {
		return row >= 0 && col >= 0 && row < board.length && col < board[0].length;
	}

}
