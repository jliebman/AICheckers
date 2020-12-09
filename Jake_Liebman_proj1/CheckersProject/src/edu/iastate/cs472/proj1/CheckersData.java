package edu.iastate.cs472.proj1;

import java.util.ArrayList;
/**
 * @author Jake Liebman
 */
/**
 * An object of this class holds data about a game of checkers.
 * It knows what kind of piece is on each square of the checkerboard.
 * Note that RED moves "up" the board (i.e. row number decreases)
 * while BLACK moves "down" the board (i.e. row number increases).
 * Methods are provided to return lists of available legal moves.
 */
public class CheckersData {

  /*  The following constants represent the possible contents of a square
      on the board.  The constants RED and BLACK also represent players
      in the game. */

    static final int
            EMPTY = 0,
            RED = 1,
            RED_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;


    int[][] board;  // board[r][c] is the contents of row r, column c.


    /**
     * Constructor.  Create the board and set it up for a new game.
     */
    CheckersData() {
        board = new int[8][8];
        setUpGame();
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            int[] row = board[i];
            sb.append(8 - i).append(" ");
            for (int n : row) {
                if (n == 0) {
                    sb.append(" ");
                } else if (n == 1) {
                    sb.append(ANSI_RED + "R" + ANSI_RESET);
                } else if (n == 2) {
                    sb.append(ANSI_RED + "K" + ANSI_RESET);
                } else if (n == 3) {
                    sb.append(ANSI_YELLOW + "B" + ANSI_RESET);
                } else if (n == 4) {
                    sb.append(ANSI_YELLOW + "K" + ANSI_RESET);
                }
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        }
        sb.append("  a b c d e f g h");

        return sb.toString();
    }

    /**
     * Set up the board with checkers in position for the beginning
     * of a game.  Note that checkers can only be found in squares
     * that satisfy  row % 2 == col % 2.  At the start of the game,
     * all such squares in the first three rows contain black squares
     * and all such squares in the last three rows contain red squares.
     */
    void setUpGame() {
        // Todo: setup the board with pieces BLACK, RED, and EMPTY
    	for(int r = 0; r < 8; r++) {
    		for(int c = 0; c < 8; c++)
    		{
    			if(r < 3)
    			{
    				if(r % 2 == c % 2)
    				{
    					this.board[r][c] = BLACK;
    				}else {
    					this.board[r][c] = EMPTY; 
    				}
    			} else if (r > 4)
    			{
    				if(r % 2 == c % 2)
    				{
    					this.board[r][c] = RED;
    				}else {
    					this.board[r][c] = EMPTY; 
    				}
    			}else {
    				this.board[r][c] = EMPTY;
    			}
    		}
    	}
    }


    /**
     * Return the contents of the square in the specified row and column.
     */
    int pieceAt(int row, int col) {
        return board[row][col];
    }


    /**
     * Make the specified move.  It is assumed that move
     * is non-null and that the move it represents is legal.
     * @return  true if the piece becomes a king, otherwise false
     */
    boolean makeMove(CheckersMove move) {
        return makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
    }


    /**
     * Make the move from (fromRow,fromCol) to (toRow,toCol).  It is
     * assumed that this move is legal.  If the move is a jump, the
     * jumped piece is removed from the board.  If a piece moves to
     * the last row on the opponent's side of the board, the
     * piece becomes a king.
     *
     * @param fromRow row index of the from square
     * @param fromCol column index of the from square
     * @param toRow   row index of the to square
     * @param toCol   column index of the to square
     * @return        true if the piece becomes a king, otherwise false
     */
    boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Todo: update the board for the given move.
        // You need to take care of the following situations:
        // 1. move the piece from (fromRow,fromCol) to (toRow,toCol)
        // 2. if this move is a jump, remove the captured piece
        // 3. if the piece moves into the kings row on the opponent's side of the board, crowned it as a king
    	
    	board[toRow][toCol] = board[fromRow][fromCol];
    	board[fromRow][fromCol] = EMPTY;
    	CheckersMove move = new CheckersMove(fromRow, fromCol, toRow, toCol);
    	if(move.isJump())
    	{
       		int jumpRow = (fromRow + toRow) / 2;
    		int jumpCol = (fromCol + toCol) / 2;
    		board[jumpRow][jumpCol] = EMPTY;
    	}
    	
