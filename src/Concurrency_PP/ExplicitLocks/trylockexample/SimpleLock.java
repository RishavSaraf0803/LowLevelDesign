
public class SimpleLock {
    private boolean isLocked = false;
    private int lockcount=0;
    private Thread lockedBy;

    public SimpleLock() {
        this.isLocked = false;
        this.lockcount = 0;
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
    lockcount++;
    isLocked = true;
    lockedBy = Thread.currentThread();
}


    public  synchronized void unlock(){
        if( !Thread.currentThread().equals(lockedBy)){
            throw new RuntimeException("Current thread does not hold the lock and therefore cannot unlock.");
        }
        else if(lockcount == 0){
            throw new RuntimeException("Lock is not currently held, cannot unlock.");
        }
        lockcount--;
        if(lockcount == 0){
            lockedBy = null;
            isLocked = false;
             notifyAll();
        }
    }
   
    
}
