package Multithreading.WaitAndNotify;

public class Notifier implements Runnable{
    private Object obj;

    Notifier(Object obj){
        this.obj = obj;
    }

    public void run(){
        synchronized(obj){
            System.out.println("Notifier: Object notified");
            obj.notify();
        }
    }
}
