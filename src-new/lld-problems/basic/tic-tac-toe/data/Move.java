package TicTacToe.data;

/**
 * Represents a move made by a player in the Tic Tac Toe game
 */
public class Move {
    private final Position position;
    private final CellValue playerSymbol;
    private final long timestamp;
    
    public Move(Position position, CellValue playerSymbol) {
        this.position = position;
        this.playerSymbol = playerSymbol;
        this.timestamp = System.currentTimeMillis();
    }
    
    public Position getPosition() {
        return position;
    }
    
    public CellValue getPlayerSymbol() {
        return playerSymbol;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "Move{" +
                "position=" + position +
                ", playerSymbol=" + playerSymbol +
                ", timestamp=" + timestamp +
                '}';
    }
}
