package TicTacToe.data;

/**
 * Represents the current status of the Tic Tac Toe game
 */
public enum GameStatus {
    WAITING_FOR_PLAYERS("Waiting for players to join"),
    IN_PROGRESS("Game in progress"),
    PLAYER_X_WON("Player X won!"),
    PLAYER_O_WON("Player O won!"),
    DRAW("Game ended in a draw"),
    GAME_OVER("Game over");
    
    private final String description;
    
    GameStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isGameOver() {
        return this == PLAYER_X_WON || this == PLAYER_O_WON || this == DRAW || this == GAME_OVER;
    }
    
    public boolean isGameInProgress() {
        return this == IN_PROGRESS;
    }
}
