package Multithreading.DiningPhilosopherProblem;

public class DinningTable {

    Chopstick[] chopsticks;
    Food[] foods;
    int philosopher;
    public DinningTable(int philosopherCount) {
        this.philosopher = philosopherCount;
        this.chopsticks = new Chopstick[philosopherCount];
        this.foods = new Food[philosopherCount];
        for (int i = 0; i < philosopherCount; i++) {
            this.chopsticks[i] = new Chopstick("Chopstick");
            this.foods[i]= new Food("Ramen");
            
        }
    }

    public Chopstick getLeftChopstick(int id){
        return chopsticks[id];
    }
    public Chopstick getRightChopstick(int id){
        return chopsticks[(id+1)%philosopher];
    }
    public Food getFood(int id){
        return foods[id];
    }

}
