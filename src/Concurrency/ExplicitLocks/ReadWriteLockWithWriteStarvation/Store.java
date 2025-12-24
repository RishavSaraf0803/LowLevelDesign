package Multithreading.ExplicitLocks.ReadWriteLockWithWriteStarvation;

public class Store {

    private String name = "";
    private ReadWriteLock readWriteLock;

    Store(ReadWriteLock lock){
        this.readWriteLock = lock;
    }

    public String read() {
        readWriteLock.lockRead();
        try{
            return name;
        }
        catch(Exception e){
            throw new RuntimeException();
        }
        finally{
            readWriteLock.unlockRead();
        }
    }

    public void write(){
        readWriteLock.lockWrite();
        try{
            name += "bla";
        }
        catch(Exception e){throw new RuntimeException();}
        finally{
            readWriteLock.unlockWrite();
        }
    }

}
