package Multithreading.ExplicitLocks.ReadWriteLock;

public class Writer implements Runnable {

    private final Store store;

    public Writer(Store store) {
        this.store = store;
    }

    public void run(){
        store.write();
    }

}
