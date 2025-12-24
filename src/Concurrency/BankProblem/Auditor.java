package Multithreading.BankProblem;
import java.util.*;

public class Auditor implements Runnable{

    private final Bank bank;
    public Auditor(Bank bank) {
        this.bank = bank;
    }


    public void run(){
        // while (true) {
 
        List<Account> accounts = bank.getAccounts();
        for(Account account : accounts){
            System.out.println("For Account ID: " + account.getId() + "  Amount is: " + account.getAmount());
    // }
}

    }

}
