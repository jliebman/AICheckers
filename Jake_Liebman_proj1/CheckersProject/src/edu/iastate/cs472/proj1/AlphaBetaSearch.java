 package edu.iastate.cs472.proj1;
/**
 * 
 * @author Jake Liebman
 *
 */
public class AlphaBetaSearch {
    private CheckersData board;

    // An instance of this class will be created in the Checkers.Board
    // It would be better to keep the default constructor.

    public void setCheckersData(CheckersData board) {
        this.board = board;
    }

    // Todo: You can implement your helper methods here
    //Evaluates board with	 Kings valued at 10, about to be kings at 3, back rank pieces are worth 4 and all else at 1
    private int evaluateGameState(CheckersData boardData)
    {
    	
    	int numReds = 0, numRedKings = 0, numBlacks = 0, numBlackKings = 0;
    	for(int r = 0; r < 8; r++)
    	{
    		for(int c = 0; c < 8; c++)
    		{
    			if(boardData.board[r][c] == CheckersData.RED)
    			{
    				if(r==1)
    				{
    					numReds += 3;
    				}else if( r== 7) {
    					numReds+=4;
    			}else {
    					numReds += 1;
    				}
    			}
    			if(boardData.board[r][c] == CheckersData.BLACK)
    			{
    				if(r== 6)
    				{
    				numBlacks += 3;
    				}else if(r== 0) {
    					numBlacks+=4;
    				} else {
    					numBlacks += 1;
    				}
    			}
    			if(boardData.board[r][c] == CheckersData.RED_KING)
    			{
    				numRedKings++;
    			}
    			if(boardData.board[r][c] == CheckersData.BLACK_KING)
    			{
    				numBlackKings++;
    			}
    		}
    	}
    			
    	return (numReds + (10*numRedKings)) - (numBlacks + (10*numBlackKings));
    }
    private CheckersMove doAlphaBeta(CheckersData board, CheckersMove[] legalMoves, int player, int depth)
    {
    	int bestScore = -10000;
    	CheckersMove bestMove = null;
    	for(CheckersMove m: legalMoves)
    	{
    		CheckersData boardCopy = new CheckersData();
            for(int r = 0; r < 8; r++) {
            	for(int c = 0; c < 8; c++) {
            		boardCopy.board[r][c] = this.board.board[r][c];
            	}
            }
            
            boardCopy.makeMove(m);
            
            int moveScore = prune(boardCopy, boardCopy.getLegalMoves(player), player, 1, depth, -10000, 10000);
            
            if(moveScore > bestScore) {
            	bestScore = moveScore;
            	bestMove = m;
            } 
    	}
    	return bestMove;
    }
    private int prune(CheckersData board, CheckersMove[] legalMoves, int player, int curDepth, int maxDepth, int alpha, int beta) 
    {
    	if(curDepth == maxDepth || legalMoves == null || legalMoves.length == 0)
    	{
    		return evaluateGameState(board);
    	}
    	if(player == CheckersData.BLACK)
    	{
    		int bestScore = 1000;
    		for(CheckersMove m: legalMoves)
        	{
        		CheckersData boardCopy = new CheckersData();
                for(int r = 0; r < 8; r++) {
                	for(int c = 0; c < 8; c++) {
                		boardCopy.board[r][c] = this.board.board[r][c];
                	}
                }
               
                	boardCopy.makeMove(m); 	
                	bestScore = Math.min(prune(boardCopy, boardCopy.getLegalMoves((player+1)%2), (player+1)%2, curDepth+1, maxDepth, alpha, beta), bestScore);
                	beta = Math.min(beta, bestScore);
                
                if(beta <= alpha)
                	break;
        	}
    		return bestScore;
    	} else if(player == CheckersData.RED){
    		int bestScore = -1000;
    		for(CheckersMove m: legalMoves)
        	{
        		CheckersData boardCopy = new CheckersData();
                for(int r = 0; r < 8; r++) {
                	for(int c = 0; c < 8; c++) {
                		boardCopy.board[r][c] = this.board.board[r][c];
                	}
                }
                
                boardCopy.makeMove(m);
                bestScore = Math.max(prune(boardCopy, boardCopy.getLegalMoves((player+1)%2), (player+1)%2, curDepth+1, maxDepth, alpha, beta), bestScore);
                alpha = Math.max(alpha, bestScore);
                
                if(alpha >= beta)
                	break;
        	}	
    	return bestScore;
    	}
    	return evaluateGameState(board);
    	
    }
    /**
     *  You need to implement the Alpha-Beta pruning algorithm here to
     * find the best move at current stage.
     * The input parameter legalMoves contains all the possible moves.
     * It contains four integers:  fromRow, fromCol, toRow, toCol
     * which represents a move from (fromRow, fromCol) to (toRow, toCol).
     * It also provides a utility method `isJump` to see whether this
     * move is a jump or a simple move.
     *
     * @param legalMoves All the legal moves for the agent at current step.
     */
    public CheckersMove makeMove(CheckersMove[] legalMoves) {
        // The checker board state can be obtained from this.board,
        // which is a int 2D array. The numbers in the `board` are
        // defined as
        // 0 - empty square,
        // 1 - red man
        // 2 - red king
        // 3 - black man
        // 4 - black king
        System.out.println(board);
        System.out.println();

        // Todo: return the move for the current state
        CheckersData boardCopy = new CheckersData();
        for(int r = 0; r < 8; r++) {
        	for(int c = 0; c < 8; c++) {
        		boardCopy.board[r][c] = this.board.board[r][c];
        	}
        }
        return doAlphaBeta(boardCopy, legalMoves, CheckersData.RED, 10);
    }
}
