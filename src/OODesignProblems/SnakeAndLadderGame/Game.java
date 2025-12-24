package SnakeAndLadderGame;

public interface Game {
    void startGame();
    void resumeGame();
    void pauseGame();
    void endGame();
    void restartGame();
    void saveGame();
    void loadGame();
    void undoMove();
    void redoMove();
}
