package TicTacToe.tester;

import TicTacToe.data.CellValue;
import TicTacToe.data.Position;
import TicTacToe.game.TicTacToeGame;
import TicTacToe.managers.GameManager;
import TicTacToe.players.ComputerPlayer;
import TicTacToe.players.HumanPlayer;
import TicTacToe.players.Player;

/**
 * Test class for Tic Tac Toe game functionality
 */
public class TicTacToeTester {
    
    public static void main(String[] args) {
        System.out.println("=== TIC TAC TOE TESTER ===");
        
        testBasicGame();
        testComputerVsComputer();
        testAPI();
        
        System.out.println("=== ALL TESTS COMPLETED ===");
    }
    
    private static void testBasicGame() {
        System.out.println("\n1. Testing Basic Game Functionality");
        System.out.println("====================================");
        
        TicTacToeGame game = new TicTacToeGame();
        
        // Add players
        Player player1 = new HumanPlayer("Alice", CellValue.X);
        Player player2 = new HumanPlayer("Bob", CellValue.O);
        
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        System.out.println("Game Status: " + game.getGameStatus());
        System.out.println("Current Player: " + game.getCurrentPlayer().getName());
        
        // Test moves
        Position move1 = new Position(1, 1); // Center
        Position move2 = new Position(0, 0); // Corner
        Position move3 = new Position(1, 0); // Edge
        
        System.out.println("Making moves...");
        game.makeMove(move1, player1);
        game.makeMove(move2, player2);
        game.makeMove(move3, player1);
        
        System.out.println("Board after moves:");
        System.out.println(game.getBoard());
        System.out.println("Game Status: " + game.getGameStatus());
    }
    
    private static void testComputerVsComputer() {
        System.out.println("\n2. Testing Computer vs Computer");
        System.out.println("===============================");
        
        TicTacToeGame game = new TicTacToeGame();
        
        ComputerPlayer computer1 = new ComputerPlayer("AI-1", CellValue.X, ComputerPlayer.DifficultyLevel.HARD);
        ComputerPlayer computer2 = new ComputerPlayer("AI-2", CellValue.O, ComputerPlayer.DifficultyLevel.MEDIUM);
        
        game.addPlayer(computer1);
        game.addPlayer(computer2);
        
        System.out.println("Starting AI vs AI game...");
        
        int moveCount = 0;
        while (!game.isGameOver() && moveCount < 9) {
            Player currentPlayer = game.getCurrentPlayer();
            Position move = currentPlayer.getNextMove();
            
            System.out.println(currentPlayer.getName() + " moves to " + move);
            game.makeMove(move, currentPlayer);
            
            System.out.println("Board after move:");
            System.out.println(game.getBoard());
            
            moveCount++;
        }
        
        System.out.println("Final Game Status: " + game.getGameStatus());
        if (game.getWinner() != null) {
            System.out.println("Winner: " + game.getWinner().getName());
        }
    }
    
    private static void testAPI() {
        System.out.println("\n3. Testing Game API");
        System.out.println("===================");
        
        TicTacToe.apis.TicTacToeGameAPI api = new TicTacToe.apis.TicTacToeGameAPI();
        
        api.startGame();
        
        Player player1 = new HumanPlayer("TestPlayer1", CellValue.X);
        Player player2 = new HumanPlayer("TestPlayer2", CellValue.O);
        
        System.out.println("Adding players...");
        api.addPlayer(player1);
        api.addPlayer(player2);
        
        System.out.println("Game Status: " + api.getGameStatus());
        System.out.println("Board State:");
        System.out.println(api.getBoardState());
        
        System.out.println("Making a move...");
        Position move = new Position(0, 0);
        boolean success = api.makeMove(move, player1);
        System.out.println("Move successful: " + success);
        
        System.out.println("Updated Board State:");
        System.out.println(api.getBoardState());
        
        System.out.println("Current Player: " + api.getCurrentPlayer().getName());
    }
    
    private static void testGameManager() {
        System.out.println("\n4. Testing Game Manager");
        System.out.println("========================");
        
        GameManager gameManager = new GameManager();
        
        // Note: This would normally start an interactive game
        // For testing purposes, we'll just verify the manager can be created
        System.out.println("GameManager created successfully");
        
        gameManager.close();
    }
}
