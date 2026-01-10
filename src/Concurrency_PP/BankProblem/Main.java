package Multithreading.BankProblem;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Account account1 = new Account(1, 100);
        Account account2 = new Account(2, 200);
        Account account3 = new Account(3, 300);
        Account account4 = new Account(4, 400);
        Bank bank = new Bank(Arrays.asList(new Account[]{account1,account2,account3,account4}));
        Auditor auditor = new Auditor(bank);
        Worker worker = new Worker(account3, account4, 100, bank);
        Worker worker2 = new Worker(account2, account3, 100, bank);
        // Create additional workers
        Worker worker3 = new Worker(account1, account2, 100, bank);
        Worker worker4 = new Worker(account4, account1, 100, bank);
        Worker worker5 = new Worker(account2, account4, 100, bank);
        Worker worker6 = new Worker(account3, account1, 100, bank);

        // Create threads for each worker and start them
        Thread t3 = new Thread(worker3);
        Thread t4 = new Thread(worker4);
        Thread t5 = new Thread(worker5);
        Thread t6 = new Thread(worker6);

        t3.start();
        t4.start();
        t5.start();
        t6.start();
        Thread t1 = new Thread(worker);
        Thread t2 = new Thread(worker2);
        Thread auThread = new Thread(auditor);
        t1.start();
        t2.start();
        auThread.start();



    }

}
