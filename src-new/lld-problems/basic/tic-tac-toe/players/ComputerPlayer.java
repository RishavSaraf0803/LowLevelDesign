package TicTacToe.players;

import TicTacToe.data.CellValue;
import TicTacToe.data.Position;
import TicTacToe.game.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Computer player implementation with different difficulty levels
 */
public class ComputerPlayer extends Player {
    private final DifficultyLevel difficulty;
    private final Random random;
    
    public enum DifficultyLevel {
        EASY("Easy"),
        MEDIUM("Medium"),
        HARD("Hard");
        
        private final String description;
        
        DifficultyLevel(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public ComputerPlayer(String name, CellValue symbol, DifficultyLevel difficulty) {
        super(name, symbol);
        this.difficulty = difficulty;
        this.random = new Random();
    }
    
    public ComputerPlayer(String name, CellValue symbol) {
        this(name, symbol, DifficultyLevel.MEDIUM);
    }
    
    @Override
    public Position getNextMove() {
        System.out.println(name + " (" + symbol.getSymbol() + ") is thinking...");
        
        // Simulate thinking time
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return switch (difficulty) {
            case EASY -> getRandomMove();
            case MEDIUM -> getMediumMove();
            case HARD -> getBestMove();
        };
    }
    
    private Position getRandomMove() {
        List<Position> availableMoves = getAvailableMoves();
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }
    
    private Position getMediumMove() {
        // 70% chance to make a smart move, 30% random
        if (random.nextDouble() < 0.7) {
            return getBestMove();
        } else {
            return getRandomMove();
        }
    }
    
    private Position getBestMove() {
        // Simple AI: Try to win, then block opponent, then take center/corners
        List<Position> availableMoves = getAvailableMoves();
        
        // Try to win
        for (Position move : availableMoves) {
            if (wouldWin(move, symbol)) {
                return move;
            }
        }
        
        // Try to block opponent
        CellValue opponentSymbol = (symbol == CellValue.X) ? CellValue.O : CellValue.X;
        for (Position move : availableMoves) {
            if (wouldWin(move, opponentSymbol)) {
                return move;
            }
        }
        
        // Take center if available
        Position center = new Position(1, 1);
        if (availableMoves.contains(center)) {
            return center;
        }
        
        // Take corners
        List<Position> corners = List.of(
            new Position(0, 0), new Position(0, 2),
            new Position(2, 0), new Position(2, 2)
        );
        for (Position corner : corners) {
            if (availableMoves.contains(corner)) {
                return corner;
            }
        }
        
        // Random move
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }
    
    private List<Position> getAvailableMoves() {
        List<Position> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Position pos = new Position(i, j);
                // Note: This would need access to the current board state
                // For now, we'll return all possible positions
                moves.add(pos);
            }
        }
        return moves;
    }
    
    private boolean wouldWin(Position move, CellValue symbol) {
        // This is a simplified version - in a real implementation,
        // you'd need access to the current board state
        return false;
    }
    
    public DifficultyLevel getDifficulty() {
        return difficulty;
    }
}
