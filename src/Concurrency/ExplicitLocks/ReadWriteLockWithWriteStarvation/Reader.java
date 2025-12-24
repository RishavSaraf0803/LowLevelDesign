package Multithreading.ExplicitLocks.ReadWriteLockWithWriteStarvation;

public class Reader  implements Runnable{
    private final Store store;

    public Reader(Store store) {
        this.store = store;
    }
    public void run(){
        while(true){
            String value = store.read();
            System.out.println(Thread.currentThread().getName() + " read: " + value);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
