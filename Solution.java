/*
 * Author: Subhasis Debsharma
 * MailID: subhasisdebsharma1@gmail.com
 * Code for Hackerrank 'Simplified Chess Engine' problem in java7.
 * Alpha Beta Pruning algorithm is used to solve the problem.
 * link to the problem: https://www.hackerrank.com/challenges/simplified-chess-engine/problem
 */



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Character{
	String t,colour;
	int c, r;
	private final Map<String,Integer> pos = new HashMap<String, Integer>();
	private Character(){
		pos.put("1", 0);
		pos.put("2", 1);
		pos.put("3", 2);
		pos.put("4", 3);
		pos.put("A", 0);
		pos.put("B", 1);
		pos.put("C", 2);
		pos.put("D", 3);
	}
	public Character(String colour, String t, String c, String r) {
		this();
		this.colour = colour;
		this.t = t;
		this.c = pos.get(c);
		this.r = pos.get(r);
	}
	public Character(Character c){
		this.colour = c.colour;
		this.t = c.t;
		this.c = c.c;
		this.r = c.r;
	}
	/*
	@Override
	public String toString() {
		String str = "|["+r+","+c+"]"+colour+" "+t+"|";
		return str;
	}
	*/
	public List<Character> nextMoves(Character cr, Character[][] board) {
		List<Character> list = new ArrayList<Character>(12);
		int c,r;
		if(cr.t.equals("B")||cr.t.equals("Q")){	//Next moves for Bishop and Queen
			r = cr.r;c = cr.c;
			while(placable(++r,++c,cr.colour,board)){ //up right diagonal
				Character pl = new Character(cr);
				pl.c=c;pl.r=r;
				list.add(pl);
				if(board[r][c]!=null)break;
			}
			r = cr.r;c = cr.c;
			while(placable(++r,--c,cr.colour,board)){ //up left diagonal
				Character pl = new Character(cr);pl.c=c;pl.r=r;list.add(pl);if(board[r][c]!=null)break;
			}
			r = cr.r;c = cr.c;
			while(placable(--r,++c,cr.colour,board)){ //down right diagonal
				Character pl = new Character(cr);pl.c=c;pl.r=r;list.add(pl);if(board[r][c]!=null)break;
			}
			r = cr.r;c = cr.c;
			while(placable(--r,--c,cr.colour,board)){ //down left diagonal
				Character pl = new Character(cr);pl.c=c;pl.r=r;list.add(pl);if(board[r][c]!=null)break;
			}
		}
		if(cr.t.equals("R")||cr.t.equals("Q")){ //Next moves for Rook and Queen
			r = cr.r;c = cr.c;
			while(placable(++r,c,cr.colour,board)){ //go up
				Character pl = new Character(cr);pl.c=c;pl.r=r;list.add(pl);if(board[r][c]!=null)break;
			}
			r = cr.r;c = cr.c;
			while(placable(--r,c,cr.colour,board)){ //go down
				Character pl = new Character(cr);pl.c=c;pl.r=r;list.add(pl);if(board[r][c]!=null)break;
			}
			r = cr.r;c = cr.c;
			while(placable(r,++c,cr.colour,board)){ //go right
				Character pl = new Character(cr);pl.c=c;pl.r=r;list.add(pl);if(board[r][c]!=null)break;
			}
			r = cr.r;c = cr.c;
			while(placable(r,--c,cr.colour,board)){ //go left
				Character pl = new Character(cr);pl.c=c;pl.r=r;list.add(pl);if(board[r][c]!=null)break;
			}
		}
		if(cr.t.equals("N")){ //Next moves for Knight
			r = cr.r;c = cr.c;
			if(placable(r+2,c+1,cr.colour,board)){
				Character pl = new Character(cr);
				pl.c=c+1;pl.r=r+2;
				list.add(pl);
			}
			if(placable(r+1,c+2,cr.colour,board)){
				Character pl = new Character(cr);pl.c=c+2;pl.r=r+1;list.add(pl);
			}
			if(placable(r-1,c+2,cr.colour,board)){
				Character pl = new Character(cr);pl.c=c+2;pl.r=r-1;list.add(pl);
			}
			if(placable(r-2,c+1,cr.colour,board)){
				Character pl = new Character(cr);pl.c=c+1;pl.r=r-2;list.add(pl);
			}
			if(placable(r-2,c-1,cr.colour,board)){
				Character pl = new Character(cr);pl.c=c-1;pl.r=r-2;list.add(pl);
			}
			if(placable(r-1,c-2,cr.colour,board)){
				Character pl = new Character(cr);pl.c=c-2;pl.r=r-1;list.add(pl);
			}
			if(placable(r+1,c-2,cr.colour,board)){
				Character pl = new Character(cr);pl.c=c-2;pl.r=r+1;list.add(pl);
			}
			if(placable(r+2,c-1,cr.colour,board)){
				Character pl = new Character(cr);pl.c=c-1;pl.r=r+2;list.add(pl);
			}
		}
		return list;
	}
	private boolean placable(int i, int j, String colour, Character[][] board) {
		if(i>=0&&i<=3&&j>=0&&j<=3){
			if(board[i][j]==null) return true;
			else if(!board[i][j].colour.equals(colour)) return true;
		}
		return false;
	}
}

