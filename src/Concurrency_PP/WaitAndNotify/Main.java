package Multithreading.WaitAndNotify;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Object obj = new Object();
        Waiter waiter = new Waiter(obj);
        Notifier notifier = new Notifier(obj);
        Thread t1 = new Thread(waiter);
        Thread t2 = new Thread(notifier);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Main thread: End of program");
        }
    }


