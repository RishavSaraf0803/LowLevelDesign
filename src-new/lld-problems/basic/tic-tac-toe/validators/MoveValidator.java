package TicTacToe.validators;

import TicTacToe.data.Position;
import TicTacToe.game.Board;

/**
 * Validates moves in the Tic Tac Toe game
 */
public class MoveValidator {
    
    public boolean isValidMove(Board board, Position position) {
        if (position == null) {
            return false;
        }
        
        // Check bounds
        if (position.getRow() < 0 || position.getRow() >= board.getSize() ||
            position.getColumn() < 0 || position.getColumn() >= board.getSize()) {
            return false;
        }
        
        // Check if cell is empty
        return board.isValidMove(position);
    }
    
    public String getValidationMessage(Board board, Position position) {
        if (position == null) {
            return "Position cannot be null";
        }
        
        if (position.getRow() < 0 || position.getRow() >= board.getSize() ||
            position.getColumn() < 0 || position.getColumn() >= board.getSize()) {
            return "Position is out of bounds. Valid range: 0-2";
        }
        
        if (!board.isValidMove(position)) {
            return "Cell is already occupied";
        }
        
        return "Valid move";
    }
}
