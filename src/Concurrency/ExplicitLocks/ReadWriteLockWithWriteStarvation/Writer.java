package Multithreading.ExplicitLocks.ReadWriteLockWithWriteStarvation;

public class Writer implements Runnable {

    private final Store store;

    public Writer(Store store) {
        this.store = store;
    }

    public void run(){
        while(true){
            store.write();
            System.out.println(Thread.currentThread().getName() + " completed write");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
