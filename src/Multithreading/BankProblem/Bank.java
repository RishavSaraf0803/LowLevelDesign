package Multithreading.BankProblem;
import java.util.*;
public class Bank {

    private List<Account> accounts;

    public Bank(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void transfer(Account sender, Account reciver, int amount){
        // Basic transfer logic with print statement
        if (sender == null || reciver == null) {
            System.out.println("Sender or receiver account is null.");
            return;
        }
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");
            return;
        }
        try {
            synchronized(sender){
                synchronized(reciver){
                    sender.deduct(amount);
 
                    reciver.add(amount);
                }

            System.out.println("Transferred " + amount + " from Account " + sender.getId() + " to Account " + reciver.getId());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Transfer failed: insufficient funds in Account " + sender.getId());
        }
    }

    public List<Account> getAccounts(){
        return this.accounts;
    }
}
