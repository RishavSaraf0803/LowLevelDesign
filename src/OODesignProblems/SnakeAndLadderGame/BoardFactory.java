package SnakeAndLadderGame;

public class BoardFactory {

    public static Board createSnakeAndLadderBoard(int size,int numberOfSnakes,int numberOfLadders) {
        Board board = new SankeAndLadderBoard();
        board.size = size;
        board.cells = new ArrayList<>();
        return board;
    }
}