    	if(toRow == 0 && board[toRow][toCol] == RED)
    	{
    		board[toRow][toCol] = RED_KING;
    		return true;
    	}
    	if(toRow == 7 && board[toRow][toCol] == BLACK)
    	{
    		board[toRow][toCol] = BLACK_KING;
    		return true;
    	}
    	return false;
    }

    /**
     * Return an array containing all the legal CheckersMoves
     * for the specified player on the current board.  If the player
     * has no legal moves, null is returned.  The value of player
     * should be one of the constants RED or BLACK; if not, null
     * is returned.  If the returned value is non-null, it consists
     * entirely of jump moves or entirely of regular moves, since
     * if the player can jump, only jumps are legal moves.
     *
     * @param player color of the player, RED or BLACK
     */
    CheckersMove[] getLegalMoves(int player) {
        // Todo: Implement your getLegalMoves here.
    	if(player != RED && player != BLACK)
    	{
    		return null;
    	}
    	int king = RED_KING;
    	if(player == BLACK)
    	{
    		king = BLACK_KING;
    	}
    	ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();
    	for(int r = 0; r < 8; r++) {
    		for(int c = 0; c < 8; c++) {
    			if(board[r][c] == player || board[r][c] == king)
    			{
    				if(getLegalJumpsFrom(player, r, c) != null && getLegalJumpsFrom(player, r, c).length != 0)
    				{
    				int rNew = r;
    				int cNew = c;
    				while(getLegalJumpsFrom(player, rNew, cNew) != null) {
    				for (CheckersMove checkersMove : getLegalJumpsFrom(player, rNew, cNew)) {
    						moves.add(checkersMove);
    						rNew = checkersMove.toRow;
    						cNew = checkersMove.toCol;
    				}
					}
    				}
    				
    			}
    		}
    	}
    	if(moves.size() == 0)
    	{
    		for(int r = 0; r < 8; r++) {
        		for(int c = 0; c < 8; c++) {
        			if(board[r][c] == player || board[r][c] == king)
        			{
        				if(isMovable(player, r, c, r+1, c+1))
        				{
        					moves.add(new CheckersMove(r, c, r+1, c+1));
        				}
        				if(isMovable(player, r, c, r-1, c+1))
        				{
        					moves.add(new CheckersMove(r, c, r-1, c+1));
        				}
        				if(isMovable(player, r, c, r+1, c-1))
        				{
        					moves.add(new CheckersMove(r, c, r+1, c-1));
        				}
        				if(isMovable(player, r, c, r-1, c-1))
        				{
        					moves.add(new CheckersMove(r, c, r-1, c-1));
        				}
        				
        			}
        		}
        	}
    	}
    	
    	 if (moves.size() == 0)
             return null;
          else {
             CheckersMove[] allCheckersMoves = new CheckersMove[moves.size()];
             for (int i = 0; i < moves.size(); i++)
               allCheckersMoves[i] = moves.get(i);
             return allCheckersMoves;
          }
    }


    /**
     * Return a list of the legal jumps that the specified player can
     * make starting from the specified row and column.  If no such
     * jumps are possible, null is returned.  The logic is similar
     * to the logic of the getLegalMoves() method.
     *
     * @param player The player of the current jump, either RED or BLACK.
     * @param row    row index of the start square.
     * @param col    col index of the start square.
     */
    CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
        // Todo: Implement your getLegalJumpsFrom here.
    	if(player != RED && player != BLACK)
    	{
    		return null;
    	}
    	int king = RED_KING;
    	if(player == BLACK)
    	{
    		king = BLACK_KING;
    	}
    	ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();
    			if(board[row][col] == player || board[row][col] == king)
    			{
    				if(isJumpable(player, row, col, row+1, col+1, row+2, col+2))
    				{
    					moves.add(new CheckersMove(row, col, row+2, col+2));
    				}
    				if(isJumpable(player, row, col, row-1, col+1, row-2, col+2))
    				{
    					moves.add(new CheckersMove(row, col, row-2, col+2));
    				}
    				if(isJumpable(player, row, col, row+1, col-1, row+2, col-2))
    				{
    					moves.add(new CheckersMove(row, col, row+2, col-2));
    				}
    				if(isJumpable(player, row, col, row-1, col-1, row-2, col-2))
    				{
    					moves.add(new CheckersMove(row, col, row-2, col-2));
    				}
    		    	 if (moves.size() == 0)
    		             return null;
    		          else {
    		             CheckersMove[] allCheckersMoves = new CheckersMove[moves.size()];
    		             for (int i = 0; i < moves.size(); i++)
    		               allCheckersMoves[i] = moves.get(i);
    		             return allCheckersMoves;
    		          }	
    	}
        return null;
    }
    
    
    private boolean isJumpable(int player, int fRow, int fCol, int jRow, int jCol, int tRow, int tCol)
    {
    	if(tRow >= 8 || tRow < 0 || tCol >= 8 || tCol < 0 || board[tRow][tCol] != EMPTY)
    	{
    		return false;
    	}
    	 if (player == RED) {
             if (board[fRow][fCol] == RED && tRow > fRow) {
                 return false;  	 
             }
             if (board[jRow][jCol] != BLACK && board[jRow][jCol] != BLACK_KING)
             {
                 return false; 
             }
             return true;  
          }
          else {
             if (board[fRow][fCol] == BLACK && tRow < fRow) {
            	 return false; 
             }
            
             if (board[jRow][jCol] != RED && board[jRow][jCol] != RED_KING) {
                 return false; 
             }
             return true;
          }
    	}
    private boolean isMovable(int player, int fRow, int fCol, int tRow, int tCol)
    {
    	if(tRow >= 8 || tRow < 0 || tCol >= 8 || tCol < 0 || board[tRow][tCol] != EMPTY)
    	{
    		return false;
    	}
    	
    	if(player == RED)
    	{
    		if(board[fRow][fCol] == RED && tRow > fRow)
    		{
    			return false;
    		}
    		return true;
    	} else {
    		if(board[fRow][fCol] == BLACK && tRow < fRow)
    		{
    			return false;
    		}
    		return true;
    	}
    }

}