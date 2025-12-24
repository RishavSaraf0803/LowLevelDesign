package Multithreading.BankProblem;

public class Worker implements Runnable{

    private final Account src,des;
    private final int amount;
    private final Bank bank;

    public Worker(Account src, Account des, int amount, Bank bank) {
        this.src = src;
        this.des = des;
        this.amount = amount;
        this.bank = bank;
    }

    public void run(){
        bank.transfer(src,  des, amount);
        
    }

}
