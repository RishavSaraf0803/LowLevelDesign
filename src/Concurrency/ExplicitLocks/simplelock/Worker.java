package Multithreading.ExplicitLocks.simplelock;

public class Worker extends Thread   {

    private SimpleLock lock;
    private final int id;

    public Worker(SimpleLock lock, int id) {
        this.lock = lock;
        this.id = id;
    }

    public void run() {
        lock.lock();
        System.out.println("Worker " + id + " is working");
        lock.unlock();
    }
}
