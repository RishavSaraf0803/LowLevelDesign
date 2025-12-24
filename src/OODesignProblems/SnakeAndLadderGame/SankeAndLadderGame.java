package SnakeAndLadderGame;

public class SankeAndLadderGame implements Game {


    Board board;
    Dice dice;
    Queue<Player> players;
    Map<Player, Integer> playerPositions;

    public SankeAndLadderGame(Board board, Dice dice, Queue<Player> players) {
        this.board = board;
        this.dice = dice;
        this.players = players;
        this.playerPositions = new HashMap<>();
        board.prepareBoard();
    }

    @Override
    public void startGame() {
    }

    @Override
    public void resumeGame() {
    }

    @Override
    public void pauseGame() {
    }

    @Override
    public void endGame() {
    }

    @Override
    public void restartGame() {
    }

    @Override
    public void saveGame() {
    }

    @Override
    public void loadGame() {
    }

    @Override
    public void undoMove() {
    }

    @Override
    public void redoMove() {
    }
    
}