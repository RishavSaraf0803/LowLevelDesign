package TicTacToe.data;

/**
 * Represents the possible values a cell can have in the Tic Tac Toe game
 */
public enum CellValue {
    EMPTY(" "),
    X("X"),
    O("O");
    
    private final String symbol;
    
    CellValue(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    @Override
    public String toString() {
        return symbol;
    }
}
