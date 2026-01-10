package Multithreading.ExplicitLocks.simplelock;

public class SimpleLock {
    private boolean isLocked = false;
    private Thread lockedBy;

    public SimpleLock() {
        this.isLocked = false;
        this.lockedBy = null;
    }

    public synchronized void lock(){

    while(isLocked && Thread.currentThread() != lockedBy){
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    isLocked = true;
    lockedBy = Thread.currentThread();
}

    public  synchronized void unlock(){
        if( !Thread.currentThread().equals(lockedBy)){
            throw new RuntimeException("Current thread does not hold the lock and therefore cannot unlock.");
        }
        else if(!isLocked){
            throw new RuntimeException("Lock is not currently held, cannot unlock.");
        }
        isLocked = false;
        lockedBy = null;
        notifyAll();
        
    }
   
    
}
