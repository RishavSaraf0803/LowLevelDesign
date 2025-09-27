package TicTacToe.observers;

import TicTacToe.game.TicTacToeGame;

/**
 * Observer interface for game events
 */
public interface GameObserver {
    void onGameUpdate(String message, TicTacToeGame game);
}
