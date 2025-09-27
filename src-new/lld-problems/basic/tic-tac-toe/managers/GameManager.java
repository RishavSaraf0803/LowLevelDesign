package TicTacToe.managers;

import TicTacToe.data.CellValue;
import TicTacToe.data.Position;
import TicTacToe.game.TicTacToeGame;
import TicTacToe.observers.GameObserver;
import TicTacToe.players.ComputerPlayer;
import TicTacToe.players.HumanPlayer;
import TicTacToe.players.Player;
import TicTacToe.ui.GameDisplay;
import TicTacToe.validators.MoveValidator;

import java.util.Scanner;

/**
 * Manages the overall game flow and user interactions
 */
public class GameManager {
    private final TicTacToeGame game;
    private final GameDisplay display;
    private final MoveValidator moveValidator;
    private final Scanner scanner;
    
    public GameManager() {
        this.game = new TicTacToeGame();
        this.display = new GameDisplay();
        this.moveValidator = new MoveValidator();
        this.scanner = new Scanner(System.in);
        
        // Add console observer
        game.addObserver(new TicTacToe.observers.ConsoleGameObserver());
    }
    
    public void startGame() {
        display.displayWelcome();
        display.displayInstructions();
        
        setupPlayers();
        
        while (!game.isGameOver()) {
            playTurn();
        }
        
        display.displayGameOver(game);
    }
    
    private void setupPlayers() {
        System.out.println("Choose game mode:");
        System.out.println("1. Human vs Human");
        System.out.println("2. Human vs Computer");
        System.out.println("3. Computer vs Computer");
        
        int choice = getIntInput("Enter your choice (1-3): ", 1, 3);
        
        switch (choice) {
            case 1 -> setupHumanVsHuman();
            case 2 -> setupHumanVsComputer();
            case 3 -> setupComputerVsComputer();
        }
    }
    
    private void setupHumanVsHuman() {
        String player1Name = getStringInput("Enter Player 1 name: ");
        String player2Name = getStringInput("Enter Player 2 name: ");
        
        game.addPlayer(new HumanPlayer(player1Name, CellValue.X));
        game.addPlayer(new HumanPlayer(player2Name, CellValue.O));
    }
    
    private void setupHumanVsComputer() {
        String playerName = getStringInput("Enter your name: ");
        
        System.out.println("Choose computer difficulty:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        
        int difficulty = getIntInput("Enter difficulty (1-3): ", 1, 3);
        ComputerPlayer.DifficultyLevel level = switch (difficulty) {
            case 1 -> ComputerPlayer.DifficultyLevel.EASY;
            case 2 -> ComputerPlayer.DifficultyLevel.MEDIUM;
            case 3 -> ComputerPlayer.DifficultyLevel.HARD;
            default -> ComputerPlayer.DifficultyLevel.MEDIUM;
        };
        
        game.addPlayer(new HumanPlayer(playerName, CellValue.X));
        game.addPlayer(new ComputerPlayer("Computer", CellValue.O, level));
    }
    
    private void setupComputerVsComputer() {
        System.out.println("Choose difficulty for Computer 1:");
        ComputerPlayer.DifficultyLevel level1 = chooseDifficulty();
        
        System.out.println("Choose difficulty for Computer 2:");
        ComputerPlayer.DifficultyLevel level2 = chooseDifficulty();
        
        game.addPlayer(new ComputerPlayer("Computer 1", CellValue.X, level1));
        game.addPlayer(new ComputerPlayer("Computer 2", CellValue.O, level2));
    }
    
    private ComputerPlayer.DifficultyLevel chooseDifficulty() {
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        
        int choice = getIntInput("Enter difficulty (1-3): ", 1, 3);
        return switch (choice) {
            case 1 -> ComputerPlayer.DifficultyLevel.EASY;
            case 2 -> ComputerPlayer.DifficultyLevel.MEDIUM;
            case 3 -> ComputerPlayer.DifficultyLevel.HARD;
            default -> ComputerPlayer.DifficultyLevel.MEDIUM;
        };
    }
    
    private void playTurn() {
        Player currentPlayer = game.getCurrentPlayer();
        Position move = currentPlayer.getNextMove();
        
        if (!game.makeMove(move, currentPlayer)) {
            display.displayError("Invalid move! Please try again.");
            playTurn(); // Retry
        }
    }
    
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }
    
    public void close() {
        scanner.close();
    }
}
