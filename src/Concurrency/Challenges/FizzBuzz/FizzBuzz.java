package Multithreading.Challenges.FizzBuzz;

public class FizzBuzz implements Runnable{
    Object lock;
    public FizzBuzz(Object lock){
        this.lock = lock;
    }

    public void run(){
        while(Main.curr <= Main.limit){
        synchronized(lock){
            if(Main.curr % 3 == 0 && Main.curr % 5 == 0){
                System.out.println("FizzBuzz is printing");
                System.out.println(Main.curr);

                Main.curr++;
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
        }}
}
