package SnakeAndLadderGame;

public class Dice {
    
    private final int min = 1;
    private final int max = 6;

    public int roll() {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
