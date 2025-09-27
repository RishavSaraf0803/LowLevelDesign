package TicTacToe.apis;

import TicTacToe.data.Position;
import TicTacToe.game.TicTacToeGame;
import TicTacToe.players.Player;

/**
 * Concrete implementation of GameAPI
 */
public class TicTacToeGameAPI implements GameAPI {
    private final TicTacToeGame game;
    
    public TicTacToeGameAPI() {
        this.game = new TicTacToeGame();
    }
    
    @Override
    public void startGame() {
        // Game starts when players are added
        System.out.println("Game initialized. Add players to start.");
    }
    
    @Override
    public boolean makeMove(Position position, Player player) {
        try {
            return game.makeMove(position, player);
        } catch (Exception e) {
            System.err.println("Error making move: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public String getGameStatus() {
        return game.getGameStatus().getDescription();
    }
    
    @Override
    public String getBoardState() {
        return game.getBoard().toString();
    }
    
    @Override
    public boolean addPlayer(Player player) {
        try {
            game.addPlayer(player);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding player: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public void resetGame() {
        game.reset();
    }
    
    @Override
    public Player getWinner() {
        return game.getWinner();
    }
    
    @Override
    public boolean isGameOver() {
        return game.isGameOver();
    }
    
    // Additional methods for advanced usage
    public TicTacToeGame getGame() {
        return game;
    }
    
    public Player getCurrentPlayer() {
        return game.getCurrentPlayer();
    }
}
