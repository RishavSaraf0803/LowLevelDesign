package Multithreading.Challenges;
import java.util.*;

public class Challenge1 {
     static volatile int curr = 1;
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
      
        Thread t1 = new Thread(new Worker(1,lock,3));
        Thread t2 = new Thread(new Worker(2,lock,3));
        Thread t3 = new Thread(new Worker(3,lock,3));
        t1.start();
        t2.start();
        t3.start();
       
    }
}
class Worker implements Runnable {
    private int id;
    private Object lock;
    private int round;

    public Worker(int id, Object lock,int round) {
        this.id = id;
        this.lock = lock;
        this.round = round;
    }

    @Override
    public  void  run() {
        while(round > 0){
            while(compare(id)){
            }
            System.out.println("Worker " + id + " is working");
            synchronized(lock){
            Challenge1.curr++;
            }
            round--;
        }
        }

        public boolean compare(int val){
            boolean ans = false;
            synchronized(lock){
                ans = !(Challenge1.curr == val);
            }
            return ans;
        }
    }

