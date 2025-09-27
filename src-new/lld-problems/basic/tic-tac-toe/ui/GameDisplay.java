package TicTacToe.ui;

import TicTacToe.data.GameStatus;
import TicTacToe.game.Board;
import TicTacToe.game.TicTacToeGame;
import TicTacToe.players.Player;

/**
 * Handles the display of the Tic Tac Toe game
 */
public class GameDisplay {
    
    public void displayWelcome() {
        System.out.println("========================================");
        System.out.println("    Welcome to Tic Tac Toe Game!      ");
        System.out.println("========================================");
        System.out.println();
    }
    
    public void displayBoard(Board board) {
        System.out.println("Current Board:");
        System.out.println(board);
    }
    
    public void displayGameStatus(TicTacToeGame game) {
        System.out.println("Game Status: " + game.getGameStatus().getDescription());
        
        if (game.getCurrentPlayer() != null) {
            System.out.println("Current Player: " + game.getCurrentPlayer().getName() + 
                             " (" + game.getCurrentPlayer().getSymbol().getSymbol() + ")");
        }
        
        if (game.getWinner() != null) {
            System.out.println("Winner: " + game.getWinner().getName() + " (" + 
                             game.getWinner().getSymbol().getSymbol() + ")");
        }
    }
    
    public void displayPlayers(TicTacToeGame game) {
        System.out.println("Players:");
        for (Player player : game.getPlayers()) {
            System.out.println("- " + player.getName() + " (" + player.getSymbol().getSymbol() + ")");
        }
        System.out.println();
    }
    
    public void displayMoveHistory(TicTacToeGame game) {
        System.out.println("Move History:");
        for (int i = 0; i < game.getMoveHistory().size(); i++) {
            System.out.println((i + 1) + ". " + game.getMoveHistory().get(i));
        }
        System.out.println();
    }
    
    public void displayInstructions() {
        System.out.println("Instructions:");
        System.out.println("- Enter moves as 'row column' (e.g., '1 2')");
        System.out.println("- Valid positions are 0-2 for both row and column");
        System.out.println("- First to get 3 in a row (horizontal, vertical, or diagonal) wins!");
        System.out.println();
    }
    
    public void displayGameOver(TicTacToeGame game) {
        System.out.println("========================================");
        System.out.println("           GAME OVER!                  ");
        System.out.println("========================================");
        
        displayGameStatus(game);
        displayBoard(game.getBoard());
        displayMoveHistory(game);
        
        System.out.println("Thank you for playing!");
    }
    
    public void displayError(String message) {
        System.out.println("ERROR: " + message);
    }
    
    public void displayMessage(String message) {
        System.out.println(message);
    }
}
