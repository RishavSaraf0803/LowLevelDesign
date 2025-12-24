package Multithreading.ExplicitLocks.ReadWriteLock;

public class ReadWriteLock {

    public int readCount = 0;
    public int writeCount = 0;
    public int writeRequest = 0;
    public synchronized void lockRead(){
        while( writeCount > 0 || writeRequest > 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        readCount++;
    }
    public synchronized void lockWrite(){
       
        writeRequest++;

        while(readCount > 0  || writeCount > 0 ){
            try{  wait(); }
            catch(Exception e){}
        }
        writeCount++;
        writeRequest--;
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
