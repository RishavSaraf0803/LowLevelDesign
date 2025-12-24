package Multithreading.BankProblem;

public class Account {

    private final int id;
    private int amount;

    public Account(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public synchronized void  add(int value) {
        this.amount += value;
    }

    public void deduct(int value) {
        if (value > amount) {
            throw new IllegalArgumentException();
        }
        synchronized(this){
            this.amount -= value;
        }
    }

    public int getId() {
        return id;
    }

    public synchronized int getAmount() {
        return this.amount;
    }



}
