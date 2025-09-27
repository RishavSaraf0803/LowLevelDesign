package TicTacToe.game;

import TicTacToe.data.CellValue;
import TicTacToe.data.Position;

/**
 * Represents the Tic Tac Toe game board
 */
public class Board {
    private static final int SIZE = 3;
    private final CellValue[][] grid;
    
    public Board() {
        this.grid = new CellValue[SIZE][SIZE];
        initializeBoard();
    }
    
    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = CellValue.EMPTY;
            }
        }
    }
    
    public boolean isValidMove(Position position) {
        return position.getRow() >= 0 && position.getRow() < SIZE &&
               position.getColumn() >= 0 && position.getColumn() < SIZE &&
               grid[position.getRow()][position.getColumn()] == CellValue.EMPTY;
    }
    
    public void makeMove(Position position, CellValue playerSymbol) {
        if (!isValidMove(position)) {
            throw new IllegalArgumentException("Invalid move at position " + position);
        }
        grid[position.getRow()][position.getColumn()] = playerSymbol;
    }
    
    public CellValue getCellValue(Position position) {
        return grid[position.getRow()][position.getColumn()];
    }
    
    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == CellValue.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean hasWinner() {
        return checkRows() || checkColumns() || checkDiagonals();
    }
    
    public CellValue getWinner() {
        if (!hasWinner()) {
            return CellValue.EMPTY;
        }
        
        // Check rows
        for (int i = 0; i < SIZE; i++) {
            if (grid[i][0] != CellValue.EMPTY && 
                grid[i][0] == grid[i][1] && 
                grid[i][1] == grid[i][2]) {
                return grid[i][0];
            }
        }
        
        // Check columns
        for (int j = 0; j < SIZE; j++) {
            if (grid[0][j] != CellValue.EMPTY && 
                grid[0][j] == grid[1][j] && 
                grid[1][j] == grid[2][j]) {
                return grid[0][j];
            }
        }
        
        // Check diagonals
        if (grid[0][0] != CellValue.EMPTY && 
            grid[0][0] == grid[1][1] && 
            grid[1][1] == grid[2][2]) {
            return grid[0][0];
        }
        
        if (grid[0][2] != CellValue.EMPTY && 
            grid[0][2] == grid[1][1] && 
            grid[1][1] == grid[2][0]) {
            return grid[0][2];
        }
        
        return CellValue.EMPTY;
    }
    
    private boolean checkRows() {
        for (int i = 0; i < SIZE; i++) {
            if (grid[i][0] != CellValue.EMPTY && 
                grid[i][0] == grid[i][1] && 
                grid[i][1] == grid[i][2]) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkColumns() {
        for (int j = 0; j < SIZE; j++) {
            if (grid[0][j] != CellValue.EMPTY && 
                grid[0][j] == grid[1][j] && 
                grid[1][j] == grid[2][j]) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkDiagonals() {
        // Main diagonal
        if (grid[0][0] != CellValue.EMPTY && 
            grid[0][0] == grid[1][1] && 
            grid[1][1] == grid[2][2]) {
            return true;
        }
        
        // Anti-diagonal
        if (grid[0][2] != CellValue.EMPTY && 
            grid[0][2] == grid[1][1] && 
            grid[1][1] == grid[2][0]) {
            return true;
        }
        
        return false;
    }
    
    public void reset() {
        initializeBoard();
    }
    
    public int getSize() {
        return SIZE;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  0 1 2\n");
        for (int i = 0; i < SIZE; i++) {
            sb.append(i).append(" ");
            for (int j = 0; j < SIZE; j++) {
                sb.append(grid[i][j].getSymbol());
                if (j < SIZE - 1) sb.append("|");
            }
            sb.append("\n");
            if (i < SIZE - 1) sb.append("  -----\n");
        }
        return sb.toString();
    }
}
