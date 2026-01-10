package Multithreading.ExplicitLocks.simplelock;

public class Main {
    public static void main(String[] args) {
        SimpleLock lock = new SimpleLock();
        Worker worker1 = new Worker(lock, 1);
        Worker worker2 = new Worker(lock, 2);
        worker1.start();
        worker2.start();
        try {
            worker1.join();
            worker2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main thread: End of program");
    }   
}
