package TicTacToe.players;

import TicTacToe.data.CellValue;
import TicTacToe.data.Position;

/**
 * Abstract base class for all Tic Tac Toe players
 */
public abstract class Player {
    protected final String name;
    protected final CellValue symbol;
    
    public Player(String name, CellValue symbol) {
        this.name = name;
        this.symbol = symbol;
    }
    
    public String getName() {
        return name;
    }
    
    public CellValue getSymbol() {
        return symbol;
    }
    
    /**
     * Get the next move from the player
     * @return Position where the player wants to make a move
     */
    public abstract Position getNextMove();
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return name.equals(player.name) && symbol == player.symbol;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode() * 31 + symbol.hashCode();
    }
    
    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", symbol=" + symbol +
                '}';
    }
}
