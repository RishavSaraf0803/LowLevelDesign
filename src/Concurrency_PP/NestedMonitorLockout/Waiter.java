package Multithreading.NestedMonitorLockout;

public class Waiter {

    private final Object lock1, lock2;

    public Waiter(Object lock1, Object lock2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    public void run() {
        synchronized(lock1) {
            synchronized(lock2) {
                try {
                    System.out.println("Waiter is waiting");
                    lock2.wait();
                    System.out.println("Waiter is notified");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Waiter interrupted");
                }
            }
        }
    }
    
}
