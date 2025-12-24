package Multithreading.DiningPhilosopherProblem.version_pp;

public class Main {

    public static void main(String[] args){
        Chopstick[] chopsticks= new Chopstick[5];
        for(int i =0; i < 5; i++){
            chopsticks[i] = new Chopstick(i);
        }
        for(int i = 0; i < 5; i++){
            Thread thread = new Thread(new Philosopher("Philospher "+i, chopsticks[i], chopsticks[(i+1)%5]));
            thread.start();
        }
    }

}
