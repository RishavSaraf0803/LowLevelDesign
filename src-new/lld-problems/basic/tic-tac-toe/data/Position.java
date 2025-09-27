package TicTacToe.data;

/**
 * Represents a position on the Tic Tac Toe board
 */
public class Position {
    private final int row;
    private final int column;
    
    public Position(int row, int column) {
        if (row < 0 || row >= 3 || column < 0 || column >= 3) {
            throw new IllegalArgumentException("Position must be within bounds (0-2)");
        }
        this.row = row;
        this.column = column;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getColumn() {
        return column;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && column == position.column;
    }
    
    @Override
    public int hashCode() {
        return row * 3 + column;
    }
    
    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}
