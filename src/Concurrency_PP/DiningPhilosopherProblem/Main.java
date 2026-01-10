package Multithreading.DiningPhilosopherProblem;

public class Main {
    
    public static void main(String[] args) {
        int philosopherCount = 5;
        DinningTable table = new DinningTable(philosopherCount);
        Thread[] philosopherThreads = new Thread[philosopherCount];

        for (int i = 0; i < philosopherCount; i++) {
            philosopherThreads[i] = new Thread(new Philosopher(i,table));
            philosopherThreads[i].start();
        }

        // Let philosophers run for demonstration; in real scenarios a mechanism to stop is needed
        try {
            Thread.sleep(2000); // Run for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Optionally interrupt all philosopher threads (if needed to stop)
        for (Thread t : philosopherThreads) {
            t.interrupt();
        }
    }
}
