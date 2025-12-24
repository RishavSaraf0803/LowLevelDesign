
public class ReadWriteLock {

    public int readCount = 0;
    public int writeCount = 0;
    public int writeRequest = 0;
    public Thread writeThread;
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

        while(readCount > 0){
            try{  wait(); }
            catch(Exception e){}
        }
        if(writeThread == null || writeThread == Thread.currentThread()){
            writeCount++;
            writeThread = Thread.currentThread();
            writeRequest--;
        }
        else{
            try{  wait(); }
            catch(Exception e){}
        }
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