public class Solution {
	static int maxDepth;
	
	static int solve(boolean minNode, int depth, int alpha, int beta, List<Character> white, List<Character> black){
		if(depth>maxDepth) return -1;	// Maximum depth reached
		
		if(minNode){ //MIN node
			if(isLost(white)){	// at max node white lost queen. So Black is winner
				return -1; 
			}
			Character[][] board = setBoard(white, black);
			//display(board);
			int best = Integer.MAX_VALUE;
			boolean flag = false;
			for(Character cr : white){
				List<Character> nextPossibleMoves = cr.nextMoves(new Character(cr), board);
				List<Character> wh = new ArrayList<Character>(white);
				List<Character> bl = new ArrayList<Character>(black);
				wh.remove(cr);
				for(Character cc : nextPossibleMoves){
					Character removedChar = null;
					if(board[cc.r][cc.c]!=null){
						removedChar = board[cc.r][cc.c];
						bl.remove(removedChar);
					}
					wh.add(cc);
					int val = solve(false, depth+1, alpha, beta, wh, bl);
					
					best = Math.min(best, val);
					beta = Math.min(beta, best);
					if(beta<=alpha) { // Pruning at MIN node
						flag = true;
						break;
					}
					wh.remove(cc);
					if(removedChar!=null) bl.add(removedChar);
				}
				if(flag) break;
			}
			return best;
			
		}else{//MAX NODE
			if(isLost(black)){	// at max node black lost queen. So White is winner
				return -2; 
			}
			Character[][] board = setBoard(black, white);
			//display(board);
			int best = Integer.MIN_VALUE;
			boolean flag = false;
			for(Character cr: black){
				List<Character> nextPossibleMoves = cr.nextMoves(new Character(cr), board);
				List<Character> wh = new ArrayList<Character>(white);
				List<Character> bl = new ArrayList<Character>(black);
				bl.remove(cr);
				for(Character cc : nextPossibleMoves){
					Character removedChar = null;
					if(board[cc.r][cc.c]!=null){
						removedChar = board[cc.r][cc.c];
						wh.remove(removedChar);
					}
					bl.add(cc);
					int val = solve(true, depth+1, alpha, beta, wh, bl);
					
					best = Math.max(best, val);
					alpha = Math.max(alpha, best);
					if(beta <= alpha){ // Pruning at MAX node
						flag = true;
						break;
					}
					bl.remove(cc);
					if(removedChar!=null) wh.add(removedChar);
				}
				if(flag) break;
			}
			return best;
		}
    }
	
	/* before using this function uncomment the toString() method of Character class.
	private static void display(Character[][] board) {
		for(int i = 3;i>=0;i--){
			for(int j = 0; j<=3;j++)
			{
				System.out.printf("%-10s  ",board[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	*/
	
	private static Character[][] setBoard(List<Character> l1, List<Character> l2) {
		Character[][] board = new Character[4][4];
		for(Character cr : l1){
			board[cr.r][cr.c] = cr;
		}
		for(Character cr : l2){
			board[cr.r][cr.c] = cr;
		}
		return board;
	}

	private static boolean isLost(List<Character> cList) {
		for(Character cr : cList){
			if(cr.t.equals("Q")) return false;
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	int g = Integer.parseInt(reader.readLine());
    	for(int i = 0;i<g;i++){
    		List<Character> white = new ArrayList<Character>(5);
        	List<Character> black = new ArrayList<Character>(5);
        	String[] in = reader.readLine().split(" ");
    		int w = Integer.parseInt(in[0]);
    		int b = Integer.parseInt(in[1]);
    		maxDepth = Integer.parseInt(in[2]);
    		for(int j = 0;j<w;j++){
    			String[] input = reader.readLine().split(" ");
    			Character wp = new Character("w", input[0], input[1], input[2]);
    			white.add(wp);
    		}
    		for(int j = 0;j<b;j++){
    			String[] input = reader.readLine().split(" ");
    			Character bp = new Character("b", input[0], input[1], input[2]);
    			black.add(bp);
    		}
    		int v = solve(true, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, white, black);
    		if(v==-2){
    			System.out.println("YES");
    		}else{
    			System.out.println("NO");
    		}
    	}
    	reader.close();
    }
}