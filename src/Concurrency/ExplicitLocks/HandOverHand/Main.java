package Multithreading.ExplicitLocks.HandOverHand;

public class Main {

    public static void main(String[] args) {
        DoubleLinkedList list = new DoubleLinkedList();
        Thread thread1 = new Thread(new Inserter(100,list));
        Thread thread2 = new Thread(new Inserter(50,list));
        Thread thread3 = new Thread(new Inserter(70,list));
        Thread thread4 = new Thread(new Inserter(150,list));
        Thread thread5 = new Thread(new Inserter(90,list));
        Thread thread6 = new Thread(new Inserter(60,list));
        Thread thread7 = new Thread(new Inserter(20,list));
        Thread thread8 = new Thread(new Inserter(35,list));
        Thread thread9 = new Thread(new Inserter(400,list));
        Thread thread10 = new Thread(new Inserter(300,list));
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();
        thread10.start();
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
            thread6.join();
            thread7.join();
            thread8.join();
            thread9.join();
            thread10.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        list.display();
    }
}
