package TicTacToe.players;

import TicTacToe.data.CellValue;
import TicTacToe.data.Position;

import java.util.Scanner;

/**
 * Human player implementation that takes input from console
 */
public class HumanPlayer extends Player {
    private final Scanner scanner;
    
    public HumanPlayer(String name, CellValue symbol) {
        super(name, symbol);
        this.scanner = new Scanner(System.in);
    }
    
    @Override
    public Position getNextMove() {
        System.out.println(name + " (" + symbol.getSymbol() + "), enter your move (row column): ");
        
        try {
            int row = scanner.nextInt();
            int column = scanner.nextInt();
            return new Position(row, column);
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter two numbers (row column).");
            return getNextMove(); // Recursive call to retry
        }
    }
    
    public void close() {
        scanner.close();
    }
}
