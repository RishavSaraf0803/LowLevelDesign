package Multithreading.DiningPhilosopherProblem;

public class Philosopher implements Runnable {
    DinningTable dinningTable;
    int id;

    public Philosopher(int id, DinningTable dinningTable) {
        this.id = id;
        this.dinningTable = dinningTable;
    }
    public void run(){
        
        //  while(){
            dinningTable.getLeftChopstick(id);
            dinningTable.getRightChopstick(id);
            dinningTable.getFood(id);
            System.out.println("Fodd eaten by:"+ id);
        // }
    }

}
