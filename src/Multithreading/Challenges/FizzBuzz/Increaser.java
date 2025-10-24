package Multithreading.Challenges.FizzBuzz;

public class Increaser implements Runnable{
 private Object lock;
 public Increaser(Object lock){
    this.lock = lock;
 }
    public void run(){
        while(true){
            synchronized(lock){
                System.out.println("Increaser is working");
                Main.curr++;
                lock.notifyAll();
            }
        }
    }
}