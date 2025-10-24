package Multithreading.BusyWaitingAndThreadSignaling;
public class Main {
    public static void main(String[] args) {
        TickingBoard tickingBoard = new TickingBoard(0);
        Manager manager = new Manager(tickingBoard);
        Customer customer1 = new Customer(1, tickingBoard);
        Customer customer2 = new Customer(2, tickingBoard);
        Customer customer3 = new Customer(3, tickingBoard);
        Customer customer4 = new Customer(4, tickingBoard);
        Thread managerThread = new Thread(manager);
        Thread customer1Thread = new Thread(customer1);
        Thread customer2Thread = new Thread(customer2);
        Thread customer3Thread = new Thread(customer3);
        Thread customer4Thread = new Thread(customer4);
        managerThread.start();
        customer1Thread.start();
        customer2Thread.start();
        customer3Thread.start();
        customer4Thread.start();
        try {
            managerThread.join();
            customer1Thread.join();
            customer2Thread.join();
            customer3Thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
    }
}
}