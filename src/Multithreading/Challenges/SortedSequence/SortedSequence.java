package Multithreading.Challenges.SortedSequence;

public class SortedSequence {
    public static int curr = 1;
    public static void main(String[] args) {
        Object lock = new Object();
        for(int i = 1; i <= 10000; i++){
        Thread t = new Thread(new Worker(i, lock));
        t.start();
        }
        
    }



}
class Worker implements Runnable{
    private int id;
    private Object lock;
    public Worker(int id, Object lock){
        this.id = id;
        this.lock = lock;
    }
    public void run(){
        while(true){
            synchronized(lock){
                if(SortedSequence.curr == id){
                    System.out.println("Worker " + id + " is working");
                    SortedSequence.curr++;
                    lock.notifyAll();
                }
                else{
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

