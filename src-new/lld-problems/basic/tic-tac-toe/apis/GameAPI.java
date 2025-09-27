package TicTacToe.apis;

import TicTacToe.data.CellValue;
import TicTacToe.data.Position;
import TicTacToe.game.TicTacToeGame;
import TicTacToe.players.Player;

/**
 * API interface for Tic Tac Toe game operations
 */
public interface GameAPI {
    
    /**
     * Start a new game
     */
    void startGame();
    
    /**
     * Make a move for a player
     * @param position The position to make the move
     * @param player The player making the move
     * @return true if move was successful, false otherwise
     */
    boolean makeMove(Position position, Player player);
    
    /**
     * Get the current game status
     * @return Current game status
     */
    String getGameStatus();
    
    /**
     * Get the current board state
     * @return String representation of the board
     */
    String getBoardState();
    
    /**
     * Add a player to the game
     * @param player The player to add
     * @return true if player was added successfully
     */
    boolean addPlayer(Player player);
    
    /**
     * Reset the game
     */
    void resetGame();
    
    /**
     * Get the winner of the game
     * @return Winner player, or null if no winner yet
     */
    Player getWinner();
    
    /**
     * Check if the game is over
     * @return true if game is over
     */
    boolean isGameOver();
}
