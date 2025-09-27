package TicTacToe;

import TicTacToe.managers.GameManager;

/**
 * Main entry point for the Tic Tac Toe game
 */
public class TicTacToeMain {
    
    public static void main(String[] args) {
        System.out.println("Starting Tic Tac Toe Game...");
        
        GameManager gameManager = new GameManager();
        
        try {
            gameManager.startGame();
        } catch (Exception e) {
            System.err.println("Error during game: " + e.getMessage());
            e.printStackTrace();
        } finally {
            gameManager.close();
        }
    }
}
