package SnakeAndLadderGame;

public class SnakeAndLadderBoard extends Board {

    public SnakeAndLadderBoard(int size,int numberOfSnakes,int numberOfLadders) {
        super(size);
        this.numberOfSnakes = numberOfSnakes;
        this.numberOfLadders = numberOfLadders;
    }
    
    private int numberOfSnakes;
    private int numberOfLadders;
    
}
