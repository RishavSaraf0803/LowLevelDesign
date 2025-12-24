package Multithreading.ExplicitLocks.ReadWriteLockWithWriteStarvation;

public class ReadWriteLock {

    private int readCount = 0;
    private int writeCount = 0;
    public synchronized void lockRead(){
        while( writeCount > 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        readCount++;
        
    }

    public synchronized void lockWrite(){
       

        while(readCount > 0  || writeCount > 0 ){
            try{  wait(); }
            catch(Exception e){}
        }
        writeCount++;
    }

    public synchronized void unlockRead(){
        readCount--;
        notifyAll();
    }

    public synchronized void unlockWrite(){
        writeCount--;
        notifyAll();
    }

}
