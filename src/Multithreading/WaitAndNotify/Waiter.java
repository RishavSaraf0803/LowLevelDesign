package Multithreading.WaitAndNotify;

public class Waiter implements Runnable{

    private Object obj;

    Waiter(Object obj){
        this.obj = obj;
    }
    public void run(){

        synchronized(obj){
            try {
                obj.wait();
                System.out.println("Waiter: Object notified");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
