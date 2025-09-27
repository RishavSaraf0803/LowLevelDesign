package TicTacToe.game;

import TicTacToe.data.*;
import TicTacToe.observers.GameObserver;
import TicTacToe.players.Player;
import TicTacToe.validators.MoveValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Tic Tac Toe game class that manages the game state and flow
 */
public class TicTacToeGame {
    private final Board board;
    private final List<Player> players;
    private final List<Move> moveHistory;
    private final List<GameObserver> observers;
    private final MoveValidator moveValidator;
    
    private GameStatus gameStatus;
    private int currentPlayerIndex;
    private Player winner;
    
    public TicTacToeGame() {
        this.board = new Board();
        this.players = new ArrayList<>();
        this.moveHistory = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.moveValidator = new MoveValidator();
        this.gameStatus = GameStatus.WAITING_FOR_PLAYERS;
        this.currentPlayerIndex = 0;
    }
    
    public void addPlayer(Player player) {
        if (players.size() >= 2) {
            throw new IllegalStateException("Game already has 2 players");
        }
        players.add(player);
        
        if (players.size() == 2) {
            gameStatus = GameStatus.IN_PROGRESS;
            notifyObservers("Game started! " + players.get(0).getName() + " vs " + players.get(1).getName());
        }
    }
    
    public boolean makeMove(Position position, Player player) {
        if (gameStatus != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game is not in progress");
        }
        
        if (!isCurrentPlayer(player)) {
            throw new IllegalArgumentException("Not your turn");
        }
        
        if (!moveValidator.isValidMove(board, position)) {
            return false;
        }
        
        CellValue playerSymbol = player.getSymbol();
        board.makeMove(position, playerSymbol);
        
        Move move = new Move(position, playerSymbol);
        moveHistory.add(move);
        
        notifyObservers("Move made: " + player.getName() + " placed " + playerSymbol.getSymbol() + " at " + position);
        
        if (checkGameEnd()) {
            return true;
        }
        
        switchPlayer();
        return true;
    }
    
    private boolean isCurrentPlayer(Player player) {
        return players.get(currentPlayerIndex).equals(player);
    }
    
    private void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
    }
    
    private boolean checkGameEnd() {
        if (board.hasWinner()) {
            winner = players.get(currentPlayerIndex);
            gameStatus = (winner.getSymbol() == CellValue.X) ? GameStatus.PLAYER_X_WON : GameStatus.PLAYER_O_WON;
            notifyObservers("Game Over! " + winner.getName() + " wins!");
            return true;
        }
        
        if (board.isFull()) {
            gameStatus = GameStatus.DRAW;
            notifyObservers("Game Over! It's a draw!");
            return true;
        }
        
        return false;
    }
    
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyObservers(String message) {
        for (GameObserver observer : observers) {
            observer.onGameUpdate(message, this);
        }
    }
    
    public void reset() {
        board.reset();
        moveHistory.clear();
        gameStatus = GameStatus.WAITING_FOR_PLAYERS;
        currentPlayerIndex = 0;
        winner = null;
        notifyObservers("Game reset");
    }
    
    // Getters
    public Board getBoard() {
        return board;
    }
    
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
    
    public List<Move> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }
    
    public GameStatus getGameStatus() {
        return gameStatus;
    }
    
    public Player getCurrentPlayer() {
        if (players.isEmpty()) return null;
        return players.get(currentPlayerIndex);
    }
    
    public Player getWinner() {
        return winner;
    }
    
    public boolean isGameOver() {
        return gameStatus.isGameOver();
    }
    
    public boolean isGameInProgress() {
        return gameStatus.isGameInProgress();
    }
}
