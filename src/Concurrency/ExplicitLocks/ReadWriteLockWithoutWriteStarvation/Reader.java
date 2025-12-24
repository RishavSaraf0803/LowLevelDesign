package Multithreading.ExplicitLocks.ReadWriteLock;

public class Reader  implements Runnable{
    private final Store store;

    public Reader(Store store) {
        this.store = store;
    }
    public void run(){
        System.out.println(store.read());
    }

}
