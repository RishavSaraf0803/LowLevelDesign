package TicTacToe.observers;

import TicTacToe.game.TicTacToeGame;

/**
 * Console-based observer that prints game updates to the console
 */
public class ConsoleGameObserver implements GameObserver {
    
    @Override
    public void onGameUpdate(String message, TicTacToeGame game) {
        System.out.println("=== GAME UPDATE ===");
        System.out.println(message);
        System.out.println("Current Board:");
        System.out.println(game.getBoard());
        System.out.println("Game Status: " + game.getGameStatus().getDescription());
        if (game.getCurrentPlayer() != null) {
            System.out.println("Current Player: " + game.getCurrentPlayer().getName());
        }
        System.out.println("==================");
        System.out.println();
    }
}
